	package com.azuqua.java.client.main;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.model.Flo;
import com.azuqua.java.client.model.Org;


/**
 * Main method displays how to use the Azuqua object to make 
 * Azuqua API calls. 
 * @author quyle
 *
 */
public class AzuquaExample {
	private static String access_key = null;
	private static String access_secret = null;
	
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
			String response = flo.invoke("{\"a\":\"foo@azuqua.com\"}");
			System.out.println("response: " + response);
			
			// Additional flo methods
			// Returns a JSON string with the flo details.
			System.out.println(flo.read());
			// Disables the flo. Returns a JSON string with the flo details.
			System.out.println(flo.disable());
			// Enables the flo. Returns a JSON string with the flo details.
			System.out.println(flo.enable());

		}
		
		System.out.println("");
		System.out.println("How to use the Azuqua object with your login credentials:");
		System.out.println("");
		
		// invoke flos through your login credentials
		Azuqua azuquaViaLogin = new Azuqua();
		
		// grab login credentials from the system env variables.
		Map<String, String> env = System.getenv();
		String email = env.get("AZUQUA_EMAIL");
		String password = env.get("AZUQUA_PASSWORD");
		Collection<Org> orgs = azuquaViaLogin.login(email, password, true);
		
		for(Org org : orgs) {			
			for(Flo flo : org.getFlos()) {
				String resp = flo.invoke("{\"a\":\"foo@azuqua.com\"}");
				System.out.println(	);
			}
		}
	}
}
