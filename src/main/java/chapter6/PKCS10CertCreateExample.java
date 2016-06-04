/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter6;

import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

/**
 * An example of a basic CA.
 * @author fran
 */
public class PKCS10CertCreateExample implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = PKCS10CertCreateExample.class.getName();

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
    public PKCS10CertCreateExample() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    public static X509Certificate[] buildChain() throws Exception {
        // Create the certification request
        KeyPair pair = Utils.generateRSAKeyPair();

        PKCS10CertificationRequest request = PKCS10ExtensionExample.generateRequest(pair);

        // Create a root certificate
        KeyPair         rootPair = Utils.generateRSAKeyPair();
        X509Certificate rootCert = X509V1CreateExample.generateV1Certificate(rootPair);

        // Validate the certification request
        if (request.verify("BC") == false)
        {
            System.out.println("Request failed to verify!!");
            System.exit(1);
        }

        // Create the certificate using the information in the request
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN    (rootCert.getSubjectX500Principal());
        certGen.setNotBefore   (new Date(System.currentTimeMillis()));
        certGen.setNotAfter    (new Date(System.currentTimeMillis() + 50000));
        certGen.setSubjectDN   (new X500Principal(request.getCertificationRequestInfo().getSubject().getEncoded()));
        certGen.setPublicKey   (request.getPublicKey("BC"));
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(rootCert));
        certGen.addExtension(X509Extensions.SubjectKeyIdentifier,   false, new SubjectKeyIdentifierStructure(request.getPublicKey("BC")));
        certGen.addExtension(X509Extensions.BasicConstraints,       true,  new BasicConstraints(false));
        certGen.addExtension(X509Extensions.KeyUsage,               true,  new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
        certGen.addExtension(X509Extensions.ExtendedKeyUsage,       true,  new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

        // Extract the extension request attribute
        ASN1Set attributes = request.getCertificationRequestInfo().getAttributes();

        for (int i = 0; i < attributes.size(); i++)
        {
            Attribute attr = Attribute.getInstance(attributes.getObjectAt(i));

            // Process extension request
            if (attr.getAttrType().equals(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest))
            {
                X509Extensions extensions = X509Extensions.getInstance(attr.getAttrValues().getObjectAt(0));

                Enumeration e = extensions.oids();
                while (e.hasMoreElements())
                {
                    DERObjectIdentifier oid = (DERObjectIdentifier) e.nextElement();
                    X509Extension       ext = extensions.getExtension(oid);

                    certGen.addExtension(oid, ext.isCritical(), ext.getValue().getOctets());
                }
            }
        }

        X509Certificate issuedCert = certGen.generateX509Certificate(rootPair.getPrivate());

        return new X509Certificate[] {issuedCert, rootCert};
    }

    public static void main(String[] args) throws Exception {
        X509Certificate[] chain = buildChain();

        PEMWriter pemWrt = new PEMWriter(new OutputStreamWriter(System.out));

        pemWrt.writeObject(chain[0]);
        pemWrt.writeObject(chain[1]);

        pemWrt.close();
    }
}
