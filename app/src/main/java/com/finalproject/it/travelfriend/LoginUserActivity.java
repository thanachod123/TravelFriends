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
import com.finalproject.it.travelfriend.Model.UserDataSocial;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginUserActivity extends AppCompatActivity {
    private static final String TAG = "LoginUserActivity";

//    FirebaseVariables
    FirebaseAuth mAuth;
    DatabaseReference mReference;

//    GoogleVariables
    SignInButton gSignInButton;
    GoogleApiClient mGoogleApiClient;

//    FacebookVariables
    LoginButton fSignInButton;
    CallbackManager mCallbackManager;

//    Email&Password Variables
    Button mLogin;
    EditText mEmail,mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Facebook
        setupFacebook();

        //Email
        mLogin = findViewById(R.id.btn_register);
        mEmail = findViewById(R.id.edt_Email);
        mPassword = findViewById(R.id.edt_Password);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignIn();
            }
        });
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
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String RegisteredUserID = currentUser.getUid();
                        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
                        mReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userType = dataSnapshot.child("role").getValue().toString();
                                if (userType.equals("tourist")){
                                    Intent intentGuide = new Intent(LoginUserActivity.this, MainUserActivity.class);
                                    intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentGuide);
                                } else if (userType.equals("guide")) {
                                    String guideStatus = dataSnapshot.child("status_allow").getValue().toString();
                                    if (guideStatus.equals("true")){
                                        Intent intentGuide = new Intent(LoginUserActivity.this, MainGuideActivity.class);
                                        intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentGuide);
                                    }
                                } else if (userType.equals("guide")) {
                                    String guideStatus = dataSnapshot.child("status_allow").getValue().toString();
                                    if (guideStatus.equals("false")){
                                        mAuth.signOut();
                                        Toast.makeText(LoginUserActivity.this, "กรุณารอการตรวจสอบข้อมูลจากผู้ดูแลระบบ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Toast.makeText(LoginUserActivity.this, "Please enter Correct email \n and Password", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    /**
     *
     *  Facebook
     *
     * **/
    private void setupFacebook() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        fSignInButton = findViewById(R.id.facebook_sign_in);
        fSignInButton.setReadPermissions("email", "public_profile");
        fSignInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean newUserFacebook = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (newUserFacebook) {
                                FirebaseUser firebaseUserFacebook = mAuth.getCurrentUser();
                                UserDataSocial userData = new UserDataSocial(firebaseUserFacebook.getEmail(), firebaseUserFacebook.getDisplayName(), "", "ชาย", "", "", "", "", "","tourist");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserFacebook.getUid()).setValue(userData);
                                startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                            } else {
                                startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                            }
                        }
                    }
                });
    }

    /**
     *
     *  Google
     *
     * **/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()){
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            }else
//            {
//                Log.w(TAG,"Google sign in failed");
//            }
//
//        }
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

