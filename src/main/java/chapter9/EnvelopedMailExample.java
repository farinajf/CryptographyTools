/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;
import utils.CryptoDefs;

/**
 * A simple example that creates and processes an enveloped mail message.
 * @author fran
 */
public class EnvelopedMailExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = EnvelopedMailExample.class.getName();

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
    public EnvelopedMailExample() {}

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

        X509Certificate cert = (X509Certificate) chain[0];

        //1.- Create the message we want encrypted
        MimeBodyPart dataPart = new MimeBodyPart();

        dataPart.setText("Hello World!!");

        //2.- Set up the generator
        SMIMEEnvelopedGenerator gen = new SMIMEEnvelopedGenerator();

        gen.addKeyTransRecipient(cert);

        //3.- Generate the enveloped message
        MimeBodyPart envPart = gen.generate(dataPart, SMIMEEnvelopedGenerator.AES256_CBC, CryptoDefs.Provider.BC.getName());

        //4.- Create the mail message
        MimeMessage mail = Utils.createMimeMessage("example enveloped message", envPart.getContent(), envPart.getContentType());

        //5.- Create the enveloped object from the mail message
        SMIMEEnveloped enveloped = new SMIMEEnveloped(mail);

        //6.- Look for our recipient identifier
        RecipientId recId = new KeyTransRecipientId(new X500Name(cert.getIssuerX500Principal().getName()), cert.getSerialNumber());

        RecipientInformationStore recipients = enveloped.getRecipientInfos();
        RecipientInformation      recipient = recipients.get(recId);

        if (recipient != null)
        {
            //7.- Decryption step
            MimeBodyPart recoveredPart = SMIMEUtil.toMimeBodyPart(recipient.getContent(key, CryptoDefs.Provider.BC.getName()));

            //8.- Content display step
            System.out.print  ("\t Content:");
            System.out.println(recoveredPart.getContent());
        }
        else System.out.println("\t could not find a matching recipient!!");
    }
}
