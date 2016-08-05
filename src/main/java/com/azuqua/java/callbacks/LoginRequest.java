package com.azuqua.java.callbacks;

import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.User;

/**
 * Created by SASi on 14-Jul-16.
 */
public interface LoginRequest {
    void onResponse(User user);

    void onError(AzuquaError error);
}
