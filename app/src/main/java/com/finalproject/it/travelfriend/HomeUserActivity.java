package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jgabrielfreitas.core.BlurImageView;

public class HomeUserActivity extends AppCompatActivity {

    Button mRegister,mLogin;
    TextView txt_tourist1,txt_tourist2;
    BlurImageView blurImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        blurImageView = findViewById(R.id.BlurImageView);
        mRegister = findViewById(R.id.btn_register);
        mLogin = findViewById(R.id.btn_login);
        txt_tourist1 = findViewById(R.id.text_tourist1);
        txt_tourist2 = findViewById(R.id.text_tourist2);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        txt_tourist1.setTypeface(myCustomFont);
        txt_tourist2.setTypeface(myCustomFont);
        mRegister.setTypeface(myCustomFont);
        mLogin.setTypeface(myCustomFont);

        blurImageView.setBlur(5);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeUserActivity.this,RegisterUserActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeUserActivity.this,LoginUserActivity.class));
            }
        });
    }
}
