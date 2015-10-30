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
import lombok.Data;
import lombok.extern.java.Log;

/**
 * <p>Enables the caller to make requests to the Azuqua API.</p>
 * 
 * <p>There's two ways to use this object:</p> 
 * 
 * <ul>
 * <li>Pass your access key and access secret into the constructor.
 * <pre>
Azuqua azuqua = new Azuqua(ACCESS_KEY, ACCESS_SECRET);
List<Flo> flos = azuqua.getFlos();
for(Flo flo : flos) {
	flo.invoke(data);
}
 * </pre>
 * </li>
 * <li>
 * Use your logon credentials:
 * <pre>
Azuqua azuqua = new Azuqua();
Orgs orgs = azuqua.login("user@azuqua.com", "password");
for(Org org : orgs.getOrgs()) {
	// set the access key and access secret from the 
	// specific org you're trying to invoke flos from.
	azuqua.setAccessKey(org.getAccessKey());
	azuqua.setAccessSecret(org.getAccessSecret());
	
	for(Flo flo : org.getFlos()) {
		o(method, "Alias: " + flo.getAlias());
		o(method, "Name: " + flo.getName());
		
		// Need to give a reference to the azuqua object so the 
		// the flo can make Http calls.
		flo.setAzuqua(azuqua);
		String resp = flo.invoke("{\"abc\":\"fooazuqua.com\"}");
		o(method, "resp login method: " + resp);
	}
}
 * </pre>
 * </li>
 * @author quyle
 *
 */
@Log
@Data
public class Azuqua {
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<>();
	
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

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	/**
	 * Custom implementation of DatatypeConverter.printHexBinary() method. Not as
	 * fast, but works just fine for this purpose. This method is needed because
	 * Android doesn't have the DatatypeConverter object. In the futute, we should
	 * use a cross platform implementation such as the Apache codec jar.
	 * @param bytes
	 * @return
	 */
	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	private String signData(String data, String verb, String path, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
		
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(this.accessSecret.getBytes("UTF-8"), "HmacSHA256");
		hmac.init(key);
		
		String meta = verb + ":" + path + ":" + timestamp;
		String dataToDigest = meta + data;
		
		byte[] digest = hmac.doFinal(dataToDigest.getBytes("UTF-8"));
		return DatatypeConverter.printHexBinary(digest).toLowerCase();
//		return bytesToHex(digest).toLowerCase();
	}
	
	private String getISOTime() {
		TimeZone timezone = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(timezone);
	    return df.format(new Date());
	}
	
	/**
	 * <p>Makes a request call to the Azuqua API.</p>
	 *
     * @param verb A String that is either GET or POST.
	 * @param path REST API route.
	 * @param data Data to send to the Azuqua web service.
	 *
     * @return The return payload as a String. The string will have a
     * JSON and it's up the user to decide on how they want to handle this.
	 *
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     *
	 */
	public String makeRequest(String verb, String path, String data) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
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
		    
		    Integer status = ((HttpURLConnection) connection).getResponseCode();
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
		    return response.toString();
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
	 * @return
	 * @throws Exception
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
	 * @param accessKey
	 * @param accessSecret
	 * @param host
	 * @param port
	 * @param protocol
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
	 * @param refresh
	 * @return
	 * @throws AzuquaException
	 */
	public Collection<Flo> getFlos(boolean refresh) throws AzuquaException{
		try {
			if(refresh || floCache.size() < 1){
				String path = listRoute;
				String out = null;
				try {
					out = makeRequest("GET", path, "");
				} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | IOException e) {
					throw new AzuquaException(e);
				}
				Type collectionType = new TypeToken< Collection<Flo> >(){}.getType();
				Collection<Flo> collection = gson.fromJson(out, collectionType);
				
				// give each Flo a reference to this so it can make a request call
				for (Flo flo : collection) {
					flo.setAzuqua(this);
				}
				
				return collection;
			}else{	
				return this.floCache;
			}
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
	}
	
	public Collection<Flo> getFlos() throws AzuquaException{
		try {
			if(floCache.size() < 1){
				String path = listRoute;
				String out = null;
				try {
					out = makeRequest("GET", path, "");
				} catch (InvalidKeyException | NoSuchAlgorithmException | 
						IllegalStateException | IOException e) {
					throw new AzuquaException(e);
				}
				Type collectionType = new TypeToken< Collection<Flo> >(){}.getType();
				Collection<Flo> collection = gson.fromJson(out, collectionType);
				
				// give each Flo a reference to this so it can make a request call
				for (Flo flo : collection) {
					flo.setAzuqua(this);
				}
				
				return collection;
			}else{	
				return this.floCache;
			}
		} catch(Exception e) {
			throw new AzuquaException(e);
		}
	}
	
	/**
	 * <p>
     *     Log on with your username and password. This method returns an Orgs object which contains a list of Org objects. Each Org object will contain a list of Flo objects. Check out the specific classes for more information regarding available methods.
	 * </p>
	 * 
	 * <p>
     *     Set streamOrgsJson to true if the device you are using has memory constraints. Once the Azuqua web service has verified your username and password, the web service returns an orgs JSON, which can be large. This Orgs JSON string will then be deserialized into an Orgs object. If streamOrgsJson is set to true, the Orgs JSON stream will be streamed into the deserialization method.
	 * </p>
	 * @param username
	 * @param password
	 * @param streamOrgsJson specifies whether the user wants the orgs 
	 * @return
	 * @throws Exception
	 */
	public Orgs login(String username, String password, boolean streamOrgsJson) throws Exception {
		String method = "login";
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
			String accountInfo = makeRequest("POST", accountsInfoRoute, loginInfo);
			Orgs orgs = gson.fromJson(accountInfo, Orgs.class);
			return orgs;
		}
	}
}