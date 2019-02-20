package com.nopassword.common.crypto;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
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

    private static final String RSA_PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String RSA_PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";
    private static final String RSA_PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String RSA_PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";

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

    @Test
    public void testLoadPublicKeyFromString() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/public-key.pem").toURI()));
        StringBuilder sb = new StringBuilder();
        lines.forEach(s -> {
            sb.append(s)
                    .append("\n");
        });
        RSAUtils.loadPublicKey(new DataInputStream(new ByteArrayInputStream(sb.toString().getBytes())));
    }
    
    public static void main(String[] args) {
        String key = "-----BEGIN RSA PRIVATE KEY----- MIIEpAIBAAKCAQEAyXcUSwnz3mFDGcXpnkCvzpSkwvLqtaJMPmYbrWueSK0D1zkU sQlLiKtfUyR2N3HbDWCM0HQgeEDBYwus8DAq+ETXUOor95bbnXQf3ZYMDONCLMto alkZA51OO8va7FOMrFKPWulU1SAih7G/mP7AHcIN6vzhGZiFkPNxl7DPgjszW7Kq f5ACSH8vxPgdKXhv3CNZYBXpJ1oFwnqVmqc8K71ox45YH75khJ8NrJDYxVHd73iA sDYr4v3SyqBdySggrHTAlCo9afkZlCFQOexGOgs5F7kQF61nEjB6mqMdY5aOfegD +wtYOFezY8INm9EV5JyBLWY/cdHdmMGD7ZpoQwIDAQABAoIBAAHyijotdvOD2iuy G4+BjZGAI1WVFoq57mW+A+95tlZ6gap/bUFG1zmTeoDpvdDMY3MuJz5OmQ/AhH4K G1pZBNlQhQpNsS4cAbR0bmhsbmJjgfy/3JsGkHTV/O8cCdo19oql46fb0Iuc22iX YcQ/VikTeqH2XtCqBkR/7e2q8l6OYoYk+coSFrJkHo8MR1Y5g0AZcr0EQ2f3Zxul sHQPcMeXIHRqhgvhRwj0hkfM7dPCIzUAn8IpdQtwIjrmg7gEy4jcgwfax1K0iHJC u8jdL3luNEmEH9rk/r8hqAF18T2wtANdbBcGi96Sq3+UacSugRVQX7G+2n7wKLn7 hBp/h6kCgYEA/hO2EWrK0s+zG+rq86FEpdEg6oUksZdQEqUK7MYiLo6MxyeZG2I7 kWY0SzjNjeKhNLY7iMDMIUxsHd+dh9U7j5TDcznk9g0f7nFp1tA2CHD+3e+FkPli EERNq98nwM1qq4prp0CAFlMS5aev1NuQTYD6WDikdYX76ggKZjAGtbsCgYEAyv1t +dhn7mtVfkP+Dg02oMdxoJMC06f1T26+N5SDGOqdHXcm9Qz9Gb43V9biwtQoFnoq RjdB85i4OxEZnpO93wV9N2IGkiDlom4CatNlsBmYSkwzwCvkcAJ1cSKMqBm623Ka SL8i1sJ/+eIXvQ6KBUfEgWgcAi0JNMVaVupg6xkCgYEAhFEblkJ5UgQckMNsjGhF 6dzZOYkRLPNSHgOZhulAT+Ko6eZzU9F6mCQTw8DXO7b0oRDuPC+7nvO/smcMEPs5 Q3MDvnQ+Cu2W3YRuzBIusYK3GDlu46scKMCJeqKCf2P6008I9Vcf3YBx7OvBJCcc JWCv/1WwQAwAv0ZktAb615UCgYEAq63Tya+hlx/oWrYetLuwLCMCkxBDH8e5zciX 0GImznf/EeCobjDiD1e1bvErMCRHE1uXTXOGsAPGLQ61YE0MFQeLc7/QVt7D2LC6 EqdgJaRADPaN/kYhGiq6MUlLNREt3FP88PHXAVPW8PyrnxYg3X5pUBlBNwXn913C nif0eGECgYB25Mt2TG8ecqQd9AMaU5P61hnR+WMHCymCPFAEEf2abb68qLJ57FDW BOu6gW+RKYlvVp8yePe830qP3m1RZK9zCKqEs/uHBHRbXKcSDeSaIUWYIPoDRSug M04VFYepk6c97gQpBVsAimx1WpAURlx9Acj095Wl6TQGmrCGi4F+Bg== -----END RSA PRIVATE KEY-----";
        System.out.println(formatRSAKey(key, RSA_PRIVATE_KEY_HEADER, RSA_PRIVATE_KEY_FOOTER));
    }

    public static String formatRSAKey(String key, String header, String footer) {
        key = key.replace(header, "")
                .replace(footer, "")
                .replaceAll(" ", "\n");

        return String.format("%s%s%s", header, key, footer);
    }

    @Test
    public void testLoadPrivateKeyFromString() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/private-key.pem").toURI()));
        StringBuilder sb = new StringBuilder();
        lines.forEach(s -> {
            sb.append(s)
                    .append("\n");
        });
        RSAUtils.loadPrivateKey(new DataInputStream(new ByteArrayInputStream(sb.toString().getBytes())));
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
