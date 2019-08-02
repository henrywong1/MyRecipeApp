package com.example.henry.myrecipeapp;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText searchEditText;
    ProgressBar progressBar;

    ActivityManager act;

    private static String API_ID = BuildConfig.ApiID;
    private static String API_KEY = BuildConfig.ApiKEY;

    public static ArrayList<String> recipeTitle = new ArrayList<String>();
    public static ArrayList<String> recipeImageURL = new ArrayList<String>();
    public static ArrayList<String> recipeURL = new ArrayList<String>();
    public static ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();
    int resultBegin = 0;
    int resultEnd = 15;




    public class DownloadImage extends AsyncTask<ArrayList<String> , Void, ArrayList<Bitmap>>{
        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>[] urls) {
            HttpURLConnection urlConnection;
            ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < urls[0].size(); i++) {
                    URL imgUrl = new URL(urls[0].get(i).toString());

                    urlConnection = (HttpURLConnection) imgUrl.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();

                    Bitmap myBitmap = BitmapFactory.decodeStream(in);

                    bitmaps.add(myBitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmaps;
        }

    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            button.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

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

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            DownloadImage imgTask = new DownloadImage();
            try {
                bitmapArrayList = imgTask.execute(recipeImageURL).get();

            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.INVISIBLE);
            button.setEnabled(true);
            Intent intent = new Intent(getApplicationContext(), listActivity.class);
            startActivity(intent);



        }
    }

    public void searchRecipe(View view) {

        Log.i("Button","Pressed");
        search(resultBegin,resultEnd);

    }

    public void search(int start, int end) {
        DownloadTask task = new DownloadTask();
        try{
            task.execute("https://api.edamam.com/search?q=" + searchEditText.getText() + "&app_id=$" + API_ID + "&app_key=$" + API_KEY + "&from=" + start + "&to=" + end +"&");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.searchButton);
        searchEditText = findViewById(R.id.searchEditText);
        progressBar = findViewById(R.id.progressBar);

        recipeTitle.clear();
        recipeImageURL.clear();
        recipeURL.clear();
        bitmapArrayList.clear();



        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchRecipe(button);
                    return true;
                }
                return false;
            }
        });



    }
}