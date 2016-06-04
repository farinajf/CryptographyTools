/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Basic SSL with client authentication.
 * @author fran
 */
public class SSLServerWithClientAuthExample extends SSLServerExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SSLServerWithClientAuthExample.class.getName();

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
    public SSLServerWithClientAuthExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory fact  = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket        sSock = (SSLServerSocket) fact.createServerSocket(Utils.PORT_NO);

        sSock.setNeedClientAuth(true);

        SSLSocket sslSock = (SSLSocket) sSock.accept();

        _doProtocol(sslSock);
    }
}
