/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Principal;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

/**
 * Basic SSL Server with client authentication and id checking.
 * @author fran
 */
public class SSLServerWithClientAuthIdExample extends SSLServerExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SSLServerWithClientAuthIdExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/
    /**
     * Check that the principal we have been given is for the end entity.
     * @param session
     * @return
     * @throws SSLPeerUnverifiedException
     */
    static boolean _isEndEntity(SSLSession session) throws SSLPeerUnverifiedException {
        Principal id = session.getPeerPrincipal();
        if (id instanceof X500Principal)
        {
            X500Principal x500 = (X500Principal) id;

            return x500.getName().equals("CN=Test End Certificate");
        }

        return false;
    }

    /**
     * Create an SSL context with the identity and trust stores in place.
     * @return
     * @throws Exception
     */
    static SSLContext _createSSLContext() throws Exception {
        //1.- Set up a key manager for our local credentials
        KeyManagerFactory mgrFact     = KeyManagerFactory.getInstance("SunX509");
        KeyStore          serverStore = KeyStore.getInstance("JKS");

        serverStore.load(new FileInputStream("server.jks"), Utils.SERVER_PASSWORD);

        mgrFact.init(serverStore, Utils.SERVER_PASSWORD);

        //2.- Set up a trust manager so we can recognize the server
        TrustManagerFactory trustFact  = TrustManagerFactory.getInstance("SunX509");
        KeyStore            trustStore = KeyStore.getInstance("JKS");

        trustStore.load(new FileInputStream("trustStore.jks"), Utils.TRUSTSTORE_PASSWORD);

        trustFact.init(trustStore);

        //3.- Create a context and set up a socket factory
        SSLContext result = SSLContext.getInstance("TLS");

        result.init(mgrFact.getKeyManagers(), trustFact.getTrustManagers(), null);

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
    public SSLServerWithClientAuthIdExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create a context and set up a socket factory
        SSLContext             sslContext = _createSSLContext();
        SSLServerSocketFactory fact       = sslContext.getServerSocketFactory();
        SSLServerSocket        sSock = (SSLServerSocket) fact.createServerSocket(Utils.PORT_NO);

        sSock.setNeedClientAuth(true);

        SSLSocket sslSock = (SSLSocket) sSock.accept();

        sslSock.startHandshake();

        //2.- Process if principal checks out
        if (_isEndEntity(sslSock.getSession())) _doProtocol(sslSock);
    }
}
