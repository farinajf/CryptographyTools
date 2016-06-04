/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimpleCBCExample {

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
    public SimpleCBCExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input    = InputData.INPUT_2;
        byte[] keyBytes = InputData.KEY_1;
        byte[] ivBytes  = new byte[] {0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

        SecretKeySpec   key    = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.DES.getName());
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        String          transf = CryptoDefs.getTransform(CryptoDefs.Algorithm.DES, CryptoDefs.Mode.CBC, CryptoDefs.Padding.PKCS7);
        Cipher          cipher = Cipher.getInstance(transf, CryptoDefs.Provider.BC.getName());

        System.out.println("input text: \t" + Utils.toHex(input) + " \tbytes: " + input.length);

        //Encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

        ctLength += cipher.doFinal(cipherText, ctLength);

        System.out.println("cipher text: \t" + Utils.toHex(cipherText, ctLength) + " \tbytes: " + ctLength);

        System.out.println("Parameters:           " + cipher.getParameters().toString());
        System.out.println("Parameters.algorithm: " + cipher.getParameters().getAlgorithm());
        System.out.println("Parameters.encoded:   " + Utils.toHex(cipher.getParameters().getEncoded()));

        //Decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

        ptLength += cipher.doFinal(plainText, ptLength);

        System.out.println("plain text: \t" + Utils.toHex(plainText, ptLength) + " \tbytes: " + ptLength);
    }
}
