package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;

public class HomeActivity extends AppCompatActivity {

    //    FirebaseVariables
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    Button mGuide,mTourist;
    BlurImageView blurImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGuide = findViewById(R.id.btn_guide);
        mTourist = findViewById(R.id.btn_register);
        blurImageView = findViewById(R.id.BlurImageView);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        checkSession();

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

    private void checkSession() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            final String currentUserId = currentUser.getUid();
            mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.child("role").getValue().toString();
                    if (userType.equals("tourist")) {
                        Intent intentTourist = new Intent(HomeActivity.this, MainUserActivity.class);
                        intentTourist.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentTourist);

                    } else if (userType.equals("guide")) {
                            Intent intentGuide = new Intent(HomeActivity.this, MainGuideActivity.class);
                            intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentGuide);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }
}
