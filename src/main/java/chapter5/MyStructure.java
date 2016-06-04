/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chapter5;

import java.util.Date;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;

/**
 * Implementation of an Example ASN.1 structure
 * <pre>
 *  MyStructure ::= SEQUENCE {
 *                  version INTEGER DEFAULT 0,
 *                  created GeneralizedTime,
 *                  baseData OCTET STRING,
 *                  extraData [0] UTF8String OPTIONAL,
 *                  commentData [1] UTF8String OPTOINAL}
 * </pre>
 *
 * @author fran
 */
public class MyStructure extends ASN1Encodable {
    private DERInteger         _version;
    private DERGeneralizedTime _created;
    private ASN1OctetString    _baseData;
    private DERUTF8String      _extraData   = null;
    private DERUTF8String      _commentData = null;

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
    public MyStructure(final ASN1Sequence seq) {
        int index = 0;

        if (seq.getObjectAt(0) instanceof DERInteger)
        {
            _version = (DERInteger) seq.getObjectAt(0);
            index++;
        }
        else _version = new DERInteger(0);

        _created  = (DERGeneralizedTime) seq.getObjectAt(index++);
        _baseData = (ASN1OctetString)    seq.getObjectAt(index++);

        //Check for optional fields
        for (int i = index; i != seq.size(); i++)
        {
            ASN1TaggedObject t = (ASN1TaggedObject) seq.getObjectAt(i);

            switch (t.getTagNo())
            {
                case 0: _extraData   = DERUTF8String.getInstance(t, false); break;
                case 1: _commentData = DERUTF8String.getInstance(t, false); break;
                default: throw new IllegalArgumentException("Unknown tag " + t.getTagNo() + " in constructor.");
            }
        }
    }

    public MyStructure(final int version, final Date created, byte[] baseData, String extraData, String commentData) {
        _version  = new DERInteger        (version);
        _created  = new DERGeneralizedTime(created);
        _baseData = new DEROctetString    (baseData);

        if (extraData   != null) _extraData   = new DERUTF8String(extraData);
        if (commentData != null) _commentData = new DERUTF8String(commentData);
    }

    /*------------------------------------------------------------------------*/
    /*                          Metodos Publicos                              */
    /*------------------------------------------------------------------------*/
    /**
     *
     * @return
     */
    public DERObject toASN1Object() {
        ASN1EncodableVector result = new ASN1EncodableVector();

        if (_version.getValue().intValue() != 0) result.add(_version);

        result.add(_created);
        result.add(_baseData);

        if (_extraData   != null) result.add(new DERTaggedObject(false, 0, _extraData));
        if (_commentData != null) result.add(new DERTaggedObject(false, 1, _commentData));

        return new DERSequence(result);
    }
}
