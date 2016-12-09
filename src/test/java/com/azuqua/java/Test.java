package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.callbacks.OrgFLOsRequest;
import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.FLO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Test {

    static final String ACCESS_KEY = "";
    static final String ACCESS_SECRET = "";

    private static Azuqua azuqua;
    private static List<FLO> floList;

    public static void main(String[] args) {

        azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET);

//        azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET, "https", "api.azuqua.com", 443);
        getOrgFLOs();

    }

    private static void getOrgFLOs() {
        azuqua.getFLOs(44, new OrgFLOsRequest() {
            @Override
            public void onResponse(List<FLO> flos) {
                floList = flos;
                System.out.println("Number of FLOs: " + flos.size());
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println("Error : " + error.getErrorMessage());
            }
        });
    }

    private static void invokeFLO(FLO flo) {
        Gson gson = new Gson();
        Map<String, String> payload = new HashMap<String, String>();
        

        azuqua.invokeFLO(flo.getAlias(), gson.toJson(payload), new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println(error.getErrorMessage());
            }
        });
    }
}
