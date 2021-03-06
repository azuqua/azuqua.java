Azuqua Java Library
===================

This library provides a Java interface for interacting with your Azuqua flos. The full source is available here in addition to the final jar file.

Dependencies
============

Add azuqua-java maven dependency to your project

Maven Dependency:

```xml 
<dependency>
  <groupId>com.azuqua</groupId>
  <artifactId>azuqua-java</artifactId>
  <version>1.0.3</version>
</dependency>
```

Build
=====

Requirements :
 
* Minimum JDK 1.6

Usage
=====

```java
Azuqua azuqua = new Azuqua("access key", "access secret");

(or)

Azuqua azuqua = new Azuqua("access key", "access secret", "protocol", "host", port);

// Get FLOs 
azuqua.getFLOs(new OrgFLOsRequest() {
    @Override
    public void onResponse(List<FLO> floList) {
        // returns list of FLOs on success
    }

    @Override
    public void onError(AzuquaError azuquaError) {
        // returns error details on failure
    }
});

// Invoke FLO
azuqua.invokeFLO("flo-alias", "payload", new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        // returns reponse on success
    }

    @Override
    public void onError(AzuquaError azuquaError) {
        // returns error details on failure
    }
});
```
