package com.azuqua.java.client;

/**
 * <p>
 *     Object representing a HTTP response from Azuqua. Basically wraps the request body and x-flo-instance
 *     header.
 * </p>
 *
 * <p>
 *     Created by quyle on 11/16/15.
 * </p>
 */
public class AzuquaResponse {
    /**
     * Represents the x-flo-instance header returned from Azuqua. Needed for FLO-resume capable FLOS.
     */
    private String xFloInstance;

    /**
     * Represents the entire of the body of the response coming
     */
    private String body;

    /**
     * Http status code.
     */
    private String status;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getXFloInstance() {
        return xFloInstance;
    }

    public void setXFloInstance(String xFloInstance) {
        this.xFloInstance = xFloInstance;
    }
}
