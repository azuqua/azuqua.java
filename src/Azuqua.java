
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
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Azuqua {
	private Gson gson = new Gson();
	private Vector<Flo> floCache = new Vector<Flo>();
	
	// routes
	private String invokeRoute = "/api/flo/:id/invoke";
	private String listRoute = "/api/account/flos";
	
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
		wrapper.setHash(signData(data));
		String body = gson.toJson(wrapper);
		URL apiUrl = new URL(this.host + path);
		HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(body.getBytes().length));
		connection.setUseCaches(false);
	    connection.setDoInput(true);
	    connection.setDoOutput(true);
	    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	    wr.writeBytes(body);
	    wr.flush();
	    wr.close();
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
				out = makeRequest(path, "{}");
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
	
	public Collection<Flo> getFlos(boolean refresh) throws AzuquaException{
		if(refresh || floCache.size() < 1){
			String path = listRoute;
			String out = null;
			try {
				out = makeRequest(path, "{}");
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
