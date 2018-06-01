package com.nopassword.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.NPCipher;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.AuthResult;
import com.nopassword.common.model.AuthStatus;
import com.nopassword.common.model.AuthResponse;
import com.nopassword.common.model.EncryptedAuthRequest;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class Authentication {

    private static final Logger LOG = LoggerFactory.getLogger(Authentication.class);
    private static final String SUCCEEDED = "Succeeded";
    private static final String VALUE = "Value";
    private static final String PAYLOAD = "Payload";
    private static final String MESSAGE = "Message";

    /**
     * Authenticates an user using NoPassword REST service.
     *
     * @param url NoPassword authentication URL.
     * @param request Authentication request.
     * @return true if user credentials are valid.
     */
    public static AuthResult authenticateUser(String url, AuthRequest request) {
        RestClient client = new RestClient();
        AuthResponse response = client.post(url, request, AuthResponse.class);
        if (response.isSucceeded()) {
            return response.getAuthResult();
        } else {
            LOG.error("authenticateUser: " + response.getMessage());
            AuthResult result = new AuthResult();
            result.setAuthStatus(AuthStatus.LogError);
            return result;
        }
    }

    public static AuthResult authenticateUserAsync(String url, AuthRequest request) {
        RestClient client = new RestClient();
        AuthResponse response = client.post(url, request, AuthResponse.class);
        if (response.isSucceeded()) {
            return response.getAuthResult();
        } else {
            LOG.error("authenticateUserAsync: " + response.getMessage());
            AuthResult result = new AuthResult();
            result.setAuthStatus(AuthStatus.LogError);
            return result;
        }
    }

    /**
     * Authenticates an user using NoPassword async authentication service.
     *
     * @param url NoPassword authentication URL.
     * @param request Authentication request.
     * @param cipher
     * @return Authentication result.
     */
    public static AuthResult authenticateUserEncAsync(String url, AuthRequest request, NPCipher cipher) {
        EncryptedAuthRequest encryptedRequest;
        try {
            encryptedRequest = new EncryptedAuthRequest(request, cipher);
        } catch (JsonProcessingException ex) {
            LOG.error("Error creating encrypted authentication request. Could't parse AuthRequest json.", ex);
            return null;
        }
        LOG.debug(encryptedRequest.toString());
        RestClient client = new RestClient();
        Map result = client.post(url, encryptedRequest, Map.class);
        return unwrap(result, cipher);
    }

    /**
     *
     * @param url
     * @param loginToken
     * @return Authentication status.
     * @throws JsonProcessingException
     */
    public static AuthStatus checkLoginToken(String url, String loginToken) throws JsonProcessingException {
        String request = "{\"AsyncLoginToken\":\"" + loginToken + "\"}";
        RestClient client = new RestClient();
        AuthResponse result = client.post(url, request, AuthResponse.class);

        if (AuthStatus.LogError.equals(result.getAuthResult().getAuthStatus())) {
            LOG.error("checkLoginToken: " + result.getMessage());
        }

        return result.getAuthResult().getAuthStatus();
    }

    /**
     *
     * @param url
     * @param loginToken
     * @param cipher
     * @return Authentication status.
     * @throws JsonProcessingException
     */
    public static AuthStatus checkLoginToken(String url, String loginToken, NPCipher cipher) throws JsonProcessingException, IOException {
        EncryptedAuthRequest request = new EncryptedAuthRequest(loginToken, cipher);
        RestClient client = new RestClient();
//        Map result = client.post(url, request, Map.class);
//        return unwrap(result, cipher).getAuthStatus();
        AuthResponse response = client.post(url, request, AuthResponse.class);
        String payload = response.getAuthResult().getPayload();
        ObjectMapper mapper = new ObjectMapper();
        response.setAuthResult(mapper.readValue(cipher.decrypt(payload), AuthResult.class));
        return response.getAuthResult().getAuthStatus();
    }

    private static AuthResult unwrap(Map result, NPCipher cipher) {
        if (result != null && (boolean) result.get(SUCCEEDED)) {
            try {
                String jsonPayload = ((String) ((Map) result.get(VALUE)).get(PAYLOAD));
                jsonPayload = cipher.decrypt(jsonPayload);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonPayload);
                if (node.get(SUCCEEDED).asBoolean()) {
                    return mapper.readValue(node.get(VALUE).toString(), AuthResult.class);
                } else {
                    LOG.error("Error checking authentication result value: " + node.get(MESSAGE).asText());
                }
            } catch (IOException ex) {
                LOG.error("An error ocurred parsing authentication result", ex);
            }
        }
        LOG.error("An error ocurred: " + result.get(MESSAGE));
        return null;
    }

}
