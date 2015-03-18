Azuqua Java Library
===================

This library provides a Java interface for interacting with your Azuqua flos. The full source is available here in addition to the final jar file.

Usage
=====

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

