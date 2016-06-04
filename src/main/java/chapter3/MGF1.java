/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import java.security.MessageDigest;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class MGF1 {

    private MessageDigest _digest;

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/
    /**
     * Convert and integer into a byte array, high byte first.
     * @param i
     * @param sp
     */
    private void _ItoOSP(int i, byte[] sp) {
        sp[0] = (byte) (i >>> 24);
        sp[1] = (byte) (i >>> 16);
        sp[2] = (byte) (i >>>  8);
        sp[3] = (byte) (i >>>  0);
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
    public MGF1(final MessageDigest digest) {_digest = digest;}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public byte[] generateMask(byte[] seed, int length) {
        byte[] mask    = new byte[length];
        byte[] C       = new byte[4];
        int    counter = 0;
        int    hLen    = _digest.getDigestLength();

        while (counter < (length / hLen))
        {
            _ItoOSP(counter, C);

            _digest.update(seed);
            _digest.update(C);

            System.arraycopy(_digest.digest(), 0, mask, counter * hLen, hLen);

            counter++;
        }

        if ((counter * hLen) < length)
        {
            _ItoOSP(counter, C);

            _digest.update(seed);
            _digest.update(C);

            System.arraycopy(_digest.digest(), 0, mask, counter * hLen, mask.length - (counter * hLen));
        }

        return mask;
    }

    public static void main(String[] args) throws Exception {
        MGF1   mgf1   = new MGF1(MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName()));
        byte[] source = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        System.out.println(Utils.toHex(mgf1.generateMask(source, 20)));
    }
}
