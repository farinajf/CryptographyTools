/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 * Basic test of the PKCS#5 Scheme 1 implementation.
 * @author fran
 */
public class PKCS5Scheme1Test {

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
    public PKCS5Scheme1Test() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        char[] password = "hello".toCharArray();
        byte[] salt     = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        byte[] input    = new byte[] {0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        int    iterationCount = 100;

        System.out.println("input:   " + Utils.toHex(input));

        //Encryption step using regular PBE
        Cipher           cipher     = Cipher.getInstance(CryptoDefs.Algorithm.PBEWithSHA1AndDES.getName(), CryptoDefs.Provider.BC.getName());
        SecretKeyFactory fact       = SecretKeyFactory.getInstance(CryptoDefs.Algorithm.PBEWithSHA1AndDES.getName(), CryptoDefs.Provider.BC.getName());
        PBEKeySpec       pbeKeySpec = new PBEKeySpec(password, salt, iterationCount);

        cipher.init(Cipher.ENCRYPT_MODE, fact.generateSecret(pbeKeySpec));

        byte[] enc = cipher.doFinal(input);

        System.out.println("encrypt: " + Utils.toHex(enc) + ".");

        //Decryption step - using the local implementation
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.DES, CryptoDefs.Mode.CBC, CryptoDefs.Padding.PKCS5);
        cipher = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        PKCS5Scheme1 pkcs5s1 = new PKCS5Scheme1(MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName()));

        byte[] derivedKey = pkcs5s1.generateDerivedKey(password, salt, iterationCount);

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(derivedKey, 0, 8, CryptoDefs.Algorithm.DES.getName()), new IvParameterSpec(derivedKey, 8, 8));

        byte[] dec = cipher.doFinal(enc);

        System.out.println("decrypt: " + Utils.toHex(dec) + ".");
    }
}
