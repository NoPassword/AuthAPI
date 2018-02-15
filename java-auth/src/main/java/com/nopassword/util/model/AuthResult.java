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
    
    @JsonProperty("LogGUID")
    private String logGUID;
    
    @JsonProperty("SSOAddress")
    private String ssoAddress;

    public String getSsoAddress() {
        return ssoAddress;
    }

    public void setSsoAddress(String ssoAddress) {
        this.ssoAddress = ssoAddress;
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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("AuthResult{authStatus=").append(authStatus)
                .append(", username=").append(username)
                .append(", webAddress=").append(webAddress)
                .append(", enc=").append(enc)
                .append(", logGUID=")
                .append(logGUID).append("}").toString();
    }

}
