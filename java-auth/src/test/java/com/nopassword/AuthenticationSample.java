package com.nopassword;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nopassword.common.crypto.NPCipher;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.AuthResult;
import com.nopassword.common.model.AuthStatus;
import com.nopassword.common.utils.Authentication;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
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
public class AuthenticationSample {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSample.class);
    private static final Properties PROPERTIES = new Properties();
    private static String AUTH_URL;
    private static String ASYNC_AUTH_URL;
    private static String ASYNC_ENC_AUTH_URL;
    private static String CHECK_LOGIN_TOKEN_URL;
    private static String CHECK_ENC_LOGIN_TOKEN_URL;

    private static final String NOPASSWORD_LOGIN_KEY = "Your NoPassword Login key here";
    private static final String USERNAME = "Your user name here";
    private static final String AES_KEY = "Your AES key here";
    private static final String AES_IV = "Your AES IV here";

    @BeforeClass
    public static void NoPasswordTest() {
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.DEBUG);
        PatternLayout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        ConsoleAppender console = new ConsoleAppender(layout);
        console.setName("consoleAppender");
        rootLogger.addAppender(console);

        try {
            PROPERTIES.load(AuthenticationSample.class.getResourceAsStream("/config.properties"));
        } catch (IOException ex) {
            LOG.error("Error loading configuration file", ex);
            Assert.assertNotNull(null);
        }

        AUTH_URL = PROPERTIES.getProperty("auth_url");
        ASYNC_AUTH_URL = PROPERTIES.getProperty("async_auth_url");
        ASYNC_ENC_AUTH_URL = PROPERTIES.getProperty("async_enc_auth_url");
        CHECK_LOGIN_TOKEN_URL = PROPERTIES.getProperty("check_login_token_url");
        CHECK_ENC_LOGIN_TOKEN_URL = PROPERTIES.getProperty("check_login_enc_login_token");
    }

    @Test
    public void main() throws JsonProcessingException, IOException {
        AuthRequest authRequest = new AuthRequest(NOPASSWORD_LOGIN_KEY, USERNAME, "10.0.0.1");
        authRequest.setDeviceName("Java Sample");
        authRequest.setBrowserId(UUID.randomUUID().toString());

        //synchronous authentication
        syncAuthentication(authRequest);

        //asynchronous authentication
        asyncAuthentication(authRequest);

        //asynchronous authentication with encrypted request
        asyncEncryptedAuthentication(authRequest, AES_KEY, AES_IV);
    }

    public static void syncAuthentication(AuthRequest authRequest) {
        LOG.info("Synchronous authentication");
        AuthResult authResult = Authentication.authenticateUser(AUTH_URL, authRequest);
        LOG.info("User authenticated: " + AuthStatus.Success.equals(authResult.getAuthStatus()));
    }

    public void asyncAuthentication(AuthRequest authRequest) {
        LOG.info("Asynchronous authentication");
        AuthResult result = Authentication.authenticateUserAsync(ASYNC_AUTH_URL, authRequest);
        AuthStatus authStatus = result.getAuthStatus();

        while (AuthStatus.WaitingForResponse.equals(authStatus)) {
            try {
                Thread.sleep(3000);
                System.out.println("waiting for response...");
                authStatus = Authentication.checkLoginToken(CHECK_LOGIN_TOKEN_URL, result.getAsyncLoginToken());
            } catch (InterruptedException ex) {
                LOG.error("Interrupted thread", ex);
            } catch (JsonProcessingException ex) {
                LOG.error("Error checking async login token", ex);
            }
        }
        System.out.println("User authenticated: " + AuthStatus.Success.equals(authStatus));
    }

    public static void asyncEncryptedAuthentication(AuthRequest authRequest, String aesKey, String aesIV) throws JsonProcessingException, IOException {
        LOG.info("Asynchronous encrypted authentication");
        NPCipher aesCipher = new NPCipher(
                Base64.getDecoder().decode(aesKey.getBytes()),
                Base64.getDecoder().decode(aesIV.getBytes()));
        AuthResult asyncAuthResult = Authentication.authenticateUserEncAsync(ASYNC_ENC_AUTH_URL, authRequest, aesCipher);
        String loginToken = asyncAuthResult.getAsyncLoginToken();
        AuthStatus authStatus = Authentication.checkLoginToken(CHECK_ENC_LOGIN_TOKEN_URL, loginToken, aesCipher);

        while (AuthStatus.WaitingForResponse.equals(authStatus)) {
            System.out.println("waiting for response...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                LOG.error("Interrupted thread", ex);
            }
            authStatus = Authentication.checkLoginToken(CHECK_ENC_LOGIN_TOKEN_URL, loginToken, aesCipher);
        }
        LOG.info("User authenticated: " + AuthStatus.Success.equals(authStatus));
    }

}
