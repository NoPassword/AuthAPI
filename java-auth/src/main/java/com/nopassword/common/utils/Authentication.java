package com.nopassword.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.RSACipher;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.AuthResult;
import com.nopassword.common.model.AuthStatus;
import com.nopassword.common.model.GenericResponse;
import com.nopassword.common.model.GenericRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class Authentication {

    private static final Logger LOG = LoggerFactory.getLogger(Authentication.class);
    private static final String LOGIN_TOKEN = "AsyncLoginToken";

    /**
     * Authenticates an user using NoPassword REST service.
     *
     * @param url NoPassword authentication URL.
     * @param request Authentication request.
     * @return true if user credentials are valid.
     * @throws java.io.IOException
     */
    public static AuthResult authenticateUser(String url, AuthRequest request) throws IOException {
        RestClient client = new RestClient();
        GenericResponse response = client.post(url, request, GenericResponse.class);

        if (LOG.isDebugEnabled()) {
            ObjectMapper mapper = new ObjectMapper();
            LOG.debug("NoPassword authentication request:\n" + mapper.writeValueAsString(request));
            LOG.debug("Response:\n" + mapper.writeValueAsString(response));
        }

        if (response.succeeded()) {
            return GenericResponseHandler.parseGenericResponse(response, AuthResult.class);
        } else {
            LOG.error("authenticateUser: " + response.getMessage());
            AuthResult result = new AuthResult();
            result.setAuthStatus(AuthStatus.LOG_ERROR);
            return result;
        }
    }

    /**
     * Authenticates an user using NoPassword encrypted authentication services.
     *
     * @param url NoPassword authentication URL.
     * @param request Authentication request.
     * @param rsaCipher RSA cipher to sign request payload and decrypt AES keys
     * from response.
     * @return Authentication result.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static AuthResult authenticateUserEncrypted(String url, AuthRequest request, RSACipher rsaCipher) throws JsonProcessingException, IOException {
        GenericRequest encryptedRequest = new GenericRequest(request.getApiKey(), request, rsaCipher);
        RestClient client = new RestClient();
        GenericResponse response = client.post(url, encryptedRequest, GenericResponse.class);

        if (response.succeeded()) {
            return GenericResponseHandler.parseGenericResponse(response, rsaCipher, AuthResult.class);
        } else {
            LOG.error("Async encrypted authentication failed: " + response.getMessage());

            if (LOG.isDebugEnabled()) {
                LOG.debug(new ObjectMapper().writeValueAsString(encryptedRequest));
            }
            AuthResult result = new AuthResult();
            result.setAuthStatus(AuthStatus.LOG_ERROR);
            return result;
        }
    }

    /**
     *
     * @param url
     * @param apiKey Generic API key.
     * @param loginToken
     * @param rsaCipher
     * @return Authentication status.
     * @throws JsonProcessingException
     */
    public static AuthResult checkEncLoginToken(String url, String apiKey, String loginToken, RSACipher rsaCipher) throws JsonProcessingException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put(LOGIN_TOKEN, loginToken);
        GenericRequest request = new GenericRequest(apiKey, loginToken, rsaCipher);
        RestClient client = new RestClient();
        GenericResponse response = client.post(url, request, GenericResponse.class);
        return GenericResponseHandler.parseGenericResponse(response, rsaCipher, AuthResult.class);
    }

    /**
     *
     * @param url
     * @param loginToken
     * @return Authentication status.
     * @throws JsonProcessingException
     */
    public static AuthResult checkLoginToken(String url, String loginToken) throws JsonProcessingException, IOException {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(LOGIN_TOKEN, loginToken);
        RestClient client = new RestClient();
        GenericResponse response = client.post(url, tokenMap, GenericResponse.class);
        return GenericResponseHandler.parseGenericResponse(response, AuthResult.class);
    }

}
