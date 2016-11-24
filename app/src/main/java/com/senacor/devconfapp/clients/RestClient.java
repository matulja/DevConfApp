package com.senacor.devconfapp.clients;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 29.10.16.
 */

public class RestClient {

   //private static final String BASE_URL = "http://"+ IPAddress.IP + ":8080/";

    private static AsyncHttpClient client = new AsyncHttpClient();



    public static void get(Context context, String url, Header[] headers, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        System.out.println("in get method");
        client.get(context, url, headers, params, responseHandler);
        client.setConnectTimeout(30000);

    }


    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("in post method");
        client.post(context, url, params, responseHandler);
        client.setConnectTimeout(30000);
    }


   /* private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }*/
}
