package com.nopassword.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.NPCipher;
import com.nopassword.common.model.AuthRequest;
import com.nopassword.common.model.EncryptedAuthRequest;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author NoPassword
 */
public class EncryptedAuthRequestTest {

    public final byte[] AES_KEY = "01234567890123456012345678901234".getBytes();
    public final byte[] AES_IV = "0123456789012345".getBytes();

    @Test
    public void testEncryptedAuthRequest() throws IOException {
        NPCipher cipher = new NPCipher(AES_KEY, AES_IV);
        AuthRequest request = new AuthRequest("my-api-key", "john.smith@example.com", "127.0.0.1");
        EncryptedAuthRequest encryptedRequest = new EncryptedAuthRequest(
                request, cipher);
        
        // check that payload is encrypted and can be decrypted
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = cipher.decrypt(encryptedRequest.getPayload());
        AuthRequest plainRequest = mapper.readValue(jsonRequest, AuthRequest.class);
        Assert.assertEquals(request, plainRequest);
        
        // check signature
        Assert.assertTrue(BCrypt.checkpw(jsonRequest, encryptedRequest.getSignature()));
        
        // check timestamp
        DateTimeFormatter dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        dtf.withZone(ZoneOffset.UTC);
        ZonedDateTime.parse(encryptedRequest.getTimestamp().replace(" ", "T"));
    }

}
