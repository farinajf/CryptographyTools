/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 * <li>PKCS#1: Estandar Criptografico RSA. Define el formato de cifrado RSA.</li>
 * <p/>
 * <li>PKCS#2: <em>Obsoleto</em>. Definia el cifrado RSA de resumenes de mensajes,
 * fue absorbido por el PKCS#1.</li>
 * <p/>
 * <li>PKCS#3: Estandar de intercambio de claves Diffie-Hellman.</li>
 * <p/>
 * <li>PKCS#4: <em>Obsoleto</em>. Definia la sintaxis de la clave RSA, pero fue
 * absobido por el PKCS#1.</li>
 * <p/>
 * <li>PKCS#5: Estandar de cifrado basado en contraseñas. Establece un sistema de
 * rellenado para cifrados en bloque.</li>
 * <p/>
 * <li>PKCS#6: Estandar de sintaxis de certificados extendidos. Define extensiones
 * a la antigua especificacion de certificados X.509.</li>
 * <p/>
 * <li>PKCS#7: Estandar sobre la sintaxis del mensaje criptografico. Se usa para
 * firmar y/o cifrar mensajes en PKI. Tambien se usa para la diseminacion de
 * certificados, p.e. como respuesta a un mensaje PKCS#10. Nota: CMS (Cryptographic
 * Message Syntax) es un estandar para firmar digitalmente.</li>
 * <p/>
 * <li>PKCS#8: Estadar sobre la sintaxis de la información de clave privada.</li>
 * <p/>
 * <li>PKCS#9: Tipos de atributos seleccionados.</li>
 * <p/>
 * <li>PKCS#10: Estandar de solicitud de certificacion. Formato de los mensajes
 * enviados a una CA para solicitar la certificacion de una clave publica.</li>
 * <p/>
 * <li>PKCS#11: Interfaz de dispositivo criptografico (cryptoki). Define un API
 * generico de acceso a dispositivos criptograficos.</li>
 * <p/>
 * <li>PKCS#12: Estandar de sintaxis de intercambio de informacion personal. Define
 * un formato de fichero usado comunmente para almacenar claves privadas con su
 * certificado de clave publica protegido mediante clave simetrica.</li>
 * <p/>
 * <li>PKCS#13: Estandar de criptografia de curva eliptica.</li>
 * <p/>
 * <li>PKCS#14: Generacion de numeros pseudo-aleatorios.</li>
 * <p/>
 * <li>PKCS#15: Estandar de formato de informacion de dispositivo criptografico.
 * Define un estandar que permite a los usuarios de dispositivos criptograficos
 * identificarse con aplicaciones independientemente de la implementacion del
 * PKCS#11 u otro API.</li>
 * <p/>
 * @author fran
 */
public class CryptoDefs {
    private static final String _SLASH = "/";

    /**
     *
     */
    public enum Algorithm {
        /**
         * AES: Advanced Encryption Standard
         * <p>Block size: 16 bytes
         */
        AES("AES"),
        AESWrap("AESWrap"),
        /**
         * ARC4:
         * <p>128-bit key, Stream Cipher
         */
        ARC4("ARC4"),
        /**
         * Blowfish
         * <p>Block size: 8 bytes
         */
        BLOWFISH("Blowfish"),
        /**
         * DES: Data Encryption Standard
         * <p>Block size: 8 bytes
         */
        DES("DES"),
        /**
         * Triple-DES: DES-Encrypt-Decrypt-Encrypt
         * <p>Block size: ? bytes
         */
        DESede("DESede"),
        /**
         * Diffie-Hellman
         */
        DH("DH"),
        /**
         * Digital Signature Algorithm
         */
        DSA("DSA"),
        /**
         * Diffie-Hellman with Eliptic Curve
         */
        ECDH("ECDH"),
        /**
         * Elliptic Curve DSA
         */
        ECDSA("ECDSA"),
        /**
         * El Gamal Algorithm
         */
        EL_GAMAL("ElGamal"),
        SHA1("SHA-1"),
        SHA1withRSA("SHA1withRSA"),
        SHA1withRSAandMGF1("SHA1withRSAandMGF1"),
        SHA256("SHA-256"),
        SHA256withRSA("SHA256withRSA"),
        SHA256withRSAEncryption("SHA256withRSAEncryption"),
        RSA("RSA"),
        HmacSHA1("HmacSHA1"),
        /**
         * PBE with function and cipher.
         * <p>- function: is the algorithm used to support tghe mechanism that generates the key.
         * <p>- cipher: is the underlying cipher used for the encryption.
         * <p>"PBEWithSHAAnd3KeyTripleDES"
         */
        PBEWithSHAAnd3KeyTripleDES("PBEWithSHAAnd3KeyTripleDES"),
        PBEWithSHAAnd2_KeyTripleDES_CBC("PBEWithSHAAnd2-KeyTripleDES-CBC"),
        PBEWithSHAAnd3_KeyTripleDES_CBC("PBEWithSHAAnd3-KeyTripleDES-CBC"),
        PBEWithSHA1AndDES("PBEWithSHA1AndDES");

        private final String _name;

