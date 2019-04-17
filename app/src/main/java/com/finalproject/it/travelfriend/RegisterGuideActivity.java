package com.finalproject.it.travelfriend;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Utility.Firebase_guide_method;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterGuideActivity extends AppCompatActivity {
    private static final String TAG = "RegisterGuideActivity";

    CircleImageView profile_image;
    TextInputEditText mName,mEmail,mPhone,mPassword,mSurname,mProvince,mDistrict;
    ImageView mUploadCertificate,mUploadLicense,mUploadCitizen,mCertificate,mLicense,mCitizen;
    Button mRegister;
    RadioGroup mGender;
    RadioButton mGenderOption;
    TextView tv_gender,tv_certificate,tv_license,tv_citizen;
    Spinner spinnerAge;
    Toolbar toolbar;

    //Firebase Variables
    Firebase_guide_method firebase_guide_method;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference mStorage;

    //Variables
    String strEmail,strName,strPhone,strGender,strPassword,strSurname,strProvince,strDistrict,strAge;
    private static final int request_Code_ProfileIMG = 5;
    private static final int request_Code_CertificateIMG = 6;
    private static final int request_Code_LicenseIMG = 7;
    private static final int request_Code_CitizenIMG = 8;
    Uri Profile_imageUri,Certificate_imageUri,License_imageUri,Citizen_imageUri;
    String guideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_guide);
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

        firebase_guide_method = new Firebase_guide_method(this);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mUploadCertificate = findViewById(R.id.imageView9);
        mUploadLicense = findViewById(R.id.imageView10);
        mUploadCitizen = findViewById(R.id.imageView11);
        mCertificate = findViewById(R.id.certificate_img);
        mLicense = findViewById(R.id.licence_img);
        mCitizen = findViewById(R.id.citizen_img);
        profile_image = findViewById(R.id.iv_profile_image);
        mEmail = findViewById(R.id.edt_Email);
        mName = findViewById(R.id.edt_Name);
        mPhone = findViewById(R.id.edt_Phone);
        mPassword = findViewById(R.id.edt_Password);
        mRegister = findViewById(R.id.btn_register);
        mGender = findViewById(R.id.rg_gender);
        mSurname = findViewById(R.id.edt_Surname);
        mProvince = findViewById(R.id.edt_Province);
        mDistrict = findViewById(R.id.edt_District);
        spinnerAge = findViewById(R.id.spinner_age);

        List age = new ArrayList<Integer>();
        for (int i = 20; i <= 60; i++){
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_dropdown_item,age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(spinnerArrayAdapter);

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
                register_new_guide();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_ProfileIMG);
            }
        });

        mUploadCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_CertificateIMG);
            }
        });

        mUploadLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_LicenseIMG);
            }
        });

        mUploadCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_CitizenIMG);
            }
        });

        setupFirebaseAuthentication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code_ProfileIMG && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(RegisterGuideActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Profile_imageUri = result.getUri();
                Log.d(TAG, "onActivityResult: Profile image uri " + Profile_imageUri.toString());
                profile_image.setImageURI(Profile_imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == request_Code_CertificateIMG && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            Certificate_imageUri = data.getData();
            Picasso.with(this).load(Certificate_imageUri).into(mCertificate);
        }
        if (requestCode == request_Code_LicenseIMG && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            License_imageUri = data.getData();
            Picasso.with(this).load(License_imageUri).into(mLicense);
        }
        if (requestCode == request_Code_CitizenIMG && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            Citizen_imageUri = data.getData();
            Picasso.with(this).load(Citizen_imageUri).into(mCitizen);
        }
    }

    private void select_image(){
        if (Profile_imageUri != null){
            StorageReference imageProfilePath = mStorage.child(getString(R.string.users)).child(guideId).child(Profile_imageUri.getLastPathSegment());
            imageProfilePath.putFile(Profile_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlProfile = urlTask.getResult();
                    final String strProfileImage = String.valueOf(downloadUrlProfile);
                    mReference.child(getString(R.string.users)).child(guideId).child("profile_image").setValue(strProfileImage);

                }
            });
        }
        if (Certificate_imageUri != null){
            StorageReference imageCertificatePath = mStorage.child(getString(R.string.users)).child(guideId).child(Certificate_imageUri.getLastPathSegment());
            imageCertificatePath.putFile(Certificate_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlCertificate = urlTask.getResult();
                    final String strCertificateImage = String.valueOf(downloadUrlCertificate);
                    mReference.child(getString(R.string.users)).child(guideId).child("certificate_image").setValue(strCertificateImage);

                }
            });
        }
        if (License_imageUri != null){
            StorageReference imageLicensePath = mStorage.child(getString(R.string.users)).child(guideId).child(License_imageUri.getLastPathSegment());
            imageLicensePath.putFile(License_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlLicense = urlTask.getResult();
                    final String strLicenseImage = String.valueOf(downloadUrlLicense);
                    mReference.child(getString(R.string.users)).child(guideId).child("license_image").setValue(strLicenseImage);

                }
            });
        }
        if (Citizen_imageUri != null){
            StorageReference imageCitizenPath = mStorage.child(getString(R.string.users)).child(guideId).child(Citizen_imageUri.getLastPathSegment());
            imageCitizenPath.putFile(Citizen_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlCitizen = urlTask.getResult();
                    final String strCitizenImage = String.valueOf(downloadUrlCitizen);
                    mReference.child(getString(R.string.users)).child(guideId).child("citizen_image").setValue(strCitizenImage);

                }
            });
        }
    }

    private boolean register_new_guide() {
        strEmail = mEmail.getText().toString();
        strName = mName.getText().toString();
        strSurname = mSurname.getText().toString();
        strPhone = mPhone.getText().toString();
        strPassword = mPassword.getText().toString();
        strProvince = mProvince.getText().toString();
        strDistrict = mDistrict.getText().toString();
        strAge = spinnerAge.getSelectedItem().toString();

//        if (check_input(strEmail,strName,strPhone,strPassword)){
        if (strName.trim().isEmpty()){
            Toast.makeText(this, "Please input Name", Toast.LENGTH_SHORT).show();
            mName.requestFocus();
            return false;
        }
        if (strSurname.trim().isEmpty()){
            Toast.makeText(this, "Please input SurName", Toast.LENGTH_SHORT).show();
            mSurname.requestFocus();
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
        if (strProvince.trim().isEmpty()){
            Toast.makeText(this, "Please input Province", Toast.LENGTH_SHORT).show();
            mProvince.requestFocus();
            return false;
        }
        if (strDistrict.trim().isEmpty()){
            Toast.makeText(this, "Please input Name", Toast.LENGTH_SHORT).show();
            mDistrict.requestFocus();
            return false;
        }
        if (mGender.getCheckedRadioButtonId()== -1){
            Toast.makeText(this,"Please input Gender",Toast.LENGTH_SHORT).show();
            mGender.requestFocus();
            return false;
        }
        firebase_guide_method.register_new_email(strEmail,strPassword);
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
                FirebaseUser guide = mAuth.getCurrentUser();
                final String devicetoken = FirebaseInstanceId.getInstance().getToken();

                if (guide != null){
                    guideId = mAuth.getCurrentUser().getUid();
                    Log.d(TAG,"onAuthStateChaged: guideId " +guideId);
                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            firebase_guide_method.send_new_guide_data(strEmail,strName,strSurname,strPhone,strPassword,strGender,"default","default","default","default",strProvince,strDistrict,strAge,"guide","false" , devicetoken);
                            select_image();
                            Toast.makeText(RegisterGuideActivity.this,"Registration Success",Toast.LENGTH_SHORT).show();
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

