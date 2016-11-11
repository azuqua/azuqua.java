package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.models.AzuquaError;
import com.google.gson.*;

import java.io.*;
import java.net.*;

/**
 * Created by SASi on 14-Jul-16.
 */
public class RequestHandler {

    private String path = null;
    private String method = null;
    private String payload = "";
    private String timeStamp = null;
    private String signedData = null;
    private String accessKey = null;
    private AsyncRequest asyncRequest = null;

    private URLConnection urlConnection = null;

    public RequestHandler(String path, String method, String payload, AsyncRequest asyncRequest) {
        this.path = path;
        this.method = method;
        this.payload = payload;
        this.asyncRequest = asyncRequest;
    }

    RequestHandler(String path, String method, String payload, String timeStamp, String signedData, String accessKey, AsyncRequest asyncRequest) {
        this.path = path;
        this.method = method.toUpperCase();
        this.payload = payload;
        this.timeStamp = timeStamp;
        this.signedData = signedData;
        this.accessKey = accessKey;
        this.asyncRequest = asyncRequest;
    }

    void execute() {
        int statusCode = 0;
        Gson gson = new Gson();
        try {
            URL url = new URL(Routes.PROTOCOL, Routes.HOST, Routes.PORT, path);
            urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            ((HttpURLConnection) urlConnection).setRequestMethod(this.method);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("host", url.getHost());
            urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(this.payload.getBytes().length));

            if (this.signedData != null && this.accessKey != null && this.timeStamp != null) {
                urlConnection.setRequestProperty("x-api-timestamp", this.timeStamp);
                urlConnection.setRequestProperty("x-api-hash", this.signedData);
                urlConnection.setRequestProperty("x-api-accessKey", this.accessKey);
            }

            if (this.method.equals("POST")) {
                urlConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(payload);
                outputStream.flush();
                outputStream.close();
            }

            statusCode = ((HttpURLConnection) urlConnection).getResponseCode();
//            System.out.println("Status Code " + statusCode);

            InputStream inputStream;

            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                asyncRequest.onResponse(parseMessage(inputStream));
            } else {
                inputStream = ((HttpURLConnection) urlConnection).getErrorStream();
                String _error = parseMessage(inputStream);
                JsonElement jsonElement = new JsonParser().parse(_error);

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonElement str = jsonObject.get("error");
                    asyncRequest.onError(new AzuquaError(statusCode, str.getAsString()));
                } else {
                    asyncRequest.onError(new AzuquaError(statusCode, _error));
                }

            }

        } catch (SocketTimeoutException e) {
            asyncRequest.onError(new AzuquaError(statusCode, "Timeout error"));
        } catch (UnknownHostException e) {
            asyncRequest.onError(new AzuquaError(statusCode, "Invalid host address."));
        } catch (JsonSyntaxException e) {
            asyncRequest.onError(new AzuquaError(statusCode, "Invalid path or not a valid request."));
        } catch (IOException e) {
            asyncRequest.onError(new AzuquaError(statusCode, e.toString()));
        } finally {
            ((HttpURLConnection) urlConnection).disconnect();
        }
    }

    private String parseMessage(InputStream inputStream) {
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
