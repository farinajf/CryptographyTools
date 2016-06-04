/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class Utils extends chapter2.Utils {

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
    public Utils() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * Create a key for use with AES.
     *
     * @param bitLength
     * @param random
     * @return A secret key of requested bitLength
     */
    public static SecretKey createKeyForAES(int bitLength, SecureRandom random) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(CryptoDefs.Algorithm.AES.getName(), CryptoDefs.Provider.BC.getName());

        generator.init(bitLength, random);

        return generator.generateKey();
    }

    /**
     * Cresate an IV suitable for using with AES in CTR mode.
     * The IV will be composed of 4 bytes of message number, 4 bytes of random
     * data, and a counter of 8 bytes.
     *
     * @param messageNumber The number of the message.
     * @param random        A source of randomness
     * @return An initialised IvParameterSpec
     */
    public static IvParameterSpec createCtrIvForAES(int messageNumber, SecureRandom random) {
        byte[] ivBytes = new byte[16];

        //Initially randomize
        random.nextBytes(ivBytes);

        //Set the message number bytes
        ivBytes[0] = (byte) (messageNumber >> 24);
        ivBytes[1] = (byte) (messageNumber >> 16);
        ivBytes[2] = (byte) (messageNumber >>  8);
        ivBytes[3] = (byte) (messageNumber >>  0);

        //Set the counter bytes to 1
        for (int i = 0; i < 7; i++) ivBytes[8 + i] = 0;

        ivBytes[15] = 1;

        return new IvParameterSpec(ivBytes);
    }

    /**
     * Convert a byte array of 8 bit characters into a String.
     *
     * @param bytes
     * @param length
     * @return
     */
    public static String toString(byte[] bytes, int length) {
        char[] chars = new char[length];

        for (int i = 0; i < chars.length; i++) chars[i] = (char) (bytes[i] & 0xff);

        return new String(chars);
    }

    /**
     * Convert a byte array of 8 bit characters into a String.
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, bytes.length);
    }

    /**
     * Convert the passed String to a byte array by taking the bottom 8
     * bits of each character it contains.
     * @param string
     * @return
     */
    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];
        char[] chars = string.toCharArray();

        for (int i = 0; i < chars.length; i++) bytes[i] = (byte) chars[i];

        return bytes;
    }
}
