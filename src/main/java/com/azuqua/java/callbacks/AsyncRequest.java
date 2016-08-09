package com.azuqua.java.callbacks;

import com.azuqua.java.models.AzuquaError;

/**
 * Created by SASi on 14-Jul-16.
 */
public interface AsyncRequest {
    void onResponse(String response);

    void onError(AzuquaError error);
}
