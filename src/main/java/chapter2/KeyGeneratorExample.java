/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class KeyGeneratorExample {

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
    public KeyGeneratorExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input   = InputData.INPUT_2;
        byte[] ivBytes = new byte[] {0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
                                     0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};

        String       transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.CTR, CryptoDefs.Padding.NOPADDING);
        Cipher       cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());
        KeyGenerator generator = KeyGenerator.getInstance(CryptoDefs.Algorithm.AES.getName(), CryptoDefs.Provider.BC.getName());

        generator.init(192);

        Key encriptionKey = generator.generateKey();

        System.out.println("e-key: " + Utils.toHex(encriptionKey.getEncoded()));
        System.out.println("input: " + Utils.toHex(input));

        //Encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, encriptionKey, new IvParameterSpec(ivBytes));

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

        ctLength += cipher.doFinal(cipherText, ctLength);

        //Create our decryption key using information extracted from the encryption key
        Key decriptionKey = new SecretKeySpec(encriptionKey.getEncoded(), encriptionKey.getAlgorithm());

        cipher.init(Cipher.DECRYPT_MODE, decriptionKey, new IvParameterSpec(ivBytes));

        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

        int ptLength = cipher.update(cipherText, 0, cipherText.length, plainText, 0);

        ptLength += cipher.doFinal(plainText, ptLength);

        System.out.println("d-key: " + Utils.toHex(decriptionKey.getEncoded()));
        System.out.println("plain: " + Utils.toHex(input));
    }
}
