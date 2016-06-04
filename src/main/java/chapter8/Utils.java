/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter8;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500PrivateCredential;

/**
 *
 * @author fran
 */
public class Utils extends chapter7.Utils implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = Utils.class.getName();

    public static String ROOT_ALIAS         = "root";
    public static String INTERMEDIATE_ALIAS = "intermediate";
    public static String END_ENTITY_ALIAS   = "end";


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
    /**
     * Generate a X500PrivateCredential for the root entity.
     * @return
     * @throws Exception
     */
    public static X500PrivateCredential createRootCredential() throws Exception {
        KeyPair         rootPair = generateRSAKeyPair();
        X509Certificate rootCert = generateRootCert(rootPair);

        return new X500PrivateCredential(rootCert, rootPair.getPrivate(), ROOT_ALIAS);
    }

    /**
     * Generate a X500PrivateCredential for the intermediate entity.
     * @param caKey
     * @param caCert
     * @return
     * @throws Exception
     */
    public static X500PrivateCredential createIntermediateCredential(PrivateKey caKey, X509Certificate caCert) throws Exception {
        KeyPair         interPair = generateRSAKeyPair();
        X509Certificate interCert = generateIntermediateCert(interPair.getPublic(), caKey, caCert);

        return new X500PrivateCredential(interCert, interPair.getPrivate(), INTERMEDIATE_ALIAS);
    }

    public static X500PrivateCredential createEndEntityCredential(PrivateKey caKey, X509Certificate caCert) throws Exception {
        KeyPair         endPair = generateRSAKeyPair();
        X509Certificate endCert = generateEndEntityCert(endPair.getPublic(), caKey, caCert);

        return new X500PrivateCredential(endCert, endPair.getPrivate(), END_ENTITY_ALIAS);
    }
}
