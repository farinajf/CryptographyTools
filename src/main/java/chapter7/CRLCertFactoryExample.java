/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import static chapter7.X509CRLExample.createCRL;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import utils.CryptoDefs;

/**
 * Reading a CRL with a CertificateFactory.
 * @author fran
 */
public class CRLCertFactoryExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CRLCertFactoryExample.class.getName();

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
    public CRLCertFactoryExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create CA keys and certificate
        KeyPair         caPair              = Utils.generateRSAKeyPair();
        X509Certificate caCert              = Utils.generateRootCert(caPair);
        BigInteger      revokedSerialNumber = BigInteger.valueOf(2);

        //2.- Create a CRL revoking certificate number 2
        X509CRL crl = createCRL(caCert, caPair.getPrivate(), revokedSerialNumber);

        //3.- Encode it and reconstruct it
        ByteArrayInputStream bIn = new ByteArrayInputStream(crl.getEncoded());
        CertificateFactory   fact = CertificateFactory.getInstance("X.509", CryptoDefs.Provider.BC.getName());

        crl = (X509CRL) fact.generateCRL(bIn);

        //4.- Verify the CRL
        crl.verify(caCert.getPublicKey(), CryptoDefs.Provider.BC.getName());

        //5.- Check if the CRL revokes certificate number 2
        final X509CRLEntry entry = crl.getRevokedCertificate(revokedSerialNumber);

        System.out.println("Revocation details:");
        System.out.println("  Certificate number: " + entry.getSerialNumber());
        System.out.println("  Issuer            : " + crl.getIssuerX500Principal());
    }
}
