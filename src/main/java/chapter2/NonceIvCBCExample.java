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
public class NonceIvCBCExample {

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
    public NonceIvCBCExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * CBC using DES with an IV based on a nonce. In this case a hypothetical
     * message number.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        byte[] input     = InputData.INPUT_2;
        byte[] keyBytes  = InputData.KEY_1;
        byte[] msgNumber = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        SecretKeySpec   key    = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.DES.getName());
        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);

        String          transf = CryptoDefs.getTransform(CryptoDefs.Algorithm.DES, CryptoDefs.Mode.CBC, CryptoDefs.Padding.PKCS7);
        Cipher          cipher = Cipher.getInstance(transf, CryptoDefs.Provider.BC.getName());

        System.out.println("input text: \t" + Utils.toHex(input) + " \tbytes: " + input.length);

        //1.-Encryption pass

        //1.1.-Generate IV
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

        IvParameterSpec encryptionIv = new IvParameterSpec(cipher.doFinal(msgNumber), 0, 8);

        //1.2.-Encrypt message
        cipher.init(Cipher.ENCRYPT_MODE, key, encryptionIv);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int ctLength = cipher.update (input,      0, input.length, cipherText, 0);
        ctLength    += cipher.doFinal(cipherText, ctLength);

        System.out.println("cipher text: \t" + Utils.toHex(cipherText, ctLength) + " \tbytes: " + ctLength);

        //2.-Decryption pass

        //2.1.-Generate IV
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

        IvParameterSpec decryptionIv = new IvParameterSpec(cipher.doFinal(msgNumber), 0, 8);

        //2.2.-Decrypt message
        cipher.init(Cipher.DECRYPT_MODE, key, decryptionIv);

        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength    += cipher.doFinal(plainText, ptLength);

        System.out.println("plain text: \t" + Utils.toHex(plainText, plainText.length) + " \tbytes: " + plainText.length);
    }
}
