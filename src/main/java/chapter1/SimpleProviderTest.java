/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter1;

import java.security.Provider;
import java.security.Security;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimpleProviderTest {

    /***************************************************************************/
    /*                           Metodos Privados                              */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Metodos Protegidos                            */
    /***************************************************************************/

    /***************************************************************************/
    /*                           Constructores                                 */
    /***************************************************************************/
    public SimpleProviderTest() {}

    /***************************************************************************/
    /*                           Metodos Publicos                              */
    /***************************************************************************/
    public static void main(String[] args) {
        Provider x            = null;
        String   providerName = CryptoDefs.Provider.BC.getName();

        x = Security.getProvider(providerName);

        if (x == null)
        {
            System.out.println(providerName + " provider not installed!!");
            System.exit (1);
        }

        System.out.println(providerName + " is installed!!");
        System.out.println("\tName:     " + x.getName());
        System.out.println("\tInfo:     " + x.getInfo());
        System.out.println("\tVersion:  " + x.getVersion());
        System.out.println("\ttoString: " + x.toString());
    }
}
