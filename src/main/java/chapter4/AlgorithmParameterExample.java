/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;
import utils.CryptoDefs;

/**
 * El Gamal example with random key generation.
 * @author fran
 */
public class AlgorithmParameterExample {

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
    public AlgorithmParameterExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input     = new byte[] {(byte) 0xbe, (byte) 0xef};
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.EL_GAMAL, CryptoDefs.Mode.NONE, CryptoDefs.Padding.NOPADDING);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        SecureRandom random = Utils.createFixedRandom();

        // Create the parameters
        AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance(CryptoDefs.Algorithm.EL_GAMAL.getName(), CryptoDefs.Provider.BC.getName());

        apg.init(256, random);

        AlgorithmParameters    params = apg.generateParameters();
        AlgorithmParameterSpec dhSpec = params.getParameterSpec(DHParameterSpec.class);

        // Create the keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.EL_GAMAL.getName(), CryptoDefs.Provider.BC.getName());

        generator.initialize(dhSpec, random);

        KeyPair pair    = generator.generateKeyPair();
        Key     pubKey  = pair.getPublic();
        Key     privKey = pair.getPrivate();

        System.out.println("input:  " + Utils.toHex(input));

        // Encryption step
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

        byte[] cipherText = cipher.doFinal(input);

        System.out.println("cipher: " + Utils.toHex(cipherText));

        // Decryption step
        cipher.init(Cipher.DECRYPT_MODE, privKey);

        byte[] plainText = cipher.doFinal(cipherText);

        System.out.println("plain:  " + Utils.toHex(plainText));
    }
}
