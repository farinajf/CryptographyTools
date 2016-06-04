/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Signature;
import javax.crypto.Cipher;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.util.ASN1Dump;
import utils.CryptoDefs;

/**
 * Basic class for exploring PKCS #1 V1.5 Signatures.
 * @author fran
 */
public class PKCS1SigEncodingExample {

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
    public PKCS1SigEncodingExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        keyGen.initialize(512, new SecureRandom());

        KeyPair keyPair = keyGen.generateKeyPair();

        Signature signature = Signature.getInstance(CryptoDefs.Algorithm.SHA256withRSA.getName(), CryptoDefs.Provider.BC.getName());

        //Generate a signature
        signature.initSign(keyPair.getPrivate());

        byte[] message = new byte[] {'a','b','c'};

        signature.update(message);

        byte[] sigBytes = signature.sign();
        System.out.println(Utils.toHex(sigBytes));

        //Verify hash in signature
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.RSA, CryptoDefs.Mode.NONE, CryptoDefs.Padding.PKCS1);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());

        byte[] decSig = cipher.doFinal(sigBytes);
        System.out.println(Utils.toHex(decSig));

        //Parse de signature
        ASN1InputStream in  = new ASN1InputStream(decSig);
        ASN1Sequence    seq = (ASN1Sequence) in.readObject();

        System.out.println(ASN1Dump.dumpAsString(seq));

        //Grab a digest of the correct type
        MessageDigest hash = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA256.getName(), CryptoDefs.Provider.BC.getName());

        hash.update(message);

        ASN1OctetString sigHash = (ASN1OctetString) seq.getObjectAt(1);
        if (MessageDigest.isEqual(hash.digest(), sigHash.getOctets()))
        {
            System.out.println("Hash verification succeeded!!");
        }
        else System.out.println("Hash verification failed!!");
    }
}
