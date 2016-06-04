/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class InlineIvCBCExample {

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
    public InlineIvCBCExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * Inline IV: save you the trouble of passing the IV out of band with
     * the encrypted message.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        byte[] input    = InputData.INPUT_2;
        byte[] keyBytes = InputData.KEY_1;
        byte[] ivBytes  = new byte[] {0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

        SecretKeySpec   key    = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.DES.getName());
        IvParameterSpec ivSpec = new IvParameterSpec(new byte[8]);

        String          transf = CryptoDefs.getTransform(CryptoDefs.Algorithm.DES, CryptoDefs.Mode.CBC, CryptoDefs.Padding.PKCS7);
        Cipher          cipher = Cipher.getInstance(transf, CryptoDefs.Provider.BC.getName());

        System.out.println("input text: \t" + Utils.toHex(input) + " \tbytes: " + input.length);

        //Encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] cipherText = new byte[cipher.getOutputSize(ivBytes.length + input.length)];

        int ctLength = cipher.update(ivBytes, 0, ivBytes.length, cipherText, 0);
        ctLength    += cipher.update(input,   0, input.length,   cipherText, ctLength);
        ctLength    += cipher.doFinal(cipherText, ctLength);

        System.out.println("cipher text: \t" + Utils.toHex(cipherText, ctLength) + " \tbytes: " + ctLength);

        //Decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] buf = new byte[cipher.getOutputSize(ctLength)];

        int bufLength = cipher.update(cipherText, 0, ctLength, buf, 0);

        bufLength += cipher.doFinal(buf, bufLength);

        //Remove the IV from the start of the message
        byte[] plainText = new byte[bufLength -ivBytes.length];

        System.arraycopy(buf, ivBytes.length, plainText, 0, plainText.length);

        System.out.println("plain text: \t" + Utils.toHex(plainText, plainText.length) + " \tbytes: " + plainText.length);
    }
}
