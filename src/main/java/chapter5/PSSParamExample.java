/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.security.AlgorithmParameters;
import java.security.Signature;
import java.security.spec.PSSParameterSpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.util.ASN1Dump;
import utils.CryptoDefs;

/**
 * Example showing PSS parameter recovery and encoding.
 * @author fran
 */
public class PSSParamExample {

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
    public PSSParamExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static void main(String[] args) throws Exception {
        Signature signature = Signature.getInstance(CryptoDefs.Algorithm.SHA1withRSAandMGF1.getName(), CryptoDefs.Provider.BC.getName());

        //Set the default parameters
        //signature.setParameter(PSSParameterSpec.DEFAULT);
        signature.setParameter(new PSSParameterSpec(0));

        //Get the default parameters
        AlgorithmParameters params = signature.getParameters();

        //Look at the ASN.1 encoding
        ASN1InputStream aIn  = new ASN1InputStream(params.getEncoded("ASN.1"));
        DERObject       asn1 = aIn.readObject();

        System.out.println(ASN1Dump.dumpAsString(asn1));

        System.out.println(Utils.base64Encode(asn1.toASN1Object().getEncoded("ASN.1")));
    }
}
