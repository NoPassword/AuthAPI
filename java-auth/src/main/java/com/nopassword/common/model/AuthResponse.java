package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NoPassword
 */
public class AuthResponse {

    @JsonProperty("Succeeded")
    private boolean succeeded;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Value")
    private AuthResult authResult;

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean Succeeded) {
        this.succeeded = Succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthResult getAuthResult() {
        return authResult;
    }

    public void setAuthResult(AuthResult authResult) {
        this.authResult = authResult;
    }

}
