/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.util.Arrays;
import org.bouncycastle.cms.CMSCompressedData;
import org.bouncycastle.cms.CMSCompressedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;

/**
 * Basic use of CM compressed-data.
 * @author fran
 */
public class CompressedDataExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CompressedDataExample.class.getName();

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
    public CompressedDataExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Set up the generator
        CMSCompressedDataGenerator gen = new CMSCompressedDataGenerator();

        //2.- Compress the data
        CMSProcessableByteArray data = new CMSProcessableByteArray("Hello World!!".getBytes());

        CMSCompressedData compressed = gen.generate(data, CMSCompressedDataGenerator.ZLIB);

        System.out.println(Utils.base64Encode(compressed.getEncoded()));

        //3.- Re-create and uncompress the data
        compressed = new CMSCompressedData(compressed.getEncoded());

        byte[] recData = compressed.getContent();

        //4.- Compare uncompressed data to the original
        if (Arrays.equals((byte[]) data.getContent(), recData) == true) System.out.println("\t data recovery succeeded!!");
        else System.out.println("\t Could not find a matching recipient!!");
    }
}
