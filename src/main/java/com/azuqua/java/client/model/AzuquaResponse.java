package com.azuqua.java.client.model;

/**
 * Created by quyle on 11/9/15.
 */
public class AzuquaResponse {
    /**
     * Represents the x-flo-instance header returned from Azuqua. Needed for FLO-resume.
     */
    private String xFloInstance;

    /**
     * The body of the response as a String.
     */
    private String response;

    public String getXFloInstance() {
        return xFloInstance;
    }

    public void setXFloInstance(String xFloInstance) {
        this.xFloInstance = xFloInstance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
