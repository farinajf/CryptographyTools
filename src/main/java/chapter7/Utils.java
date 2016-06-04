/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class Utils extends chapter6.Utils implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = Utils.class.getName();

    private static final int VALIDITY_PERIOD = 7 * 24 * 60 * 60 * 1000; // One week

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
    public Utils() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * Generate a sample V1 certificate to use as a CA root certificate.
     * @param pair
     * @return
     * @throws Exception
     */
    public static X509Certificate generateRootCert(final KeyPair pair) throws Exception {
        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

        certGen.setSerialNumber      (BigInteger.ONE);
        certGen.setIssuerDN          (new X500Principal("CN=Test CA Certificate"));
        certGen.setNotBefore         (new Date(System.currentTimeMillis()));
        certGen.setNotAfter          (new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
        certGen.setSubjectDN         (new X500Principal("CN=Test CA Certificate"));
        certGen.setPublicKey         (pair.getPublic());
        certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

        return certGen.generateX509Certificate(pair.getPrivate(), CryptoDefs.Provider.BC.getName());
    }

    /**
     * Generate a sample V3 certificate to use as an intermediate CA certificate.
     * @param intKey
     * @param caKey
     * @param caCert
     * @return
     * @throws Exception
     */
    public static X509Certificate generateIntermediateCert(final PublicKey intKey, final PrivateKey caKey, final X509Certificate caCert) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        certGen.setSerialNumber      (BigInteger.ONE);
        certGen.setIssuerDN          (caCert.getSubjectX500Principal());
        certGen.setNotBefore         (new Date(System.currentTimeMillis()));
        certGen.setNotAfter          (new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
        certGen.setSubjectDN         (new X500Principal("CN=Test Intermediate Certificate"));
        certGen.setPublicKey         (intKey);
        certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
        certGen.addExtension(X509Extensions.SubjectKeyIdentifier,   false, new SubjectKeyIdentifierStructure(intKey));
        certGen.addExtension(X509Extensions.BasicConstraints,       true,  new BasicConstraints(0));
        certGen.addExtension(X509Extensions.KeyUsage,               true,  new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

        return certGen.generateX509Certificate(caKey, CryptoDefs.Provider.BC.getName());
    }

    /**
     * Generate a sample V3 certificate to use as an end entity certificate.
     * @param entityKey
     * @param caKey
     * @param caCert
     * @return
     * @throws Exception
     */
    public static X509Certificate generateEndEntityCert(final PublicKey entityKey, final PrivateKey caKey, final X509Certificate caCert) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        certGen.setSerialNumber      (BigInteger.ONE);
        certGen.setIssuerDN          (caCert.getSubjectX500Principal());
        certGen.setNotBefore         (new Date(System.currentTimeMillis()));
        certGen.setNotAfter          (new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
        certGen.setSubjectDN         (new X500Principal("CN=Test End Certificate"));
        certGen.setPublicKey         (entityKey);
        certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
        certGen.addExtension(X509Extensions.SubjectKeyIdentifier,   false, new SubjectKeyIdentifierStructure(entityKey));
        certGen.addExtension(X509Extensions.BasicConstraints,       true,  new BasicConstraints(false));
        certGen.addExtension(X509Extensions.KeyUsage,               true,  new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

        return certGen.generateX509Certificate(caKey, CryptoDefs.Provider.BC.getName());
    }
}
