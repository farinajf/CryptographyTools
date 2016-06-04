/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Vector;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.ocsp.CertificateID;
import org.bouncycastle.ocsp.OCSPException;
import org.bouncycastle.ocsp.OCSPReq;
import org.bouncycastle.ocsp.OCSPReqGenerator;
import org.bouncycastle.ocsp.Req;

/**
 * Example of unsigned OCSP request generation.
 * @author fran
 */
public class OCSPClientExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = OCSPClientExample.class.getName();

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
    public OCSPClientExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param issuerCert
     * @param serialNumber
     * @return
     * @throws OCSPException
     */
    public static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException {
        //1.- Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(CertificateID.HASH_SHA1, issuerCert, serialNumber);

        //2.- Basic request generation with nonce
        OCSPReqGenerator gen = new OCSPReqGenerator();

        gen.addRequest(id);

        //3.- Create details for nonce extension
        BigInteger nonce  = BigInteger.valueOf(System.currentTimeMillis());
        Vector     oids   = new Vector();
        Vector     values = new Vector();

        oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        values.add(new X509Extension(false, new DEROctetString(nonce.toByteArray())));

        gen.setRequestExtensions(new X509Extensions(oids, values));

        return gen.generate();
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create certificates and CRLs
        KeyPair rootPair  = Utils.generateRSAKeyPair();
        KeyPair interPair = Utils.generateRSAKeyPair();

        X509Certificate rootCert  = Utils.generateRootCert(rootPair);
        X509Certificate interCert = Utils.generateIntermediateCert(interPair.getPublic(), rootPair.getPrivate(), rootCert);

        OCSPReq request = generateOCSPRequest(rootCert, interCert.getSerialNumber());

        Req[] requests = request.getRequestList();

        for (Req x : requests)
        {
            CertificateID certID = x.getCertID();

            System.out.println("OCSP Request to check certificate number: " + certID.getSerialNumber());
        }
    }
}
