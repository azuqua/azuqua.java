Azuqua Java Library
===================

[PLACEHOLDER_DESC_HERE]

Requirements
============

* Minimum JDK 1.6

Install
=======

Add azuqua-java maven dependency to your project

Maven Dependency:

```xml 
<dependency>
  <groupId>com.azuqua</groupId>
  <artifactId>azuqua-java</artifactId>
  <version>1.0.2</version>
</dependency>
```

Usage
=====
```java
Azuqua azuqua = new Azuqua("access key", "access secret");

(or)

Azuqua azuqua = new Azuqua("access key", "access secret", "protocol", "host", port);



azuqua.readAllAccounts(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readAccount(account_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.deleteAccount(account_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"role\":\"NONE\"}";
azuqua.updateAccountUserPermissions(account_id, user_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readConnectorVersion(connector_name, connector_version, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFlo(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"name\":\"\",\"description\":\"\"}";
azuqua.updateFlo(flo_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.deleteFlo(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.enableFlo(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.disableFlo(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFloInputs(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFloAccounts(flo_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.moveFloToFolder(flo_id, folder_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"configs\":\"\",\"inputs\":[],\"outputs\":[]}";
azuqua.modifyFlo(flo_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"folder_id\":0}";
azuqua.copyFlo(flo_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"folder_id\":0}";
azuqua.copyFloToOrg(flo_id, org_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readAllFolders(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"name\":\"\",\"description\":\"\"}";
azuqua.createFolder(payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFolder(folder_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"name\":\"\",\"description\":\"\"}";
azuqua.updateFolder(folder_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.deleteFolder(folder_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFolderFlos(folder_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readFolderUsers(folder_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"role\":\"NONE\"}";
azuqua.updateFolderUserPermissions(folder_id, user_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readOrg(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"name\":\"\",\"display_name\":\"\"}";
azuqua.updateOrg(payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readOrgFlos(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readOrgConnectors(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.removeUserFromOrg(user_id, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});


String payload = "{\"role\":\"MEMBER\"}";
azuqua.updateOrgUserPermissions(user_id, payload, new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});



azuqua.readUserOrgs(new AsyncRequest() {
    @Override
    public void onResponse(String response) {
        System.out.println(response);
    }

    @Override
    public void onError(AzuquaError error) {
        System.out.println("Error : " + error.getErrorMessage());
    }
});

```

LICENSE - "MIT License"
=======================
Copyright (c) 2017 Azuqua

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
