package com.nopassword.common.crypto;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author NoPassword
 */
public class RSAUtilsTest {

    public RSAUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loadPublicKey method, of class RSAKeyLoader.
     */
    @Test
    public void testLoadPublicKey_Path() throws Exception {
        System.out.println("loadPublicKey");
        Path filename = Paths.get(getClass().getResource("/public-key.pem").toURI());
        PublicKey publicKey = RSAUtils.loadPublicKey(filename);
        assertTrue(publicKey.getEncoded().length > 0);

        try {
            filename = Paths.get(getClass().getResource("/private-key.pem").toURI());
            RSAUtils.loadPublicKey(filename);
            fail("Private key loaded as public key");
        } catch (Exception e) {
        }
    }

    /**
     * Test of loadPrivateKey method, of class RSAKeyLoader.
     */
    @Test
    public void testLoadPrivateKey_Path() throws Exception {
        System.out.println("loadPrivateKey");
        Path filename = Paths.get(getClass().getResource("/private-key.pem").toURI());
        PrivateKey result = RSAUtils.loadPrivateKey(filename);
        assertTrue(result.getEncoded().length > 0);

        try {
            filename = Paths.get(getClass().getResource("/public-key.pem").toURI());
            RSAUtils.loadPrivateKey(filename);
            fail("Public key loaded as private key");
        } catch (Exception e) {
        }
    }

    /**
     * Test of generateKeyPair method, of class RSAKeyLoader.
     */
    @Test
    public void testGenerateKeyPair() throws Exception {
        System.out.println("generateKeyPair");
        KeyPair kp = RSAUtils.generateKeyPair(2048);
        assertNotNull(kp.getPublic());
        assertNotNull(kp.getPrivate());
    }

    /**
     * Test of writePemFile method, of class RSAKeyLoader.
     */
    @Test
    public void testWritePemFile() throws Exception {
        System.out.println("writePemFile");
        KeyPair kp = RSAUtils.generateKeyPair(2048);
        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path keyPath = Paths.get(tmpDir.toString(), "public.pem");
        RSAUtils.writePemFile(kp.getPublic(), keyPath);
        PublicKey pk = RSAUtils.loadPublicKey(keyPath);
        assertTrue(pk.getEncoded().length > 0);
    }

}
