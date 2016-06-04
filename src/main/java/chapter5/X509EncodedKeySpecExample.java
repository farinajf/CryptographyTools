/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class X509EncodedKeySpecExample {

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
    public X509EncodedKeySpecExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        //Create thr keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        generator.initialize(128, Utils.createFixedRandom());

        KeyPair pair = generator.genKeyPair();

        //Dump public key
        ASN1InputStream aIn = new ASN1InputStream(pair.getPublic().getEncoded());

        SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(aIn.readObject());

        System.out.println("Format: " + pair.getPublic().getFormat());
        System.out.println(ASN1Dump.dumpAsString(info));
        System.out.println(ASN1Dump.dumpAsString(info.getPublicKey()));

        //Create from specification
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(pair.getPublic().getEncoded());
        KeyFactory         keyFact = KeyFactory.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());
        PublicKey          pubKey  = keyFact.generatePublic(x509Spec);

        if (pubKey.equals(pair.getPublic())) System.out.println("Key recovery successful!!");
        else System.out.println("Key recovery failed!!");

        System.out.println(info.getAlgorithmId().getAlgorithm().getId());
    }
}
