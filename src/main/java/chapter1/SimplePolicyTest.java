/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter1;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimplePolicyTest {

    /***************************************************************************/
    /*                           Metodos Privados                              */
    /***************************************************************************/


    /***************************************************************************/
    /*                           Metodos Protegidos                            */
    /***************************************************************************/


    /***************************************************************************/
    /*                           Constructores                                 */
    /***************************************************************************/
    public SimplePolicyTest() {}

    /***************************************************************************/
    /*                           Metodos Publicos                              */
    /***************************************************************************/
    public static void main(String [] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

        //1.- Create a 64 bit secret key from raw bytes
        SecretKey key64 = new SecretKeySpec(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07}, CryptoDefs.Algorithm.BLOWFISH.getName());

        //2.- Create a cipher and attempt to encrypt the data block with our key
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.BLOWFISH, CryptoDefs.Mode.ECB, CryptoDefs.Padding.NOPADDING);
        Cipher c         = Cipher.getInstance(transform);

        c.init(Cipher.ENCRYPT_MODE, key64);
        c.doFinal(data);

        System.out.println("64 bit test: passed");

        //3.- Create a 192 bit secret key from raw bytes
        byte [] raw = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                  0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                                  0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
        SecretKey key192 = new SecretKeySpec(raw, CryptoDefs.Algorithm.BLOWFISH.getName());

        //4.- Now try encrypting with the larger key
        c.init(Cipher.ENCRYPT_MODE, key192);
        c.doFinal(data);

        System.out.println("192 bit test: passed");

        System.out.println("Test completed");
    }
}
