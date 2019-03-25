package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jgabrielfreitas.core.BlurImageView;

public class HomeActivity extends AppCompatActivity {

    Button mGuide,mTourist;
    BlurImageView blurImageView;
    TextView text_welcome1;
    TextView text_welcome2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGuide = findViewById(R.id.btn_guide);
        mTourist = findViewById(R.id.btn_register);
        blurImageView = findViewById(R.id.BlurImageView);
        text_welcome1 = findViewById(R.id.text_welcome1);
        text_welcome2 = findViewById(R.id.text_welcome2);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        text_welcome1.setTypeface(myCustomFont);
        text_welcome2.setTypeface(myCustomFont);
        mGuide.setTypeface(myCustomFont);
        mTourist.setTypeface(myCustomFont);

        blurImageView.setBlur(5);


        mGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,HomeGuideActivity.class));
            }
        });

        mTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,HomeUserActivity.class));
            }
        });
    }
}
