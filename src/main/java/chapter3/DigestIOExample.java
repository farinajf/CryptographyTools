/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter3;

import chapter2.InputData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class DigestIOExample {

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
    public DigestIOExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] input = InputData.INPUT_3;

        MessageDigest hash = MessageDigest.getInstance(CryptoDefs.Algorithm.SHA1.getName());

        System.out.println("input:      " + Utils.toHex(input));

        //Input pass
        ByteArrayInputStream  bIn  = new ByteArrayInputStream(input);
        DigestInputStream     dIn  = new DigestInputStream(bIn, hash);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        int ch;
        while ((ch = dIn.read()) >= 0) bOut.write(ch);

        byte[] newInput = bOut.toByteArray();

        System.out.println("in digest:  " + Utils.toHex(dIn.getMessageDigest().digest()));
        System.out.println("output:     " + Utils.toHex(newInput));

        //Output pass
        bOut = new ByteArrayOutputStream();

        DigestOutputStream dOut = new DigestOutputStream(bOut, hash);

        dOut.write(newInput);
        dOut.close();

        System.out.println("out digest: " + Utils.toHex(dOut.getMessageDigest().digest()));
    }
}
