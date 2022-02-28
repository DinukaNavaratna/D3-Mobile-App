package com.nenasa;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

public class Nenasa extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
    }

    public static Context getAppContext() {
        return Nenasa.context;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showDialogBox(Context context, String type, String title, String subtitle){
        if(type.equals("info")){
            new iOSDialogBuilder(context)
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setBoldPositiveLabel(true)
                    .setCancelable(false)
                    .setPositiveListener("Got it",new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        } else if(type.equals("error")){
            new iOSDialogBuilder(context)
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setCancelable(false)
                    .setPositiveListener("Opzzz", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        }

    }
}
