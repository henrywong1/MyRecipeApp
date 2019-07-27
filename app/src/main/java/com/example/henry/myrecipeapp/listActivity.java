package com.example.henry.myrecipeapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class listActivity extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);
        //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MainActivity.recipeTitle);

        CustomAdapter customAdapter = new CustomAdapter();



        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), webActivity.class);
                intent.putExtra("url", MainActivity.recipeURL.get(i));
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MainActivity.bitmapArray.size();
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
            TextView textView = view.findViewById(R.id.infoTextView);

            imageView.setImageBitmap(MainActivity.bitmapArray.get(i));
            textView.setText(MainActivity.recipeTitle.get(i));
            return view;
        }
    }



}
