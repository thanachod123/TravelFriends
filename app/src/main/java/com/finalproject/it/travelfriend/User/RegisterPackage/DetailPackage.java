package com.finalproject.it.travelfriend.User.RegisterPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.Guide.CreatePackage.CreatePackageGuide;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPackage extends AppCompatActivity {
    Button btnRequestPackage;
    Toolbar toolbar;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferencePackage,mReferenceGuide;
    String strProvince, strPackage_type, strVehicle_type, strDescription, strImage, strPackageName, strNumberTourist, strPrice_per_person, strSchedule, strLanguage;
    TextView txtProvince,txtPackage_type,txtVehicle_type,txtDescription,txtPackageName, txtNumberTourist, txtPricePerPerson, txtSchedule,txtLanguage,txtNameGuide;
    ImageView img_package;
    CircleImageView img_guide_image;
    String packageID,guideID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_package);
        toolbar = findViewById(R.id.app_bar);
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
        toolbar.setTitleTextAppearance(DetailPackage.this, R.style.FontForActionBar);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));

        txtProvince = findViewById(R.id.txt_province);
        txtPackage_type = findViewById(R.id.txt_activity);
        txtVehicle_type = findViewById(R.id.txt_vehicle_type);
        txtDescription = findViewById(R.id.txt_description);
        txtNameGuide = findViewById(R.id.txt_name_guide);
        img_package = findViewById(R.id.img_package);
        img_guide_image = findViewById(R.id.img_guide_image);
        txtPackageName = findViewById(R.id.txt_name_package);
        txtNumberTourist = findViewById(R.id.txt_max_guest);
        txtPricePerPerson = findViewById(R.id.txt_price_per_person);
        txtSchedule = findViewById(R.id.txt_schedule);
        txtLanguage = findViewById(R.id.txt_language);
        btnRequestPackage =findViewById(R.id.btn_request_package);

        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference();
        mReferenceGuide = mDatabase.getReference();
        packageID = getIntent().getExtras().getString("PackageID");
        getPackageData();

        btnRequestPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRequestPackage = new Intent(DetailPackage.this,RequestPackageStepOne.class);
                intentRequestPackage.putExtra("PackageID",packageID);
                startActivity(intentRequestPackage);
            }
        });

    }

    private void getPackageData() {
        mReferencePackage.child("Packages").child(packageID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strPackage_type = dataSnapshot.child("package_type").getValue(String.class);
                        strProvince = dataSnapshot.child("province").getValue(String.class);
                        strVehicle_type = dataSnapshot.child("vehicle_type").getValue(String.class);
                        strDescription = dataSnapshot.child("description").getValue(String.class);
                        strImage = dataSnapshot.child("image").getValue(String.class);
                        strPackageName = dataSnapshot.child("name").getValue(String.class);
                        strNumberTourist = dataSnapshot.child("number_tourist").getValue(String.class);
                        strPrice_per_person = dataSnapshot.child("price_per_person").getValue(String.class);
                        strSchedule = dataSnapshot.child("schedule").getValue(String.class);
                        strLanguage = dataSnapshot.child("language").getValue(String.class);
                        guideID = dataSnapshot.child("guideId").getValue(String.class);
                        toolbar.setTitle(strPackageName);
                        txtPackageName.setText(strPackageName);
                        txtDescription.setText(strDescription);
                        txtProvince.setText(strProvince);
                        txtPackage_type.setText(strPackage_type);
                        txtLanguage.setText(strLanguage);
                        txtVehicle_type.setText(strVehicle_type);
                        txtSchedule.setText(strSchedule);
                        txtNumberTourist.setText(strNumberTourist);
                        txtPricePerPerson.setText(strPrice_per_person+ " THB");

                        if ("".equalsIgnoreCase(strImage)) {
                            strImage = "default";
                        }
                        Picasso.with(DetailPackage.this).load(strImage).placeholder(R.drawable.package_image).into(img_package);
                        mReferenceGuide.child("Users").child(guideID)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String strProfile_image, strName, strSurname;
                                        strProfile_image = dataSnapshot.child("profile_image").getValue(String.class);
                                        strName = dataSnapshot.child("name").getValue(String.class);
                                        strSurname = dataSnapshot.child("surname").getValue(String.class);

                                        txtNameGuide.setText(strName+" "+strSurname);
                                        if ("".equalsIgnoreCase(strProfile_image)) {
                                            strProfile_image = "default";
                                        }
                                        Picasso.with(DetailPackage.this).load(strProfile_image).placeholder(R.drawable.package_image).into(img_guide_image);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
