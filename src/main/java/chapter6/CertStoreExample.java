/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import javax.security.auth.x500.X500Principal;

/**
 * Example using a CertStore and a CertSelector.
 * @author fran
 */
public class CertStoreExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CertStoreExample.class.getName();

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
    public CertStoreExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        X509Certificate[] chain = PKCS10CertCreateExample.buildChain();

        //Create the store
        CollectionCertStoreParameters params = new CollectionCertStoreParameters(Arrays.asList(chain));

        CertStore store = CertStore.getInstance("Collection", params);

        //Create the selector
        X509CertSelector selector = new X509CertSelector();

        selector.setSubject(new X500Principal("CN=Requested Test Certificate").getEncoded());

        //Print the subject of the results
        Iterator<? extends Certificate> certsIt = store.getCertificates(selector).iterator();
        while (certsIt.hasNext())
        {
            X509Certificate cert = (X509Certificate) certsIt.next();

            System.out.println(cert.getSubjectX500Principal());
        }
    }
}
