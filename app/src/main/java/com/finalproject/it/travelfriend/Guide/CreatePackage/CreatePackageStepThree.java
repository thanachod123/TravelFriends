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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePackageStepThree extends Fragment {
    EditText edt_schedule;
    Button btnSaveNext,btnBack;
    NonSwipeableViewPager viewPager;
    ImageView btnPinMap;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    TextView txtPlace;
    String strProvince,strPackage_type,strVehicle_type,strBank,strBank_number,strDescription,strImage,strName,strNumberTourist,strPrice_per_person,strSchedule,strLanguage,strNameLocation,strLat,strLng,strLocationName;
    String guideID,packageID,strGuideName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_package_three, container, false);

        btnSaveNext = view.findViewById(R.id.btn_save_next);
        btnBack = view.findViewById(R.id.btn_back);
        edt_schedule= view.findViewById(R.id.edt_schedule);
        viewPager = getActivity().findViewById(R.id.view_pager);
        txtPlace = view.findViewById(R.id.txt_place);
        btnPinMap = view.findViewById(R.id.btn_pin_map);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        guideID = mAuth.getCurrentUser().getUid();
        getPackageData();

        btnPinMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePackageMapsActivity.class);
                intent.putExtra("PackageId",packageID);
                startActivity(intent);
            }
        });
        btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPackageStepThree();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(getItemForprevious(-1),true);
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
                        strNameLocation =dataSnapshot.child("location_name").getValue(String.class);
                        strLat = dataSnapshot.child("lat").getValue(String.class);
                        strLng = dataSnapshot.child("lng").getValue(String.class);
                        strLocationName = dataSnapshot.child("location_name").getValue(String.class);
                        strGuideName = dataSnapshot.child("guideName").getValue(String.class);
                        txtPlace.setText(strNameLocation);
                        edt_schedule.setText(strSchedule);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private boolean createPackageStepThree() {
        strSchedule = edt_schedule.getText().toString();
        strNameLocation = txtPlace.getText().toString();

        if (strSchedule.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุแผนการท่องเที่ยว", Toast.LENGTH_SHORT).show();
            edt_schedule.requestFocus();
            return false;
        }
        else if (strNameLocation.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุสถานที่ท่องเที่ยว", Toast.LENGTH_SHORT).show();
            txtPlace.requestFocus();
            return false;
        }
        PackageData packageData = new PackageData(guideID,strGuideName,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"ยังไม่สมบูรณ์","ยังไม่สมบูรณ์_"+strPackage_type,strLat,strLng,strLocationName,"0.0");
        mReference.child("Packages").child(packageID).setValue(packageData);
        viewPager.setCurrentItem(getItemFornext(+1),true);
        return true;
    }

    private int getItemFornext(int i) {
        return viewPager.getCurrentItem() +1;
    }

    private int getItemForprevious(int i) {
        return viewPager.getCurrentItem() -1;
    }
}