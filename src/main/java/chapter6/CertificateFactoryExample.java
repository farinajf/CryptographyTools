/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import utils.CryptoDefs;

/**
 * Basic example of using a CertificateFactory.
 *
 * @author fran
 */
public class CertificateFactoryExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CertificateFactoryExample.class.getName();

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
    public CertificateFactoryExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        //Create the keys
        KeyPair pair = Utils.generateRSAKeyPair();

        //Create the input stream
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        bOut.write(X509V1CreateExample.generateV1Certificate(pair).getEncoded());

        bOut.close();

        InputStream in = new ByteArrayInputStream(bOut.toByteArray());

        //Create the certificate factory
        CertificateFactory fact = CertificateFactory.getInstance(CryptoDefs.CertType.X509.getName(), CryptoDefs.Provider.BC.getName());

        //Read the certificate
        X509Certificate x509Cert = (X509Certificate) fact.generateCertificate(in);

        System.out.println("Issuer: " + x509Cert.getIssuerX500Principal());
    }
}
