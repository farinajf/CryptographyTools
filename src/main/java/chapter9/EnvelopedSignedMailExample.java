/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMEUtil;
import utils.CryptoDefs;

/**
 * A simple example that creates and processes an enveloped signed mail
 * message.
 * @author fran
 */
public class EnvelopedSignedMailExample extends SignedDataProcessor implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = EnvelopedSignedMailExample.class.getName();

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
    public EnvelopedSignedMailExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyStore      credentials = Utils.createCredentials();
        PrivateKey    key         = (PrivateKey) credentials.getKey(Utils.END_ENTITY_ALIAS, Utils.KEY_PASSWD);
        Certificate[] chain       = credentials.getCertificateChain(Utils.END_ENTITY_ALIAS);

        CertStore certsAndCRLs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(chain)), CryptoDefs.Provider.BC.getName());

        X509Certificate cert = (X509Certificate) chain[0];

        //1.- Create the message we want signed
        MimeBodyPart dataPart = new MimeBodyPart();

        dataPart.setText("Hello World!!");

        //2.- Create the signed message
        MimeMultipart signedMulti = SignedMailExample.createMultipartWithSignature(key, cert, certsAndCRLs, dataPart);

        //3.- Create the body part containing the signed message
        MimeBodyPart signedPart = new MimeBodyPart();

        signedPart.setContent(signedMulti);

        //4.- Set up the generator
        SMIMEEnvelopedGenerator gen = new SMIMEEnvelopedGenerator();

        gen.addKeyTransRecipient(cert);

        //5.- Generate the enveloped message
        MimeBodyPart envPart = gen.generate(signedPart, SMIMEEnvelopedGenerator.AES256_CBC, CryptoDefs.Provider.BC.getName());

        //6.- Create the mail message
        MimeMessage mail = Utils.createMimeMessage("example signed and enveloped message", envPart.getContent(), envPart.getContentType());

        //7.- Create the enveloped object from the mail message
        SMIMEEnveloped enveloped = new SMIMEEnveloped(mail);

        //8.- Look for our recipient identifier
        RecipientId recId = new KeyTransRecipientId(new X500Name(cert.getIssuerX500Principal().getName()), cert.getSerialNumber());

        RecipientInformationStore recipients = enveloped.getRecipientInfos();
        RecipientInformation      recipient = recipients.get(recId);

        //9.- Decryption step
        MimeBodyPart res = SMIMEUtil.toMimeBodyPart(recipient.getContent(key, CryptoDefs.Provider.BC.getName()));

        //10.- Extract the multipart from the body part
        if (res.getContent() instanceof MimeMultipart)
        {
            SMIMESigned signed = new SMIMESigned((MimeMultipart) res.getContent());

            //11.- Verification step
            X509Certificate rootCert = (X509Certificate) credentials.getCertificate(Utils.ROOT_ALIAS);

            if (isValid(signed, rootCert)) System.out.println("\t verification succeeded!!");
            else System.out.println("\t verification failed!!");

            //12.- Content display step
            MimeBodyPart content = signed.getContent();

            System.out.print  ("\t Content: ");
            System.out.println(content.getContent());
        }
        else System.out.println("\t wrong content found!!");
    }
}
