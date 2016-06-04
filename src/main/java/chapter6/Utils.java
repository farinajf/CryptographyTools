/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class Utils extends chapter5.Utils {

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
    public Utils() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * Create a random 1024 bit RSA key pair.
     * @return
     * @throws java.lang.Exception
     */
    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        kpGen.initialize(1024, new SecureRandom());

        return kpGen.generateKeyPair();
    }
}
