/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;

/**
 * SSL Client with client-side authentication.
 * @author fran
 */
public class HTTPSClientExample extends SSLClientWithClientAuthTrustExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = HTTPSClientExample.class.getName();

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
    public HTTPSClientExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final SSLContext       sslContext = _createSSLContext();
        final SSLSocketFactory fact       = sslContext.getSocketFactory();

        //1.- Specify the URL and connection attributes
        java.net.URL url = new URL("https://" + Utils.HOST + ":" + Utils.PORT_NO);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setSSLSocketFactory(fact);
        connection.setHostnameVerifier(new Validator());

        connection.connect();

        //2.- Read the response
        InputStream in = connection.getInputStream();

        int ch = 0;
        while ((ch = in.read()) >= 0) System.out.print((char) ch);

        System.out.println("");
    }

    /*------------------------------------------------------------------------*/
    /*                          Clases Privadas                               */
    /*------------------------------------------------------------------------*/
    /**
     * Verifier to check host has identified itself using "Test CA Certificate".
     */
    private static class Validator implements HostnameVerifier {
        @Override
        public boolean verify(final String hostName, final SSLSession session) {
            try
            {
                final X500Principal hostID = (X500Principal) session.getPeerPrincipal();

                return hostID.getName().equals("CN=Test CA Certificate");
            }
            catch (Exception e) {return false;}
        }
    }
}
