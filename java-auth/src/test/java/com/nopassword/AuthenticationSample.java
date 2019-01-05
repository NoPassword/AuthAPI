package com.nopassword;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nopassword.common.crypto.RSACipher;
import com.nopassword.common.crypto.RSAUtils;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.AuthResult;
import com.nopassword.common.model.AuthStatus;
import com.nopassword.common.utils.Authentication;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates how to authenticate an user with NoPassword.
 *
 * @author NoPassword
 */
public class AuthenticationSample extends NoPasswordTest {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSample.class);
    private static final Properties PROPERTIES = new Properties();
    private static String AUTH_URL;
    private static String ASYNC_AUTH_URL;
    private static String CHECK_LOGIN_TOKEN_URL;
    private static String ENC_AUTH_URL;
    private static String ENC_ASYNC_AUTH_URL;
    private static String ENC_CHECK_LOGIN_TOKEN_URL;
    private static final Charset CHARSET = StandardCharsets.UTF_16LE;

    /**
     * API key for encrypted endpoints.
     */
    private static String GENERIC_API_KEY;

    /**
     * API key for plain endpoints.
     */
    private static String NOPASSWORD_LOGIN_KEY;

    private static final String USERNAME = "user@client.org";

    @BeforeClass
    public static void setup() {
        try {
            PROPERTIES.load(AuthenticationSample.class.getResourceAsStream("/config.properties"));
        } catch (IOException ex) {
            LOG.error("Error loading configuration file", ex);
            Assert.assertNotNull(null);
        }
        GENERIC_API_KEY = PROPERTIES.getProperty("generic_api_key");
        NOPASSWORD_LOGIN_KEY = PROPERTIES.getProperty("nopassword_login_key");

        //plain endpoints
        AUTH_URL = PROPERTIES.getProperty("auth_url");
        ASYNC_AUTH_URL = PROPERTIES.getProperty("async_auth_url");
        CHECK_LOGIN_TOKEN_URL = PROPERTIES.getProperty("check_login_token_url");

        //encrypted endpoints
        ENC_AUTH_URL = PROPERTIES.getProperty("enc_auth_url");
        ENC_ASYNC_AUTH_URL = PROPERTIES.getProperty("enc_async_auth_url");
        ENC_CHECK_LOGIN_TOKEN_URL = PROPERTIES.getProperty("enc_check_login_token_url");
    }

    @Test
    public void main() throws JsonProcessingException, IOException, Exception {
        AuthRequest authRequest = new AuthRequest(USERNAME, NOPASSWORD_LOGIN_KEY, "10.0.0.1");
        authRequest.setDeviceName("Java Sample");
        authRequest.setBrowserId(UUID.randomUUID().toString());

        //synchronous authentication
        authentication(authRequest);

        //asynchronous authentication
//        asyncAuthentication(authRequest);

        //switch to generic api key for encrypted endpoints
//        authRequest.setApiKey(GENERIC_API_KEY);

        //synchronous encrypted authentication
//        encyptedAuthentication(authRequest);

        //asynchronous encrypted authentication
//        asyncEncryptedAuthentication(authRequest);
    }

    /**
     * Plain authentication.
     *
     * @param authRequest Authentication request.
     * @throws java.io.IOException
     */
    public static void authentication(AuthRequest authRequest) throws IOException {
        LOG.info("Authentication");
        AuthResult authResult = Authentication.authenticateUser(AUTH_URL, authRequest);
        LOG.info("User authenticated: " + AuthStatus.SUCCESS.equals(authResult.getAuthStatus()));
        LOG.info("Status: " + authResult.getAuthStatus());
    }

    /**
     * Plain asynchronous authentication.
     *
     * @param authRequest Authentication request.
     * @throws java.io.IOException
     */
    public void asyncAuthentication(AuthRequest authRequest) throws IOException {
        LOG.info("Asynchronous authentication");
        AuthResult result = Authentication.authenticateUser(ASYNC_AUTH_URL, authRequest);
        String authStatus = result.getAuthStatus();

        while (AuthStatus.WAITING_FOR_RESPONSE.equals(authStatus)) {
            try {
                Thread.sleep(3000);
                LOG.info("waiting for response...");
                authStatus = Authentication.checkLoginToken(CHECK_LOGIN_TOKEN_URL, result.getAsyncLoginToken()).getAuthStatus();
            } catch (InterruptedException ex) {
                LOG.error("Interrupted thread", ex);
            } catch (JsonProcessingException ex) {
                LOG.error("Error checking async login token", ex);
            }
        }
        LOG.info("User authenticated: " + AuthStatus.SUCCESS.equals(authStatus));
        LOG.info("Status: " + authStatus);
    }

    /**
     * Encrypted authentication.
     *
     * @param authRequest Authentication result.
     */
    public void encyptedAuthentication(AuthRequest authRequest) {
        LOG.info("Encrypted authentication");
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(AuthenticationSample.class.getResource("/private-key.pem").getPath());
            PublicKey publicKey = RSAUtils.loadPublicKey(AuthenticationSample.class.getResource("/public-key.pem").getPath());
            RSACipher rsaCipher = new RSACipher(publicKey, privateKey, CHARSET);
            AuthResult result = Authentication.authenticateUserEncrypted(ENC_AUTH_URL, authRequest, rsaCipher);
            LOG.info("User authenticated: " + AuthStatus.SUCCESS.equals(result.getAuthStatus()));
            LOG.info("Status: " + result.getAuthStatus());
        } catch (IOException ex) {
            LOG.error("Error exceting encrypted authentication", ex);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            LOG.error("Error loading RSA keys", ex);
        } catch (NoSuchProviderException ex) {
            LOG.error("BouncyCastle provider not loaded", ex);
        }
    }

    /**
     * Encrypted asynchronous authentication.
     *
     * @param authRequest Authentication result.
     */
    public static void asyncEncryptedAuthentication(AuthRequest authRequest) {
        LOG.info("Asynchronous encrypted authentication");
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(AuthenticationSample.class.getResource("/private-key.pem").getPath());
            PublicKey publicKey = RSAUtils.loadPublicKey(AuthenticationSample.class.getResource("/public-key.pem").getPath());
            RSACipher rsaCipher = new RSACipher(publicKey, privateKey, CHARSET);
            System.out.println(rsaCipher.decrypt(new String(Base64.getDecoder().decode("ImYwMGFiNzUxLTJkYjEtNDJlOC1hZjljLWRlMjBmZmU2NTJhNCI=".getBytes()))));
//            AuthResult result = Authentication.authenticateUserEncrypted(ENC_ASYNC_AUTH_URL, authRequest, rsaCipher);
//            String authStatus = result.getAuthStatus();
//
//            while (AuthStatus.WAITING_FOR_RESPONSE.equals(authStatus)) {
//                Thread.sleep(3000);
//                LOG.info("waiting for response...");
//                authStatus = Authentication.checkEncLoginToken(
//                        ENC_CHECK_LOGIN_TOKEN_URL, authRequest.getApiKey(),
//                        result.getAsyncLoginToken(), rsaCipher).getAuthStatus();
//            }
//            LOG.info("User authenticated: " + AuthStatus.SUCCESS.equals(authStatus));
//            LOG.info("Status: " + authStatus);
//            Assert.assertNotEquals(AuthStatus.LOG_ERROR, authStatus);
//        } catch (InterruptedException ex) {
//            LOG.error("Interrupted thread", ex);
        } catch (IOException ex) {
            LOG.error("Error exceting encrypted authentication", ex);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            LOG.error("Error loading RSA keys", ex);
        } catch (NoSuchProviderException ex) {
            LOG.error("BouncyCastle provider not loaded", ex);
        }
    }

}
