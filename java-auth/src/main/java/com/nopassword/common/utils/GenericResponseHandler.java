package com.nopassword.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.crypto.AESCipher;
import com.nopassword.common.crypto.RSACipher;
import com.nopassword.common.model.GenericResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class GenericResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenericResponseHandler.class);
    private static final String SUCCEEDED = "Succeeded";
    private static final String VALUE = "Value";
    private static final String PAYLOAD = "Payload";
    private static final String MESSAGE = "Message";
    private static final String ENC_KEY = "EncKey";

    public static <T> T parseGenericResponse(GenericResponse response, Class<T> clazz) throws IOException {
        if (!response.succeeded()) {
            LOG.error("An error ocurred parsing generic response: " + response.getMessage());
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getValue().toString(), clazz);
    }

    public static <T> T parseGenericResponse(GenericResponse response, RSACipher rsaCipher, Class<T> clazz) throws IOException {
        if (!response.succeeded()) {
            LOG.error("An error ocurred parsing generic response: " + response.getMessage());
            return null;
        }

        String encKey = response.getValue().get(ENC_KEY).asText();
        ObjectMapper mapper = new ObjectMapper();

        //decrypt AES keys with RSA
        Map<String, String> keys = mapper.readValue(rsaCipher.decrypt(encKey), Map.class);
        byte[] aesKey = Base64.getDecoder().decode(keys.get("K"));
        byte[] aesIV = Base64.getDecoder().decode(keys.get("V"));
        AESCipher aesCipher = new AESCipher(aesKey, aesIV, rsaCipher.getCharset());

        //decrypt payload with AES
        String payload = aesCipher.decrypt(response.getValue().get(PAYLOAD).asText());
        JsonNode node = mapper.readTree(payload);

        if (node.get(SUCCEEDED).asBoolean()) {
            if (node.has(VALUE)) {
                return mapper.readValue(node.get(VALUE).toString(), clazz);
            }
        } else {
            LOG.error("Error checking result: " + node.get(MESSAGE).asText());
        }
        return mapper.readValue(payload, clazz);
    }

}
