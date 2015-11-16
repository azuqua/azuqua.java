package com.azuqua.java.client;

import com.google.gson.JsonObject;

/**
 * <p>
 *     Represents a response coming back from Flo#invoke|resume.
 * </p>
 *
 * <p>
 *     For Flo#invoke, the response can have two forms:
 *     <br><br>
 *     Success:
 *     <br>
 *     {
 *         "data": [object - data coming back from the API]
 *     }
 *     <br><br>
 *     Error:
 *     <br>
 *     {
 *         "statusCode": "some status code not equal to 200",
 *         "message" "error message coming back from either the Azuqua engine or the API you are calling"
 *     }
 *     <br>
 *
 * </p>
 *
 * <p>
 *     The Flo#resume response is similar to Flo#invoke except in the case where the user doesn't define
 *     the response format in the resume card for the Flo. In that case, the Azuqua engine, will send back
 *     the default response message for a resume:
 *     <br><br>
 *     '{"response":"success"}'
 * </p>
 *
 * <p>
 *     Created by quyle on 11/9/15.
 * </p>
 */
public class FloResponse {
    /**
     * Represents the x-flo-instance header returned from Azuqua. Needed for FLO-resume capable FLOS.
     */
    private String xFloInstance;

    /**
     * The body of the response from the Azuqua platform could have three properties.
     *
     * If the call was a success, the response from the API is wrapped around a data object.
     *
     * If there was an error returned from the API, we return a statusCode property + the
     * error message from the API you are calling.
     */
    private JsonObject data;
    private Integer statusCode;
    private String message;
    private String response;

    public String getXFloInstance() {
        return xFloInstance;
    }

    public void setXFloInstance(String xFloInstance) {
        this.xFloInstance = xFloInstance;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
