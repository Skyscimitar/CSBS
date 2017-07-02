package com.csb.csb_test_webview;

/**
 * Created by Danny on 05/06/2017.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.csb.csb_test_webview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Datacollector extends AsyncTask<Void, Void, String> {
    private Exception exception;
    private  String search;
    private String type;
    private String response;
    private Activity activity;
    private ArrayList<Article> article_data = new ArrayList<>();
    public Datacollector(String type, Activity activity,String search){this.type=type;this.activity= activity; this.search=search;}

    public String getResponse() {
        return response;
    }

    public ArrayList<Article> getArticle_data() {
        return article_data;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {
        // Do some validation here

        try {
            URL url = new URL("http://nicolasfley.fr:7898/items/description?desc="+search+type);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
        if (response == null) {
            response = "THERE WAS AN ERROR";
        }else
        {
            this.response = response;
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tel= jsonObject.getString("phoneNumber");
                    String name = jsonObject.getString("lName");
                    String surname = jsonObject.getString("fName");
                    String price = jsonObject.getString("price");
                    String nom = jsonObject.getString("description");
                    Article article = new Article(nom,price, tel, name, surname );
                    article_data.add(article);
                    Log.i("lol",article.toString());

                    List<Article> articles = article_data;
                    Article article_dat[] = new Article[articles.size()];
                    article_dat = articles.toArray(article_dat);
                    final Article article_data[] = article_dat;
                    ArticleAdapter offerAdapter = new ArticleAdapter(activity, R.layout.offer_view,article_data);
                    final ListView listView = (ListView) activity.findViewById(android.R.id.list);
                    listView.setAdapter(offerAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            comunicate cm;
                            cm = (comunicate) activity;
                            cm.sendData(article_data[position]);
                        }
                    });

                }


            }catch(JSONException e){

            }


        }




    }
}
