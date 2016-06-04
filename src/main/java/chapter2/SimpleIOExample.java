package chapter2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class SimpleIOExample {

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
    public SimpleIOExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input    = InputData.INPUT_3;
        byte[] keyBytes = InputData.KEY_0;
        byte[] ivBytes  = new byte[] {0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03,
                                      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};

        SecretKeySpec   key    = new SecretKeySpec(keyBytes, CryptoDefs.Algorithm.AES.getName());
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        String transform = CryptoDefs.getTransform(CryptoDefs.Algorithm.AES, CryptoDefs.Mode.CTR, CryptoDefs.Padding.NOPADDING);
        Cipher cipher    = Cipher.getInstance(transform, CryptoDefs.Provider.BC.getName());

        System.out.println("input:  " + Utils.toHex(input));

        //Encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        ByteArrayInputStream  bIn = new ByteArrayInputStream(input);
        CipherInputStream     cIn = new CipherInputStream   (bIn, cipher);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int ch;
        while ((ch = cIn.read()) >= 0) bout.write(ch);

        byte[] cipherText = bout.toByteArray();

        System.out.println("cipher: " + Utils.toHex(cipherText));

        //Decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        bout = new ByteArrayOutputStream();

        CipherOutputStream cOut = new CipherOutputStream(bout, cipher);

        cOut.write(cipherText);

        cOut.close();

        System.out.println("plain:  " + Utils.toHex(bout.toByteArray()));
    }
}
