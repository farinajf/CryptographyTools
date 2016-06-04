/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.Principal;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * Basic SSL Server with optional client authentication.
 * @author fran
 */
public class HTTPSServerExample extends SSLServerWithClientAuthIdExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = HTTPSServerExample.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/
    /**
     * Read a HTTP requets.
     * @param in
     * @throws IOException
     */
    private static void _readRequest(InputStream in) throws IOException {
        System.out.print("Request: ");
        int ch     = 0;
        int lastCh = 0;

        while ((ch = in.read()) >= 0 && (ch != '\n' && lastCh != '\n'))
        {
            System.out.print((char) ch);
            if (ch != '\r') lastCh = ch;
        }

        System.out.println("");
    }

    /**
     * Send a response
     * @param out
     */
    private static void _sendResponse(OutputStream out) {
        PrintWriter pWrt = new PrintWriter(new OutputStreamWriter(out));

        pWrt.print("HTTP/1.1 200 OK\r\n");
        pWrt.print("Content-Type: text/html\r\n");
        pWrt.print("\r\n");
        pWrt.print("<html>\r\n");
        pWrt.print("<body>\r\n");
        pWrt.print("Hello World!\r\n");
        pWrt.print("</body>\r\n");
        pWrt.print("</html>\r\n");
        pWrt.flush();
    }

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
    public HTTPSServerExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final SSLContext             sslContext = _createSSLContext();
        final SSLServerSocketFactory fact       = sslContext.getServerSocketFactory();
        final SSLServerSocket        sSock      = (SSLServerSocket) fact.createServerSocket(Utils.PORT_NO);

        //1.- Client authenticate where possible
        sSock.setWantClientAuth(true);

        while (true)
        {
            SSLSocket sslSock = (SSLSocket) sSock.accept();

            try
            {
                sslSock.startHandshake();
            }
            catch (IOException e) {continue;}

            _readRequest(sslSock.getInputStream());

            SSLSession session = sslSock.getSession();

            try
            {
                Principal clientID = session.getPeerPrincipal();

                System.out.println("client identified as: " + clientID);
            }
            catch (SSLPeerUnverifiedException e)
            {
                System.out.println("client not authenticated!!");
            }

            _sendResponse(sslSock.getOutputStream());

            sslSock.close();
        }
    }
}
