package com.example.henry.myrecipeapp;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class listActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar loadRecipes;

    MainActivity main = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);

        final CustomAdapter customAdapter = new CustomAdapter();

        loadRecipes = findViewById(R.id.loadMoreProgressBar);


        listView.setAdapter(customAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), webActivity.class);
                intent.putExtra("url", MainActivity.recipeURL.get(i));
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount != 0) {
                    // add items when reached end of list. Change result begin and end.
                    // Implement Download Task within ListActivity, with image downloader? but with updated search results, then update listview.
                    Log.i("LOADING", "MORE RESULTS!!!!!!!!!!!!");
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            MainActivity.bitmapArrayList.clear();
            MainActivity.recipeImageURL.clear();
            MainActivity.recipeURL.clear();
            MainActivity.recipeTitle.clear();
        }

        return super.onKeyDown(keyCode, event);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MainActivity.bitmapArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.customlistlayout, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView textView = view.findViewById(R.id.textView);



            imageView.setImageBitmap(MainActivity.bitmapArrayList.get(i));
            textView.setText(MainActivity.recipeTitle.get(i));


            return view;
        }
    }



}