package com.nenasa.dyslexia;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.nenasa.R;
import com.nenasa.Services.SharedPreference;
import com.nenasa.dyslexia.networking.ApiConfig;
import com.nenasa.dyslexia.networking.AppConfig;
import com.nenasa.dyslexia.networking.ServerResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class testUpload {

    static String SERVER_PATH = "";

    public void upload(Context context, String filePath){
        SharedPreference sp = new SharedPreference(context);
        SERVER_PATH = sp.getPreference("ServerHost");
        if(SERVER_PATH == null){
            SERVER_PATH = context.getResources().getString(R.string.server_host);
        }

        // Map is used to multipart the file using okhttp3.RequestBody
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(filePath);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
        map.put("function", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "AudioUpload"));
        Log.d("FileUpload", "Before the AppConfig.getRetrofit in testUpload.java");
        ApiConfig getResponse = AppConfig.getRetrofit(SERVER_PATH+"/").create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.upload("token", map);
        Log.d("FileUpload", "Before the call.enqueue in testUpload.java");
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        ServerResponse serverResponse = response.body();
                        Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("FileUpload", "Line 51 in testUpload.java");
                        Log.d("FileUpload", "Res: "+serverResponse.getMessage());
                    }
                } else {
                    Toast.makeText(context, "Problem uploading the file", Toast.LENGTH_SHORT).show();
                    Log.d("FileUpload", "Line 56 in testUpload.java");
                    Log.d("FileUpload", "Problem uploading the file");
                    Log.d("FileUpload", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.v("Response gotten is", t.getMessage());
                Toast.makeText(context, "Problem uploading the file " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("FileUpload", "Line 65 in testUpload.java");
                Log.d("FileUpload", "Problem uploading the file " + t.getMessage());
            }
        });
    }
}
