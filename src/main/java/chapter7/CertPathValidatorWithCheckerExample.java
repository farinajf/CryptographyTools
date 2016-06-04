/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import utils.CryptoDefs;

/**
 * Basic example of certificate path validation using a PKIXCertPathChecker.
 * @author fran
 */
public class CertPathValidatorWithCheckerExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CertPathValidatorWithCheckerExample.class.getName();

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
    public CertPathValidatorWithCheckerExample() {}

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

        //2.- Create CertStore to support validation
        List<X509Extension> list = new ArrayList();

        list.add(rootCert);
        list.add(interCert);
        list.add(endCert);

        CollectionCertStoreParameters params = new CollectionCertStoreParameters(list);
        CertStore                     store  = CertStore.getInstance("Collection", params, CryptoDefs.Provider.BC.getName());

        //3.- Create certificate path
        CertificateFactory fact = CertificateFactory.getInstance("X.509", CryptoDefs.Provider.BC.getName());

        List<X509Certificate> certChain = new ArrayList();

        certChain.add(endCert);
        certChain.add(interCert);

        CertPath certPath = fact.generateCertPath(certChain);

        Set trust = Collections.singleton(new TrustAnchor(rootCert, null));

        //4.- Perform validation
        PKIXParameters param = new PKIXParameters(trust);

        param.addCertPathChecker  (new PathChecker(rootPair, rootCert, revokedSerialNumber));
        param.setRevocationEnabled(false);
        param.addCertStore        (store);
        param.setDate             (new Date());

        CertPathValidator validator = CertPathValidator.getInstance("PKIX", CryptoDefs.Provider.BC.getName());

        try
        {
            CertPathValidatorResult result = validator.validate(certPath, param);

            System.out.println("Certificate path validated!!");
        }
        catch (CertPathValidatorException e)
        {
            System.out.println("Validation failed on certificate number " + e.getIndex() + ", details: " + e.getMessage());
        }
    }
}
