package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginGuideActivity extends AppCompatActivity {
    private static final String TAG = "LoginUserActivity";

    //    FirebaseVariables
    FirebaseAuth mAuth;
    DatabaseReference mReference;


    //    Email&Password Variables
    Button mLogin;
    EditText mEmail,mPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_login);
        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Email
        mLogin = findViewById(R.id.btn_login);
        mEmail = findViewById(R.id.edt_Email);
        mPassword = findViewById(R.id.edt_Password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignIn();
            }
        });
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        mLogin.setTypeface(myCustomFont);


    }


    /**
     *
     * Email&Password
     *
     * **/

    private void emailSignIn() {
        String strEmail,strPassword;
        strEmail = mEmail.getText().toString();
        strPassword = mPassword.getText().toString();


        if (strEmail.equals("") || strPassword.equals("")){
            Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();

        }
        else {
            mAuth.signInWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String RegisteredUserID = currentUser.getUid();
                        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
                        mReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userType = dataSnapshot.child("role").getValue().toString();
                                if (userType.equals("tourist")){
                                    Intent intentTourist = new Intent(LoginGuideActivity.this,MainUserActivity.class);
                                    intentTourist.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentTourist);
                                    Toast.makeText(LoginGuideActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (userType.equals("guide")){
                                    Intent intentGuide = new Intent(LoginGuideActivity.this,MainGuideActivity.class);
                                    intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentGuide);
                                    Toast.makeText(LoginGuideActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(LoginGuideActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginGuideActivity.this, "Please enter Correct email \n and Password", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
    }


    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.finalproject.it.travelfriend",PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH",Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

