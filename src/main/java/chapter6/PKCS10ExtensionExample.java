/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import utils.CryptoDefs;

/**
 * Generation of a basic PKCS#10 request with an extension.
 * @author fran
 */
public class PKCS10ExtensionExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = PKCS10ExtensionExample.class.getName();

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
    public PKCS10ExtensionExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static PKCS10CertificationRequest generateRequest(KeyPair pair) throws Exception {
        // Create a SubjectAlternativeName extension value
        GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.rfc822Name, "test@test.test"));

        // Create the extensions object and add it as an attribute
        Vector oids   = new Vector();
        Vector values = new Vector();

        oids.add  (X509Extensions.SubjectAlternativeName);
        values.add(new X509Extension(false, new DEROctetString(subjectAltName)));

        X509Extensions extensions = new X509Extensions(oids, values);

        Attribute attribute = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, new DERSet(extensions));

        return new PKCS10CertificationRequest("SHA256withRSA",
                                              new X500Principal("CN=Requested Test Certificate"),
                                              pair.getPublic(),
                                              new DERSet(attribute),
                                              pair.getPrivate());
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
