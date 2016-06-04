/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimpleWrapExample {

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
    public SimpleWrapExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        //Create a key to wrap
        KeyGenerator generator = KeyGenerator.getInstance(CryptoDefs.Algorithm.AES.getName(), CryptoDefs.Provider.BC.getName());
        generator.init(128);

        Key keyToBeWrapped = generator.generateKey();

        System.out.println("input:     " + Utils.toHex(keyToBeWrapped.getEncoded()));

        //Create a wrapper and do the wrapping
        Cipher cipher = Cipher.getInstance(CryptoDefs.Algorithm.AESWrap.getName(), CryptoDefs.Provider.BC.getName());

        KeyGenerator keyGen = KeyGenerator.getInstance(CryptoDefs.Algorithm.AES.getName(), CryptoDefs.Provider.BC.getName());
        keyGen.init(256);

        Key wrapKey = keyGen.generateKey();

        cipher.init(Cipher.WRAP_MODE, wrapKey);

        byte[] wrappedKey = cipher.wrap(keyToBeWrapped);

        System.out.println("wrapped:   " + Utils.toHex(wrappedKey));

        //Unwrap the wrapped key
        cipher.init(Cipher.UNWRAP_MODE, wrapKey);

        Key key = cipher.unwrap(wrappedKey, CryptoDefs.Algorithm.AES.getName(), Cipher.SECRET_KEY);

        System.out.println("unwrapped: " + Utils.toHex(key.getEncoded()));
    }
}
