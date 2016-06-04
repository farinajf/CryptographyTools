/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter1;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class PrecedenceTest {

    /***************************************************************************/
    /*                           Metodos Privados                              */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Metodos Protegidos                            */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Constructores                                 */
    /***************************************************************************/
    public PrecedenceTest() {}

    /***************************************************************************/
    /*                           Metodos Publicos                              */
    /***************************************************************************/
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        Cipher cipher    = null;
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.BLOWFISH, CryptoDefs.Mode.ECB, CryptoDefs.Padding.NOPADDING);

        cipher = Cipher.getInstance(transform);
        System.out.println("cipher.getProvider: " + cipher.getProvider());

        cipher = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());
        System.out.println("cipher.getProvider: " + cipher.getProvider());
    }
}
