/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.ocsp.BasicOCSPResp;
import org.bouncycastle.ocsp.BasicOCSPRespGenerator;
import org.bouncycastle.ocsp.CertificateID;
import org.bouncycastle.ocsp.CertificateStatus;
import org.bouncycastle.ocsp.OCSPException;
import org.bouncycastle.ocsp.OCSPReq;
import org.bouncycastle.ocsp.OCSPResp;
import org.bouncycastle.ocsp.OCSPRespGenerator;
import org.bouncycastle.ocsp.Req;
import org.bouncycastle.ocsp.RevokedStatus;
import org.bouncycastle.ocsp.SingleResp;
import utils.CryptoDefs;

/**
 * Example of OCSP response generation.
 * @author fran
 */
public class OCSPResponderExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = OCSPResponderExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Metodos Protegidos                            */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                            Constructores                               */
    /*------------------------------------------------------------------------*/
    /**
     * Constructor por defecto.
     */
    public OCSPResponderExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param request
     * @param responderKey
     * @param pubKey
     * @param revokedID
     * @return
     * @throws NoSuchProviderException
     * @throws OCSPException
     */
    public static OCSPResp generateOCSPResponse(
            final OCSPReq       request,
            final PrivateKey    responderKey,
            final PublicKey     pubKey,
            final CertificateID revokedID) throws NoSuchProviderException, OCSPException {
        BasicOCSPRespGenerator basicRespGen  = new BasicOCSPRespGenerator(pubKey);
        X509Extensions         reqExtensions = request.getRequestExtensions();

        if (reqExtensions != null)
        {
            X509Extension ext = reqExtensions.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);

            if (ext != null)
            {
                Vector oids   = new Vector();
                Vector values = new Vector();

                oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
                values.add(ext);

                basicRespGen.setResponseExtensions(new X509Extensions(oids, values));
            }
        }

        Req[] requests = request.getRequestList();

        for (Req x : requests)
        {
            CertificateID certID = x.getCertID();

            // This would normally be a lot more general!!
            if (certID.equals(revokedID))
            {
                basicRespGen.addResponse(certID, new RevokedStatus(new Date(), CRLReason.privilegeWithdrawn));
            }
            else
            {
                basicRespGen.addResponse(certID, CertificateStatus.GOOD);
            }
        }

        BasicOCSPResp basicResp = basicRespGen.generate(CryptoDefs.Algorithm.SHA256withRSA.getName(), responderKey, null, new Date(), CryptoDefs.Provider.BC.getName());

        OCSPRespGenerator respGen = new OCSPRespGenerator();

        return respGen.generate(OCSPRespGenerator.SUCCESSFUL, basicResp);
    }

    /**
     *
     * @param responderPair
     * @param caCert
     * @param revokedSerialNumber
     * @param cert
     * @return
     * @throws Exception
     */
    public static String getStatusMessage(
            final KeyPair         responderPair,
            final X509Certificate caCert,
            final BigInteger      revokedSerialNumber,
            final X509Certificate cert) throws Exception {
        OCSPReq request = OCSPClientExample.generateOCSPRequest(caCert, cert.getSerialNumber());

        CertificateID revokedID = new CertificateID(CertificateID.HASH_SHA1, caCert, revokedSerialNumber);

        OCSPResp response = generateOCSPResponse(request, responderPair.getPrivate(), responderPair.getPublic(), revokedID);

        BasicOCSPResp basicResponse = (BasicOCSPResp) response.getResponseObject();

        // Verify the response
        if (basicResponse.verify(responderPair.getPublic(), CryptoDefs.Provider.BC.getName()))
        {
            SingleResp[] responses = basicResponse.getResponses();

            byte[] reqNonce  = request.getExtensionValue      (OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId());
            byte[] respNonce = basicResponse.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId());

            // Validate the nonce if it is present
            if (reqNonce == null || Arrays.equals(reqNonce, respNonce))
            {
                String message = "";

                for (SingleResp x : responses)
                {
                    message += " certificate number " + x.getCertID().getSerialNumber();

                    if (x.getCertStatus() == CertificateStatus.GOOD)
                    {
                        return message + " status: good";
                    }
                    else return message + " status: revoked";
                }

                return message;
            }
            else return "response nonce failed to validate";
        }
        else return "response failed to verify";
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyPair rootPair  = Utils.generateRSAKeyPair();
        KeyPair interPair = Utils.generateRSAKeyPair();

        X509Certificate rootCert  = Utils.generateRootCert(rootPair);
        X509Certificate interCert = Utils.generateIntermediateCert(interPair.getPublic(), rootPair.getPrivate(), rootCert);

        System.out.println(getStatusMessage(rootPair, rootCert, BigInteger.valueOf(1), interCert));
        System.out.println(getStatusMessage(rootPair, rootCert, BigInteger.valueOf(2), interCert));
    }
}
