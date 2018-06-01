package com.nopassword.common.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.CertificateException;

public class SSLUtil {

    private static final String KEY_STORE_TYPE = "JKS";
    private static final String TRUST_STORE_TYPE = "JKS";
    private static final String KEY_MANAGER_TYPE = "SunX509";
    private static final String TRUST_MANAGER_TYPE = "SunX509";
    private static final String PROTOCOL = "TLS";

    private static SSLContext serverSSLCtx = null;
    private static SSLContext clientSSLCtx = null;

    public static SSLContext createServerSSLContext(final String keyStoreLocation,
            final char[] keyStorePwd)
            throws KeyStoreException,
            NoSuchAlgorithmException,
            CertificateException,
            FileNotFoundException,
            IOException,
            UnrecoverableKeyException,
            KeyManagementException,
            java.security.cert.CertificateException {

        if (serverSSLCtx == null) {
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(new FileInputStream(keyStoreLocation), keyStorePwd);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_TYPE);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());            
            keyManagerFactory.init(keyStore, keyStorePwd);
//            serverSSLCtx = SSLContext.getInstance(PROTOCOL);
            serverSSLCtx = SSLContext.getDefault();
//            serverSSLCtx.init(keyManagerFactory.getKeyManagers(), null, null);
        }
        return serverSSLCtx;
    }

    public static SSLContext createClientSSLContext(final String trustStoreLocation,
            final char[] trustStorePwd)
            throws KeyStoreException,
            NoSuchAlgorithmException,
            CertificateException,
            FileNotFoundException,
            IOException,
            KeyManagementException,
            java.security.cert.CertificateException {

        if (clientSSLCtx == null) {
            KeyStore trustStore = KeyStore.getInstance(TRUST_STORE_TYPE);
            trustStore.load(new FileInputStream(trustStoreLocation), trustStorePwd);
            TrustManagerFactory trustManagerFactory
                    = TrustManagerFactory.getInstance(TRUST_MANAGER_TYPE);
            trustManagerFactory.init(trustStore);
            clientSSLCtx = SSLContext.getInstance(PROTOCOL);
            clientSSLCtx.init(null, trustManagerFactory.getTrustManagers(), null);
        }
        return clientSSLCtx;
    }

}
