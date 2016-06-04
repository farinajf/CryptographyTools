/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter9;

import java.security.cert.CertStore;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import utils.CryptoDefs;

/**
 * Base class for signed examples.
 * @author fran
 */
public class SignedDataProcessor implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = SignedDataProcessor.class.getName();

    /*------------------------------------------------------------------------*/
    /*                          Metodos Privados                              */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                          Default Access                                */
    /*------------------------------------------------------------------------*/
    /**
     * Return a boolean array representing keyUsage with digitalSignature set.
     * @return
     */
    static boolean [] getKeyUsageForSignature() {
        boolean[] val = new boolean[9];

        val[0] = true;

        return val;
    }

    /*------------------------------------------------------------------------*/
    /*                          Metodos Protegidos                            */
    /*------------------------------------------------------------------------*/

    /*------------------------------------------------------------------------*/
    /*                            Constructores                               */
    /*------------------------------------------------------------------------*/
    /**
     * Constructor por defecto.
     */
    public SignedDataProcessor() {}

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     * Take a CMS SignedData message and a trust anchor and determine if
     * the message is signed with a valid signature from a end entity
     * certificate recognized by the trust anchor rootCert.
     * @param signedData
     * @param rootCert
     * @return
     * @throws Exception
     */
    public static boolean isValid(CMSSignedData signedData, X509Certificate rootCert) throws Exception {
        CertStore certsAndCRLs = signedData.getCertificatesAndCRLs("Collection", CryptoDefs.Provider.BC.getName());

        SignerInformationStore signers = signedData.getSignerInfos();
        Iterator               it      = signers.getSigners().iterator();

        if (it.hasNext())
        {
            SignerInformation signer            = (SignerInformation) it.next();
            X509CertSelector  signerConstraints = signer.getSID();

            signerConstraints.setKeyUsage(getKeyUsageForSignature());

            PKIXCertPathBuilderResult result = Utils.buildPath(rootCert, signer.getSID(), certsAndCRLs);

            return signer.verify(result.getPublicKey(), CryptoDefs.Provider.BC.getName());
        }

        return false;
    }
}
