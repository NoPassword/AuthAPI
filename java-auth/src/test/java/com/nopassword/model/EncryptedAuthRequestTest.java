package com.nopassword.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.NoPasswordTest;
import com.nopassword.common.crypto.RSACipher;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.GenericRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author NoPassword
 */
public class EncryptedAuthRequestTest extends NoPasswordTest {

    public final byte[] AES_KEY = "01234567890123456012345678901234".getBytes();
    public final byte[] AES_IV = "0123456789012345".getBytes();

    @Test
    public void testEncryptedAuthRequest() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        RSACipher cipher = new RSACipher(kp.getPublic(), kp.getPrivate(), Charset.defaultCharset());
        AuthRequest request = new AuthRequest("my-api-key", "john.smith@example.com", "127.0.0.1");
        GenericRequest encryptedRequest = new GenericRequest(
                request.getApiKey(), request, cipher);
        // check that payload is encrypted and can be decrypted
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = new String(Base64.getDecoder().decode(encryptedRequest.getPayload()));
        AuthRequest plainRequest = mapper.readValue(jsonRequest, AuthRequest.class);
        Assert.assertEquals(request, plainRequest);
        
        // check timestamp
        DateTimeFormatter dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        dtf.withZone(ZoneOffset.UTC);
        ZonedDateTime.parse(encryptedRequest.getTimestamp().replace(" ", "T"));
    }

}
