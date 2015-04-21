Azuqua Java Library
===================

This library provides a Java interface for interacting with your Azuqua flos. The full source is available here in addition to the final jar file.

Usage
=====

There's two ways to use the Azuqua object. 

The first is by specifying your access key and access secret:

```
Azuqua azuqua = new Azuqua("access key", "access secret");
List<Flo> flos = (List<Flo>) azuqua.getFlos();

// you can also manually refresh the flo cache
flos = (List<Flo>) azuqua.getFlos(true);
		
for(Flo flo : flos) {
	System.out.println("Alias: " + flo.getAlias());
	System.out.println("Name: " + flo.getName());
	
	// flo.invoke accepts any valid JSON string
	String response = flo.invoke("{\"foo\":\"bar\"}");
	System.out.println(response);
}
```

The second is by specifying your logon credentials for your Azuqua account:

```
// invoke flos through your login credentials
Azuqua azuqua = new Azuqua();
Orgs orgs = azuqua.login("user@azuqua.com", "password");
	
for(Org org : orgs.getOrgs()) {
	// set the access key and access secret from the 
	// specific org you're trying to invoke flos from
	azuqua.setAccessKey(org.getAccessKey());
	azuqua.setAccessSecret(org.getAccessSecret());
	
	for(Flo flo : org.getFlos()) {
		System.out.println("Alias: " + flo.getAlias());
		System.out.println("Name: " + flo.getName());
		
		// give a reference to the Azuqua object so that 
		// the flo can make Azuqua API calls
		flo.setAzuqua(azuqua);
		
		String resp = flo.invoke("{\"abc\":\"foo@azuqua.com\"}", true);
		System.out.println("resp login method: " + resp);
	}
}
```

