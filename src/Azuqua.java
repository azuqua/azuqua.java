
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
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Azuqua {
	private static boolean DEBUG = false;
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<Flo>();
	
	// routes
	public final static String invokeRoute = "/flo/:id/invoke";
	public final static String listRoute = "/account/flos";	
								
	// http options
	private String host = "api.azuqua.com";
	private String protocol = "https";
	private int port = 443;
							   
	// account
	private String accessKey;
	private String accessSecret;
		
	/**
	 * Wrapper for System.out.println.
	 * @param objects
	 */
	public static void out(Object... objects) {
		if (DEBUG) {
			for(Object object : objects) {
				System.out.println(object);
			}
		}
	}
	
	public String signData(String data, String verb, String path, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(this.accessSecret.getBytes("UTF-8"), "HmacSHA256");
		hmac.init(key);
		
		String meta = verb + ":" + path + ":" + timestamp;
		String dataToDigest = meta + data;
		out("data to digest " + dataToDigest);
		
		byte[] digest = hmac.doFinal(dataToDigest.getBytes("UTF-8"));
		String digestString = DatatypeConverter.printHexBinary(digest).toLowerCase();
		out("digested string " + digestString);		
		
		return 	digestString;
	}
		
	private class RequestObjectWrapper {
		private String hash;
		private String data;
		private String accessKey;
		
		public RequestObjectWrapper(){};
		
		public String getHash(){ return hash; }
		public String getData(){ return data; }
		public String getAccessKey() { return accessKey; }
		public void setHash(String hash){ this.hash = hash; }
		public void setData(String data){ this.data = data; }
		public void setAccessKey(String accessKey){ this.accessKey = accessKey; }
	}
	
	private String getISOTime() {
		TimeZone timezone = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(timezone);
	    String timestamp = df.format(new Date());
	    
	    return timestamp;
	}
	
	private void printHeaders(String verb, URLConnection connection, URL apiUrl) {
	    out("headers =====================");
	    String curl = "curl -i ";
	    
		for (Entry<String, List<String>> k : connection.getRequestProperties().entrySet()) {
		    for (String v : k.getValue()){
		         System.out.println(k.getKey() + ":" + v);
		         curl += "-H \"" + k.getKey() + ":" + v + "\" ";
		    }
		}
		
		curl += " --verbose ";
		if (verb.toUpperCase().equals("POST")) {
			curl += " -X POST -d \'{ \"abc\":\"this is a test.\" }' ";
		}
		curl += apiUrl.toString();
		out("curl: " + curl);
		out("headers =====================");
		out("DEBUG =======================");
	    out("agent: " + connection.getRequestProperty("agent"));
	    out("url: " + apiUrl.toString());	
	    out("METHOD: " + ((HttpURLConnection) connection).getRequestMethod());
	    out("host " + apiUrl.getHost());
	    out("DEBUG =======================");
	}
	
	protected String makeRequest(String verb, String path, String data) throws IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException{		
		URLConnection connection;
		String timestamp = getISOTime();
		String signedData = signData(data, verb.toLowerCase(), path, timestamp);
		
		URL apiUrl = new URL(this.protocol, this.host, this.port, path);
		if(this.protocol.equals("https")){
			connection = (HttpsURLConnection) apiUrl.openConnection();
		}else{
			connection = (HttpURLConnection) apiUrl.openConnection();
		}
		
		try {			
			connection.setUseCaches(false);
		    connection.setDoOutput(true);
			connection.setDoInput(true);
			((HttpURLConnection) connection).setRequestMethod(verb.toUpperCase());
			connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
			connection.setRequestProperty("x-api-timestamp", timestamp);
			connection.setRequestProperty("x-api-hash", signedData);
			connection.setRequestProperty("x-api-accessKey", this.accessKey);
			connection.setRequestProperty("host", this.host);
			
			printHeaders(verb, connection, apiUrl);
					    
		    if (verb.toUpperCase().equals("POST")) {
			    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			    wr.writeBytes(data);
			    wr.flush();
			    wr.close();
		    }
		    
		    int status = ((HttpURLConnection) connection).getResponseCode();
		    out("response code " + status);
		    StringBuffer response = new StringBuffer();
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
		    out("response " + response.toString());
		    return response.toString();
		}
		finally {
			((HttpURLConnection) connection).disconnect();
		}
	}
	
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
	
	public Collection<Flo> getFlos(boolean refresh) throws AzuquaException{		
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
	}
	
	public Collection<Flo> getFlos() throws AzuquaException{		
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
	}
	
}