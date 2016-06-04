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
import java.util.Arrays;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import utils.CryptoDefs;

/**
 * Demostrate creation and processing a public key recipient
 * enveloped-message.
 * @author fran
 */
public class KeyTransEnvelopedDataExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = KeyTransEnvelopedDataExample.class.getName();

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
    public KeyTransEnvelopedDataExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyStore   credentials = Utils.createCredentials();
        PrivateKey key         = (PrivateKey) credentials.getKey(Utils.END_ENTITY_ALIAS, Utils.KEY_PASSWD);

        Certificate[] chain = credentials.getCertificateChain(Utils.END_ENTITY_ALIAS);

        X509Certificate cert = (X509Certificate) chain[0];

        //1.- Set up the generator
        CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();

        gen.addKeyTransRecipient(cert);

        //2.- Create the enveloped-data object
        CMSProcessable   data      = new CMSProcessableByteArray("Hello World!!".getBytes());
        CMSEnvelopedData enveloped = gen.generate(data, CMSEnvelopedDataGenerator.AES128_CBC, CryptoDefs.Provider.BC.getName());

        //3.- Re-create
        enveloped = new CMSEnvelopedData(enveloped.getEncoded());

        //4.- Look for our recipient identifier
        RecipientId recId = new KeyTransRecipientId(new X500Name(cert.getIssuerX500Principal().getName()), cert.getSerialNumber());

        recId.setSerialNumber(cert.getSerialNumber());
        recId.setIssuer      (cert.getIssuerX500Principal().getEncoded());

        RecipientInformationStore recipients = enveloped.getRecipientInfos();
        RecipientInformation      recipient  = recipients.get(recId);

        if (recipient != null)
        {
            //5.- Decrypt the data
            byte[] recData = recipient.getContent(key, CryptoDefs.Provider.BC.getName());

            //6.- Compare recovered data to the original data
            if (Arrays.equals((byte[]) data.getContent(), recData)) System.out.println("\t data recovery succeeded!!");
            else System.out.println("\t data recovery failed!!");
        }
        else System.out.println("\t Could not find a matching recipient!!");
    }
}
