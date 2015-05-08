package com.azuqua.java.cliet.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaException;
import com.azuqua.java.client.model.Flo;
import com.azuqua.java.client.model.Org;

public class AzuquaTestCase {
	private Azuqua azuqua;
	private String email;
	private String password;
	@Before
	public void setUp() throws Exception {
		// grabs access key and secret from the system env variables
		azuqua = new Azuqua();
		
		Map<String, String> env = System.getenv();
		
		this.email = env.get("AZUQUA_EMAIL");
		this.password = env.get("AZUQUA_PASSWORD");
	}

	@Test
	public void getFlosTest() {
		Collection<Flo> flos;
		try {
			flos = azuqua.getFlos();
			assertNotNull("The flos collection should not be null.", flos);
		} catch (AzuquaException e) {
			e.printStackTrace();
		}
	}
	
	@Test (expected = Exception.class)
	public void loginNoCredentialsTest() throws Exception {
		azuqua.login("", "", false);
	}
	
	@Test 
	public void loginWithCredentialsTest() throws Exception{
		Collection<Org> orgs = azuqua.login(email, password, false);
		assertNotNull("The orgs collection should not be null.", orgs);
	}
	
	@Test
	public void getFlosFromOrgTest() throws Exception {
		Collection<Org> orgs = azuqua.login(email, password, true);
		for(Org org : orgs) {
			assertNotNull("The flos collection should not be null.", org.getFlos());
		}
	}
}
