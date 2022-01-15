package com.nenasa.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.nenasa.Home;
import com.nenasa.Nenasa;
import com.nenasa.R;
import com.nenasa.dyslexia.Read;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPResponseHandler {

    public void analyzeResponse(Context context, Activity activity, String endpoint, String jsonString) throws JSONException {
        if(endpoint == "/"){
            Log.v("Server_Health", jsonString);
            showResponse(context, activity, "info", "Server Health", jsonString);
        } else if(endpoint == "/login"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")){
                    Log.v("http_response_login", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("user_id", jsonObject.getString("user_id"));
                    sp.setPreference("email", jsonObject.getString("email"));
                    sp.setPreference("username", jsonObject.getString("username"));
                    sp.setPreference("isLoggedIn", "true");
                    //showResponse(context, activity, "error", "Login Successful!", "Thank you for being a loyal user...");
                    context.startActivity(new Intent(context, Home.class));
                } else {
                    Log.v("http_response_login", "failed");
                    showResponse(context, activity, "error", "Login Failed!", jsonObject.getString("response"));
                }
            } else {
                Log.v("http_response_login", "empty");
                showResponse(context, activity, "error", "Login Failed!", "Error Occurred!");
            }
        } else if(endpoint == "/register"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")){
                    Log.v("http_response_register", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("user_id", jsonObject.getString("user_id"));
                    sp.setPreference("email", jsonObject.getString("email"));
                    sp.setPreference("username", jsonObject.getString("username"));
                    sp.setPreference("isLoggedIn", "true");
                    //showResponse(context, activity, "error", "Registration Successful!", "Warmly welcome you to our loyal user group...");
                    context.startActivity(new Intent(context, Home.class));
                } else {
                    Log.v("http_response_register", "failed");
                    showResponse(context, activity, "error", "Registration Failed!", jsonObject.getString("response"));
                }
            } else {
                Log.v("http_response_register", "empty");
                showResponse(context, activity, "error", "Registration Failed!", "Error Occurred!");
            }
        }
    }

    private void showResponse(Context context, Activity activity, String type, String topic, String message){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Nenasa nenasa = new Nenasa();
                nenasa.showDialogBox(context, type, topic, message);
            }
        });
    }
}
