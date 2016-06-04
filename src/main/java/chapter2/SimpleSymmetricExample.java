/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimpleSymmetricExample {

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
    public SimpleSymmetricExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     */
    public static void main(String...args) throws Exception {
        byte[] input    = InputData.INPUT_0;
        byte[] keyBytes = InputData.KEY_0;

        SecretKeySpec key = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.AES.getName());

        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.ECB, CryptoDefs.Padding.NOPADDING);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        System.out.println("input text: \t" + Utils.toHex(input) + " \tbytes: " + input.length);

        //Encryption pass
        byte[] cipherText = new byte[input.length];

        cipher.init(Cipher.ENCRYPT_MODE, key);

        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

        ctLength += cipher.doFinal(cipherText, ctLength);

        System.out.println("cipher text: \t" + Utils.toHex(cipherText) + " \tbytes: " + ctLength);

        //Decryption pass
        byte[] plainText = new byte[ctLength];

        cipher.init(Cipher.DECRYPT_MODE, key);

        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

        ptLength += cipher.doFinal(plainText, ptLength);

        System.out.println("plain text: \t" + Utils.toHex(plainText) + " \tbytes: " + ptLength);
    }
}
