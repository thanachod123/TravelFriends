package com.finalproject.it.travelfriend.Guide.CreatePackage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.MainGuideActivity;
import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CreatePackageStepFive extends Fragment {
    Button btnSaveNext, btnBack;
    NonSwipeableViewPager viewPager;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ImageView img_upload;
    TextView txt_name_trip,txt_summary_trip,txt_province,txt_activities,txt_language,txt_vehicle,txt_schedule,txt_max_guests,txt_price,txt_bank,txt_bank_number;
    String guideID,packageID,strGuideName;
    String strProvince, strPackage_type, strVehicle_type, strBank, strBank_number, strDescription, strImage, strName, strNumberTourist, strPrice_per_person, strSchedule, strLanguage,strLat,strLng,strLocationName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_package_five, container, false);

        btnSaveNext = view.findViewById(R.id.btn_save_next);
        btnBack = view.findViewById(R.id.btn_back);
        viewPager = getActivity().findViewById(R.id.view_pager);
        img_upload = view.findViewById(R.id.imageView);
        txt_name_trip = view.findViewById(R.id.txt_name_trip);
        txt_summary_trip = view.findViewById(R.id.txt_summary_trip);
        txt_province = view.findViewById(R.id.txt_province);
        txt_activities = view.findViewById(R.id.txt_activities);
        txt_language = view.findViewById(R.id.txt_language);
        txt_vehicle = view.findViewById(R.id.txt_vehicle);
        txt_schedule = view.findViewById(R.id.txt_description);
        txt_max_guests = view.findViewById(R.id.txt_max_guests);
        txt_price = view.findViewById(R.id.txt_price);
        txt_bank = view.findViewById(R.id.txt_bank);
        txt_bank_number = view.findViewById(R.id.txt_bank_number);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        guideID = mAuth.getCurrentUser().getUid();
        getPackageData();

        btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainGuideActivity.class);
                PackageData packageData = new PackageData(guideID,strGuideName,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"รอการตรวจสอบ","รอการตรวจสอบ_"+strPackage_type,strLat,strLng,strLocationName,"0.0");
                mReference.child("Packages").child(packageID).setValue(packageData);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(getItemForprevious(-1), true);
            }
        });
        return view;
    }


    private void getPackageData() {
        packageID = getActivity().getIntent().getExtras().getString("PackageID");
        mReference.child("Packages").child(packageID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strPackage_type = dataSnapshot.child("package_type").getValue(String.class);
                        strProvince = dataSnapshot.child("province").getValue(String.class);
                        strVehicle_type = dataSnapshot.child("vehicle_type").getValue(String.class);
                        strBank = dataSnapshot.child("bank").getValue(String.class);
                        strBank_number = dataSnapshot.child("bank_number").getValue(String.class);
                        strDescription = dataSnapshot.child("description").getValue(String.class);
                        strImage = dataSnapshot.child("image").getValue(String.class);
                        strName = dataSnapshot.child("name").getValue(String.class);
                        strNumberTourist = dataSnapshot.child("number_tourist").getValue(String.class);
                        strPrice_per_person = dataSnapshot.child("price_per_person").getValue(String.class);
                        strSchedule = dataSnapshot.child("schedule").getValue(String.class);
                        strLanguage = dataSnapshot.child("language").getValue(String.class);
                        strLat = dataSnapshot.child("lat").getValue(String.class);
                        strLng = dataSnapshot.child("lng").getValue(String.class);
                        strLocationName = dataSnapshot.child("location_name").getValue(String.class);
                        strGuideName = dataSnapshot.child("guideName").getValue(String.class);

                        txt_name_trip.setText(strName);
                        txt_summary_trip.setText(strDescription);
                        txt_province.setText(strProvince);
                        txt_activities.setText(strPackage_type);
                        txt_language.setText(strLanguage);
                        txt_vehicle.setText(strVehicle_type);
                        txt_schedule.setText(strSchedule);
                        txt_max_guests.setText(strNumberTourist);
                        txt_price.setText(strPrice_per_person);
                        txt_bank.setText(strBank);
                        txt_bank_number.setText(strBank_number);

                        if ("".equalsIgnoreCase(strImage)) {
                            strImage = "default";
                        }
                        Picasso.with(getActivity()).load(strImage).placeholder(R.drawable.package_image).into(img_upload);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private int getItemForprevious(int i) {
        return viewPager.getCurrentItem() - 1;
    }
}
