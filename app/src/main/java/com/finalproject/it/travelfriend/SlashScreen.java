package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SlashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_screen);

        int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent splashIntent = new Intent(SlashScreen.this, HomeActivity.class);
                SlashScreen.this.startActivity(splashIntent);
                SlashScreen.this.finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
