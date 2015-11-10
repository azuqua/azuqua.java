Azuqua Java Library
===================

This library provides a Java interface for interacting with your Azuqua flos. The full source is available here in addition to the final jar file.

Build
=====

Requirements :
 
* Minimum JDK 1.5
* maven

This project uses maven to build the jar file:
 	
```
git clone https://github.com/azuqua/azuqua.java
cd azuqua.java
mvn package
```

There's a prebuilt jar file with all the dependencies jars built with JDK 1.8 via the targets folder.


Usage
=====

```

Azuqua azuqua = new Azuqua("access key", "access secret");
List<Flo> flos = (List<Flo>) azuqua.getFlos();

flos = (List<Flo>) azuqua.getFlos(true);		
for(Flo flo : flos) {
	String response = flo.invoke("{\"foo\":\"bar\"}");
}

```

Javadocs 
========



