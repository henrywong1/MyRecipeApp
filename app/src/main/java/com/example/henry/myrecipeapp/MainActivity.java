package com.example.henry.myrecipeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText searchEditText;

    private static String API_ID = BuildConfig.ApiID;
    private static String API_KEY = BuildConfig.ApiKEY;

    public static ArrayList<String> recipeTitle = new ArrayList<String>();
    public static ArrayList<String> recipeImageURL = new ArrayList<String>();
    public static ArrayList<String> recipeURL = new ArrayList<String>();
    public static ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    public class DownloadImages extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();

                Bitmap myBitmap =  BitmapFactory.decodeStream(in);

                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
                StringBuilder stringBuilder = new StringBuilder();
                String result;
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
            DownloadImages imgDownloader = new DownloadImages();
            try {

                JSONObject jsonObject = new JSONObject(s);
                String hits = jsonObject.getString("hits");
                Log.i("HITS!!!!!!", hits);
                JSONArray arr = new JSONArray(hits);

                JSONObject jsonPart;
                JSONObject jsonLabel;

                for (int i = 0; i < arr.length(); i++){
                    jsonPart = arr.getJSONObject(i);
                    jsonLabel = jsonPart.getJSONObject("recipe");

                    recipeTitle.add(jsonLabel.getString("label"));
                    recipeImageURL.add(jsonLabel.getString("image"));
                    recipeURL.add(jsonLabel.getString("url"));

                    bitmapArray.add(imgDownloader.execute(recipeImageURL.get(i)).get());            // cannot keep on executing call.
                    Log.i("SIZE AS OF NOW!!!!!!!!!!!!!!!!!!!!!!!!!!", Integer.toString(bitmapArray.size()));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            Log.i("BITMAP SIZE", Integer.toString(bitmapArray.size()));

            Intent intent = new Intent(getApplicationContext(), listActivity.class);
            startActivity(intent);



        }
    }

    public void searchRecipe(View view) {
        recipeTitle.clear();
        recipeImageURL.clear();
        recipeURL.clear();
        bitmapArray.clear();
        Log.i("Button","Pressed");
        DownloadTask task = new DownloadTask();
        try {
            task.execute("https://api.edamam.com/search?q=" + searchEditText.getText() + "&app_id=$" + API_ID + "&app_key=$" + API_KEY + "&from=0&to=15&");
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
