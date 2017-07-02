package com.csb.csb_test_webview;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Danny on 07/06/2017.
 */

public class DataSender extends AsyncTask<Void, Void, String> {
    private Exception exception;
    private String description;
    private String price;
    private String type;
    private String token;

    public DataSender(String description, String price, String type, String token){
        this.description = description;
        this.price = price;
        this.type = type;
        this.token = token;
    }



    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {
        // Do some validation here

        try {
            URL url = new URL("http://nicolasfley.fr:7898/items?description="+description+"&token="+token+"&prix="+price+"&"+type);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("info",urlConnection.toString());
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
    }
}
