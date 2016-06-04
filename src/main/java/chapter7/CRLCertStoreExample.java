/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import static chapter7.X509CRLExample.createCRL;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import utils.CryptoDefs;

/**
 * Using the X509CRLSelector and the CertStore classes.
 * @author fran
 */
public class CRLCertStoreExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CRLCertStoreExample.class.getName();

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
    public CRLCertStoreExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create CA keys and certificate
        KeyPair         caPair              = Utils.generateRSAKeyPair();
        X509Certificate caCert              = Utils.generateRootCert(caPair);
        BigInteger      revokedSerialNumber = BigInteger.valueOf(2);

        //2.- Create a CRL revoking certificate number 2
        X509CRL crl = createCRL(caCert, caPair.getPrivate(), revokedSerialNumber);

        //3.- Place the CRL into a CertStore
        final CollectionCertStoreParameters params = new CollectionCertStoreParameters(Collections.singleton(crl));

        final CertStore       store    = CertStore.getInstance("Collection", params, CryptoDefs.Provider.BC.getName());
        final X509CRLSelector selector = new X509CRLSelector();

        selector.addIssuerName(caCert.getSubjectX500Principal().getEncoded());

        final Iterator it = store.getCRLs(selector).iterator();

        while (it.hasNext())
        {
            crl = (X509CRL) it.next();

            //4.- Verify the CRL
            crl.verify(caCert.getPublicKey(), CryptoDefs.Provider.BC.getName());

            //5.- Check if the CRL revokes certificate number 2
            final X509CRLEntry entry = crl.getRevokedCertificate(revokedSerialNumber);

            System.out.println("Revocation details:");
            System.out.println("  Certificate number: " + entry.getSerialNumber());
            System.out.println("  Issuer            : " + crl.getIssuerX500Principal());
        }
    }
}
