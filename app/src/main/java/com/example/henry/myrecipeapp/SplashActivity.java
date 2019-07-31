package com.example.henry.myrecipeapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMain = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(toMain);

                finish();
            }
        }, 3000);
    }
}
