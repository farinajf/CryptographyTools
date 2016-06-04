/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.KEKRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import utils.CryptoDefs;

/**
 * Demostrate creation and processing a key-encrypted key enveloped-message.
 * @author fran
 */
public class KEKEnvelopedDataExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = KEKEnvelopedDataExample.class.getName();

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
    public KEKEnvelopedDataExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(CryptoDefs.Algorithm.DESede.getName(), CryptoDefs.Provider.BC.getName());
        SecretKey    key    = keyGen.generateKey();

        //1.- Set up the generator
        CMSEnvelopedDataGenerator edGen = new CMSEnvelopedDataGenerator();

        byte[] kekID = new byte[] {1,2,3,4,5};

        edGen.addKEKRecipient(key, kekID);

        //2.- Create the enveloped-data object
        CMSProcessable   data      = new CMSProcessableByteArray("Hello World!!".getBytes());
        CMSEnvelopedData enveloped = edGen.generate(data, CMSEnvelopedDataGenerator.AES128_CBC, CryptoDefs.Provider.BC.getName());

        //3.- Re-create
        enveloped = new CMSEnvelopedData(enveloped.getEncoded());

        //4.- Look for our recipient identifier
        RecipientId recId = new KEKRecipientId(kekID);

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
