/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class TamperedWithDigestExample {

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
    public TamperedWithDigestExample() {}

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
        MessageDigest   hash      = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName());

        System.out.println("input: " + input);

        //Encryption step
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length() + hash.getDigestLength())];

        int ctLength = cipher.update(Utils.toByteArray(input), 0, input.length(), cipherText, 0);

        hash.update(Utils.toByteArray(input));

        ctLength += cipher.doFinal(hash.digest(), 0, hash.getDigestLength(), cipherText, ctLength);

        //Tampering step
        cipherText[9] ^= '0' ^ '9';

        //Decryption step
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] plainText     = cipher.doFinal(cipherText, 0, ctLength);
        int    messageLength = plainText.length - hash.getDigestLength();

        hash.update(plainText, 0, messageLength);

        byte[] messageHash = new byte[hash.getDigestLength()];
        System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);

        System.out.println("plain: " + Utils.toString(plainText, messageLength));

        byte[] digest = hash.digest();

        System.out.println("\t Verified: " + MessageDigest.isEqual(digest, messageHash));

        System.out.println("\t digest:       " + Utils.toHex(digest));
        System.out.println("\t message hash: " + Utils.toHex(messageHash));
    }
}
