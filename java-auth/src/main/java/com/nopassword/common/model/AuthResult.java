package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response when authenticating an user with NoPassword API
 *
 * @author NoPassword
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResult {

    @JsonProperty("AuthStatus")
    private AuthStatus authStatus;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("WebAddress")
    private String webAddress;

    @JsonProperty("Enc")
    private String enc;

    @JsonProperty("EncCert")
    private String encCert;

    @JsonProperty("LogGUID")
    private String logGUID;

    @JsonProperty("SSOAddress")
    private String ssoAddress;

    @JsonProperty("SSOUrl")
    private String ssoUrl;

    @JsonProperty("SSOToken")
    private String ssoToken;

    @JsonProperty("AsyncLoginToken")
    private String asyncLoginToken;

    @JsonProperty("PublicCert")
    private String publicCert;

    @JsonProperty("SignedChallenge")
    private String signedChallenge;

    @JsonProperty("AutoCreateAccountToken")
    private String autoCreateAccountToken;

    @JsonProperty("Payload")
    private String payload;

    /**
     * Gets SSO address
     *
     * @return SSO address
     */
    public String getSsoAddress() {
        return ssoAddress;
    }

    /**
     * Sets SSO address
     *
     * @param ssoAddress SSO address
     */
    public void setSsoAddress(String ssoAddress) {
        this.ssoAddress = ssoAddress;
    }

    /**
     * Gets Enc
     *
     * @return Enc
     */
    public String getEnc() {
        return enc;
    }

    /**
     * Sets Enc
     *
     * @param enc Enc
     */
    public void setEnc(String enc) {
        this.enc = enc;
    }

    /**
     * Gets log guid
     *
     * @return Log guid
     */
    public String getLogGUID() {
        return logGUID;
    }

    /**
     * Sets log guid
     *
     * @param logGUID Log guid
     */
    public void setLogGUID(String logGUID) {
        this.logGUID = logGUID;
    }

    /**
     * Gets status
     *
     * @return Status
     */
    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    /**
     * Sets status
     *
     * @param authStatus Status
     */
    public void setAuthStatus(AuthStatus authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * Gets username
     *
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username
     *
     * @param username Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets web address
     *
     * @return Web address
     */
    public String getWebAddress() {
        return webAddress;
    }

    /**
     * Sets web address
     *
     * @param webAddress Web address
     */
    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getEncCert() {
        return encCert;
    }

    public void setEncCert(String encCert) {
        this.encCert = encCert;
    }

    public String getSsoUrl() {
        return ssoUrl;
    }

    public void setSsoUrl(String ssoUrl) {
        this.ssoUrl = ssoUrl;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getAsyncLoginToken() {
        return asyncLoginToken;
    }

    public void setAsyncLoginToken(String asyncLoginToken) {
        this.asyncLoginToken = asyncLoginToken;
    }

    public String getPublicCert() {
        return publicCert;
    }

    public void setPublicCert(String publicCert) {
        this.publicCert = publicCert;
    }

    public String getSignedChallenge() {
        return signedChallenge;
    }

    public void setSignedChallenge(String signedChallenge) {
        this.signedChallenge = signedChallenge;
    }

    public String getAutoCreateAccountToken() {
        return autoCreateAccountToken;
    }

    public void setAutoCreateAccountToken(String autoCreateAccountToken) {
        this.autoCreateAccountToken = autoCreateAccountToken;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "AuthResult{" + "authStatus=" + authStatus
                + ", username=" + username
                + ", webAddress=" + webAddress
                + ", enc=" + enc
                + ", encCert=" + encCert
                + ", logGUID=" + logGUID
                + ", ssoAddress=" + ssoAddress
                + ", ssoUrl=" + ssoUrl
                + ", ssoToken=" + ssoToken
                + ", asyncLoginToken=" + asyncLoginToken
                + ", publicCert=" + publicCert
                + ", signedChallenge=" + signedChallenge
                + ", autoCreateAccountToken=" + autoCreateAccountToken
                + ", payload=" + payload + '}';
    }

}
