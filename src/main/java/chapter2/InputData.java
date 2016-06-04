/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

import java.math.BigInteger;

/**
 *
 * @author fran
 */
public class InputData {
    /** 16 bytes = 128 bits */
    public static byte[] INPUT_0  = new byte[] {       0x00,        0x11,        0x22,        0x33,
                                                       0x44,        0x55,        0x66,        0x77,
                                                (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb,
                                                (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff};
    /** 24 bytes = 192 bits */
    public static byte[] INPUT_1  = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                                                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
    /** 24 bytes = 192 bits. Patron repetido */
    public static byte[] INPUT_2  = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                                                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
    /** 23 bytes */
    public static byte[] INPUT_3  = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                                                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};




    /** 24 bytes = 192 bits */
    public static byte[] KEY_0 = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                             0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                                             0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
    /** 8 bytes = 64 bits */
    public static byte[] KEY_1 = new byte[] {       0x01,        0x23,        0x45,        0x67,
                                             (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};

    /** 24 bytes = 192 bits */
    public static byte[] KEY_2 = new byte[] {       0x73,        0x2f,        0x2d,        0x33, (byte) 0xc8, 0x01,        0x73,        0x2b,
                                             (byte) 0x72, (byte) 0x06, (byte) 0x75, (byte) 0x6c, (byte) 0xbd, 0x44, (byte) 0xf9, (byte) 0xc1,
                                             (byte) 0xc1,        0x03, (byte) 0xdd, (byte) 0xd9,        0x7c, 0x7c, (byte) 0xbe, (byte) 0x8e};
    /** 24 bytes = 192 bits Equivalente a KEY_2 con DESede*/
    public static byte[] KEY_x = new byte[] {       0x73,        0x2f,        0x2c,        0x32, (byte) 0xc8, 0x01,        0x73,        0x2a,
                                             (byte) 0x73, (byte) 0x07, (byte) 0x75, (byte) 0x6d, (byte) 0xbc, 0x45, (byte) 0xf8, (byte) 0xc1,
                                             (byte) 0xc1,        0x02, (byte) 0xdc, (byte) 0xd9,        0x7c, 0x7c, (byte) 0xbf, (byte) 0x8f};
    /** 16 bytes = 128 bits */
    public static byte[] KEY_3 = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                             0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};



    /** 8 bytes = 64 bits */
    public static byte[] SALT_0 = new byte[] {0x7d, 0x60, 0x43, 0x5f, 0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0xae};

    /** */
    public static byte[] MESSAGE_1 = new byte[] {(byte) 'a', (byte) 'b', (byte) 'c'};

    public static BigInteger G_512 = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7" +
                                                    "749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b" +
                                                    "410b7a0f12ca1cb9a428cc", 16);
    public static BigInteger P_512 = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd387" +
                                                    "44d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94b" +
                                                    "f0573bf047a3aca98cdf3b", 16);

    public static BigInteger F_1 = new BigInteger("fffffffffffffffffffffffffffffffeffffffffffffffff", 16);
    public static BigInteger F_2 = new BigInteger("fffffffffffffffffffffffffffffffefffffffffffffffc", 16);
    public static BigInteger F_3 = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
    public static BigInteger F_4 = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
    public static BigInteger F_5 = new BigInteger("f8e6d46a003725879cefee1294db32298c06885ee186b7ee", 16);
    public static BigInteger F_6 = new BigInteger("ffffffffffffffffffffffff99def836146bc9b1b4d22831", 16);

    /***************************************************************************/
    /*                           Metodos Privados                              */
    /***************************************************************************/
    /***************************************************************************/
    /*                           Metodos Protegidos                            */
    /***************************************************************************/
    /***************************************************************************/
    /*                           Constructores                                 */
    /***************************************************************************/
    public InputData() {}

    /***************************************************************************/
    /*                           Metodos Publicos                              */
    /***************************************************************************/
}
