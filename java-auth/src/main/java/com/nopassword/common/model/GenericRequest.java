package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.RSACipher;
import com.nopassword.common.utils.Utils;
import java.util.Base64;

/**
 *
 * @author NoPassword
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GenericRequest {

    @JsonIgnore
    private final RSACipher rsaCipher;

    @JsonProperty("Payload")
    private String payload;

    @JsonProperty("Timestamp")
    private final String timestamp;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("Key")
    private String key;

    @JsonIgnore
    private String plainPayload;

    /**
     * Plain generic request to NoPassword API
     *
     * @param key NoPassword Generic API key.
     * @param request Authentication request. This request is parsed as a JSON
     * and encrypted with the provided cipher.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public GenericRequest(String key, Object request) throws JsonProcessingException {
        this.timestamp = Utils.currentTime();
        this.key = key;
        this.rsaCipher = null;
        setPayload(request);
    }

    /**
     * Encrypted generic request to NoPassword API
     *
     * @param request Authentication request. This request is parsed as a JSON
     * and encrypted with the provided cipher.
     * @param cipher Cipher used to encrypt the authentication request.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public GenericRequest(Object request, RSACipher cipher) throws JsonProcessingException {
        this.rsaCipher = cipher;
        this.timestamp = Utils.currentTime();
        setPayload(request);
    }

    /**
     * Encrypted generic request to NoPassword API
     *
     * @param key NoPassword Generic API key.
     * @param request Authentication request. This request is parsed as a JSON
     * and encrypted with the provided cipher.
     * @param cipher Cipher used to encrypt the authentication request.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public GenericRequest(String key, Object request, RSACipher cipher) throws JsonProcessingException {
        this.rsaCipher = cipher;
        this.timestamp = Utils.currentTime();
        this.key = key;
        setPayload(request);
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(Object request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.plainPayload = mapper.writeValueAsString(request);
        this.payload = new String(Base64.getEncoder().encode(plainPayload.getBytes()));
        setSignature(this.payload);
    }

    /**
     * Gets the timestamp in UTC format.
     *
     * @return UTC timestamp when this object was created
     */
    public String getTimestamp() {
        return timestamp;
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
        if (rsaCipher != null) {
            this.signature = rsaCipher.sign(timestamp + signature);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EncryptedAuthRequest{"
                + "cipher=" + rsaCipher
                + ", payload=" + payload
                + ", timestamp=" + timestamp
                + ", signature=" + signature
                + ", plainPayload=" + plainPayload
                + ", key=" + (key == null ? null : "<omited for safety>") + '}';
    }

}
