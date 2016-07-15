package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.callbacks.LoginRequest;
import com.azuqua.java.callbacks.OrgFLOsRequest;
import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.FLO;
import com.azuqua.java.models.Org;
import com.azuqua.java.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Test {

    private static final String ACCESS_KEY = "";
    private static final String ACCESS_SECRET = "";

    private static Azuqua azuqua;
    private static List<FLO> floList;

    public static void main(String[] args) {

        azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET);
        getOrgFLOs();
    }

    private static void getOrgFLOs() {
        azuqua.getFLOs(new OrgFLOsRequest() {
            @Override
            public void onResponse(List<FLO> flos) {
                floList = flos;
                System.out.println(flos.size());
//                floList.stream().filter(flo -> flo.getName().equals("Java HTTP")).forEach(Test::invokeFLO);
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println("Error : " + error.getMessage());
            }
        });
    }

    private static void invokeFLO(FLO flo) {
        Gson gson = new Gson();
        Map<String, String> payload = new HashMap<String, String>();
        azuqua.injectFLO(flo.getAlias(), gson.toJson(payload), new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });
    }
}