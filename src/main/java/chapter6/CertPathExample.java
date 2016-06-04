/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.ByteArrayInputStream;
import java.security.cert.CertPath;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import utils.CryptoDefs;

/**
 * Basic example of creating and encoding a CertPath.
 * @author fran
 */
public class CertPathExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CertPathExample.class.getName();

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
    public CertPathExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        X509Certificate[] chain = PKCS10CertCreateExample.buildChain();

        //Create the factory and the path object
        CertificateFactory factory  = CertificateFactory.getInstance("X.509", CryptoDefs.Provider.BC.getName());
        CertPath           certPath = factory.generateCertPath(Arrays.asList(chain));

        byte[] encoded = certPath.getEncoded("PEM");

        System.out.println(Utils.toString(encoded));

        //Re-read the CertPath
        CertPath newCertPath = factory.generateCertPath(new ByteArrayInputStream(encoded), "PEM");

        if (newCertPath.equals(certPath)) System.out.println("CertPath recovered correctly!!");

        System.out.println("......................");

        for (Iterator it = factory.getCertPathEncodings(); it.hasNext();)
            System.out.println("factory encodings:  " + it.next());

        System.out.println("");

        for (Iterator it = certPath.getEncodings(); it.hasNext();)
            System.out.println("certpath encodings: " + it.next());
    }
}
