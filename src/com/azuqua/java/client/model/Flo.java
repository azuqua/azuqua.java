package com.azuqua.java.client.model;
import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaException;

/**
 * 
 * @author quyle
 *
 */
public class Flo {
	private static boolean DEBUG = true;
	private String name;
	private String alias;
	private Azuqua azuqua;

	public Flo(String name, String alias){
		this.name = name;
		this.alias = alias;			
	}
	
	public Flo(){}
	
	public String getName(){ return name; }
	public String getAlias(){ return alias; }
	public void setName(String name){ this.name = name; }
	public void setAlias(String alias){ this.alias = alias; }
	public void setAzuqua(Azuqua azuqua) { this.azuqua = azuqua; }

	
	/**
	 * Wrapper for System.out.println.
	 * @param objects
	 */
	public static void o(String method, String msg) {
		if (DEBUG) {
			System.out.println(Flo.class.getName() + "." + method + ": " + msg);
		}
	}
	
	public String invoke(String json) throws AzuquaException{
		String method = "invoke";
		String path = Azuqua.invokeRoute.replace(":id", this.alias);
		o(method, "path " + path);
		o(method, "json " + json);
		String out = null;
		try {
			out = azuqua.makeRequest("POST", path, json);
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
	
	public String read() throws AzuquaException{
		String method = "read";
		String path = Azuqua.readRoute.replace(":id", this.alias);
		o(method, "path " + path);
		String out = null;
		try {
			out = azuqua.makeRequest("GET", path, "");
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
	
	public String enable() throws AzuquaException{
		String method = "read";
		String path = Azuqua.enableRoute.replace(":id", this.alias);
		o(method, "path " + path);
		String out = null;
		try {
			out = azuqua.makeRequest("GET", path, "");
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
	
	public String disable() throws AzuquaException{
		String method = "read";
		String path = Azuqua.disableRoute.replace(":id", this.alias);
		o(method, "path " + path);
		String out = null;
		try {
			out = azuqua.makeRequest("GET", path, "");
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
	
}