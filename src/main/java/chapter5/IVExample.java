/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.security.AlgorithmParameters;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.util.ASN1Dump;
import utils.CryptoDefs;

/**
 *
 * @author fran
 */
public class IVExample {

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
    public IVExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(CryptoDefs.Algorithm.AES.getName(), CryptoDefs.Provider.BC.getName());

        IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);

        params.init(ivSpec);

        ASN1InputStream aIn = new ASN1InputStream(params.getEncoded("ASN.1"));

        System.out.println(ASN1Dump.dumpAsString(aIn.readObject()));
    }
}
