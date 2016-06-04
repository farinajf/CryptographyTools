/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import java.security.MessageDigest;
import java.security.SecureRandom;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class Utils extends chapter3.Utils {
    public static final String N1 = "d46f473a2d746537de2056ae3092c451";
    public static final String N2 = "57791d5430d593164082036ad8b29fb1";

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/
    private static class FixedRand extends SecureRandom {
        MessageDigest _sha;
        byte[]        _state;

        FixedRand() {
            try
            {
                _sha   = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName());
                _state = _sha.digest();
            }
            catch (Exception e)
            {
                throw new RuntimeException("ERROR. Can't find SHA-1!!");
            }
        }

        @Override
        public void nextBytes(byte[] bytes) {
            int off = 0;

            _sha.update(_state);

            while (off < bytes.length)
            {
                _state = _sha.digest();

                if (bytes.length - off > _state.length) System.arraycopy(_state, 0, bytes, off, _state.length);
                else                                    System.arraycopy(_state, 0, bytes, off, bytes.length - off);

                off += _state.length;

                _sha.update(_state);
            }
        }
    }

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
     * Return a SecureRandom which produces the same value.
     * @return
     */
    public static SecureRandom createFixedRandom() {return new FixedRand();}
}
