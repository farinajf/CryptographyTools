/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CRLReason;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import utils.CryptoDefs;

/**
 * Basic example of generating and using a CRL.
 * @author fran
 */
public class X509CRLExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = X509CRLExample.class.getName();

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
    public X509CRLExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param caCert
     * @param caKey
     * @param revokedSerialNumber
     * @return
     * @throws java.lang.Exception
     */
    public static X509CRL createCRL(final X509Certificate caCert, final PrivateKey caKey, final BigInteger revokedSerialNumber) throws Exception {
        X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
        Date               now    = new Date();

        crlGen.setIssuerDN(caCert.getSubjectX500Principal());

        crlGen.setThisUpdate        (now);
        crlGen.setNextUpdate        (new Date(now.getTime() + 100000));
        crlGen.setSignatureAlgorithm(CryptoDefs.Algorithm.SHA256withRSAEncryption.getName());

        crlGen.addCRLEntry(revokedSerialNumber, now, CRLReason.PRIVILEGE_WITHDRAWN.ordinal());

        crlGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
        crlGen.addExtension(X509Extensions.CRLNumber,              false, new CRLNumber(BigInteger.valueOf(1)));

        return crlGen.generateX509CRL(caKey, CryptoDefs.Provider.BC.getName());
    }

    /**
     *
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create CA keys and certificate
        KeyPair         caPair              = Utils.generateRSAKeyPair();
        X509Certificate caCert              = Utils.generateRootCert(caPair);
        BigInteger      revokedSerialNumber = BigInteger.valueOf(2);

        //2.- Create a CRL revoking certificate number 2
        final X509CRL crl = createCRL(caCert, caPair.getPrivate(), revokedSerialNumber);

        //3.- Verify the CRL
        crl.verify(caCert.getPublicKey(), CryptoDefs.Provider.BC.getName());

        //4.- Check if the CRL revokes certificate number 2
        final X509CRLEntry entry = crl.getRevokedCertificate(revokedSerialNumber);

        System.out.println("Revocation details:");
        System.out.println("  Certificate number: " + entry.getSerialNumber());
        System.out.println("  Issuer            : " + crl.getIssuerX500Principal());

        if (entry.hasExtensions() == true)
        {
            byte[] ext = entry.getExtensionValue(X509Extensions.ReasonCode.getId());

            if (ext != null)
            {
                DEREnumerated reasonCode = (DEREnumerated) X509ExtensionUtil.fromExtensionValue(ext);

                System.out.println("  Reason Code      : " + reasonCode.getValue());
            }
        }
    }
}
