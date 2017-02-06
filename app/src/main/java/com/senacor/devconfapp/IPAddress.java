package com.senacor.devconfapp;

/**
 * Created by saba on 31.10.16.
 */

public class IPAddress {



    public static String IPevent = "http://192.168.2.104:8080/events";
    public static String IPuser = "http://192.168.2.104:8081/user";
    public static String IPrating = "http://192.168.2.104:8082/rating";


    public static void setIPevent(String IPevent) {
        IPAddress.IPevent = "http://" + IPevent + ":8080/events";
    }


    public static void setIPuser(String IPuser) {
        IPAddress.IPuser = "http://" + IPuser + ":8081/user";
    }

    public static void setIPrating(String IPrating) {
        IPAddress.IPrating = "http://" + IPrating + ":8082/rating";
    }
}
