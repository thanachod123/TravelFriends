package com.finalproject.it.travelfriend;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Utility.Firebase_method;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    ImageView back_bt;
    CircleImageView profile_image;
    TextInputEditText mName,mEmail,mPhone,mPassword;
    Button mRegister;
    RadioGroup mGender;
    RadioButton mGenderOption;

    //Firebase Variables
    Firebase_method firebase_method;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference mStorage;

    //Variables
    String strEmail,strName,strPhone,strGender,strPassword;
    private static final int request_Code = 5;
    Uri imageUri;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebase_method = new Firebase_method(this);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        profile_image = findViewById(R.id.iv_profile_image);
        mEmail = findViewById(R.id.edt_Email);
        mName = findViewById(R.id.edt_Name);
        mPhone = findViewById(R.id.edt_Phone);
        mPassword = findViewById(R.id.edt_Password);
        mRegister = findViewById(R.id.btn_register);
        back_bt = findViewById(R.id.btn_back);
        mGender = findViewById(R.id.rg_gender);

        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mGenderOption = mGender.findViewById(i);
                switch (i){
                    case R.id.rb_male:
                        strGender = mGenderOption.getText().toString();
                        break;
                    case R.id.rb_female:
                        strGender = mGenderOption.getText().toString();
                        break;
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_new_user();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code);
            }
        });
        setupFirebaseAuthentication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(RegisterActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Log.d(TAG, "onActivityResult: Image Uri " + imageUri.toString());
                profile_image.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void select_image(){
        if (imageUri != null){
            StorageReference imagePath = mStorage.child(getString(R.string.users)).child(userId).child(imageUri.getLastPathSegment());
            imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    String image_firebase_uri = taskSnapshot.getStorage().getDownloadUrl().toString();
//                    strProfileImage = image_firebase_uri;
//                    mReference.child(getString(R.string.users)).child(userId).child("profile_image").setValue(strProfileImage);

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    final String strProfileImage = String.valueOf(downloadUrl);
                    mReference.child(getString(R.string.users)).child(userId).child("profile_image").setValue(strProfileImage);

                }
            });
        }
    }

    private boolean register_new_user() {
        strEmail = mEmail.getText().toString();
        strName = mName.getText().toString();
        strPhone = mPhone.getText().toString();
        strPassword = mPassword.getText().toString();

//        if (check_input(strEmail,strName,strPhone,strPassword)){
        if (strName.trim().isEmpty()){
            Toast.makeText(this, "Please input Name", Toast.LENGTH_SHORT).show();
            mName.requestFocus();
            return false;
        }
        if (strEmail.trim().isEmpty() || !isValidEmail(strEmail) ){
            Toast.makeText(this, "Please input Email", Toast.LENGTH_SHORT).show();
            mEmail.requestFocus();
            return false;
        }
        if (strPhone.trim().isEmpty()){
            Toast.makeText(this, "Please input Phone", Toast.LENGTH_SHORT).show();
            mPhone.requestFocus();
            return false;
        }
        if (strPassword.trim().isEmpty()){
            Toast.makeText(this, "Please input Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            return false;
        }
        firebase_method.register_new_email(strEmail,strPassword);
        return true;
    }

    private boolean isValidEmail(String strEmail){
        return !TextUtils.isEmpty(strEmail) && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    private void setupFirebaseAuthentication(){

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG,"setupFirebaseAuthentication: ready for send data");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null){
                    userId = mAuth.getCurrentUser().getUid();

                    Log.d(TAG,"onAuthStateChaged: userID " +userId);

                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            firebase_method.send_new_user_data(strEmail,strName,strPhone,strPassword,strGender,"Default");
                            select_image();

                            Toast.makeText(RegisterActivity.this,"Registration Success",Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
