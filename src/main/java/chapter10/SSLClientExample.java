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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Basic SSL Client - using the '!' protocol.
 * @author fran
 */
public class SSLClientExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SSLClientExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/
    /**
     * Carry out the '!' protocol - client side.
     * @param cSock
     * @throws IOException
     */
    static void _doProtocol(Socket cSock) throws IOException {
        OutputStream out = cSock.getOutputStream();
        InputStream  in  = cSock.getInputStream();

        out.write(Utils.toByteArray("World"));
        out.write('!');

        int ch = 0;
        while ((ch = in.read()) != '!') System.out.print((char) ch);

        System.out.println((char) ch);
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
    public SSLClientExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket        cSock   = (SSLSocket)        factory.createSocket(Utils.HOST, Utils.PORT_NO);

        _doProtocol(cSock);
    }
}
