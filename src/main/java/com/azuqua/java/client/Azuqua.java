package com.azuqua.java.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.azuqua.java.client.model.*;

/**
 * <p>Enables the caller to make requests to the Azuqua API.</p>
 *
 * <pre>
 *     Azuqua azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET);
 *     for (Flo flo : azuqua.getFlos()) {
 *         AzuquaResponse resp = flo.invoke(data);
 *     }
 * </pre>
 * @author quyle
 *
 */
public class Azuqua {
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<Flo>();
	
	// routes
	public final static String invokeRoute = "/flo/:id/invoke";
	public final static String listRoute = "/account/flos";	
	public final static String accountsInfoRoute = "/account/data";
    public final static String telemetryData = "/telemetry/:alias/:exec/data";
    public final static String telemetryMetrics = "/telemetry/:alias/:exec/metrics";

								
	// http options
	private String host = "api.azuqua.com";
	private String protocol = "https";
	private int port = 443;
							   
	// account
	private String accessKey;
	private String accessSecret;

    /**
     * Generates a hash from the data, verb, path and timestamp with the given access secret.
     *
     * @param data
     * @param verb
     * @param path
     * @param timestamp
     * @return
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws UnsupportedEncodingException UTF-8 encoding is not supported.
     */
	private String signData(String data, String verb, String path, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(this.accessSecret.getBytes("UTF-8"), "HmacSHA256");
		hmac.init(key);
		
		String meta = verb + ":" + path + ":" + timestamp;
		String dataToDigest = meta + data;
		
		byte[] digest = hmac.doFinal(dataToDigest.getBytes("UTF-8"));
		return DatatypeConverter.printHexBinary(digest).toLowerCase();
	}
	
	private String getISOTime() {
		TimeZone timezone = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(timezone);
	    return df.format(new Date());
	}
	
	/**
	 * <p>
     *     Makes a request call to the Azuqua API.
	 * </p>
	 *
     * @param verb A String that is either GET or POST.
	 * @param path REST API route.
	 * @param data Data to send to the Azuqua web service.
	 *
     * @return
     * <p>
     *     An AzuquaResponse object with two String properties:
     * </p>
     * <ul>
     *     <li>xFloInstance - x-flo-instance header property. Used for flo-resume.</li>
     *     <li>response - Represents the body of the response</li>
     * </ul>
     *
	 *
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
     *
	 */
	public AzuquaResponse makeRequest(String verb, String path, String data) throws  IOException, InvalidKeyException, NoSuchAlgorithmException {
		URLConnection connection;
		String timestamp = getISOTime();
		
		String signedData = null;
		if (accessKey != null && accessSecret != null) {
			signedData = signData(data, verb.toLowerCase(), path, timestamp);
		}
		
		URL apiUrl = new URL(this.protocol, this.host, this.port, path);
		connection = this.protocol.equals("https") ? (HttpsURLConnection) apiUrl.openConnection() : (HttpURLConnection) apiUrl.openConnection();
		
		try {			
			connection.setUseCaches(false);
		    connection.setDoOutput(true);
			connection.setDoInput(true);
			((HttpURLConnection) connection).setRequestMethod(verb.toUpperCase());
			connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
			connection.setRequestProperty("x-api-timestamp", timestamp);
			
			if (signedData != null) {
				connection.setRequestProperty("x-api-hash", signedData);
				connection.setRequestProperty("x-api-accessKey", this.accessKey);
			}
			
			connection.setRequestProperty("host", this.host);

		    if (verb.toUpperCase().equals("POST")) {
			    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			    wr.writeBytes(data);
			    wr.flush();
			    wr.close();
		    }

			HttpURLConnection httpConnection = (HttpURLConnection) connection;
		    Integer status = httpConnection.getResponseCode();
            String xFloInstance = httpConnection.getHeaderField("x-flo-instance");

		    StringBuilder response = new StringBuilder();
            if (verb.toUpperCase().equals("GET") || verb.toUpperCase().equals("POST")) {
			    InputStream is = connection.getInputStream();
			    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			    String line;
			    while((line = rd.readLine()) != null) {
			    	response.append(line);
			    	response.append('\r');
			    }
			    rd.close();
		    }

            AzuquaResponse azuquaResponse = new AzuquaResponse();
            azuquaResponse.setResponse(response.toString());
            azuquaResponse.setXFloInstance(xFloInstance);
            return azuquaResponse;
		}
		finally {
			((HttpURLConnection) connection).disconnect();
		}
	}
	
