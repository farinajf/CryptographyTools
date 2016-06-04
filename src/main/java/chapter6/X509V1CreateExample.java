/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import utils.CryptoDefs;

/**
 *  Basic X.509 V1 Certificate creation.
 * @author fran
 */
public class X509V1CreateExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = X509V1CreateExample.class.getName();

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
    public X509V1CreateExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static X509Certificate generateV1Certificate(KeyPair pair) throws Exception {
        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN    (new X500Principal("CN=Test Certificate"));
        certGen.setNotBefore   (new Date(System.currentTimeMillis() - 50000));
        certGen.setNotAfter    (new Date(System.currentTimeMillis() + 50000));
        certGen.setSubjectDN   (new X500Principal("CN=Test Certificate"));
        certGen.setPublicKey   (pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        return certGen.generateX509Certificate(pair.getPrivate(), CryptoDefs.Provider.BC.getName());
    }

    public static void main(String[] args) throws Exception {
        KeyPair pair = Utils.generateRSAKeyPair();

        X509Certificate cert = generateV1Certificate(pair);

        cert.checkValidity(new Date());

        cert.verify(cert.getPublicKey());

        System.out.println("\t- Valid certificate generated!!");
    }
}
