package com.cilpl.clusters.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.cilpl.clusters.R;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
        SharedPreferences userdata;
    String authtoken="",userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplash_main);
        userdata = getSharedPreferences("Userdata", MODE_PRIVATE);
        authtoken = userdata.getString("authToken", "");
        userId = userdata.getString("userId", "");
        Log.e("userId",userId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(userId.isEmpty()||userId.equals("")){
                    Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this,HomeMainActivity.class);
                    startActivity(i);
                    finish();
                }
                /* Create an Intent that will start the Menu-Activity. */
                // runNextScreen();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }



}