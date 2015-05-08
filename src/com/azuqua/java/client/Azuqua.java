package com.azuqua.java.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.azuqua.java.client.model.*;

/**
 * <p>Enables the caller to make requests to the Azuqua API.</p>
 * @author quyle
 *
 */
public class Azuqua {
	private boolean DEBUG = false;
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<Flo>();
	
	// routes
	public final static String invokeRoute = "/flo/:id/invoke";
	public final static String readRoute = "/flo/:id/read";
	public final static String disableRoute = "/flo/:id/disable";
	public final static String enableRoute = "/flo/:id/enable";
	public final static String listRoute = "/org/flos";	
	public final static String accountsInfoRoute = "/org/login";
								
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
		String method = "signData";
		
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(this.accessSecret.getBytes("UTF-8"), "HmacSHA256");
		hmac.init(key);
		
		String meta = verb + ":" + path + ":" + timestamp;
		String dataToDigest = meta + data;
		out(method, "data to digest " + dataToDigest);
		
		byte[] digest = hmac.doFinal(dataToDigest.getBytes("UTF-8"));
		String digestString = bytesToHex(digest).toLowerCase();
		out(method, "digested string " + digestString);		
		
		return 	digestString;
	}
	
	private String getISOTime() {
		TimeZone timezone = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(timezone);
	    String timestamp = df.format(new Date());
	    
	    return timestamp;
	}
	
	private void printHeaders(String verb, URLConnection connection, URL apiUrl) {
		String method = "printHeaders";
	    out(method, "headers =====================");
	    String curl = "curl -i ";
	    
		for (Entry<String, List<String>> k : connection.getRequestProperties().entrySet()) {
		    for (String v : k.getValue()){
		         out(method, k.getKey() + ":" + v);
		         curl += "-H \"" + k.getKey() + ":" + v + "\" ";
		    }
		}
		
		curl += " --verbose ";
		if (verb.toUpperCase().equals("POST")) {
			curl += " -X POST -d \'{ \"abc\":\"this is a test.\" }' ";
		}
		curl += apiUrl.toString();
		out(method, "curl: " + curl);
		out(method, "headers =====================");
		out(method, "DEBUG =======================");
	    out(method, "agent: " + connection.getRequestProperty("agent"));
	    out(method, "url: " + apiUrl.toString());	
	    out(method, "METHOD: " + ((HttpURLConnection) connection).getRequestMethod());
	    out(method, "host " + apiUrl.getHost());
	    out(method, "DEBUG =======================");
	}
	
	/**
	 * Makes a request call to the Azuqua API.
	 * @param verb A String that is either GET or POST.
	 * @param Path REST API route.
	 * @param data Data to send to the Azuqua web service.
	 * @return
	 * @throws Exception
	 */
	public String makeRequest(String verb, String path, String data) throws Exception {
		String method = "makeRequest";
		
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
			
//			printHeaders(verb, connection, apiUrl);
					    
		    if (verb.toUpperCase().equals("POST")) {
			    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			    wr.writeBytes(data);
			    wr.flush();
			    wr.close();
		    }
		    
		    int status = ((HttpURLConnection) connection).getResponseCode();
		    out(method, "response code " + status);
		    StringBuffer response = new StringBuffer();
		    InputStream is = null;
		    if (status < 400) {
			    is = connection.getInputStream();
		    }
		    else {
		    	// probably doesn't matter, but good to check which connection to get the error stream anyway
		    	is = this.protocol.equals("https") ? ((HttpsURLConnection) connection).getErrorStream() : ((HttpURLConnection) connection).getErrorStream();
		    }
		    
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    while((line = rd.readLine()) != null) {
		    	response.append(line);
		    	response.append('\r');
		    }
		    rd.close();
		    
		    out(method, "response " + response.toString());
		    if (status >= 400) {
		    	throw new AzuquaException(new Throwable(response.toString()));
		    }
		    return response.toString();
		} catch(Exception e) {
			throw e;
		}
		finally {
			((HttpURLConnection) connection).disconnect();
		}
	}
	
	/**
	 * Makes a request call to the Azuqua API.
	 * @param verb A String that is either GET or POST.
	 * @param Path REST API route.
	 * @param data Data to send to the Azuqua web service.
	 * @return
	 * @throws Exception
	 */
	public URLConnection makeRequestForInputStream(String verb, String path, String data) throws Exception {
		String method = "makeRequest";
		
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
		    return connection;
		} catch(Exception e) {
			throw e;
		}
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
		if (accessKey != null && accessSecret != null) {
			this.accessKey = accessKey;
			this.accessSecret = accessSecret;
		}
		else {
			// if calling the default constructor, the constructor 
			Map<String, String> env = System.getenv();
			this.accessKey = env.get("ACCESS_KEY");
			this.accessSecret = env.get("ACCESS_SECRET");		
		}
		
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
	
	public Azuqua(){
		loadConfig(accessKey, accessSecret, host, port, protocol);
	} 
	
	/**
	 * Returns a collection of flos. 
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
	 * Log on with your username and password. This method returns an 
	 * Orgs object which contains a list of Org objects. Each Org object
	 * will contain a list of Flo objects. Check out the specific classes 
	 * for more information regarding available methods. 
	 * 
	 * Set streamOrgsJson to true if the device you are using has memory 
	 * constraints. Once the Azuqua web service has verified your username 
	 * and password, the web service returns an orgs JSON, which can be large.
	 * This Orgs JSON string will then be deserialized into an Orgs object.
	 * If streamOrgsJson is set to true, the Orgs JSON stream will be streamed 
	 * into the deserialization method.
	 * @param username
	 * @param password
	 * @param streamOrgsJson specifies whether the user wants the orgs 
	 * @return
	 * @throws Exception
	 */
	public Collection<Org> login(String username, String password, boolean streamOrgsJson) throws Exception {
		String method = "login";
		String loginInfo = gson.toJson(new LoginInfo(username, password));
		out(method, "username: " + username);
		
		Type collectionType = new TypeToken<Collection<Org>>(){}.getType();
		Collection<Org> orgs = null;
		
		if (streamOrgsJson) {
			URLConnection connection = null;
			try {
				connection = makeRequestForInputStream("POST", accountsInfoRoute, loginInfo);
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				JsonReader jsonReader = new JsonReader(isr);
				orgs = gson.fromJson(jsonReader, collectionType);
			}
			finally {
				((HttpURLConnection) connection).disconnect();
			}
		} 
		else {
			String accountInfo = makeRequest("POST", accountsInfoRoute, loginInfo);
			out(method, "accountInfo: " + accountInfo);
			orgs = gson.fromJson(accountInfo, collectionType);
			
		}
		// give the org object a new Azuqua object with the org's key and secret
		for (Org org : orgs) {
			org.setAzuqua(new Azuqua(org.getAccessKey(), org.getAccessSecret()));
		}
		return orgs;
	}

	/**
	 * Wrapper for System.out.println.
	 * @param objects
	 */
	public void out(String method, String msg) {
		if (DEBUG) {
			System.out.println(Azuqua.class.getName() + "." + method + ": " + msg);
		}
	}


	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
}