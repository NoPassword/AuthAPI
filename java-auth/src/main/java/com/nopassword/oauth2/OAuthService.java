package com.nopassword.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple REST service that shows how to authenticate with NoPassword through
 * OAuth2 protocol
 *
 * @author NoPassword
 */
@SpringBootApplication
@RestController
public class OAuthService {

    private static final String AUTHORIZE_URL
            = "https://nopassword.com/external/OAuth/authorize?client_id=%s&redirect_uri=%s&state=%s&scope=%s&response_type=%s";
    private static String CALLBACK_URL;
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static final String SCOPE = "publicapi";
    private static final String RESPONSE_TYPE = "token";
    
    public static void main(String[] args) {
        SpringApplication.run(OAuthService.class, args);
    }

    /**
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        Properties properites = new Properties();
        properites.load(getClass().getResourceAsStream("/oauth2.properties"));
        CLIENT_ID = properites.getProperty("client_id");
        CLIENT_SECRET = properites.getProperty("client_secret");
        CALLBACK_URL = properites.getProperty("callback_url");
    }

    /**
     * Forwards authentication request to NoPassword
     * https://localhost:8443/authorize?state=anystate
     * @param state
     * @return
     */
    @RequestMapping(path = "/authorize", method = GET)
    public ResponseEntity authorize(@RequestParam(defaultValue = "none") String state) {
        System.out.println("URL=" + String.format(AUTHORIZE_URL, CLIENT_ID, CALLBACK_URL, state, SCOPE, RESPONSE_TYPE));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(
                String.format(AUTHORIZE_URL, CLIENT_ID, CALLBACK_URL,
                        state, SCOPE, RESPONSE_TYPE)));
        ResponseEntity response = new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        return response;
    }

    @RequestMapping
    public String callback() {
        System.out.println("callback url");
        return "hello";
    }

}
