package com.nenasa.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nenasa.Home;
import com.nenasa.dyslexia.Read;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPResponseHandler {

    public void analyzeResponse(Context context, String endpoint, String jsonString) throws JSONException {
        if(endpoint == "login"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")){
                    Log.v("111", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("email", jsonObject.getString("email"));
                    sp.setPreference("username", jsonObject.getString("username"));
                    sp.setPreference("isLoggedIn", "true");
                    context.startActivity(new Intent(context, Home.class));
                } else {
                    Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show();
                    Log.v("222", "failed");
                }
            } else {
                Toast.makeText(context, "Login Failed!\nError Occurred!", Toast.LENGTH_SHORT).show();
                Log.v("333", "empty");
            }
        } else if(endpoint == "register"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")){
                    Log.v("111", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("email", jsonObject.getString("email"));
                    sp.setPreference("username", jsonObject.getString("username"));
                    sp.setPreference("isLoggedIn", "true");
                    context.startActivity(new Intent(context, Home.class));
                } else {
                    Toast.makeText(context, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    Log.v("222", "failed");
                }
            } else {
                Toast.makeText(context, "Registration Failed!\nError Occurred!", Toast.LENGTH_SHORT).show();
                Log.v("333", "empty");
            }
        }
    }
}
