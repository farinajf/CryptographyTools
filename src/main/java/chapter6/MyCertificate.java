/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class MyCertificate implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = MyCertificate.class.getName();

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
    public MyCertificate() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        KeyStore ks = KeyStore.getInstance(CryptoDefs.KeyStoreType.PKCS12.getName(), CryptoDefs.Provider.BC.getName());

        ks.load(new FileInputStream("src/main/resources/cixtecArquitecturas2028.pfx"), "cixtec2028".toCharArray());

        Certificate certificate = ks.getCertificate("le-97585391-8095-4c16-8a19-164a4e322847");

        System.out.println("Type: " + certificate.getType());

        X509Certificate x509Certificate = (X509Certificate) certificate;

        System.out.println("TBSCertificate:");
        System.out.println(Utils.toHex(x509Certificate.getTBSCertificate()));

        System.out.println("Version:       " + x509Certificate.getVersion());
        System.out.println("Serial number: " + x509Certificate.getSerialNumber());
        System.out.println("Issuer P.:     " + x509Certificate.getIssuerX500Principal().getName());
        System.out.println(Utils.toHex(x509Certificate.getIssuerX500Principal().getEncoded()));

        System.out.println("SigAlgName:    " + x509Certificate.getSigAlgName());
        System.out.println("-----------------");

        System.out.println("Critical Extensions:    " + x509Certificate.getCriticalExtensionOIDs());
        System.out.println(Utils.toHex(x509Certificate.getExtensionValue("2.5.29.15")));

        System.out.println("NonCritical Extensions: " + x509Certificate.getNonCriticalExtensionOIDs());

        System.out.println("Key Usage:              " + Arrays.toString(x509Certificate.getKeyUsage()));

        System.out.println("Subject Alternative Names:");
        System.out.println(x509Certificate.getSubjectAlternativeNames());

        System.out.println("Path Len Constraint:    " + x509Certificate.getBasicConstraints());

        System.out.println("Extended Key Usage:     " + x509Certificate.getExtendedKeyUsage());
    }
}
