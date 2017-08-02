package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.models.AzuquaError;

import java.util.List;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Test {
    static final String ACCESS_KEY = "";
    static final String ACCESS_SECRET = "";

    private static Azuqua azuqua;

    public static void main(String[] args) {

        azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET, "http", "localhost.azuqua.com", 6072);

        testRequest();
    }

    static String data = "{\"description\":\"¥\"}";
    private static void testRequest() {
        azuqua.updateFolder(610, data, new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println("Error : " + error.getErrorMessage());
            }
        });
    }
}
