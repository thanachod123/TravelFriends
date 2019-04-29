package com.finalproject.it.travelfriend.Guide.WorkProceduresGuide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.WorkProceduresUser.DetailBooking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailBookingGuide extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CALL = 1;
    Toolbar toolbar;
    TextView txtProvince, txtPackage_type, txtVehicle_type, txtDescription, txtPackageName, txtNumTourist, txtSchedule, txtLanguage, txtNameGuide, txtDate;
    ImageView img_package;
    String bookingId, packageId, guideId, touristId, strProvince, strPackage_type, strVehicle_type, strDescription, strPackageImage, strPackageName, strNumTourist, strSchedule, strLanguage, strDate, strGuideName, strGuideSurname, strGuideImage, strTouristPhone, strLat, strLng, strLocationName;
    CircleImageView img_guide_image;
    FirebaseDatabase mDatabase;
    Button btnCallTourist, btnEmergencyCall;
    DatabaseReference mReferencePackage, mReferenceUsers, mReferenceBooking;
    Double lat;
    Double lng;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking_guide);

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
        toolbar.setTitleTextAppearance(DetailBookingGuide.this, R.style.FontForActionBar);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtProvince = findViewById(R.id.txt_province);
        txtPackage_type = findViewById(R.id.txt_activity);
        txtVehicle_type = findViewById(R.id.txt_vehicle_type);
        txtDescription = findViewById(R.id.txt_description);
        txtPackageName = findViewById(R.id.txt_name_package);
        txtNumTourist = findViewById(R.id.txt_max_guest);
        txtSchedule = findViewById(R.id.txt_schedule);
        txtLanguage = findViewById(R.id.txt_language);
        txtNameGuide = findViewById(R.id.txt_name_guide);
        txtDate = findViewById(R.id.txt_date_booking);
        img_package = findViewById(R.id.img_package);
        img_guide_image = findViewById(R.id.img_guide_image);
        btnCallTourist = findViewById(R.id.btn_call_tourist);
        btnEmergencyCall = findViewById(R.id.btn_emergency_call);

        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference("Packages");
        mReferenceUsers = mDatabase.getReference("Users");
        mReferenceBooking = mDatabase.getReference().child("Booking");
        bookingId = getIntent().getStringExtra("BookingId");

        bindData();

        btnCallTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callGuide = new Intent(Intent.ACTION_CALL);
                callGuide.setData(Uri.parse("tel:" + strTouristPhone));
                if (ActivityCompat.checkSelfPermission(DetailBookingGuide.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailBookingGuide.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(callGuide);
            }
        });

        btnEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callEmergency = new Intent(Intent.ACTION_CALL);
                callEmergency.setData(Uri.parse("tel:" + "191"));
                if (ActivityCompat.checkSelfPermission(DetailBookingGuide.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailBookingGuide.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(callEmergency);
            }
        });

    }

    private void bindData() {
        mReferenceBooking.child(bookingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String packageIds = dataSnapshot.child("packageId").getValue(String.class);
                guideId = dataSnapshot.child("guideId").getValue(String.class);
                touristId = dataSnapshot.child("touristId").getValue(String.class);
                strDate = dataSnapshot.child("booking_date").getValue(String.class);
                strNumTourist = dataSnapshot.child("booking_number_tourist").getValue(String.class);

                txtDate.setText(strDate);
                txtNumTourist.setText(strNumTourist);
                mReferencePackage.child(packageIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strPackage_type = dataSnapshot.child("package_type").getValue(String.class);
                        strVehicle_type = dataSnapshot.child("vehicle_type").getValue(String.class);
                        strProvince = dataSnapshot.child("province").getValue(String.class);
                        strPackageImage = dataSnapshot.child("image").getValue(String.class);
                        strPackageName = dataSnapshot.child("name").getValue(String.class);
                        strDescription = dataSnapshot.child("description").getValue(String.class);
                        strLanguage = dataSnapshot.child("language").getValue(String.class);
                        strSchedule = dataSnapshot.child("schedule").getValue(String.class);

                        toolbar.setTitle(strPackageName);
                        txtLanguage.setText(strLanguage);
                        txtSchedule.setText(strSchedule);
                        txtDescription.setText(strDescription);
                        txtProvince.setText(strProvince);
                        txtPackageName.setText(strPackageName);
                        txtVehicle_type.setText(strVehicle_type);
                        txtPackage_type.setText(strPackage_type);
                        Picasso.with(DetailBookingGuide.this).load(strPackageImage).placeholder(R.drawable.package_image).into(img_package);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mReferenceUsers.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strGuideName = dataSnapshot.child("name").getValue(String.class);
                        strGuideSurname = dataSnapshot.child("surname").getValue(String.class);
                        strGuideImage = dataSnapshot.child("profile_image").getValue(String.class);
                        txtNameGuide.setText(strGuideName + " " + strGuideSurname);
                        Picasso.with(DetailBookingGuide.this).load(strGuideImage).placeholder(R.drawable.default_profile).into(img_guide_image);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mReferenceUsers.child(touristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strTouristPhone = dataSnapshot.child("phone").getValue(String.class);
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mReferenceBooking.child(bookingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                packageId = dataSnapshot.child("packageId").getValue().toString();

                mReferencePackage.child(packageId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        strLat = dataSnapshot.child("lat").getValue(String.class);
                        strLng = dataSnapshot.child("lng").getValue(String.class);
                        strLocationName = dataSnapshot.child("location_name").getValue(String.class);

                        lat = Double.parseDouble(strLat);
                        lng = Double.parseDouble(strLng);

                        mMap = googleMap;
                        LatLng latLng = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(strLocationName));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));

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