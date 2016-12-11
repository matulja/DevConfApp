package com.senacor.devconfapp.clients;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by saba on 29.10.16.
 */

public class RestClient {


    //private static final String BASE_URL = "http://"+ IPAddress.IP + ":8080/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String tokenId;


    public static void get(Context context, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        System.out.println("in get method");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        addHeaders(sharedPref);
        client.get(context, url, params, responseHandler);
        client.setConnectTimeout(30000);

    }


    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("in post method");
        System.out.println(params.toString());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        addHeaders(sharedPref);
        client.post(context, url, params, responseHandler);
        client.setConnectTimeout(30000);
    }

    public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("in put method");
        System.out.println(params.toString());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        addHeaders(sharedPref);
        client.put(context, url, params, responseHandler);
        client.setConnectTimeout(30000);
    }

    public static void delete(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        System.out.println("in delete method");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        addHeaders(sharedPref);
        client.delete(context, url, responseHandler);
        client.setConnectTimeout(30000);

    }
    public static void addHeaders(SharedPreferences sharedPref){
        String tokenId = sharedPref.getString("tokenId", "tokenId");
        client.addHeader("Authorization", tokenId);
        client.addHeader("Accept", "application/json");

    }


}