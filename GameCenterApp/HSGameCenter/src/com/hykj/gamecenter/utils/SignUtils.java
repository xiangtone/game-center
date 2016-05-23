
package com.hykj.gamecenter.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import android.util.Log;

public class SignUtils {

    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String sign(String content, String privateKey) {
        try {
            Log.e("privateKey", privateKey);

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            Log.e("priPKCS8", "" + priPKCS8);
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM, "BC");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            Log.e("e", "e=" + e);
            e.printStackTrace();
        }
        //com.android.org.bouncycastle.jcajce.provider.asymmetric.util.ExtendedInvalidKeySpecException: 
        //unable to process key spec: java.lang.ClassCastException: 
        //com.android.org.bouncycastle.asn1.DLSequence cannot be cast to com.android.org.bouncycastle.asn1.ASN1Integer

        return null;
    }

}
