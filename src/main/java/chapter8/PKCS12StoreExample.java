/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;
import javax.security.auth.x500.X500PrivateCredential;
import utils.CryptoDefs;

/**
 * Example of the creation of a PKCS #12 store.
 * @author fran
 */
public class PKCS12StoreExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = PKCS12StoreExample.class.getName();

    //public static char[] keyPassword = "keyPassword".toCharArray();

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
    public PKCS12StoreExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @return
     * @throws Exception
     */
    public static KeyStore createKeyStore() throws Exception {
        KeyStore store = KeyStore.getInstance(CryptoDefs.KeyStoreType.PKCS12.getName(), CryptoDefs.Provider.BC.getName());

        //1.- Initialize
        store.load(null, null);

        X500PrivateCredential rootCredential  = Utils.createRootCredential();
        X500PrivateCredential interCredential = Utils.createIntermediateCredential(rootCredential.getPrivateKey(),  rootCredential.getCertificate());
        X500PrivateCredential endCredential   = Utils.createEndEntityCredential   (interCredential.getPrivateKey(), interCredential.getCertificate());

        Certificate[] chain = new Certificate[3];

        chain[0] = endCredential.getCertificate();
        chain[1] = interCredential.getCertificate();
        chain[2] = rootCredential.getCertificate();

        //2.- Set the entries
        store.setCertificateEntry(rootCredential.getAlias(), rootCredential.getCertificate());
        store.setKeyEntry        (endCredential.getAlias(),  endCredential.getPrivateKey(), null, chain);

        return store;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyStore store    = createKeyStore();
        char[]   password = "storePassword".toCharArray();

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        //1.- Save the store
        store.store(bOut, password);

        //2.- Reload from scratch
        store = KeyStore.getInstance(CryptoDefs.KeyStoreType.PKCS12.getName(), CryptoDefs.Provider.BC.getName());

        store.load(new ByteArrayInputStream(bOut.toByteArray()), password);

        Enumeration<String> en = store.aliases();

        while (en.hasMoreElements())
        {
            String alias = en.nextElement();
            System.out.println("found " + alias + ", isCertificate? " + store.isCertificateEntry(alias));
        }
    }
}
