/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.security.KeyStore;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.x500.X500PrivateCredential;
import utils.CryptoDefs;

/**
 * Chapter 9 Utils.
 * @author fran
 */
public class Utils extends chapter8.Utils implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = Utils.class.getName();

    public static char[] KEY_PASSWD = "keyPassword".toCharArray();

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
     * Create a KeyStore containing the a private credential with
     * certificate chain and a trust anchor.
     * @return
     * @throws Exception
     */
    public static KeyStore createCredentials() throws Exception {
        KeyStore store = KeyStore.getInstance(CryptoDefs.KeyStoreType.JKS.getName());

        store.load(null, null);

        X500PrivateCredential rootCredential  = Utils.createRootCredential();
        X500PrivateCredential interCredential = Utils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential endCredential   = Utils.createEndEntityCredential(interCredential.getPrivateKey(),   interCredential.getCertificate());

        store.setCertificateEntry(rootCredential.getAlias(), rootCredential.getCertificate());

        store.setKeyEntry(endCredential.getAlias(), endCredential.getPrivateKey(), KEY_PASSWD, new Certificate[] {
                                                                                                    endCredential.getCertificate(),
                                                                                                    interCredential.getCertificate(),
                                                                                                    rootCredential.getCertificate()});

        return store;
    }

    /**
     * Build a path using the given root as the trust anchor, and the
     * passed in end constraints and certificate store.
     * Note: the path is built with revocation checking turned off.
     *
     * @param rootCert
     * @param endConstraints
     * @param certsAndCRLs
     * @return
     * @throws Exception
     */
    public static PKIXCertPathBuilderResult buildPath(X509Certificate rootCert, X509CertSelector endConstraints, CertStore certsAndCRLs) throws Exception {
        CertPathBuilder       builder     = CertPathBuilder.getInstance("PKIX", CryptoDefs.Provider.BC.getName());
        PKIXBuilderParameters buildParams = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(rootCert, null)), endConstraints);

        buildParams.addCertStore        (certsAndCRLs);
        buildParams.setRevocationEnabled(false);

        return (PKIXCertPathBuilderResult) builder.build(buildParams);
    }

    /**
     * Create a MIME message from using the passed-in content.
     * @param subject
     * @param content
     * @param contentType
     * @return
     * @throws MessagingException
     */
    public static MimeMessage createMimeMessage(String subject, Object content, String contentType) throws MessagingException {
        Properties props   = System.getProperties();
        Session    session = Session.getDefaultInstance(props, null);

        Address fromUser = new InternetAddress("\"Fran F\"<jfarang@gmail.com>");
        Address toUser   = new InternetAddress("example@bouncycastle.org");

        MimeMessage message = new MimeMessage(session);

        message.setFrom     (fromUser);
        message.setRecipient(Message.RecipientType.TO, toUser);
        message.setSubject  (subject);
        message.setContent  (content, contentType);
        message.saveChanges ();

        return message;
    }
}
