package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jgabrielfreitas.core.BlurImageView;

public class HomeGuideActivity extends AppCompatActivity {
    Button mRegister,mLogin;
    TextView txt_guide1,txt_guide2;
    BlurImageView blurImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_guide);
        blurImageView = findViewById(R.id.BlurImageView);
        mRegister = findViewById(R.id.btn_register);
        mLogin = findViewById(R.id.btn_login);
        txt_guide1 = findViewById(R.id.text_guide1);
        txt_guide2 = findViewById(R.id.text_tourist2);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        txt_guide1.setTypeface(myCustomFont);
        txt_guide2.setTypeface(myCustomFont);
        mRegister.setTypeface(myCustomFont);
        mLogin.setTypeface(myCustomFont);

        blurImageView.setBlur(5);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeGuideActivity.this,RegisterGuideActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeGuideActivity.this,LoginGuideActivity.class));
            }
        });
    }
}
