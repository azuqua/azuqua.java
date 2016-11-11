package com.azuqua.java;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SASi on 14-Jul-16.
 */
class Routes {

    static String PROTOCOL = "https";
    static String HOST = "api.azuqua.com";
    static int PORT = 443;

    static final String METHOD_POST = "POST";
    static final String METHOD_GET = "GET";

    static final String ORG_FLOS = "/org/flos?org_id=:org_id&channel_key=predixedge";

    static final String FLO_INVOKE = "/flo/:alias/invoke";
    static final String FLO_INPUTS = "/flo/:alias/inputs";
    static final String FLO_OUTPUTS = "/flo/:alias/outputs";


    Routes() {
        // empty constructor
    }

    Routes(String protocol, String host, int port) {
        Routes.PROTOCOL = protocol;
        Routes.HOST = host;
        Routes.PORT = port;
    }

}
