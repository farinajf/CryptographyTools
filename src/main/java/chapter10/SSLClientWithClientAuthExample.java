/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * SSL Client with client-side authentication.
 * @author fran
 */
public class SSLClientWithClientAuthExample extends SSLClientExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SSLClientWithClientAuthExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/
    /**
     * Create an SSL context with a KeyManager providing our identity.
     * @return
     * @throws Exception
     */
    static SSLContext _createSSLContext() throws Exception {
        //1.- Set up a key manager for our local credentials
        KeyManagerFactory mgrFact     = KeyManagerFactory.getInstance("SunX509");
        KeyStore          clientStore = KeyStore.getInstance("PKCS12");

        clientStore.load(new FileInputStream("client.p12"), Utils.CLIENT_PASSWORD);

        mgrFact.init(clientStore, Utils.CLIENT_PASSWORD);

        //2.- Create a context and set up a socket factory
        SSLContext result = SSLContext.getInstance("TLS");

        result.init(mgrFact.getKeyManagers(), null, null);

        return result;
    }

    /*------------------------------------------------------------------------*/
    /*                          Metodos Protegidos                            */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                            Constructores                               */
    /*------------------------------------------------------------------------*/
    /**
     * Constructor por defecto.
     */
    public SSLClientWithClientAuthExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SSLContext       sslContext = _createSSLContext();
        SSLSocketFactory fact       = sslContext.getSocketFactory();
        SSLSocket        cSock      = (SSLSocket) fact.createSocket(Utils.HOST, Utils.PORT_NO);

        _doProtocol(cSock);
    }
}
