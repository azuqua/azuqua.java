package com.azuqua.java;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Routes {

    private String protocol = "https";
    private String host = "api.azuqua.com";
    private int port = 443;

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    //public static String BASE = "https://api.azuqua.com";

    public static final String ORG_LOGIN = "/login";
    public static final String ORG_FLOS = "/org/flos?type=mobile";

    public static final String FLO_READ = "/flo/:alias/read";
    public static final String FLO_INJECT = "/flo/:alias/inject";
    public static final String FLO_INVOKE = "/flo/:alias/invoke";

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
