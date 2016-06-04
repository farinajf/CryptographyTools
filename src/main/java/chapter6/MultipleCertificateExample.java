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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import utils.CryptoDefs;

/**
 * Basic example of rerading multiple certificates with a CertificateFactory.
 *
 * @author fran
 */
public class MultipleCertificateExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = MultipleCertificateExample.class.getName();

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
    public MultipleCertificateExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        //Create the keys
        KeyPair pair = Utils.generateRSAKeyPair();

        //Create the input stream
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        bOut.write(X509V1CreateExample.generateV1Certificate(pair).getEncoded());
        bOut.write(X509V3CreateExample.generateV3Certificate(pair).getEncoded());

        bOut.close();

        InputStream in = new ByteArrayInputStream(bOut.toByteArray());

        //Create the certificate factory
        CertificateFactory fact = CertificateFactory.getInstance(CryptoDefs.CertType.X509.getName(), CryptoDefs.Provider.BC.getName());

        //Read the certificate
        X509Certificate x509Cert;
        Collection      collection = new ArrayList();

        while ((x509Cert = (X509Certificate) fact.generateCertificate(in)) != null) collection.add(x509Cert);

        Iterator it = collection.iterator();
        while (it.hasNext())
        {
            System.out.println("version: " + ((X509Certificate) it.next()).getVersion());
        }
    }
}
