/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import utils.CryptoDefs;

/**
 * Basic example of the use of CertPathBuilder.
 * @author fran
 */
public class CertPathBuilderExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CertPathBuilderExample.class.getName();

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
    public CertPathBuilderExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create certificates and CRLs
        KeyPair rootPair  = Utils.generateRSAKeyPair();
        KeyPair interPair = Utils.generateRSAKeyPair();
        KeyPair endPair   = Utils.generateRSAKeyPair();

        X509Certificate rootCert  = Utils.generateRootCert        (rootPair);
        X509Certificate interCert = Utils.generateIntermediateCert(interPair.getPublic(), rootPair.getPrivate(),  rootCert);
        X509Certificate endCert   = Utils.generateEndEntityCert   (endPair.getPublic(),   interPair.getPrivate(), interCert);

        BigInteger revokedSerialNumber = BigInteger.valueOf(2);

        X509CRL rootCRL  = X509CRLExample.createCRL(rootCert,  rootPair.getPrivate(),  revokedSerialNumber);
        X509CRL interCRL = X509CRLExample.createCRL(interCert, interPair.getPrivate(), revokedSerialNumber);

        //2.- Create CertStore to support path building
        List<X509Extension> list = new ArrayList();

        list.add(rootCert);
        list.add(interCert);
        list.add(endCert);
        list.add(rootCRL);
        list.add(interCRL);

        CollectionCertStoreParameters params = new CollectionCertStoreParameters(list);
        CertStore                     store  = CertStore.getInstance("Collection", params, CryptoDefs.Provider.BC.getName());

        //3.- Build the path
        CertPathBuilder  builder        = CertPathBuilder.getInstance("PKIX", CryptoDefs.Provider.BC.getName());
        X509CertSelector endConstraints = new X509CertSelector();

        endConstraints.setSerialNumber(endCert.getSerialNumber());
        endConstraints.setIssuer      (endCert.getIssuerX500Principal().getEncoded());

        PKIXBuilderParameters buildParams = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(rootCert, null)), endConstraints);

        buildParams.addCertStore(store);
        buildParams.setDate     (new Date());

        PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(buildParams);
        CertPath                  path   = result.getCertPath();

        for (Certificate x : path.getCertificates())
        {
            System.out.println(((X509Certificate) x).getSubjectX500Principal());
        }

        System.out.println(result.getTrustAnchor().getTrustedCert().getSubjectX500Principal());
    }
}
