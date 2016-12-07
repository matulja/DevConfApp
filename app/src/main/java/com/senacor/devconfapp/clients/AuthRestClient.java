package com.senacor.devconfapp.clients;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;

/**
 * Created by saba on 02.12.16.
 */

public class AuthRestClient {

    private static final String BASE_URL = IPAddress.IPuser;

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(context, url, params, responseHandler);
        client.setConnectTimeout(30000);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
