package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.NPCipher;
import com.nopassword.common.utils.Utils;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author NoPassword
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EncryptedAuthRequest {

    @JsonIgnore
    private final NPCipher cipher;

    @JsonProperty("Payload")
    private String payload;

    @JsonProperty("Timestamp")
    private final String timestamp;

    @JsonProperty("Encrypted")
    private boolean encrypted;

    @JsonProperty("NoSignature")
    private boolean noSignature;

    @JsonProperty("Signature")
    private String signature;

    @JsonIgnore
    private String plainPayload;

    /**
     * Encrypted authentication request to NoPassword API
     *
     * @param request Authentication request. This request is parsed as a JSON
     * and encrypted with the provided cipher.
     * @param cipher Cipher used to encrypt the authentication request
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public EncryptedAuthRequest(Object request, NPCipher cipher) throws JsonProcessingException {
        this.timestamp = Utils.currentTime();
        this.encrypted = true;
        this.noSignature = true;
        this.cipher = cipher;
        setPayload(request);
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(Object request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.plainPayload = mapper.writeValueAsString(request);
        setSignature(plainPayload);
        this.payload = cipher.encrypt(plainPayload);
    }

    /**
     * Gets the timestamp in UTC format.
     *
     * @return UTC timestamp when this object was created
     */
    public String getTimestamp() {
        return timestamp;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isNoSignature() {
        return noSignature;
    }

    public void setNoSignature(boolean noSignature) {
        this.noSignature = noSignature;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * Hashes the authentication request using Blowfish algorithm.
     *
     * @param signature JSON authentication request
     */
    private void setSignature(String signature) {
        String salt = BCrypt.gensalt();
        this.signature = BCrypt.hashpw(signature, salt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EncryptedAuthRequest{"
                + "cipher=" + cipher
                + ", payload=" + payload
                + ", timestamp=" + timestamp
                + ", encrypted=" + encrypted
                + ", noSignature=" + noSignature
                + ", signature=" + signature
                + ", plainPayload=" + plainPayload + '}';
    }

}
