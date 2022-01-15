package com.nenasa.Services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nenasa.Home;
import com.nenasa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

    private Context context;
    private JSONObject body;
    private String url;
    private String endpoint;
    private String jsonString;

    public HTTP(Context context) {
        this.context = context;
        url = context.getResources().getString(R.string.server_host);
    }

    public void request(String endpoint, String json_body) {
        try {
            this.endpoint = endpoint;
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            body = new JSONObject(json_body);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+endpoint, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    String requestBody = "";
                    try {
                        // request body goes here
                        //JSONObject jsonBody = new JSONObject();
                        //jsonBody.put("attribute1", "value1");
                        requestBody = body.toString();
                        return requestBody.getBytes("utf-8");
                    } catch (Exception uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    //params.put("id", "oneapp.app.com");
                    //params.put("key", "fgs7902nskagdjs");

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                        try {
                            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            HTTPResponseHandler httpResponseHandler = new HTTPResponseHandler();
                            httpResponseHandler.analyzeResponse(context, endpoint, jsonString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            Log.d("string", stringRequest.toString());
            requestQueue.add(stringRequest);
        } catch (Exception ex){
            Log.e("HTTP Error", ex.toString());
        }
    }
}