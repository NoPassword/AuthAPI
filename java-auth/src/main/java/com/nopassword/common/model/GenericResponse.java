package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author NoPassword
 */
public class GenericResponse {

    @JsonProperty("Succeeded")
    private boolean succeeded;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Value")
    private JsonNode value;

    public GenericResponse() {
    }

    public GenericResponse(boolean succeeded, String message) {
        this.succeeded = succeeded;
        this.message = message;
    }

    public boolean succeeded() {
        return succeeded;
    }

    public void succeeded(boolean Succeeded) {
        this.succeeded = Succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GenericResponse{" + "succeeded=" + succeeded + ", message=" + message + ", value=" + value + '}';
    }

}
