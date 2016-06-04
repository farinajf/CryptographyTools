/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class AESWrapRSAExample {

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
    public AESWrapRSAExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.ECB, CryptoDefs.Padding.PKCS7);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        SecureRandom random = new SecureRandom();

        // Create the keys
        KeyPairGenerator fact = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        fact.initialize(1024, random);

        KeyPair keyPair = fact.generateKeyPair();
        Key     wrapKey = Utils.createKeyForAES(256, random);

        // Wrap the RSA private key
        cipher.init(Cipher.WRAP_MODE, wrapKey);

        byte[] wrappedKey = cipher.wrap(keyPair.getPrivate());

        // Unwrao the RSA private key
        cipher.init(Cipher.UNWRAP_MODE, wrapKey);

        Key key = cipher.unwrap(wrappedKey, CryptoDefs.Algorithm.RSA.getName(), Cipher.PRIVATE_KEY);

        System.out.println("wrappedKey: " + Utils.toHex(wrappedKey));
        System.out.println("key:        " + Utils.toHex(key.getEncoded()));

        if (keyPair.getPrivate().equals(key)) System.out.println("Key recovered!!");
        else System.out.println("Key recovery failed!!");
    }
}
