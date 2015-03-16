
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Azuqua {
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<Flo>();
	
	// routes
//	private String invokeRoute = "/api/flo/:id/invoke";
//	private String listRoute = "/api/account/flos";
	private String invokeRoute = "/flo/:id/invoke";
	private String listRoute = "/account/flos";	
								
	// http options
	private String host = "https://api.azuqua.com";	
							   
	
	// account
	private String accessKey;
	private String accessSecret;
	
	private class AzuquaException extends Exception{
		
		public AzuquaException(Throwable cause){ 
			super(cause); 
		}
		
	}
	
	public static void out(Object... objects) {
		for(Object object : objects) {
			System.out.println(object);
		}
	}
	
	public String signDataQ(String data, String verb, String path, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(this.accessSecret.getBytes("UTF-8"), "HmacSHA256");
		hmac.init(key);
		
//		String dataToDigest = "get:/account/flos:2015-03-16T05:06:34.804Z";
		String dataToDigest = verb + ":" + path + ":" + timestamp;
		out("data to digest " + dataToDigest);
		byte[] digest = hmac.doFinal(dataToDigest.getBytes("UTF-8"));
		String digestString = DatatypeConverter.printHexBinary(digest).toLowerCase();
		
		out("digested string " + digestString);
		
		return 	digestString;
	}
	
	private String signData(String data) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException{
		Mac hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec key = new SecretKeySpec(Charset.forName("UTF-8").encode(this.accessSecret).array(), "HmacSHA256");
		hmac.init(key);
		byte[] digest = hmac.doFinal(data.getBytes());
		return DatatypeConverter.printHexBinary(digest);	
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
	
	private String makeRequest(String path, String data) throws IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException{
		RequestObjectWrapper wrapper = new RequestObjectWrapper();
		wrapper.setAccessKey(this.accessKey);
		wrapper.setData(data);
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(tz);
	    String timestamp = df.format(new Date());
		
		String signedData = signDataQ(data, "get", path, timestamp);
		
		wrapper.setHash(signedData);
		String body = gson.toJson(wrapper);
		out("body " + body);
		
		//		URL apiUrl = new URL(this.host + ":443" +  path);
		URL apiUrl = new URL("https", "api.azuqua.com", 443, path);
		
		HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
		
		try {			
			connection.setUseCaches(false);
		    connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(body.getBytes().length));
			connection.setRequestProperty("x-api-timestamp", timestamp);
			connection.setRequestProperty("x-api-hash", signedData);
			connection.setRequestProperty("x-api-accessKey", this.accessKey);
			connection.setRequestProperty("host", "api.azuqua.com");
					    
		    out("headers =====================");
		    String curl = "curl -i ";
			for (Entry<String, List<String>> k : connection.getRequestProperties().entrySet()) {
			    for (String v : k.getValue()){
			         System.out.println(k.getKey() + ":" + v);
			         curl += "-H \"" + k.getKey() + ":" + v + "\" ";
			    }
			}
			curl += " --verbose https://api.azuqua.com:443/account/flos";
			out("curl: " + curl);
			out("headers =====================");
		    out("agent: " + connection.getRequestProperty("agent"));
		    out("url: " + apiUrl.toString());	
		    out("METHOD: " + connection.getRequestMethod());
		    out("host " + apiUrl.getHost());
		    
		    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		    wr.writeBytes(body);
		    wr.flush();
		    wr.close();
		    
//		    int status = connection.getResponseCode();
//		    out("response code " + status);
		    
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = rd.readLine()) != null) {
		    	response.append(line);
		    	response.append('\r');
		    }
		    rd.close();
		    

		    
		    return response.toString();
		}
		finally {
			connection.disconnect();
		}
	}
	
	// Azuqua azuqua = new Azuqua(key, secret);
	// Azuqua.Flo flo = azuqua.new Flo();
	public class Flo {
		private String name;
		private String alias;
			
		public Flo(String name, String alias){
			this.name = name;
			this.alias = alias;			
		}
		
		public Flo(){}
		
		public String getName(){ return name; }
		public String getAlias(){ return alias; }
		public void setName(String name){ this.name = name; }
		public void setAlias(String alias){ this.alias = alias; }
		
		public String invoke(String json) throws AzuquaException{
			String path = invokeRoute.replace(":id", this.alias);
			String out = null;
			try {
				out = makeRequest(path, json);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| IllegalStateException | IOException e) {
				throw new AzuquaException(e);
			}
			return out;
		}
		
	}
	
	public void loadConfig(String accessKey, String accessSecret){
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;
	}
	
	public Azuqua(String accessKey, String accessSecret){
		loadConfig(accessKey, accessSecret);
	}
	
	public Azuqua(){}
	
	public Collection<Flo> getFlos(boolean refresh) throws AzuquaException{
		if(refresh || floCache.size() < 1){
			String path = listRoute;
			String out = null;
			try {
				out = makeRequest(path, "");
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| IllegalStateException | IOException e) {
				throw new AzuquaException(e);
			}
			Type collectionType = new TypeToken<Collection<Flo>>(){}.getType();
			return gson.fromJson(out, collectionType);
		}else{	
			return this.floCache;
		}		
	}
	
}