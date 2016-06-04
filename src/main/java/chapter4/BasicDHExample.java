/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import chapter2.InputData;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class BasicDHExample {
    private static BigInteger g512 = InputData.G_512;
    private static BigInteger p512 = InputData.P_512;

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
    public BasicDHExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        DHParameterSpec  dhParams = new DHParameterSpec(p512, g512);
        KeyPairGenerator keyGen   = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.DH.getName(), CryptoDefs.Provider.BC.getName());

        keyGen.initialize(dhParams, Utils.createFixedRandom());

        // Set up
        KeyAgreement aKeyAgree = KeyAgreement.getInstance(CryptoDefs.Algorithm.DH.getName(), CryptoDefs.Provider.BC.getName());
        KeyPair      aPair     = keyGen.genKeyPair();
        KeyAgreement bKeyAgree = KeyAgreement.getInstance(CryptoDefs.Algorithm.DH.getName(), CryptoDefs.Provider.BC.getName());
        KeyPair      bPair     = keyGen.genKeyPair();

        // Two party agreement
        aKeyAgree.init(aPair.getPrivate());
        bKeyAgree.init(bPair.getPrivate());

        aKeyAgree.doPhase(bPair.getPublic(), true);
        bKeyAgree.doPhase(aPair.getPublic(), true);

        // Generate the key bytes
        MessageDigest hash    = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName());
        byte[]        aShared = hash.digest(aKeyAgree.generateSecret());
        byte[]        bShared = hash.digest(bKeyAgree.generateSecret());

        System.out.println(Utils.toHex(aShared));
        System.out.println(Utils.toHex(bShared));
    }
}
