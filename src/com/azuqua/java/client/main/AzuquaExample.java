package com.azuqua.java.client.main;
import java.util.List;

import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.model.Flo;
import com.azuqua.java.client.model.Orgs;
import com.azuqua.java.client.model.Org;


/**
 * Main method displays how to use the Azuqua object to make 
 * Azuqua API calls. 
 * @author quyle
 *
 */
public class AzuquaExample {
	private static String access_key = "access key";
	private static String access_secret = "access secret";
	
	/**
	 * In this example, we're making a calling to the 
	 * Azuqua API to invoke a HTTP channel. The HTTP channel
	 * accepts the following input:
	 * 
	 * <pre>
	 * { "abc":"payload" }
	 * </pre>
	 * 
	 * The channel will then echo the request as the response.
	 * 
	 * <pre>
	 * {"data":{"abc":"foo@azuqua.com"}}
	 * </pre>
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv) throws Exception {
		String method = "main";
		
		// invoke flos via your access key and access secret
		Azuqua azuqua = new Azuqua(access_key, access_secret);
		List<Flo> flos = (List<Flo>) azuqua.getFlos();
				
		for(Flo flo : flos) {
			System.out.println("Alias: " + flo.getAlias());
			String response = flo.invoke("{\"abc\":\"foo@azuqua.com\"}");
			System.out.println("response: " + response);
		}
		
		System.out.println("");
		System.out.println("How to use the Azuqua object with your login credentials:");
		System.out.println("");
		
		// invoke flos through your login credentials
		Azuqua azuquaViaLogin = new Azuqua();
		Orgs orgs = azuquaViaLogin.login("user@azuqua.com", "password");
		
		for(Org org : orgs.getOrgs()) {
			// set the access key and access secret from the 
			// specific org you're trying to invoke flos from
			azuquaViaLogin.setAccessKey(org.getAccessKey());
			azuquaViaLogin.setAccessSecret(org.getAccessSecret());
			
			for(Flo flo : org.getFlos()) {
				out(method, "Alias: " + flo.getAlias());
				out(method, "Name: " + flo.getName());
				
				// give a reference to the Azuqua object so that 
				// the flo can make Azuqua API calls
				flo.setAzuqua(azuquaViaLogin);
				
				String resp = flo.invoke("{\"abc\":\"foo@azuqua.com\"}");
				out(method, "resp login method: " + resp);
			}
		}
	}
		
	/**
	 * Wrapper for System.out.println.
	 * @param objects
	 */
	public static void out(String method, String msg) {
		System.out.println(AzuquaExample.class.getName() + "." + method + ": " + msg);
	}
}
