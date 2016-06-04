/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter8;

import java.io.FileOutputStream;
import java.security.KeyStore;

/**
 * Create some keystore files in the current directory.
 * @author fran
 */
public class KeyStoreFileUtility implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = KeyStoreFileUtility.class.getName();

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
    public KeyStoreFileUtility() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        char[] password = "storePassword".toCharArray();

        //1.- Create and save a JKS store
        KeyStore store = JKSStoreExample.createKeyStore();

        store.store(new FileOutputStream("keystore.jks"), password);

        //2.- Create and save a PKCS #12 store
        store = PKCS12StoreExample.createKeyStore();

        store.store(new FileOutputStream("keystore.p12"), password);
    }
}
