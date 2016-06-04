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
public class ListBCCapabilities {

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
    public ListBCCapabilities() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) {
        Provider provider = Security.getProvider(CryptoDefs.Provider.BC.getName());

        for (Object x : provider.keySet())
        {
            String entry = (String) x;

            if (entry.startsWith("Alg.Alias.")) entry = entry.substring("Alg.Alias.".length());

            String factoryClass = entry.substring(0, entry.indexOf('.'));
            String name         = entry.substring(factoryClass.length() + 1);

            System.out.println(factoryClass + ": " + name);
        }

        System.out.println("\n-------------------------------------------\n");

        for (Provider.Service service : provider.getServices())
        {
            System.out.println("service: " + service.toString());
        }
    }
}
