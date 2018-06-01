package com.nopassword.common.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encrypts and decrypts data with AES/CBC/PKCS5PADDING or RSA/ECB/PKCS1Padding
 *
 * @author NoPassword
 */
public class NPCipher {

    /*
     * In Java, the standard padding name is PKCS5Padding, not
     * PKCS7Padding. Java is actually performing PKCS #7 padding, but in
     * the JCA specification, PKCS5Padding is the name given
     */
    private static final String AES = "AES/CBC/PKCS5PADDING";
    private static final String RSA = "RSA/ECB/PKCS1Padding";
    private static final Logger LOG = LoggerFactory.getLogger(NPCipher.class);
    private final Charset charset;
    private IvParameterSpec randomIvSpec;
    private SecretKeySpec skeySpec;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String algorithm;

    /**
     * Creates a AES cipher
     *
     * @param key Encryption key
     * @param iv Initialization vector
     */
    public NPCipher(byte[] key, byte[] iv) {
        this.algorithm = AES;
        this.charset = StandardCharsets.UTF_8;
        try {
            this.skeySpec = new SecretKeySpec(key, "AES");
            this.randomIvSpec = new IvParameterSpec(iv);
        } catch (Exception ex) {
            LOG.error("Error initializing, have you installed JCE unlimited strength policy files?", ex);
        }
    }
    
    /**
     * Creates a AES cipher
     *
     * @param key Encryption key
     * @param iv Initialization vector
     * @param charset
     */
    public NPCipher(byte[] key, byte[] iv, Charset charset) {
        this.algorithm = AES;
        this.charset = charset;
        try {
            this.skeySpec = new SecretKeySpec(key, "AES");
            this.randomIvSpec = new IvParameterSpec(iv);
        } catch (Exception ex) {
            LOG.error("Error initializing, have you installed JCE unlimited strength policy files?", ex);
        }
    }

    /**
     * Creates a RSA cipher
     *
     * @param publicKey RSA public encryption key
     * @param privateKey RSA private encryption key
     */
    public NPCipher(PublicKey publicKey, PrivateKey privateKey) {
        this.algorithm = RSA;
        this.charset = StandardCharsets.UTF_8;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Creates a RSA cipher
     *
     * @param publicKey RSA public encryption key
     * @param privateKey RSA private encryption key
     * @param charset
     */
    public NPCipher(PublicKey publicKey, PrivateKey privateKey, Charset charset) {
        this.algorithm = RSA;
        this.charset = charset;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Encrypts data using AES algorithm
     *
     * @param plaintext Data to be encrypted
     * @return Encrypted data
     */
    public String encrypt(String plaintext) {
        try {
            Cipher cipher = createCipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(charset));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception ex) {
            LOG.error("Error encrypting, have you installed JCE unlimited strength policy files?", ex);
        }
        return null;
    }

    /**
     * Decrypts data that has been encrypted with AES algorithm
     *
     * @param encrypted Encrypted data
     * @return Decrypted data
     */
    public String decrypt(String encrypted) {
        try {
            Cipher cipher = createCipher(Cipher.DECRYPT_MODE);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted.getBytes()));
            return new String(original, charset);
        } catch (Exception ex) {
            LOG.error("Error decrypting, have you installed JCE unlimited strength policy files?", ex);
        }
        return null;
    }

    public String sign(String data) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(data.getBytes(charset));
            return new String(Base64.getEncoder().encode(sig.sign()));
        } catch (Exception ex) {
            LOG.error("error signing data", ex);
            return null;
        }
    }

    private Cipher createCipher(int cipherMode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(this.algorithm);

        switch (this.algorithm) {
            case RSA:
                if (cipherMode == Cipher.DECRYPT_MODE) {
                    cipher.init(cipherMode, privateKey);
                } else {
                    cipher.init(cipherMode, publicKey);
                }
                return cipher;
            case AES:
                cipher.init(cipherMode, skeySpec, randomIvSpec, new SecureRandom());
                return cipher;
        }
        throw new IllegalArgumentException();
    }

}
