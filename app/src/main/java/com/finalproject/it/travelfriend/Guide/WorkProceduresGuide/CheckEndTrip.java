package com.finalproject.it.travelfriend.Guide.WorkProceduresGuide;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.MainGuideActivity;
import com.finalproject.it.travelfriend.MainUserActivity;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.RegisterPackage.RequestPackageStepTwo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CheckEndTrip extends AppCompatActivity {
    TextView txtTouristName;
    ImageView imgBooking;
    Toolbar toolbar;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceUser;
    String strBookingId,strTouristName,strTouristSurname,strTouristId,strGuideId;
    StorageReference mStorage;
    Uri IMG_Booking_Uri;
    Button btnCheckEndTrip;
    private static final int request_Code_IMG_Booking = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_end_trip);

        imgBooking = findViewById(R.id.img_booking);
        btnCheckEndTrip = findViewById(R.id.btn_check_endtrip);
        txtTouristName = findViewById(R.id.txt_tourist_name);
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

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferenceUser = mDatabase.getReference().child("Users");
        mStorage = FirebaseStorage.getInstance().getReference();
        strBookingId = getIntent().getExtras().getString("BookingId");
        bindData();

        imgBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_IMG_Booking);
            }
        });
        btnCheckEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCheckEndtrip = new Intent(CheckEndTrip.this,MainGuideActivity.class);
                select_image();
                startActivity(intentCheckEndtrip);
            }
        });
    }

    private void bindData() {
        mReferenceBooking.child(strBookingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strTouristId = dataSnapshot.child("touristId").getValue(String.class);
                strGuideId = dataSnapshot.child("guideId").getValue(String.class);
                mReferenceUser.child(strTouristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strTouristName = dataSnapshot.child("name").getValue(String.class);
                        strTouristSurname = dataSnapshot.child("surname").getValue(String.class);

                        txtTouristName.setText(strTouristName +" "+ strTouristSurname);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code_IMG_Booking && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            IMG_Booking_Uri = data.getData();
            Picasso.with(this).load(IMG_Booking_Uri).into(imgBooking);
        }
    }

    private void select_image(){
        if (IMG_Booking_Uri != null){
            StorageReference imageMTSPath = mStorage.child("Booking").child(strBookingId).child("Booking_Image.jpg");
            imageMTSPath.putFile(IMG_Booking_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlBookingImg = urlTask.getResult();
                    final String strBookingImg = String.valueOf(downloadUrlBookingImg);
                    mReferenceBooking.child(strBookingId).child("booking_image").setValue(strBookingImg);
                    mReferenceBooking.child(strBookingId).child("status_guideId").setValue("จบลงไปแล้ว_"+strGuideId);
                }
            });
        }
    }
}
