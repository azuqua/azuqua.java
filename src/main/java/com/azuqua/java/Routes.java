package com.azuqua.java;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Routes {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    public static final String BASE = "https://api.azuqua.com";

    public static final String ORG_LOGIN = "/login";
    public static final String ORG_FLOS = "/org/flos";

    public static final String FLO_READ = "/flo/:alias/read";
    public static final String FLO_INJECT = "/flo/:alias/inject";
    public static final String FLO_INVOKE = "/flo/:alias/invoke";

}
