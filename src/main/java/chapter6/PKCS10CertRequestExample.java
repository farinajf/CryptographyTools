/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import utils.CryptoDefs;

/**
 * Generation of a basic PKCS#10 request.
 * @author fran
 */
public class PKCS10CertRequestExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = PKCS10CertRequestExample.class.getName();

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
    public PKCS10CertRequestExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static PKCS10CertificationRequest generateRequest(KeyPair pair) throws Exception {
        return new PKCS10CertificationRequest("SHA256withRSA", new X500Principal("CN=Requested Test Certificate"), pair.getPublic(), null, pair.getPrivate());
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance(CryptoDefs.Algorithm.RSA.getName(), CryptoDefs.Provider.BC.getName());

        kpGen.initialize(1024, Utils.createFixedRandom());

        KeyPair pair = kpGen.generateKeyPair();

        PKCS10CertificationRequest request = generateRequest(pair);

        PEMWriter pemWrt = new PEMWriter(new OutputStreamWriter(System.out));

        pemWrt.writeObject(request);

        pemWrt.close();
    }
}
