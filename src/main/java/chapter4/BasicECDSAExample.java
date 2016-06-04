/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import chapter2.InputData;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import utils.CryptoDefs;

/**
 * Simple example showing signature creation and verification using ECDSA.
 * @author fran
 */
public class BasicECDSAExample {

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
    public BasicECDSAExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        KeyPairGenerator   keyGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.ECDSA.getName(), CryptoDefs.Provider.BC.getName());
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

        keyGen.initialize(ecSpec, new SecureRandom());

        KeyPair   keyPair   = keyGen.genKeyPair();
        Signature signature = Signature.getInstance(CryptoDefs.Algorithm.ECDSA.getName(), CryptoDefs.Provider.BC.getName());

        // Generate a signature
        byte[] message = InputData.MESSAGE_1;

        signature.initSign(keyPair.getPrivate(), Utils.createFixedRandom());

        signature.update(message);

        byte[] signatureBytes = signature.sign();

        // Verify a signature
        signature.initVerify(keyPair.getPublic());

        signature.update(message);

        boolean result = signature.verify(signatureBytes);

        //Result: Signature is represented by the numbers R and S.
        System.out.println("Signature: " + Utils.toHex(signatureBytes));
        System.out.println("result:    " + result);
    }
}
