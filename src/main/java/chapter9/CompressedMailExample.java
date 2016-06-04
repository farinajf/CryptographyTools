/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.mail.smime.SMIMECompressed;
import org.bouncycastle.mail.smime.SMIMECompressedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;

/**
 * A simple example that creates and processes an compressed mail message.
 * @author fran
 */
public class CompressedMailExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = CompressedMailExample.class.getName();

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
    public CompressedMailExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //1.- Create the message we want compressed
        MimeBodyPart dataPart = new MimeBodyPart();

        dataPart.setText("Hello World!!");

        //2.- Set up the generator
        SMIMECompressedGenerator gen = new SMIMECompressedGenerator();

        //3.- Generate the compressed message
        MimeBodyPart comPart = gen.generate(dataPart, SMIMECompressedGenerator.ZLIB);

        //4-. Create the mail message
        MimeMessage mail = Utils.createMimeMessage("example compressed message", comPart.getContent(), comPart.getContentType());

        //5.- Create the enveloped object from the mail message
        SMIMECompressed compressed = new SMIMECompressed(mail);

        //6.- Uncompression step
        MimeBodyPart recoveredPart = SMIMEUtil.toMimeBodyPart(compressed.getContent());

        //7.- Content display step
        System.out.print  ("Content: ");
        System.out.println(recoveredPart.getContent());
    }
}
