package com.nopassword.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResult {

    @JsonProperty("AuthStatus")
    private String authStatus;

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

    @JsonProperty("SSOUrl")
    private String ssoURL;

    @JsonProperty("SSOToken")
    private String ssoToken;

    public String getSsoAddress() {
        return ssoURL;
    }

    public void setSsoAddress(String ssoAddress) {
        this.ssoURL = ssoAddress;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public String getLogGUID() {
        return logGUID;
    }

    public void setLogGUID(String logGUID) {
        this.logGUID = logGUID;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getEncCert() {
        return encCert;
    }

    public void setEncCert(String encCert) {
        this.encCert = encCert;
    }

    public String getSsoURL() {
        return ssoURL;
    }

    public void setSsoURL(String ssoURL) {
        this.ssoURL = ssoURL;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    @Override
    public String toString() {
        return "AuthResult{" + "authStatus=" + authStatus 
                + ", username=" + username 
                + ", webAddress=" + webAddress 
                + ", enc=" + enc 
                + ", encCert=" + encCert 
                + ", logGUID=" + logGUID 
                + ", ssoURL=" + ssoURL 
                + ", ssoToken=" + ssoToken + '}';
    }

}
