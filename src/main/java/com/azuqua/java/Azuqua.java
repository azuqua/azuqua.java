package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.models.AzuquaError;
import com.google.gson.Gson;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashMap;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Azuqua {

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private Gson gson = new Gson();
    private RequestHandler requestHandler;
    private String accessKey;
    private String accessSecret;
    private Routes routes = new Routes();

    public Azuqua(String accessKey, String accessSecret) {
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }

    public Azuqua(String accessKey, String accessSecret, String protocol, String host, int port) {
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
        routes.setProtocol(protocol);
        routes.setHost(host);
        routes.setPort(port);
    }


    public static class InvokeData {
        private String method;
        private String querystring;
        private String body;
        private HashMap<String, String> headers;

        public InvokeData(String method, String queryString, String body, HashMap<String, String> headers) {
            this.method = method;
            this.querystring = queryString;
            this.body = body;
            this.headers = headers;
        }
    }

    public void invoke(String alias, InvokeData data, final AsyncRequest asyncRequest) {
        String endpoint = "/v2/flo/" + alias + "/invoke";
        String timeStamp = getISOTime();
        if (data.querystring != null && data.querystring.length() > 0) {
            endpoint = endpoint + '?' + data.querystring;
        }
        if (data.method.equalsIgnoreCase("get") || data.method.equalsIgnoreCase("delete")) {
            data.body = "";
        }
        String signedData = signData(endpoint, data.method.toUpperCase(), data.body, accessSecret, timeStamp);
        requestHandler = new RequestHandler(routes, endpoint, data.method.toUpperCase(), data.body, timeStamp, signedData, accessKey, data.headers, new AsyncRequest() {
            public void onResponse(String response) {
                asyncRequest.onResponse(response);
            }
            public void onError(AzuquaError azuquaError) {
                asyncRequest.onError(azuquaError);
            }
        });
        requestHandler.execute();
    }


    public void exportFolderToFlopack(java.lang.Integer folder_id,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id/flopack/export";
      String method = "POST";
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFolder(java.lang.Integer folder_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id";
      String method = "GET";
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateFolderUserPermissions(java.lang.Integer folder_id, java.lang.Integer user_id,
        java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id/user/:user_id/permissions";
      String method = "PUT";
      path = path.replace(":folder_id", "" + folder_id);
      path = path.replace(":user_id", "" + user_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void createFolder(java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder";
      String method = "POST";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readAllFolders(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folders";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFolderFlos(java.lang.Integer folder_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id/flos";
      String method = "GET";
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void deleteFolder(java.lang.Integer folder_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id";
      String method = "DELETE";
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateFolder(java.lang.Integer folder_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id";
      String method = "PUT";
      path = path.replace(":folder_id", "" + folder_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFolderUsers(java.lang.Integer folder_id,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/folder/:folder_id/users";
      String method = "GET";
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readConnectorVersion(java.lang.String connector_name,
        java.lang.String connector_version, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/connectors/:connector_name/:connector_version";
      String method = "GET";
      path = path.replace(":connector_name", connector_name);
      path = path.replace(":connector_version", connector_version);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readConnectorAuthScheme(java.lang.String connector_name,
        java.lang.String connector_version, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/connectors/:connector_name/:connector_version/authSchema";
      String method = "GET";
      path = path.replace(":connector_name", connector_name);
      path = path.replace(":connector_version", connector_version);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void importFlopack(java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flopack/import";
      String method = "POST";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void exportFlopack(java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flopack/export";
      String method = "POST";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateOrgUserPermissions(java.lang.Integer user_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/org/user/:user_id/permissions";
      String method = "PUT";
      path = path.replace(":user_id", "" + user_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readOrg(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/org";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readOrgConnectors(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/org/connectors";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readOrgFlos(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/org/flos";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateOrg(java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/org";
      String method = "PUT";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void enableFlo(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/enable";
      String method = "PUT";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFloAccounts(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/accounts";
      String method = "GET";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateFlo(java.lang.Integer flo_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id";
      String method = "PUT";
      path = path.replace(":flo_id", "" + flo_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void copyFlo(java.lang.Integer flo_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/copy";
      String method = "POST";
      path = path.replace(":flo_id", "" + flo_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void deleteFlo(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id";
      String method = "DELETE";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFloInputs(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/inputs";
      String method = "GET";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void moveFloToFolder(java.lang.Integer flo_id, java.lang.Integer folder_id,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/move/folder/:folder_id";
      String method = "PUT";
      path = path.replace(":flo_id", "" + flo_id);
      path = path.replace(":folder_id", "" + folder_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void copyFloToOrg(java.lang.Integer flo_id, java.lang.Integer org_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/copy/org/:org_id";
      String method = "POST";
      path = path.replace(":flo_id", "" + flo_id);
      path = path.replace(":org_id", "" + org_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFloOpenApiSpec(java.lang.String flo_alias,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_alias/openAPISpec";
      String method = "GET";
      path = path.replace(":flo_alias", flo_alias);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void disableFlo(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/disable";
      String method = "PUT";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void modifyFlo(java.lang.Integer flo_id, java.lang.String data,
        final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id/modify";
      String method = "PUT";
      path = path.replace(":flo_id", "" + flo_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readFlo(java.lang.Integer flo_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/flo/:flo_id";
      String method = "GET";
      path = path.replace(":flo_id", "" + flo_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readUserOrgs(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/user/orgs";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void deleteAccount(java.lang.Integer account_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/account/:account_id";
      String method = "DELETE";
      path = path.replace(":account_id", "" + account_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void updateAccountUserPermissions(java.lang.Integer account_id, java.lang.Integer user_id,
        java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/account/:account_id/user/:user_id/permissions";
      String method = "PUT";
      path = path.replace(":account_id", "" + account_id);
      path = path.replace(":user_id", "" + user_id);
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readAccount(java.lang.Integer account_id, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/account/:account_id";
      String method = "GET";
      path = path.replace(":account_id", "" + account_id);
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void createAccount(java.lang.String data, final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/account";
      String method = "POST";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }

    public void readAllAccounts(final AsyncRequest asyncRequest) {
      String timeStamp = getISOTime();
      String path = "/v2/accounts";
      String method = "GET";
      String data = "";
      String signedData = signData(path, method, data, accessSecret, timeStamp);
      requestHandler = new RequestHandler(routes, path, method, data, timeStamp, signedData, accessKey, new AsyncRequest() {
         public void onResponse(String response) {
              asyncRequest.onResponse(response);
         }
         public void onError(AzuquaError azuquaError) {
             asyncRequest.onError(azuquaError);
         }
      });
      requestHandler.execute();
    }



    private String getISOTime() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(new Date());
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String signData(String path, String verb, String data, String accessSecret, String timeStamp) {
        Mac hMac = null;
        SecretKeySpec key = null;
        String meta = verb.toLowerCase() + ":" + path + ":" + timeStamp;
        String dataToDigest = meta + data;
        String hash = null;
        try {
            hMac = Mac.getInstance("HmacSHA256");
            key = new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256");
            hMac.init(key);
            byte[] digest = hMac.doFinal(dataToDigest.getBytes("UTF-8"));
            hash = bytesToHex(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return hash;
    }
}