        private Algorithm(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum CertType {
        X509("X.509");

        private final String _name;

        private CertType(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum Mode {
        NONE("None"),
        /**
         * "CBC": Cipher Block Chaining.
         * This mode reduces the likelihood of patterns appearing in the ciphertext
         * by XORing the block of data to be encrypted with the last block of
         * ciphertext produced and then applying the raw cipher to produce the
         * next block of ciphertext.
         */
        CBC("CBC"),
        /**
         * "CFB" Cipher Feedback.
         * Streaming Mode
         */
        CFB("CFB"),
        /**
         * "CFB8" Streaming Mode
         */
        CFB8("CFB8"),
        /**
         * "CTR": also known as SIC (Segmented Integer Counter) mode.
         * Streaming mode.
         */
        CTR("CTR"),
        /**
         * Cipher Test Stealing.
         * Combines the use of CBC mode with some additional XOR operations on the
         * final encrypted block of the data being processed to produce encrypted
         * data that is the same length as the input.
         */
        CTS("CTS"),
        /**
         * Electronic Code Book.
         * Problem: if there are patterns in the data, there will be patterns in
         * the encrypted data. Given the same block of input bytes, you will
         * always get the same block of output bytes.
         */
        ECB("ECB"),
        /**
         * "OFB" Output Feedback.
         * Streaming mode.
         * Current wisdom is to use CTR instead of OFB, as it gives you more control
         * over the key stream.
         */
        OFB("OFB"),
        /**
         * "OFB8" Streaming Mode
         */
        OFB8("OFB8"),
        /**
         * "Wrap"
         */
        WRAP("Wrap");

        private final String _name;

        private Mode(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum Padding {
        /**
         * OAEP With Digest And Mask-Function-Padding
         */
        OAEPWithSHA1AndMGF1Padding("OAEPWithSHA1AndMGF1Padding"),
        /**
         * "PKCS1Padding". Original mechanism used with RSA algorithm.
         * <ul>
         * <le>type 0: is equivalent to NoPadding.</le>
         * <le>type 1: used with the public key.</le>
         * <le>type 2: used with the private key.</le>
         * </ul>
         */
        PKCS1("PKCS1Padding"),
        /**
         * "PKCS5Padding"
         */
        PKCS5("PKCS5Padding"),
        /**
         * "PKCS7Padding"
         */
        PKCS7("PKCS7Padding"),
        /**
         * "NoPadding"
         */
        NOPADDING("NoPadding");

        private final String _name;

        private Padding(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum Provider {
        BC("BC");

        private final String _name;

        private Provider(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum SignAlgorithm {
        NONEwithRSA("NONEwithRSA"),
        MD2withRSA("MD2withRSA"),
        MD5withRSA("MD5withRSA"),
        SHA1withRSA("SHA1withRSA"),
        SHA256withRSA("SHA256withRSA"),
        SHA384withRSA("SHA384withRSA"),
        SHA512withRSA("SHA512withRSA"),
        NONEwithDSA("NONEwithDSA"),
        SHA1withDSA("SHA1withDSA"),
        NONEwithECDSA("NONEwithECDSA"),
        SHA1withECDSA("SHA1withECDSA"),
        SHA256withECDSA("SHA256withECDSA"),
        SHA384withECDSA("SHA384withECDSA"),
        SHA512withECDSA("SHA512withECDSA");

        private final String _name;

        private SignAlgorithm(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum SSLContextAlgorithm {
        SSL("SSL"),
        SSLv2("SSLv2"),
        SSLv3("SSLv3"),
        TLS("TLS"),
        TLSv1("TLSv1"),
        TLSv1_1("TLSv1.1"),
        TLSv1_2("TLSv1.2");

        private final String _name;

        private SSLContextAlgorithm(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    /**
     *
     */
    public enum KeyManagerFactoryAlgorithm {
        PKIX("PKIX");

        private final String _name;

        private KeyManagerFactoryAlgorithm(String name) {this._name = name;}

        public String getName() {return _name;}
    }

    public enum KeyStoreType {
        JKS   ("JKS"),
        PKCS12("PKCS12"),
        P12   ("P12"),
        JCEKS ("JCEKS");

        private final String _name;

        private KeyStoreType(String name) {this._name = name;}

        public String getName() {return _name;}
    }

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
    private CryptoDefs() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * AlgorithName/Mode/TypeOfPadding
     * PKCS: Public-Key Cryptography Standars
     * <p><b>Symmetric Block Cipher Modes:</b>
     * <p> - ECB: Electronic Code Book
     * <p> - CBC: Cipher Block Chaining - reduces the likehood of patterns appearing
     * in the ciphertext by XORing the block of data to be encrypted with the last
     * block of ciphertext produced and then applying the raw cipher to produce
     * the next block of ciphertext.
     * <p> - CTS: Cipher Text Stealing - combines the use of CBC mode with some
     * additional XOR operations on the final encrypted block of the data being
     * processed ti produce encrypted data that is the same length as the input data.
     * <p><b>Streaming Symmetric Block Cipher Modes:</b>
     * <p> - CTR: Counter mode (also known as SIC Segmented Integer Counter)
     */
    public static final String getTransform(Algorithm algorithm, Mode mode, Padding padding) {
        return algorithm.getName() + _SLASH + mode.getName() + _SLASH + padding.getName();
    }
}
