package com.finalproject.it.travelfriend.User;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.it.travelfriend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaeger.library.StatusBarUtil;

public class ForgotPasswordUser extends AppCompatActivity {

    EditText mEmail;
    Button btnChangePassword, btnCancel;
    String userPassword, guideId;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_user);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_app_bar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        StatusBarUtil.setTransparent(this);

        mEmail = findViewById(R.id.edt_EmailResetpasswor);
        btnChangePassword = findViewById(R.id.btn_change_password_user);

        btnCancel = findViewById(R.id.btn_cancel_user);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        userPassword = getIntent().getExtras().getString("Userpassword");
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setFont();


    }

    private boolean changePassword() {

        mAuth.sendPasswordResetEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordUser.this , "Password sent to email " , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ForgotPasswordUser.this , task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;

    }


    private void setFont() {

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/FC Lamoon Bold ver 1.00.ttf");
        mEmail.setTypeface(myCustomFont);

        btnChangePassword.setTypeface(myCustomFont);
        btnCancel.setTypeface(myCustomFont);

    }

}

