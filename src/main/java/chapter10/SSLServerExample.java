/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Basic SSL Server - using the '!' protocol.
 * @author fran
 */
public class SSLServerExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SSLServerExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/
    /**
     * Carry out the '!' protocol - server side.
     * @param sSock
     * @throws IOException
     */
    static void _doProtocol(Socket sSock) throws IOException {
        System.out.println("session started.");

        InputStream  in  = sSock.getInputStream();
        OutputStream out = sSock.getOutputStream();

        out.write(Utils.toByteArray("Hello "));

        int ch = 0;
        while ((ch = in.read()) != '!') out.write(ch);

        out.write('!');

        sSock.close();

        System.out.println("session closed!!");
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
    public SSLServerExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket        sSock   = (SSLServerSocket)        factory.createServerSocket(Utils.PORT_NO);

        SSLSocket sslSock = (SSLSocket) sSock.accept();

        _doProtocol(sslSock);
    }
}
