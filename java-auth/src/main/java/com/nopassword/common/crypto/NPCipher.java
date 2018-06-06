package com.nopassword.common.crypto;

import java.nio.charset.Charset;
import javax.crypto.Cipher;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encrypts and decrypts data.
 *
 * @author NoPassword
 */
public abstract class NPCipher {

    private static final Logger LOG = LoggerFactory.getLogger(NPCipher.class);
    protected Charset charset;

    protected abstract Cipher getCipher(int cipherMode);

    /**
     * Encrypts data using AES algorithm
     *
     * @param plaintext Data to be encrypted
     * @return Encrypted data
     */
    public String encrypt(String plaintext) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
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
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted.getBytes()));
            return new String(original, charset);
        } catch (Exception ex) {
            LOG.error("Error decrypting, have you installed JCE unlimited strength policy files?", ex);
        }
        return null;
    }

    public Charset getCharset() {
        return charset;
    }

}
