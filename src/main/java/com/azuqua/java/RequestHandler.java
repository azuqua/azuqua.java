package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SASi on 14-Jul-16.
 */
public class RequestHandler {

    private String url = null;
    private String method = null;
    private String payload = "";
    private String timeStamp = null;
    private String signedData = null;
    private String accessKey = null;
    private AsyncRequest asyncRequest = null;

    private HttpsURLConnection urlConnection = null;

    public RequestHandler(String url, String method, String payload, AsyncRequest asyncRequest) {
        this.url = url;
        this.method = method;
        this.payload = payload;
        this.asyncRequest = asyncRequest;
    }

    public RequestHandler(String url, String method, String payload, String timeStamp, String signedData, String accessKey, AsyncRequest asyncRequest) {
        this.url = url;
        this.method = method.toUpperCase();
        this.payload = payload;
        this.timeStamp = timeStamp;
        this.signedData = signedData;
        this.accessKey = accessKey;
        this.asyncRequest = asyncRequest;
    }

    public void execute() {
        try {
            URL url = new URL(this.url);

            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod(this.method);
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

            int statusCode = urlConnection.getResponseCode();
            System.out.println("Status Code " + statusCode);

            InputStream inputStream;

            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                asyncRequest.onResponse(parseMessage(inputStream));
            } else {
                inputStream = urlConnection.getErrorStream();
                asyncRequest.onError(parseMessage(inputStream));
            }

        } catch (IOException e) {
//            e.printStackTrace();
            asyncRequest.onError(e.toString());
        } finally {
            urlConnection.disconnect();
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
