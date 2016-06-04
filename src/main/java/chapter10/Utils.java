/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter10;

/**
 * Chapter 10 Utils.
 * @author fran
 */
public class Utils extends chapter9.Utils implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = Utils.class.getName();

    /**
     * Host name for our examples to use.
     */
    static final String HOST = "localhost";

    /**
     * Port number for our examples to use.
     */
    static final int PORT_NO = 9020;

    public static final String SERVER_NAME     = "server";
    public static final char[] SERVER_PASSWORD = "serverPassword".toCharArray();

    public static final String CLIENT_NAME     = "client";
    public static final char[] CLIENT_PASSWORD = "clientPassword".toCharArray();

    public static final String TRUSTSTORE_NAME     = "trustStore";
    public static final char[] TRUSTSTORE_PASSWORD = "trustStore".toCharArray();

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
    public Utils() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/

}
