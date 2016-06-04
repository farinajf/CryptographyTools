/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import chapter2.InputData;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class BasicDSAExample {

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
    public BasicDSAExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.DSA.getName(), CryptoDefs.Provider.BC.getName());

        keyGen.initialize(512, new SecureRandom());

        KeyPair   keyPair   = keyGen.genKeyPair();
        Signature signature = Signature.getInstance(CryptoDefs.Algorithm.DSA.getName(), CryptoDefs.Provider.BC.getName());

        //Generate a signature
        byte[] message = InputData.MESSAGE_1;

        signature.initSign(keyPair.getPrivate(), Utils.createFixedRandom());

        signature.update(message);

        byte[] signatureBytes = signature.sign();

        //Verify a signature
        signature.initVerify(keyPair.getPublic());

        signature.update(message);

        boolean result = signature.verify(signatureBytes);

        //Result: Signature is represented by the numbers R and S.
        System.out.println("Signature: " + Utils.toHex(signatureBytes));
        System.out.println("result:    " + result);
    }
}
