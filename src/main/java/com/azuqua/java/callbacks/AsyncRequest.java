package com.azuqua.java.callbacks;

/**
 * Created by SASi on 14-Jul-16.
 */
public interface AsyncRequest {
    void onResponse(String response);

    void onError(String error);
}
