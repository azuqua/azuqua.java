Azuqua Java Library
===================

This library provides a Java interface for interacting with your Azuqua flos. The full source is available here in addition to the final jar file.

Dependencies
============

The Azuqua Java client depends on the Gson 2.4.

Maven Dependency:

```
<dependency>
	<groupId>com.google.code.gson</groupId>
	<artifactId>gson</artifactId>
	<version>2.4</version>
</dependency>
```

Build
=====

Requirements :
 
* Minimum JDK 1.5
* maven

This project uses maven to build the jar file:
 	
```
git clone https://github.com/azuqua/azuqua.java
cd azuqua.java
mvn package -Dmaven.test.skip=true
```

The package goal builds two jar files: 

* azuqua.java-[VERSION]-jar-with-dependencies.jar 
* azuqua.java-[VERSION].jar

The `azuqua.java-[VERSION]-jar-with-dependencies.jar` contains all the dependencies required for the Azuqua Java client to work. The azuqua.java-[VERSION].jar contains just the compiled code from this repo. It's assumed that the user will attach the required jars to their project externally.

There's a prebuilt jar file with all the dependencies jars built with JDK 1.8 via the targets folder.


Usage
=====

```java
Azuqua azuqua = new Azuqua("access key", "access secret");
List<Flo> flos = (List<Flo>) azuqua.getFlos();

flos = (List<Flo>) azuqua.getFlos(true);		
for(Flo flo : flos) {
	String response = flo.invoke("{\"foo\":\"bar\"}");
}

// if you know the alias associated with the key and secret 
// and don't want to deal with the going through the flos 
// collection.
String floName = "some flo name, can be anything";
String floAlias = "alias";
String requestPayload = "some json";
Flo flo = azuqua.getFloInstance(floName, floAlias);
flo.invoke(requestPayload};
```

Javadocs 
========

Javadocs are located under 

`target/site/apidocs`



