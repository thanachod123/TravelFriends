package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeGuideActivity extends AppCompatActivity {
    Button mRegister,mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_guide);
        mRegister = findViewById(R.id.btn_register);
        mLogin = findViewById(R.id.btn_login);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeGuideActivity.this,RegisterGuideActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeGuideActivity.this,LoginUserActivity.class));
            }
        });
    }
}
