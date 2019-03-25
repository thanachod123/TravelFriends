package com.finalproject.it.travelfriend.Guide;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.RegisterGuideActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;


public class ChangePasswordGuide extends AppCompatActivity {
    EditText mCurrentPassword, mNewPassword, mPassword;
    Button btnChangePassword,btnCancel;
    String guidePassword,guideId;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_guide);
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
        StatusBarUtil.setTransparent(ChangePasswordGuide.this);

        mCurrentPassword = findViewById(R.id.edt_CurrentPassword);
        mNewPassword = findViewById(R.id.edt_NewPassword);
        mPassword = findViewById(R.id.edt_Password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnCancel = findViewById(R.id.btn_cancel);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        guidePassword = getIntent().getExtras().getString("GuidePassword");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setFont();
    }

    private boolean changePassword() {
        final String currentPassword,newPassword,password;

        currentPassword =  mCurrentPassword.getText().toString();
        newPassword = mNewPassword.getText().toString();
        password = mPassword.getText().toString();

        if (currentPassword.trim().isEmpty()){
            Toast.makeText(this, "กรุณากรอกรหัสผ่านปัจจุบัน", Toast.LENGTH_SHORT).show();
            mCurrentPassword.requestFocus();
            return false;
        }
        if (!currentPassword.equals(guidePassword)){
            Toast.makeText(this, "รหัสผ่านปัจจุบันไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            mCurrentPassword.requestFocus();
            return false;
        }
        if (newPassword.trim().isEmpty()){
            Toast.makeText(this, "กรุณากรอกรหัสผ่านใหม่ของคุณ", Toast.LENGTH_SHORT).show();
            mNewPassword.requestFocus();
            return false;
        }
        if (password.trim().isEmpty()){
            Toast.makeText(this, "กรุณากรอกรหัสผ่านใหม่ของคุณ", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            return false;
        }
        if (!newPassword.equals(password)){
            Toast.makeText(this, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            return false;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    mReference.child(getString(R.string.users)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            guideId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mReference.child(getString(R.string.users)).child(guideId).child("password").setValue(password);
                            finish();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(ChangePasswordGuide.this, "เปลี่ยนรหัสผ่านเสร็จแล้ว", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordGuide.this, "ไม่สมารถเปลี่ยนรหัสผ่านได้", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;
    }

    private void setFont() {
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        mCurrentPassword.setTypeface(myCustomFont);
        mNewPassword.setTypeface(myCustomFont);
        mPassword.setTypeface(myCustomFont);
        btnChangePassword.setTypeface(myCustomFont);
        btnCancel.setTypeface(myCustomFont);
    }
}
