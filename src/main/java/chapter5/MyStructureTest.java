/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.util.Date;

/**
 *
 * @author fran
 */
public class MyStructureTest {

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
    public MyStructureTest() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        byte[] baseData = new byte[5];
        Date   created  = new Date(0);

        MyStructure structure = new MyStructure(0, created, baseData, null, null);
        System.out.println(Utils.toHex(structure.toASN1Object().getEncoded()));

        if (structure.equals(structure.toASN1Object()) == false) System.out.println("Comparison failed!!");

        structure = new MyStructure(0, created, baseData, "hello", null);
        System.out.println(Utils.toHex(structure.toASN1Object().getEncoded()));

        if (structure.equals(structure.toASN1Object()) == false) System.out.println("Comparison failed!!");

        structure = new MyStructure(0, created, baseData, null, "world");
        System.out.println(Utils.toHex(structure.toASN1Object().getEncoded()));

        if (structure.equals(structure.toASN1Object()) == false) System.out.println("Comparison failed!!");

        structure = new MyStructure(0, created, baseData, "hello", "world");
        System.out.println(Utils.toHex(structure.toASN1Object().getEncoded()));

        if (structure.equals(structure.toASN1Object()) == false) System.out.println("Comparison failed!!");

        structure = new MyStructure(1, created, baseData, null, null);
        System.out.println(Utils.toHex(structure.toASN1Object().getEncoded()));

        if (structure.equals(structure.toASN1Object()) == false) System.out.println("Comparison failed!!");

    }
}
