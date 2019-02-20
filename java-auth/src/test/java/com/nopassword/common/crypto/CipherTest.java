package com.nopassword.common.crypto;

import com.nopassword.NoPasswordTest;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author NoPassword
 */
public class CipherTest extends NoPasswordTest {

    public static final byte[] AES_KEY = "01234567890123456012345678901234".getBytes();
    public static final byte[] AES_IV = "0123456789012345".getBytes();

    @Test
    public void aesCipher() {
        String plain = "any text";
        AESCipher cihper = new AESCipher(AES_KEY, AES_IV, Charset.defaultCharset());
        String encrypted = cihper.encrypt(plain);
        Assert.assertEquals(plain, cihper.decrypt(encrypted));
        plain = "00000000-0000-0000-0000-000000000000";
        encrypted = cihper.encrypt(plain);
        Assert.assertEquals(plain, cihper.decrypt(encrypted));
    }

    @Test
    public void rsaCipher() throws NoSuchAlgorithmException {
        String plain = "any text";
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        RSACipher cipher = new RSACipher(kp.getPublic(), kp.getPrivate(), Charset.defaultCharset());
        String encrypted = cipher.encrypt(plain);
        Assert.assertEquals(plain, cipher.decrypt(encrypted));
        plain = "00000000-0000-0000-0000-000000000000";
        encrypted = cipher.encrypt(plain);
        Assert.assertEquals(plain, cipher.decrypt(encrypted));
    }

}
