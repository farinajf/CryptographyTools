/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.util.Date;
import org.bouncycastle.asn1.util.ASN1Dump;

/**
 *
 * @author fran
 */
public class ASN1DumpExample {

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
    public ASN1DumpExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] baseData = new byte[5];
        Date   created  = new Date(0);

        MyStructure structure = new MyStructure(0, created, baseData, "hello", "world");

        System.out.println(ASN1Dump.dumpAsString(structure));

        structure = new MyStructure(1, created, baseData, "hello", "world");

        System.out.println(ASN1Dump.dumpAsString(structure));
    }
}
