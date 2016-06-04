/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import utils.CryptoDefs;

/**
 * Example of generating a detached signature.
 * @author fran
 */
public class SignedDataExample extends SignedDataProcessor implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SignedDataExample.class.getName();

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
    public SignedDataExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyStore   credentials = Utils.createCredentials();
        PrivateKey key         = (PrivateKey) credentials.getKey(Utils.END_ENTITY_ALIAS, Utils.KEY_PASSWD);

        Certificate[] chain        = credentials.getCertificateChain(Utils.END_ENTITY_ALIAS);
        CertStore     certsAndCRLs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(chain)), CryptoDefs.Provider.BC.getName());

        X509Certificate cert = (X509Certificate) chain[0];

        // Set up the generator
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

        gen.addSigner             (key, cert, CMSSignedDataGenerator.DIGEST_SHA224);
        gen.addCertificatesAndCRLs(certsAndCRLs);

        // Create the signed-data object
        CMSProcessable data = new CMSProcessableByteArray("Hello World!!".getBytes());

        CMSSignedData signed = gen.generate(data, CryptoDefs.Provider.BC.getName());

        // Re-create
        signed = new CMSSignedData(data, signed.getEncoded());

        // Verification step
        X509Certificate rootCert = (X509Certificate) credentials.getCertificate(Utils.ROOT_ALIAS);

        if (isValid(signed, rootCert)) System.out.println("verification succeeded!!");
        else                           System.out.println("verification failed!!");
    }
}
