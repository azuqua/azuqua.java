package com.azuqua.java.callbacks;

import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.FLO;

import java.util.List;

/**
 * Created by SASi on 14-Jul-16.
 */
public interface OrgFLOsRequest {
    void onResponse(List<FLO> flos);

    void onError(AzuquaError error);
}
