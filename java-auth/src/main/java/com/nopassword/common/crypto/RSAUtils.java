package com.nopassword.common.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 *
 * @author NoPassword
 */
public class RSAUtils {

    static {
        try {
            if (java.security.Security.getProvider("BC") == null) {
                java.security.Security.addProvider(new BouncyCastleProvider());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static PublicKey loadPublicKey(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return loadPublicKey(path.toString());
    }

    public static PublicKey loadPublicKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return loadPublicKey(new FileInputStream(new File(path)));
    }

    public static PublicKey loadPublicKey(InputStream input) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader pemReader = new PemReader(new InputStreamReader(input));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pemReader.readPemObject().getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey loadPrivateKey(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return loadPrivateKey(path.toString());
    }

    public static PrivateKey loadPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return loadPrivateKey(new FileInputStream(new File(path)));
    }

    public static PrivateKey loadPrivateKey(InputStream input) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        PemReader pemReader = new PemReader(new InputStreamReader(input));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pemReader.readPemObject().getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
        return kf.generatePrivate(spec);
    }

    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(keySize);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }

    public static void writePemFile(Key key, Path path) throws IOException {
        String desc = key instanceof PublicKey
                ? "PUBLIC KEY"
                : "RSA PRIVATE KEY";

        try (PemWriter writer = new PemWriter(new FileWriter(path.toFile()))) {
            PemObject pemObject = new PemObject(desc, key.getEncoded());
            writer.writeObject(pemObject);
        }
    }

}
