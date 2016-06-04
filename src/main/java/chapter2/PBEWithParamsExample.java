/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter2;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 * Example of using PBE with a PBEParameterSpec.
 * @author fran
 */
public class PBEWithParamsExample {

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
    public PBEWithParamsExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input    = InputData.INPUT_2;
        byte[] keyBytes = InputData.KEY_2;
        byte[] ivBytes  = new byte[]{(byte) 0xb0, 0x7b, (byte) 0xf5, 0x22, (byte) 0xc8, (byte) 0xd6, 0x08, (byte) 0xb8};

        //Encrypt
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.DESede, CryptoDefs.Mode.CBC, CryptoDefs.Padding.PKCS7);
        Cipher cEnc      = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        cEnc.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.DESede.getName()), new IvParameterSpec(ivBytes));

        byte[] out = cEnc.doFinal(input);

        //Decrypt
        char[] password       = "password".toCharArray();
        byte[] salt           = new byte[]{0x7d, 0x60, 0x43, 0x5f, 0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0xae};
        int    iterationCount = 2048;

        PBEKeySpec       pbeSpec = new PBEKeySpec(password);
        SecretKeyFactory keyFact = SecretKeyFactory.getInstance(CryptoDefs.Algorithm.PBEWithSHAAnd3KeyTripleDES.getName(), CryptoDefs.Provider.BC.getName());
        Key              sKey    = keyFact.generateSecret(pbeSpec);

        Cipher cDec = Cipher.getInstance(CryptoDefs.Algorithm.PBEWithSHAAnd3KeyTripleDES.getName(), CryptoDefs.Provider.BC.getName());

        cDec.init(Cipher.DECRYPT_MODE, sKey, new PBEParameterSpec(salt, iterationCount));

        byte[] result = cDec.doFinal(out);

        System.out.println("cipher:  " + Utils.toHex(out));
        System.out.println("gen key: " + Utils.toHex(sKey.getEncoded()));
        System.out.println("gen iv:  " + Utils.toHex(cDec.getIV()));
        System.out.println("algo:    " + cDec.getAlgorithm() + " - " + cDec.getBlockSize());
        System.out.println("plain :  " + Utils.toHex(result));
        System.out.println("");
        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64String(result));
    }
}
