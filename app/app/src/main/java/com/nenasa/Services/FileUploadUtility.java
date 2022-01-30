package com.nenasa.Services;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.nenasa.R;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUploadUtility {

    static String SERVER_PATH = "http://192.168.8.152//audio.php";
    static Context context;

    public FileUploadUtility(Context context){
        this.context = context;
        /*
        SharedPreference sp = new SharedPreference(context);
        SERVER_PATH = sp.getPreference("ServerHost");
        if(SERVER_PATH == null){
            SERVER_PATH = context.getResources().getString(R.string.server_host);
        }
         */
    }

    public void doFileUpload(final String selectedPath) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "AaB03x87yxdkjnxvi7";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String responseFromServer = "";
                try {
                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(selectedPath));
                    // open a URL connection to the Servlet
                    URL url = new URL(SERVER_PATH+"/upload_audio");
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");

                    conn.setRequestProperty("Content-Type", "multipart/form-data;");

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; function=sentence; name=\"uploadedfile\"; filename=\""
                            + selectedPath + "\"" + lineEnd);

                    /*
                    conn.setRequestProperty("Content-Type", "audio/m4a");

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; filename=\"" + selectedPath + "\"" + lineEnd); // after form-data; --> name="" + fileParameterName + "";
                    dos.writeBytes("Content-Type: audio/m4a" + lineEnd);
                    */

                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams

                    Log.e("Debug", "File is written - "+selectedPath);


                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is: "
                            + serverResponseMessage + ": " + serverResponseCode);

                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {
                    Log.e("Debug", "error MalformedURLException: " + ex.getMessage(), ex);
                    sendMessageBack(responseFromServer, 0);
                    return;
                } catch (IOException ioe) {
                    Log.e("Debug", "error IOException: " + ioe.getMessage(), ioe);
                    sendMessageBack(responseFromServer, 0);
                    return;
                }
                responseFromServer = processResponse(conn, responseFromServer);
                sendMessageBack(responseFromServer, 1);
            }
        }).start();
    }

    private static String processResponse(HttpURLConnection conn, String responseFromServer) {
        DataInputStream inStream;
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;

            while ((str = inStream.readLine()) != null) {
                responseFromServer = str;
            }
            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return responseFromServer;
    }

    static void sendMessageBack(String responseFromServer, int success) {
        Log.i("File", responseFromServer+" | "+responseFromServer.toString()+" | "+Integer.toString(success));
        Message message = new Message();
        message.obj = responseFromServer;
        message.arg1 = success;
        SharedPreference sp = new SharedPreference(context);
        sp.setPreference("audio_upload_response", responseFromServer.toString());
    }


    public void upload(String fileurl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(fileurl);
                    URL url = new URL(SERVER_PATH + "/upload_audio");
                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    DataInputStream dis = null;
                    FileInputStream fileInputStream = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "AaB03x87yxdkjnxvi7";

                    byte[] buffer;
                    int maxBufferSize = 20 * 1024;
                    try {
                        //------------------ CLIENT REQUEST
                        fileInputStream = new FileInputStream(file);

                        // open a URL connection to the Servlet
                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        // Allow Inputs
                        conn.setDoInput(true);
                        // Allow Outputs
                        conn.setDoOutput(true);
                        // Don't use a cached copy.
                        conn.setUseCaches(false);
                        // Use a post method.
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "audio/m4a");

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; filename=\"" + file.toString() + "\"" + lineEnd); // after form-data; --> name="" + fileParameterName + "";
                        dos.writeBytes("Content-Type: audio/m4a" + lineEnd);
                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
                        int length;
                        // read file and write it into form...
                        while ((length = fileInputStream.read(buffer)) != -1) {
                            dos.write(buffer, 0, length);
                        }

                        //for (String name : parameters.keySet()) {
                        //    dos.writeBytes(lineEnd);
                        //    dos.writeBytes(twoHyphens + boundary + lineEnd);
                        //    dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd);
                        //    dos.writeBytes(lineEnd);
                        //    dos.writeBytes(parameters.get(name));
                        //}

                        // send multipart form data necessary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        dos.flush();
                    } finally {
                        if (fileInputStream != null) fileInputStream.close();
                        if (dos != null) dos.close();
                    }

                    //------------------ read the SERVER RESPONSE
                    try {
                        dis = new DataInputStream(conn.getInputStream());
                        StringBuilder response = new StringBuilder();
                        Log.i("Response", response.toString());
                    } finally {
                        if (dis != null) dis.close();
                    }
                } catch (Exception ex) {

                }
            }
        });
    }
}
