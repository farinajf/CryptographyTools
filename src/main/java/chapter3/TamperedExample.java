/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import utils.CryptoDefs;

/**
 * Tampered message, plain encryption, AES in CTR mode.
 * @author fran
 */
public class TamperedExample {

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
    public TamperedExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        SecureRandom    random    = new SecureRandom();
        IvParameterSpec ivSpec    = Utils.createCtrIvForAES(1, random);
        Key             key       = Utils.createKeyForAES(256, random);
        String          algorithm = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.CTR, CryptoDefs.Padding.NOPADDING);
        Cipher          cipher    = Cipher.getInstance(algorithm, CryptoDefs.Provider.BC.getName());
        String          input     = "Transfer 0000100 to AC 1234-5678";

        System.out.println("input: " + input);

        //Encryption step
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] cipherText = cipher.doFinal(Utils.toByteArray(input));

        //Tampering step
        cipherText[9] ^= '0' ^ '9';

        //Decryption step
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] plainText = cipher.doFinal(cipherText);

        System.out.println("plain: " + Utils.toString(plainText));
    }
}
