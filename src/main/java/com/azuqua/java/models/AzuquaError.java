package com.azuqua.java.models;

/**
 * Created by SASi on 14-Jul-16.
 */
public class AzuquaError {
    private int statusCode;
    private String message;

    public AzuquaError() {
    }

    public AzuquaError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