	/**
	 * Makes a request call to the Azuqua API.
	 * @param verb A String that is either GET or POST.
	 * @param path REST API route.
	 * @param data Data to send to the Azuqua web service.
	 * @return A url connection object.
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public URLConnection makeRequestForInputStream(String verb, String path, String data) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		
		URLConnection connection;
		String timestamp = getISOTime();
		
		String signedData = null;
		if (accessKey != null && accessSecret != null) {
			signedData = signData(data, verb.toLowerCase(), path, timestamp);
		}
		
		URL apiUrl = new URL(this.protocol, this.host, this.port, path);
		connection = this.protocol.equals("https") ? (HttpsURLConnection) apiUrl.openConnection() : (HttpURLConnection) apiUrl.openConnection();
		

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        ((HttpURLConnection) connection).setRequestMethod(verb.toUpperCase());
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
        connection.setRequestProperty("x-api-timestamp", timestamp);

        if (signedData != null) {
            connection.setRequestProperty("x-api-hash", signedData);
            connection.setRequestProperty("x-api-accessKey", this.accessKey);
        }

        connection.setRequestProperty("host", this.host);
        if (verb.toUpperCase().equals("POST")) {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();
        }
        return connection;

	}
	
	/**
	 * Load config info for this class.
	 * @param accessKey The access key for the account.
	 * @param accessSecret The access secret for the access secret.
	 * @param host The Azuqua host.
	 * @param port The Azuqua port.
	 * @param protocol The HTTP protocol.
	 */
	public void loadConfig(String accessKey, String accessSecret, String host, int port, String protocol){
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;
		this.host = host;
		this.port = port;
		this.protocol = protocol;
	}
	
	public Azuqua(String accessKey, String accessSecret){
		loadConfig(accessKey, accessSecret, host, port, protocol);
	}
	
	public Azuqua(String accessKey, String accessSecret, String _host){
		loadConfig(accessKey, accessSecret, _host, port, protocol);
	}
	
	public Azuqua(String accessKey, String accessSecret, String _host, int _port){
		loadConfig(accessKey, accessSecret, _host, _port, protocol);
	}
	
	public Azuqua(String accessKey, String accessSecret, String _host, int _port, String _protocol){
		loadConfig(accessKey, accessSecret, _host, _port, _protocol);
	}
	
	public Azuqua(){}
	
	/**
	 * <p>Returns a collection of flos.</p>
	 * @param refresh Refresh the cache.
	 * @return A collection of FLOs.
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public Collection<Flo> getFlos(boolean refresh) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if(refresh || floCache.size() < 1){
            String path = listRoute;
            AzuquaResponse out = null;
            out = makeRequest("GET", path, "");
            Type collectionType = new TypeToken< Collection<Flo> >(){}.getType();
            Collection<Flo> collection = gson.fromJson(out.getResponse(), collectionType);

            // give each Flo a reference to this so it can make a request call
            for (Flo flo : collection) {
                flo.setAzuqua(this);
            }

            return collection;
        }
        else{
            return this.floCache;
        }
	}

    /**
     *
     * @return a list of FLOs.
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
     */
	public Collection<Flo> getFlos() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (floCache.size() < 1) {
            String path = listRoute;
            AzuquaResponse out = null;
            out = makeRequest("GET", path, "");

            Type collectionType = new TypeToken<Collection<Flo>>() {
            }.getType();
            Collection<Flo> collection = gson.fromJson(out.getResponse(), collectionType);

            // give each Flo a reference to this so it can make a request call
            for (Flo flo : collection) {
                flo.setAzuqua(this);
            }

            return collection;
        } else {
            return this.floCache;
        }
	}
	
	/**
	 * <p>
     *     Log on with your username and password. This method returns an Orgs object which contains a
     *     list of Org objects. Each Org object will contain a list of Flo objects. Check out the specific
     *     classes for more information regarding available methods.
	 * </p>
	 * 
	 * <p>
     *     Set streamOrgsJson to true if the device you are using has memory constraints. Once the Azuqua web
     *     service has verified your username and password, the web service returns an orgs JSON, which can be
     *     large. This Orgs JSON string will then be deserialized into an Orgs object. If streamOrgsJson is set
     *     to true, the Orgs JSON stream will be streamed into the deserialization method.
	 * </p>
	 * @param username The username of the account.
	 * @param password The password of the account.
	 * @param streamOrgsJson specifies whether the user wants the orgs 
	 * @return an Orgs object.
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public Orgs login(String username, String password, boolean streamOrgsJson) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String loginInfo = gson.toJson(new LoginInfo(username, password));
		
		if (streamOrgsJson) {
			URLConnection connection = null;
			try {
				connection = makeRequestForInputStream("POST", accountsInfoRoute, loginInfo);
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				JsonReader jsonReader = new JsonReader(isr);
				Orgs orgs = gson.fromJson(jsonReader, Orgs.class);
				return orgs;
			}
			finally {
				((HttpURLConnection) connection).disconnect();
			}
		} 
		else {
			AzuquaResponse accountInfo = makeRequest("POST", accountsInfoRoute, loginInfo);
			Orgs orgs = gson.fromJson(accountInfo.getResponse(), Orgs.class);
			return orgs;
		}
	}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Vector<Flo> getFloCache() {
        return floCache;
    }

    public void setFloCache(Vector<Flo> floCache) {
        this.floCache = floCache;
    }
}