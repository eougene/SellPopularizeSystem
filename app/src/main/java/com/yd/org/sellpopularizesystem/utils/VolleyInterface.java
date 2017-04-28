package com.yd.org.sellpopularizesystem.utils;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by hejin on 2017/3/10.
 */

public abstract class VolleyInterface {
    private Context mContext;
    public static Response.Listener<String> mListener;
    public static Response.ErrorListener mErrorListener;

    public VolleyInterface(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public abstract void onSuccess(String result);

    public abstract void onError(VolleyError error);

    public Response.Listener<String> loadingListener() {
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onSuccess(response);
            }
        };
        return mListener;
    }

    /*public Response.Listener<JSONObject> loadingJsonListener() {
        mListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }

        };
        return mListener;
    }*/

    public Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
        return mErrorListener;
    }
}
