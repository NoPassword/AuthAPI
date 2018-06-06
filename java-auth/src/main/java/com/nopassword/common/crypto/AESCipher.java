package com.nopassword.common.crypto;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class AESCipher extends NPCipher {

    /*
     * In Java, the standard padding name is PKCS5Padding, not
     * PKCS7Padding. Java is actually performing PKCS #7 padding, but in
     * the JCA specification, PKCS5Padding is the name given
     */
    public static final String DEFAULT_AES_MODE = "AES/CBC/PKCS5PADDING";

    private static final Logger LOG = LoggerFactory.getLogger(AESCipher.class);
    private IvParameterSpec randomIvSpec;
    private SecretKeySpec skeySpec;
    private String aesMode;

    public AESCipher(byte[] key, byte[] iv, Charset charset) {
        this(key, iv, DEFAULT_AES_MODE, charset);
    }

    /**
     * Creates a AES cipher.
     *
     * @param key Encryption key.
     * @param iv Initialization vector.
     * @param aesMode
     * @param charset
     */
    public AESCipher(byte[] key, byte[] iv, String aesMode, Charset charset) {
        this.aesMode = aesMode;
        this.charset = charset;
        try {
            this.skeySpec = new SecretKeySpec(key, "AES");
            this.randomIvSpec = new IvParameterSpec(iv);
        } catch (Exception ex) {
            LOG.error("Error initializing, have you installed JCE unlimited strength policy files?", ex);
        }
    }

    @Override
    protected Cipher getCipher(int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance(this.aesMode);
            cipher.init(cipherMode, skeySpec, randomIvSpec, new SecureRandom());
            return cipher;
        } catch (Exception ex) {
            LOG.error("Error obtaining AES cipher", ex);
        }
        return null;
    }

}
