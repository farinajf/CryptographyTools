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
public class SimpleECBExample {

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
    public SimpleECBExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input    = InputData.INPUT_2;
        byte[] keyBytes = InputData.KEY_1;

        SecretKeySpec key = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.DES.getName());

        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.DES, CryptoDefs.Mode.ECB, CryptoDefs.Padding.PKCS7);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        System.out.println("input text: \t" + Utils.toHex(input) + " \tbytes: " + input.length);

        //Encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

        ctLength += cipher.doFinal(cipherText, ctLength);

        System.out.println("cipher text: \t" + Utils.toHex(cipherText) + " \tbytes: " + ctLength);

        //Decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

        ptLength += cipher.doFinal(plainText, ptLength);

        System.out.println("plain text: \t" + Utils.toHex(plainText) + " \tbytes: " + ptLength);
    }
}
