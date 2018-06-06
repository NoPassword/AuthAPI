package com.nopassword.common.crypto;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import javax.crypto.Cipher;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class RSACipher extends NPCipher {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RSACipher.class);
    private static final String DEFAULT_RSA_MODE = "RSA/ECB/PKCS1Padding";
    private String rsaMode;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * Creates a RSA cipher
     *
     * @param publicKey RSA public encryption key
     * @param privateKey RSA private encryption key
     * @param charset
     */
    public RSACipher(PublicKey publicKey, PrivateKey privateKey, Charset charset) {
        this(publicKey, privateKey, DEFAULT_RSA_MODE, charset);
    }

    /**
     * Creates a RSA cipher
     *
     * @param publicKey RSA public encryption key
     * @param privateKey RSA private encryption key
     * @param rsaMode
     * @param charset
     */
    public RSACipher(PublicKey publicKey, PrivateKey privateKey, String rsaMode, Charset charset) {
        this.rsaMode = rsaMode;
        this.charset = charset;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Signs data with RSA algorithm.
     *
     * @param data Data to be signed.
     * @return Signed data.
     */
    public String sign(String data) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(data.getBytes(getCharset()));
            return new String(Base64.getEncoder().encode(sig.sign()));
        } catch (Exception ex) {
            LOG.error("error signing data", ex);
            return null;
        }
    }

    
    public boolean verify(String plain, String signed) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(plain.getBytes(getCharset()));
            return signature.verify(signed.getBytes(getCharset()));
        } catch (Exception ex) {
            LOG.error("Error veifying signature", ex);
            return false;
        }
    }

    @Override
    protected Cipher getCipher(int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance(this.rsaMode);

            if (cipherMode == Cipher.DECRYPT_MODE) {
                cipher.init(cipherMode, privateKey);
            } else {
                cipher.init(cipherMode, publicKey);
            }
            return cipher;
        } catch (Exception ex) {
            LOG.error("Error obtaining RSA cipher", ex);
            return null;
        }
    }

}
