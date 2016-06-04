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
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import utils.CryptoDefs;

/**
 *  Basic X.509 V1 Certificate creation.
 * @author fran
 */
public class X509V3CreateExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = X509V3CreateExample.class.getName();

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
    public X509V3CreateExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static X509Certificate generateV3Certificate(KeyPair pair) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN    (new X500Principal("CN=Test Certificate"));
        certGen.setNotBefore   (new Date(System.currentTimeMillis() - 50000));
        certGen.setNotAfter    (new Date(System.currentTimeMillis() + 50000));
        certGen.setSubjectDN   (new X500Principal("CN=Test Certificate"));
        certGen.setPublicKey   (pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        // Extension ::= SEQUENCE {
        //  extnID      OBJECT IDENTIFIER,
        //  critical    BOOLEAN DEFAULT FALSE
        //  extnValue   OCTET STRING }
        certGen.addExtension(X509Extensions.BasicConstraints,       true,  new BasicConstraints(false));
        certGen.addExtension(X509Extensions.KeyUsage,               true,  new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
        certGen.addExtension(X509Extensions.ExtendedKeyUsage,       true,  new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));
        certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new GeneralNames(new GeneralName(GeneralName.rfc822Name, "test@test.test")));

        return certGen.generateX509Certificate(pair.getPrivate(), CryptoDefs.Provider.BC.getName());
    }

    public static void main(String[] args) throws Exception {
        KeyPair pair = Utils.generateRSAKeyPair();

        X509Certificate cert = generateV3Certificate(pair);

        cert.checkValidity(new Date());

        cert.verify(cert.getPublicKey());

        System.out.println("\t- Valid certificate generated!!");
    }
}
