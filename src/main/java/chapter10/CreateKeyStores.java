/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import javax.security.auth.x500.X500PrivateCredential;
import utils.CryptoDefs;

/**
 * Create the various credentials for an SSL session.
 * @author fran
 */
public class CreateKeyStores implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CreateKeyStores.class.getName();

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
    public CreateKeyStores() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        X500PrivateCredential rootCredential  = Utils.createRootCredential();
        X500PrivateCredential interCredential = Utils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential endCredential   = Utils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());

        //1.- Client credentials
        KeyStore keystore = KeyStore.getInstance(CryptoDefs.KeyStoreType.PKCS12.getName(), CryptoDefs.Provider.BC.getName());

        keystore.load(null, null);

        keystore.setKeyEntry(Utils.CLIENT_NAME,
                             endCredential.getPrivateKey(),
                             Utils.CLIENT_PASSWORD,
                             new Certificate[] {endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate()});

        keystore.store(new FileOutputStream(Utils.CLIENT_NAME + ".p12"), Utils.CLIENT_PASSWORD);

        //2.- Trust store for both ends
        keystore = KeyStore.getInstance(CryptoDefs.KeyStoreType.JKS.getName());

        keystore.load(null, null);

        keystore.setCertificateEntry(Utils.SERVER_NAME, rootCredential.getCertificate());

        keystore.store(new FileOutputStream(Utils.TRUSTSTORE_NAME + ".jks"), Utils.TRUSTSTORE_PASSWORD);

        //3.- Server Credentials
        keystore = KeyStore.getInstance(CryptoDefs.KeyStoreType.JKS.getName());

        keystore.load(null, null);

        keystore.setKeyEntry(Utils.SERVER_NAME,
                             rootCredential.getPrivateKey(),
                             Utils.SERVER_PASSWORD,
                             new Certificate[] {rootCredential.getCertificate()});

        keystore.store(new FileOutputStream(Utils.SERVER_NAME + ".jks"), Utils.SERVER_PASSWORD);
    }
}
