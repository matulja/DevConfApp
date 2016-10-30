package com.senacor.devconfapp;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;



class EventRestClient {

    private static final String BASE_URL = "http://192.168.2.103:8080/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    static void get(Context context, String url,
                    AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(url), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
