/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.MessageDigest;

/**
 * A basic implementation of PKCS#5 Scheme 1.
 * @author fran
 */
public class PKCS5Scheme1 {
    private MessageDigest _digest;

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
    public PKCS5Scheme1(final MessageDigest x) {_digest = x;}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public byte[] generateDerivedKey(char[] password, byte[] salt, int iterationCount) {
        for (int i = 0; i < password.length; i++) _digest.update((byte) password[i]);

        _digest.update(salt);

        byte[] digestBytes = _digest.digest();

        for (int i = 1; i < iterationCount; i++)
        {
            _digest.update(digestBytes);
            digestBytes = _digest.digest();
        }

        return digestBytes;
    }
}
