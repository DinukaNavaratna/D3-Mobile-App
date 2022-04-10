package com.nenasa.dyslexia.networking;

/**
 * Created by delaroystudios on 10/5/2016.
 */
import android.util.Log;

import com.nenasa.R;
import com.nenasa.Services.SharedPreference;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppConfig {

    public static String BASE_URL = "";

    public static Retrofit getRetrofit(String SERVER_PATH) {

        BASE_URL = SERVER_PATH;
        Log.d("FileUpload", "Line 22 AppConfig.java");
        Log.d("FileUpload", BASE_URL);

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
