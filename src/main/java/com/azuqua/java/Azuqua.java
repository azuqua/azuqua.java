package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.callbacks.LoginRequest;
import com.azuqua.java.callbacks.OrgFLOsRequest;
import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.FLO;
import com.azuqua.java.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Azuqua {

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private Gson gson = new Gson();
    private RequestHandler requestHandler;
    private String accessKey;
    private String accessSecret;

    public Azuqua(String accessKey, String accessSecret) {
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }


    //    public void login(String email, String password, final LoginRequest loginRequest) {
//
//        JsonObject dataObject = new JsonObject();
//        dataObject.addProperty("email", email);
//        dataObject.addProperty("password", password);
//
//        requestHandler = new RequestHandler(Routes.BASE + Routes.ORG_LOGIN, Routes.METHOD_POST, gson.toJson(dataObject), new AsyncRequest() {
//            @Override
//            public void onResponse(String response) {
//                User user = gson.fromJson(response, User.class);
//                loginRequest.onResponse(user);
//            }
//
//            @Override
//            public void onError(String error) {
//                AzuquaError azuquaError;
//                try {
//                    azuquaError = gson.fromJson(error, AzuquaError.class);
//                } catch (Exception e) {
//                    azuquaError = new AzuquaError(400, error);
//                }
//                loginRequest.onError(azuquaError);
//            }
//        });
//
//        requestHandler.execute();
//    }

    public void getFLOs(final OrgFLOsRequest orgFLOsRequest) {
        String timeStamp = getISOTime();
        String signedData = signDate("", Routes.METHOD_GET, Routes.ORG_FLOS, accessSecret, timeStamp);

        requestHandler = new RequestHandler(Routes.BASE + Routes.ORG_FLOS, Routes.METHOD_GET, "", timeStamp, signedData,
                accessKey, new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<FLO>>() {
                }.getType();
                List<FLO> flos = gson.fromJson(response, collectionType);
                orgFLOsRequest.onResponse(flos);
            }

            @Override
            public void onError(String error) {
                AzuquaError azuquaError;
                try {
                    azuquaError = gson.fromJson(error, AzuquaError.class);
                } catch (Exception e) {
                    azuquaError = new AzuquaError(400, error);
                }

                orgFLOsRequest.onError(azuquaError);
            }
        });
        requestHandler.execute();
    }

    public void readFLO(String alias) {
        String path = Routes.FLO_READ.replace(":alias", alias);
        String timeStamp = getISOTime();

        String signedData = signDate("", Routes.METHOD_GET, path, accessSecret, timeStamp);

        requestHandler = new RequestHandler(Routes.BASE + path, Routes.METHOD_GET, "", timeStamp, signedData, accessKey, new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onError(String error) {
                AzuquaError azuquaError;
                try {
                    azuquaError = gson.fromJson(error, AzuquaError.class);
                } catch (Exception e) {
                    azuquaError = new AzuquaError(400, error);
                }
                System.out.println(azuquaError.getMessage());
            }
        });

        requestHandler.execute();
    }

    public void invokeFLO(String alias, String data, AsyncRequest asyncRequest) {
        String path = Routes.FLO_INVOKE.replace(":alias", alias);
        runFLO(path, data, asyncRequest);
    }

    public void injectFLO(String alias, String data, AsyncRequest asyncRequest) {
        String path = Routes.FLO_INJECT.replace(":alias", alias);
        runFLO(path, data, asyncRequest);
    }

    private void runFLO(String path, String data, final AsyncRequest asyncRequest) {
        String timeStamp = getISOTime();

        String signedData = signDate(data, Routes.METHOD_POST, path, accessSecret, timeStamp);

        requestHandler = new RequestHandler(Routes.BASE + path, Routes.METHOD_POST, data, timeStamp, signedData, accessKey, new AsyncRequest() {
            @Override
            public void onResponse(String response) {
                asyncRequest.onResponse(response);
            }

            @Override
            public void onError(String error) {
                AzuquaError azuquaError;
                try {
                    azuquaError = gson.fromJson(error, AzuquaError.class);
                } catch (Exception e) {
                    azuquaError = new AzuquaError(400, error);
                }
                System.out.println(azuquaError.getMessage());
            }
        });

        requestHandler.execute();
    }

    /**
     * Time Stamp
     */
    private String getISOTime() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(new Date());
    }

    /**
     * Generate Hex from byte data
     */
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Generate HASH
     */
    private String signDate(String data, String method, String path, String accessSecret, String timeStamp) {
        Mac hMac = null;
        SecretKeySpec key = null;
        String meta = method.toLowerCase() + ":" + path + ":" + timeStamp;
        String dataToDigest = meta + data;
        String hash = null;
        try {
            hMac = Mac.getInstance("HmacSHA256");
            key = new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256");
            hMac.init(key);
            byte[] digest = hMac.doFinal(dataToDigest.getBytes("UTF-8"));
            hash = bytesToHex(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
