/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter7;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author fran
 */
public class PathChecker extends PKIXCertPathChecker implements java.io.Serializable {
    private static final long serialVersionUID = -1L;
    private static final String _NAME = PathChecker.class.getName();

    private KeyPair         _responderPair;
    private X509Certificate _caCert;
    private BigInteger      _revokedSerialNumber;

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
     * @param responderPair
     * @param caCert
     * @param revokedSerialNumber
     */
    public PathChecker(KeyPair responderPair, X509Certificate caCert, BigInteger revokedSerialNumber) {
        _responderPair       = responderPair;
        _caCert              = caCert;
        _revokedSerialNumber = revokedSerialNumber;
    }

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @param forward
     * @throws CertPathValidatorException
     */
    @Override
    public void init(boolean forward) throws CertPathValidatorException {
        // Ignore
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isForwardCheckingSupported() {return true;}

    /**
     *
     * @return
     */
    @Override
    public Set<String> getSupportedExtensions() {return null;}

    /**
     *
     * @param cert
     * @param unresolvedCritExts
     * @throws CertPathValidatorException
     */
    @Override
    public void check(Certificate cert, Collection<String> unresolvedCritExts) throws CertPathValidatorException {
        X509Certificate x509Cert = (X509Certificate) cert;

        try
        {
            String message = OCSPResponderExample.getStatusMessage(_responderPair, _caCert, _revokedSerialNumber, x509Cert);

            if (message.endsWith("good")) System.out.println(message);
            else throw new CertPathValidatorException(message);
        }
        catch (Exception e)
        {
            throw new CertPathValidatorException("Exception verifying certificate: " + e, e);
        }
    }
}
