/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 * RSA example with OAEP Padding and random key generation.
 *
 * @author fran
 */
public class RSAKeyExchangeExample {

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/
    private static byte[] _packKeyAndIv(final Key key, final IvParameterSpec ivSpec) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        bOut.write(ivSpec.getIV());
        bOut.write(key.getEncoded());

        return bOut.toByteArray();
    }

    private static Object[] _unpackKeyAndIv(final byte[] data) {
        byte[] keyD = new byte[16];
        byte[] iv   = new byte[data.length - 16];

        return new Object[] {new SecretKeySpec(data, 16, data.length - 16, CryptoDefs.Algorithm.AES.getName()),
                             new IvParameterSpec(data, 0, 16)};
    }

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
    public RSAKeyExchangeExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[]       input  = new byte[] {0x00, (byte) 0xbe, (byte) 0xef};
        SecureRandom random = Utils.createFixedRandom();

        //Create the RSA Key
        KeyPairGenerator generator = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        generator.initialize(1024, random);

        KeyPair pair    = generator.genKeyPair();
        Key     pubKey  = pair.getPublic();
        Key     privKey = pair.getPrivate();

        System.out.println("input:              " + Utils.toHex(input));

        //Create the symmetric key and iv
        Key             sKey    = Utils.createKeyForAES  (256, random);
        IvParameterSpec sIvSpec = Utils.createCtrIvForAES(0,   random);

        //Symmetric key/iv wrapping step
        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.RSA, CryptoDefs.Mode.NONE, CryptoDefs.Padding.OAEPWithSHA1AndMGF1Padding);
        Cipher xCipher   = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        xCipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

        byte[] keyBlock = xCipher.doFinal(_packKeyAndIv(sKey, sIvSpec));

        //Encryption step
        transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.CTR, CryptoDefs.Padding.NOPADDING);
        Cipher sCipher = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        sCipher.init(Cipher.ENCRYPT_MODE, sKey, sIvSpec);

        byte[] cipherText = sCipher.doFinal(input);

        System.out.println("keyBlock length:    " + keyBlock.length);
        System.out.println("cipherText length:  " + cipherText.length);

        //Symmetric key/iv unwrapping step
        xCipher.init(Cipher.DECRYPT_MODE, privKey);

        Object[] keyIv = _unpackKeyAndIv(xCipher.doFinal(keyBlock));

        //Decryption step
        sCipher.init(Cipher.DECRYPT_MODE, (Key) keyIv[0], (IvParameterSpec) keyIv[1]);

        byte[] plainText = sCipher.doFinal(cipherText);

        System.out.println("plain:              " + Utils.toHex(plainText));
    }
}
