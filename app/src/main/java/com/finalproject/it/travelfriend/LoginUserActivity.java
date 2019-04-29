package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.finalproject.it.travelfriend.Model.UserDataSocial;
import com.finalproject.it.travelfriend.User.ForgotPasswordUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaeger.library.StatusBarUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginUserActivity extends AppCompatActivity {
    private static final String TAG = "LoginUserActivity";
    private static final int RC_SIGN_IN = 9001;

    Toolbar toolbar;
    //    FirebaseVariables
    FirebaseAuth mAuth;
    DatabaseReference mReference, Userref;

    //    GoogleVariables
    SignInButton mGooglebtn;
    Button gSignInButton;
    GoogleApiClient mGoogleApiClient;

    //    FacebookVariables
    Button fSignInButton;
    CallbackManager mCallbackManager;

    //    Email&Password Variables
    Button mLogin;
    EditText mEmail, mPassword;
    TextView mforgot;
    LoginButton mLoginFb;

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));
        loading = findViewById(R.id.loadloginuser);

        loading.setVisibility(View.GONE);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Userref = FirebaseDatabase.getInstance().getReference().child("Users");

        //google
        signinGSO();
        mGooglebtn  = findViewById(R.id.mGoogle);
        gSignInButton = findViewById(R.id.google_sign_in);
        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGooglebtn.performClick();
                google_sign_in_process();

            }
        });



        //Facebook
        fSignInButton = findViewById(R.id.facebook_sign_in);
        mLoginFb = findViewById(R.id.buttonFacebookMain);
        fSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFacebook();
            }
        });


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


        mforgot = findViewById(R.id.forgotpassword);

        mforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser = new Intent(LoginUserActivity.this, ForgotPasswordUser.class);
                startActivity(intentUser);



            }
        });


    }

    /**
     * Email&Password
     **/

    private void emailSignIn() {
        String strEmail, strPassword;
        strEmail = mEmail.getText().toString();
        strPassword = mPassword.getText().toString();

        if (strEmail.equals("") || strPassword.equals("")) {
            Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        loading.setVisibility(View.VISIBLE);
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        String devicetoken = FirebaseInstanceId.getInstance().getToken();

                        Userref.child(currentUserId).child("device_token").setValue(devicetoken).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String RegisteredUserID = currentUser.getUid();
                                    mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
                                    mReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String userType = dataSnapshot.child("role").getValue().toString();
                                            if (userType.equals("tourist")) {
                                                Intent intentGuide = new Intent(LoginUserActivity.this, MainUserActivity.class);
                                                intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intentGuide);
                                            } else if (userType.equals("guide")) {
                                                String guideStatus = dataSnapshot.child("status_allow").getValue().toString();
                                                if (guideStatus.equals("true")) {
                                                    Intent intentGuide = new Intent(LoginUserActivity.this, MainGuideActivity.class);
                                                    intentGuide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentGuide);
                                                }
                                            } else if (userType.equals("guide")) {
                                                String guideStatus = dataSnapshot.child("status_allow").getValue().toString();
                                                if (guideStatus.equals("false")) {
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
            });
        }
    }


    /**
     * Facebook
     **/
    private void setupFacebook() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        fSignInButton = findViewById(R.id.facebook_sign_in);

        fSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginFb.performClick();
            }
        });

        mLoginFb.setReadPermissions(Arrays.asList("email", "public_profile"));
//        LoginManager.getInstance().logInWithReadPermissions(LoginUserActivity.this,
//                Arrays.asList("email", "public_profile"));

        mLoginFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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
                            final String currentUserId = mAuth.getCurrentUser().getUid();
                            String devicetoken = FirebaseInstanceId.getInstance().getToken();

                            final boolean newUserFacebook = task.getResult().getAdditionalUserInfo().isNewUser();
                            Userref.child(currentUserId).child("device_token").setValue(devicetoken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (newUserFacebook) {

                                                FirebaseUser firebaseUserFacebook = mAuth.getCurrentUser();
                                                UserDataSocial userData = new UserDataSocial(firebaseUserFacebook.getEmail(), firebaseUserFacebook.getDisplayName(), "", "ชาย", "", "", "", "", "", "tourist");
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserFacebook.getUid()).setValue(userData);
                                                startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                                            } else {
                                                startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                                            }
                                        }

                                    });

                        }
                    }
                });
    }

    /**
     * Google
     **/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String currentUserId = mAuth.getCurrentUser().getUid();
                            String devicetoken = FirebaseInstanceId.getInstance().getToken();

                            final boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();

                            Userref.child(currentUserId).child("device_token")
                                    .setValue(devicetoken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (newuser) {
                                        FirebaseUser firebaser = mAuth.getCurrentUser();
                                        UserDataSocial userData = new UserDataSocial(firebaser.getEmail(), firebaser.getDisplayName(), "", "", firebaser.getPhotoUrl().toString(), "", "", "", "", "tourist");
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaser.getUid()).setValue(userData);

                                        startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                                    } else {

                                        startActivity(new Intent(LoginUserActivity.this, MainUserActivity.class));
                                    }
                                }
                            });


                        }
                        // ...
                    }
                });
    }

    private void google_sign_in_process() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signinGSO() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }

                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.finalproject.it.travelfriend", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

