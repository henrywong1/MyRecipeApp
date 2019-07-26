package com.example.henry.myrecipeapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText searchEditText;

    private static String API_ID = BuildConfig.ApiID;
    private static String API_KEY = BuildConfig.ApiKEY;


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
                StringBuilder stringBuilder = new StringBuilder();
                String result = " ";
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    int data = reader.read();

                    while (data != -1) {
                        char curr  = (char) data;
                        stringBuilder.append(curr);
                        data = reader.read();
                    }
                    result = stringBuilder.toString();
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                String hits = jsonObject.getString("hits");

                JSONArray arr = new JSONArray(hits);
                JSONObject jsonPart;
                JSONObject jsonLabel;
                for (int i = 0; i < 10; i++){
                    jsonPart = arr.getJSONObject(i);
                    jsonLabel = jsonPart.getJSONObject("recipe");
                    Log.i("Title", jsonLabel.getString("label"));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void searchRecipe(View view) {
        Log.i("Button","Pressed");
        DownloadTask task = new DownloadTask();
        try {
            task.execute("https://api.edamam.com/search?q=" + searchEditText.getText() + "&app_id=$" + API_ID + "&app_key=$" + API_KEY);
        }catch (Exception e) {
            Toast.makeText(this,"Fail", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.searchButton);
        searchEditText = findViewById(R.id.searchEditText);


    }
}
