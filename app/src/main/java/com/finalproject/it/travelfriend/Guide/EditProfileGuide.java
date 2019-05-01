package com.finalproject.it.travelfriend.Guide;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.RegisterGuideActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class EditProfileGuide extends AppCompatActivity {

    private static final int request_Code_ProfileIMG = 5;
    Uri Profile_imageUri;
    String name, surname, phone, province, district, age, gender;
    CircleImageView iv_profile_image;
    TextInputEditText mName, mSurname, mPhone, mProvince, mDistrict;
    Button mUpdate;
    RadioGroup mGender;
    RadioButton mGenderMale, mGenderFemale, mGenderOption;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    Spinner spinnerAge;
    String guideId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_guide);
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
        StatusBarUtil.setColor(this,getResources().getColor(R.color.yellow));

        iv_profile_image = findViewById(R.id.iv_profile_image);
        mName = findViewById(R.id.edt_Name);
        mSurname = findViewById(R.id.edt_Surname);
        mPhone = findViewById(R.id.edt_Phone);
        mProvince = findViewById(R.id.edt_Province);
        mDistrict = findViewById(R.id.edt_District);
        mGender = findViewById(R.id.rg_gender);
        mGenderMale = findViewById(R.id.rb_male);
        mGenderFemale = findViewById(R.id.rb_female);
        mUpdate = findViewById(R.id.btn_save_change);
        spinnerAge = findViewById(R.id.spinner_age);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        List age = new ArrayList<Integer>();
        for (int i = 20; i <= 60; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_dropdown_item, age);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(spinnerArrayAdapter);

        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mGenderOption = mGender.findViewById(i);
                switch (i) {
                    case R.id.rb_male:
                        gender = mGenderOption.getText().toString();
                        break;
                    case R.id.rb_female:
                        gender = mGenderOption.getText().toString();
                        break;
                }
            }
        });

        setFont();
        getUser_Profile_Data();

        iv_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, request_Code_ProfileIMG);
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile();
            }
        });
    }

    private void setFont() {
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/FC Lamoon Bold ver 1.00.ttf");
        mName.setTypeface(myCustomFont);
        mSurname.setTypeface(myCustomFont);
        mPhone.setTypeface(myCustomFont);
        mProvince.setTypeface(myCustomFont);
        mDistrict.setTypeface(myCustomFont);
        mUpdate.setTypeface(myCustomFont);

    }

    private void getUser_Profile_Data() {
        mReference.child(getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name, surname, phone, province, district, age, gender, profile_image;

                        guideId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        name = dataSnapshot.child("name").getValue().toString();
                        surname = dataSnapshot.child("surname").getValue().toString();
                        phone = dataSnapshot.child("phone").getValue().toString();
                        province = dataSnapshot.child("province").getValue().toString();
                        district = dataSnapshot.child("district").getValue().toString();
                        age = dataSnapshot.child("age").getValue().toString();
                        gender = dataSnapshot.child("gender").getValue().toString();
                        profile_image = dataSnapshot.child("profile_image").getValue().toString();
                        mName.setText(name);
                        mSurname.setText(surname);
                        mPhone.setText(phone);
                        mProvince.setText(province);
                        mDistrict.setText(district);

                        if (gender.equalsIgnoreCase("ชาย")) {
                            mGenderMale.setChecked(true);
                        } else if (gender.equalsIgnoreCase("หญิง")) {
                            mGenderFemale.setChecked(true);
                        }
                        int age1 = Integer.parseInt(age);
                        int age2 = age1 - 20;
                        spinnerAge.setSelection(age2);

                        Picasso.with(EditProfileGuide.this).load(profile_image).placeholder(R.drawable.default_profile).into(iv_profile_image);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private boolean UpdateProfile() {
        name = mName.getText().toString();
        surname = mSurname.getText().toString();
        phone = mPhone.getText().toString();
        province = mProvince.getText().toString();
        district = mDistrict.getText().toString();
        age = spinnerAge.getSelectedItem().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please input Name", Toast.LENGTH_SHORT).show();
            mName.requestFocus();
            return false;
        }
        if (surname.trim().isEmpty()) {
            Toast.makeText(this, "Please input SurName", Toast.LENGTH_SHORT).show();
            mSurname.requestFocus();
            return false;
        }

        if (phone.trim().isEmpty()) {
            Toast.makeText(this, "Please input Phone", Toast.LENGTH_SHORT).show();
            mPhone.requestFocus();
            return false;
        }


        if (phone.length() < 10 || phone.length() > 10) {
            Toast.makeText(this, "Please Enter valid phone number", Toast.LENGTH_SHORT).show();
            mPhone.requestFocus();
            return false;
        }

       if (phone.matches("[a-zA-Z]+")){
           Toast.makeText(this, "Please Enter valid phone number 0-9", Toast.LENGTH_SHORT).show();
           mPhone.requestFocus();
           return false;
       }

        if (province.trim().isEmpty()) {
            Toast.makeText(this, "Please input Province", Toast.LENGTH_SHORT).show();
            mProvince.requestFocus();
            return false;
        }
        if (district.trim().isEmpty()) {
            Toast.makeText(this, "Please input Name", Toast.LENGTH_SHORT).show();
            mDistrict.requestFocus();
            return false;
        }
        if (mGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please input Gender", Toast.LENGTH_SHORT).show();
            mGender.requestFocus();
            return false;
        }
        mReference.child(getString(R.string.users)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReference.child(getString(R.string.users)).child(guideId).child("name").setValue(name);
                mReference.child(getString(R.string.users)).child(guideId).child("surname").setValue(surname);
                mReference.child(getString(R.string.users)).child(guideId).child("phone").setValue(phone);
                mReference.child(getString(R.string.users)).child(guideId).child("province").setValue(province);
                mReference.child(getString(R.string.users)).child(guideId).child("district").setValue(district);
                mReference.child(getString(R.string.users)).child(guideId).child("age").setValue(age);
                mReference.child(getString(R.string.users)).child(guideId).child("gender").setValue(gender);


                Toast.makeText(getApplicationContext(), "Edit profile success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code_ProfileIMG && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(EditProfileGuide.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Profile_imageUri = result.getUri();
                iv_profile_image.setImageURI(Profile_imageUri);

                StorageReference imageProfilePath = mStorage.child(getString(R.string.users)).child(guideId).child("profile.jpg");
                imageProfilePath.putFile(Profile_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrlProfile = urlTask.getResult();
                        final String strProfileImage = String.valueOf(downloadUrlProfile);
                        mReference.child(getString(R.string.users)).child(guideId).child("profile_image").setValue(strProfileImage);
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
