/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import chapter2.InputData;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import javax.crypto.KeyAgreement;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class BasicECDHExample {

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
    public BasicECDHExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.ECDH.getName(), CryptoDefs.Provider.BC.getName());
        EllipticCurve    curve  = new EllipticCurve(new ECFieldFp(InputData.F_1), InputData.F_2, InputData.F_3);

        ECParameterSpec ecSpec = new ECParameterSpec(curve, new ECPoint(InputData.F_4, InputData.F_5), InputData.F_6, 1);

        keyGen.initialize(ecSpec, Utils.createFixedRandom());

        // Set up
        KeyAgreement aKeyAgree = KeyAgreement.getInstance(CryptoDefs.Algorithm.ECDH.getName(), CryptoDefs.Provider.BC.getName());
        KeyPair      aPair     = keyGen.generateKeyPair();
        KeyAgreement bKeyAgree = KeyAgreement.getInstance(CryptoDefs.Algorithm.ECDH.getName(), CryptoDefs.Provider.BC.getName());
        KeyPair      bPair     = keyGen.generateKeyPair();

        // Two party agreement
        aKeyAgree.init(aPair.getPrivate());
        bKeyAgree.init(bPair.getPrivate());

        aKeyAgree.doPhase(bPair.getPublic(), true);
        bKeyAgree.doPhase(aPair.getPublic(), true);

        // Generate the key bytes
        MessageDigest hash = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName(), CryptoDefs.Provider.BC.getName());
        byte[]        aShared = hash.digest(aKeyAgree.generateSecret());
        byte[]        bShared = hash.digest(bKeyAgree.generateSecret());

        System.out.println(Utils.toHex(aShared));
        System.out.println(Utils.toHex(bShared));
    }
}
