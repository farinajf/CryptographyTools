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
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;
import utils.CryptoDefs;

/**
 * A simple example that creates and processes a signed mail message.
 * @author fran
 */
public class SignedMailExample extends SignedDataProcessor implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SignedMailExample.class.getName();

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
    public SignedMailExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param key
     * @param cert
     * @param certsAndCRLs
     * @param dataPart
     * @return
     * @throws Exception
     */
    public static MimeMultipart createMultipartWithSignature(PrivateKey key, X509Certificate cert, CertStore certsAndCRLs, MimeBodyPart dataPart) throws Exception {
        //1.- Create some smime capabilities in case someone wants to respond
        ASN1EncodableVector   signedAttrs = new ASN1EncodableVector();
        SMIMECapabilityVector caps        = new SMIMECapabilityVector();

        caps.addCapability(SMIMECapability.aES256_CBC);
        caps.addCapability(SMIMECapability.dES_EDE3_CBC);
        caps.addCapability(SMIMECapability.rC2_CBC, 128);

        signedAttrs.add(new SMIMECapabilitiesAttribute(caps));
        signedAttrs.add(new SMIMEEncryptionKeyPreferenceAttribute(SMIMEUtil.createIssuerAndSerialNumberFor(cert)));

        //2.- Set up the generator
        SMIMESignedGenerator gen = new SMIMESignedGenerator();

        gen.addSigner(key, cert, SMIMESignedGenerator.DIGEST_SHA256, new AttributeTable(signedAttrs), null);

        gen.addCertificatesAndCRLs(certsAndCRLs);

        //3.- Create the signed message
        return gen.generate(dataPart, CryptoDefs.Provider.BC.getName());
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyStore      credentials  = Utils.createCredentials();
        PrivateKey    key          = (PrivateKey) credentials.getKey(Utils.END_ENTITY_ALIAS, Utils.KEY_PASSWD);
        Certificate[] chain        = credentials.getCertificateChain(Utils.END_ENTITY_ALIAS);
        CertStore     certsAndCRLs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(chain)), CryptoDefs.Provider.BC.getName());

        X509Certificate cert = (X509Certificate) chain[0];

        //1.- Create the message we want signed
        MimeBodyPart dataPart = new MimeBodyPart();

        dataPart.setText("Hello World!!");

        //2.- Create the signed message
        MimeMultipart multiPart = createMultipartWithSignature(key, cert, certsAndCRLs, dataPart);

        //3.- Create the mail message
        MimeMessage mail = Utils.createMimeMessage("example signed message", multiPart, multiPart.getContentType());

        //4.- Extract the message from the mail message
        if (mail.isMimeType("multipart/signed"))
        {
            SMIMESigned signed = new SMIMESigned((MimeMultipart) mail.getContent());

            //5.- Verification step
            X509Certificate rootCert = (X509Certificate) credentials.getCertificate(Utils.ROOT_ALIAS);

            if (isValid(signed, rootCert)) System.out.println("\t verification succeeded!!");
            else System.out.println("\t verification failed!!");

            //6.- Content display step
            MimeBodyPart content = signed.getContent();

            System.out.print  ("\t Content: ");
            System.out.println(content.getContent());
        }
        else System.out.println("\t wrong content found!!");
    }
}
