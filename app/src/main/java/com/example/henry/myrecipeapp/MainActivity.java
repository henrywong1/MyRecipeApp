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

    private static String API_ID = BuildConfig.ApiID;
    private static String API_KEY = BuildConfig.ApiKEY;

    String word;
    public static ArrayList<Recipe> recipes = new ArrayList<Recipe>();
  
    int resultBegin = 0;
    int resultEnd = 15;

    public class DownloadImage extends AsyncTask<ArrayList<String>, Void, Void>{
        @Override
        protected Void doInBackground(ArrayList<String>[] urls) {
            HttpURLConnection urlConnection;
            try {
                for (int i = 0; i < urls[0].size(); i++) {
                    URL imgUrl = new URL(urls[0].get(i));

                    urlConnection = (HttpURLConnection) imgUrl.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();

                    Bitmap myBitmap = BitmapFactory.decodeStream(in);

                    recipes.get(i).setRecipeImage(myBitmap);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
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


                for (int i = resultBegin; i < resultEnd; i++){
                    jsonPart = arr.getJSONObject(i);
                    jsonLabel = jsonPart.getJSONObject("recipe");

                    String title = jsonLabel.getString("label");
                    String imgUrl = jsonLabel.getString("image");
                    String recipeUrl = jsonLabel.getString("url");
                    Recipe info = new Recipe(title, imgUrl, recipeUrl );

                    recipes.add(info);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < recipes.size(); i++){
                Log.i("Recipe title at ",  Integer.toString(i) + " = " + recipes.get(i).getRecipeTitle());
                Log.i("Recipe Image URL at " , Integer.toString(i) + " = " + recipes.get(i).getRecipeImageURL());
                Log.i("Recipe URL at " , Integer.toString(i) + " = " + recipes.get(i).getRecipeURL());
            }

            DownloadImage imgTask = new DownloadImage();
            ArrayList<String> tempImgUrl = new ArrayList<String>();
            for (int i = 0; i < recipes.size(); i++){
                 tempImgUrl.add(recipes.get(i).getRecipeImageURL());
            }
            try {
                imgTask.execute(tempImgUrl).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(getApplicationContext(), listActivity.class);
            startActivity(intent);

            button.setEnabled(true);

        }
    }



    public void searchRecipe(View view) {

        Log.i("Button","Pressed");
        word = searchEditText.getText().toString();
        search(resultBegin,resultEnd, word);
        button.setEnabled(false);

    }

    public void search(int start, int end, String word) {
        DownloadTask task = new DownloadTask();
        try{
            task.execute("https://api.edamam.com/search?q=" + word + "&app_id=$" + API_ID + "&app_key=$" + API_KEY + "&from=" + start + "&to=" + end +"&");
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

        searchEditText.setText("");
        button.setEnabled(true);
        recipes.clear();


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