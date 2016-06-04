/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.util.ASN1Dump;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class EncryptedPrivateKeyInfoExample {

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
    public EncryptedPrivateKeyInfoExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        //Generate a key pair
        KeyPairGenerator pkg = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());
        pkg.initialize(128, Utils.createFixedRandom());

        KeyPair pair = pkg.generateKeyPair();

        //Wrapping step
        char[]           password      = "hello".toCharArray();
        byte[]           salt          = new byte[20];
        int              iCount        = 100;
        String           pbeAlgorithm  = CryptoDefs.Algorithm.PBEWithSHAAnd3_KeyTripleDES_CBC.getName();
        PBEKeySpec       pbeKeySpec    = new PBEKeySpec(password, salt, iCount);
        SecretKeyFactory secretKeyFact = SecretKeyFactory.getInstance(pbeAlgorithm, CryptoDefs.Provider.BC.getName());

        Cipher cipher = Cipher.getInstance(pbeAlgorithm, CryptoDefs.Provider.BC.getName());
        cipher.init(Cipher.WRAP_MODE, secretKeyFact.generateSecret(pbeKeySpec));

        byte[] wrappedKey = cipher.wrap(pair.getPrivate());

        //Create carrier
        System.out.println(cipher.getAlgorithm() + " " + cipher.getParameters().getAlgorithm());
        System.out.println(Utils.toHex(cipher.getParameters().getEncoded()));
        System.out.println(ASN1Dump.dumpAsString(new ASN1InputStream(cipher.getParameters().getEncoded()).readObject()));
        
        EncryptedPrivateKeyInfo pInfo = new EncryptedPrivateKeyInfo(cipher.getParameters(), wrappedKey);

        //Unwrapping step - note we only use the passwortd
        pbeKeySpec = new PBEKeySpec(password);

        cipher = Cipher.getInstance(pInfo.getAlgName(), CryptoDefs.Provider.BC.getName());

        cipher.init(Cipher.DECRYPT_MODE, secretKeyFact.generateSecret(pbeKeySpec), pInfo.getAlgParameters());

        PKCS8EncodedKeySpec pkcs8Spec = pInfo.getKeySpec(cipher);
        KeyFactory          keyFact   = KeyFactory.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());
        PrivateKey          privKey   = keyFact.generatePrivate(pkcs8Spec);

        if (privKey.equals(pair.getPrivate())) System.out.println("Key recovery successful!!");
        else System.out.println("Key recovery failed!!");
    }
}
