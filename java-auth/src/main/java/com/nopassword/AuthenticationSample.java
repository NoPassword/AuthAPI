package com.nopassword;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.util.model.AuthResult;
import com.nopassword.util.model.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import org.springframework.web.client.RestTemplate;

/**
 * A simple class to authenticate with NoPassword
 *
 * @author NoPassword
 */
public class AuthenticationSample {

    private static final String SUCCESS = "Success";
    public static final String AUTH_SERVICE_URL = "https://api.nopassword.com/id";

    public static void main(String[] args) throws SocketException, IOException {
        //put your username here
        Message message = new Message("john.smith@example.com", "8.8.8.8");
        System.out.println("User authenticated with Java: " + AuthenticationSample.authenticateUser(message));
        System.out.println("User authenticated with Spring: " + AuthenticationSample.authenticateUserWithSpring(message));
    }

    /**
     * Authenticates user against NoPassword using pure Java
     *
     * @param msg Authentication request
     * @return true if user is authenticated successfully
     * @throws java.io.IOException
     */
    public static boolean authenticateUser(Message msg) throws IOException {
        AuthResult result = doPost(AUTH_SERVICE_URL, msg, AuthResult.class);
        return SUCCESS.equals(result.getAuthStatus());
    }

    /**
     * Authenticates an user against NoPassword using spring framework
     *
     * @param msg message containing user and user's IP address
     * @return true if user is authenticated successfully
     */
    public static boolean authenticateUserWithSpring(Message msg) {
        RestTemplate rt = new RestTemplate();
        AuthResult authResult = rt.postForObject(AUTH_SERVICE_URL, msg, AuthResult.class);
        return SUCCESS.equalsIgnoreCase(authResult.getAuthStatus());
    }

    /**
     * Pure JSE REST client
     *
     * @param <T>
     * @param url URL
     * @param o Data to be posted
     * @param resultType Class
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static <T> T doPost(String url, Object o, Class<T> resultType) throws MalformedURLException, IOException {
        URL urlx = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlx.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(o);
        OutputStream out = conn.getOutputStream();
        out.write(payload.getBytes());
        out.flush();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed: HTTP error code " + conn.getResponseCode());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder input = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            input.append(line);
        }
        conn.disconnect();
        return mapper.readValue(input.toString(), resultType);
    }

}
