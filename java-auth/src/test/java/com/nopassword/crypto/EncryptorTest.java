package com.nopassword.crypto;

import com.nopassword.common.crypto.NPCipher;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author NoPassword
 */
public class EncryptorTest {
    
    public static final byte[] AES_KEY = "01234567890123456012345678901234".getBytes();
    public static final byte[] AES_IV = "0123456789012345".getBytes();
    private static final NPCipher AES_CIPHER = new NPCipher(AES_KEY, AES_IV);

    @Test
    public void encrypt() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String plain = "any text";
        String encrypted = AES_CIPHER.encrypt(plain);
        Assert.assertEquals(plain, AES_CIPHER.decrypt(encrypted));
        plain = "00000000-0000-0000-0000-000000000000";
        encrypted = AES_CIPHER.encrypt(plain);
        Assert.assertEquals(plain, AES_CIPHER.decrypt(encrypted));
    }

}
