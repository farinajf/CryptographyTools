/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author fran
 */
public class Utils {
    private static String _DIGITS = "0123456789abcdef";

    /***************************************************************************/
    /*                           Metodos Privados                              */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Metodos Protegidos                            */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Constructores                                 */
    /***************************************************************************/
    public Utils() {}

    /***************************************************************************/
    /*                           Metodos Publicos                              */
    /***************************************************************************/
    /**
     *
     * @param data
     * @param length
     * @return
     */
    public static String toHex(byte[] data, int length) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < length; i++)
        {
            int v = data[i] & 0xFF;

            result.append(_DIGITS.charAt(v >> 4));
            result.append(_DIGITS.charAt(v & 0x0F));
        }

        return result.toString();
    }

    /**
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {return toHex(data, data.length);}

    /**
     * Devuelve un array de bytes.
     * @param x
     * @return
     */
    public static byte[] base64Decode(final String x) {
        return Base64.decodeBase64(x);
    }

    /**
     * Devuelve una representacion en Base64 de x.
     * @param x
     * @return
     */
    public static String base64Encode(final byte[] x) {
        return Base64.encodeBase64String(x);
    }
}
