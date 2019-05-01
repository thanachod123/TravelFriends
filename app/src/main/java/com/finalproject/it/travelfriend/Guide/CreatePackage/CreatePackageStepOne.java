package com.finalproject.it.travelfriend.Guide.CreatePackage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePackageStepOne extends Fragment {
    Button btnSaveNext;
    NonSwipeableViewPager viewPager;
    Spinner spinnerProvince,spinnerPackageType,spinnerLanguage;
    ImageView imgCar,imgMotorcycle,imgVan,imgJetSki,imgBike,imgOther;
    EditText edt_vehicle;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    String guideID,packageID;
    String strProvince,strPackage_type,strVehicle_type,strBank,strBank_number,strDescription,strImage,strName,strNumberTourist,strPrice_per_person,strSchedule,strLanguage,strLat,strLng,strLocationName;
    String strVehicle_type_more,strGuideName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_package_one, container, false);
        setHasOptionsMenu(true);
        btnSaveNext = view.findViewById(R.id.btn_save_next);
        viewPager = getActivity().findViewById(R.id.view_pager);
        spinnerProvince = view.findViewById(R.id.spinner_destination);
        spinnerPackageType = view.findViewById(R.id.spinner_activities);
        spinnerLanguage = view.findViewById(R.id.spinner_languages);
        imgCar = view.findViewById(R.id.img_car);
        imgMotorcycle = view.findViewById(R.id.img_scooter);
        imgVan = view.findViewById(R.id.img_van);
        imgJetSki = view.findViewById(R.id.img_jetski);
        imgBike = view.findViewById(R.id.img_bicycle);
        imgOther = view.findViewById(R.id.img_more);
        edt_vehicle = view.findViewById(R.id.edt_vehicle);
        edt_vehicle.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        guideID = mAuth.getCurrentUser().getUid();
        getPackageData();


        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.selectedcar);
                imgMotorcycle.setImageResource(R.drawable.scooter);
                imgVan.setImageResource(R.drawable.van);
                imgJetSki.setImageResource(R.drawable.jetboating);
                imgBike.setImageResource(R.drawable.bicycle);
                imgOther.setImageResource(R.drawable.selectedmore);
                edt_vehicle.setVisibility(View.GONE);
                edt_vehicle.getText().clear();
                strVehicle_type = "Car";
            }
        });
        imgMotorcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.car);
                imgMotorcycle.setImageResource(R.drawable.selectedscooter);
                imgVan.setImageResource(R.drawable.van);
                imgJetSki.setImageResource(R.drawable.jetboating);
                imgBike.setImageResource(R.drawable.bicycle);
                imgOther.setImageResource(R.drawable.selectedmore);
                edt_vehicle.setVisibility(View.GONE);
                edt_vehicle.getText().clear();
                strVehicle_type = "Motorcycle";
            }
        });
        imgVan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.car);
                imgMotorcycle.setImageResource(R.drawable.scooter);
                imgVan.setImageResource(R.drawable.selectedvan);
                imgJetSki.setImageResource(R.drawable.jetboating);
                imgBike.setImageResource(R.drawable.bicycle);
                imgOther.setImageResource(R.drawable.selectedmore);
                edt_vehicle.setVisibility(View.GONE);
                edt_vehicle.getText().clear();
                strVehicle_type = "Van";
            }
        });
        imgJetSki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.car);
                imgMotorcycle.setImageResource(R.drawable.scooter);
                imgVan.setImageResource(R.drawable.van);
                imgJetSki.setImageResource(R.drawable.selectedboating);
                imgBike.setImageResource(R.drawable.bicycle);
                imgOther.setImageResource(R.drawable.selectedmore);
                edt_vehicle.setVisibility(View.GONE);
                edt_vehicle.getText().clear();
                strVehicle_type = "JetSki";
            }
        });
        imgBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.car);
                imgMotorcycle.setImageResource(R.drawable.scooter);
                imgVan.setImageResource(R.drawable.van);
                imgJetSki.setImageResource(R.drawable.jetboating);
                imgBike.setImageResource(R.drawable.selectedbicycle);
                imgOther.setImageResource(R.drawable.selectedmore);
                edt_vehicle.setVisibility(View.GONE);
                edt_vehicle.getText().clear();
                strVehicle_type = "Bike";
            }
        });
        imgOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCar.setImageResource(R.drawable.car);
                imgMotorcycle.setImageResource(R.drawable.scooter);
                imgVan.setImageResource(R.drawable.van);
                imgJetSki.setImageResource(R.drawable.jetboating);
                imgBike.setImageResource(R.drawable.bicycle);
                imgOther.setImageResource(R.drawable.more);
                edt_vehicle.setVisibility(View.VISIBLE);

            }
        });

        btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPackageStepOne();
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
                        spinnerPackageType.setSelection(((ArrayAdapter<String>)spinnerPackageType.getAdapter()).getPosition(strPackage_type));
                        spinnerProvince.setSelection(((ArrayAdapter<String>)spinnerProvince.getAdapter()).getPosition(strProvince));
                        spinnerLanguage.setSelection(((ArrayAdapter<String>)spinnerLanguage.getAdapter()).getPosition(strLanguage));

                        if ("Car".equalsIgnoreCase(strVehicle_type)){
                            imgCar.setImageResource(R.drawable.selectedcar);
                        } else if ("Motorcycle".equalsIgnoreCase(strVehicle_type)){
                            imgMotorcycle.setImageResource(R.drawable.selectedscooter);
                        } else if ("Van".equalsIgnoreCase(strVehicle_type)){
                            imgVan.setImageResource(R.drawable.selectedvan);
                        } else if ("JetSki".equalsIgnoreCase(strVehicle_type)){
                            imgJetSki.setImageResource(R.drawable.selectedboating);
                        } else if ("Bike".equalsIgnoreCase(strVehicle_type)){
                            imgBike.setImageResource(R.drawable.selectedbicycle);
                        } else if ("".equalsIgnoreCase(strVehicle_type)) {
                            imgCar.setImageResource(R.drawable.selectedcar);
                            strVehicle_type = "Car";
                        } else {
                            imgOther.setImageResource(R.drawable.more);
                            edt_vehicle.setVisibility(View.VISIBLE);
                            edt_vehicle.setText(strVehicle_type);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private boolean createPackageStepOne() {
        Bitmap bImgOther = ((BitmapDrawable)imgOther.getDrawable()).getBitmap();
        Drawable selectedOther = getResources().getDrawable(R.drawable.more);
        Bitmap bImgSelectedOther = ((BitmapDrawable)selectedOther).getBitmap();
        strVehicle_type_more = edt_vehicle.getText().toString();


        if (bImgOther.sameAs(bImgSelectedOther) && strVehicle_type_more.trim().isEmpty()) {
            edt_vehicle.requestFocus();
            return false;
        }else if (bImgOther.sameAs(bImgSelectedOther)){
            strVehicle_type = strVehicle_type_more;
            strProvince = spinnerProvince.getSelectedItem().toString();
            strPackage_type = spinnerPackageType.getSelectedItem().toString();
            strLanguage = spinnerLanguage.getSelectedItem().toString();
            PackageData packageData = new PackageData(guideID,strGuideName,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"ยังไม่สมบูรณ์","ยังไม่สมบูรณ์_"+strPackage_type,strLat,strLng,strLocationName,"0.0");
            mReference.child("Packages").child(packageID).setValue(packageData);
            viewPager.setCurrentItem(getItem(+1),true);
            return true;
        }
        strProvince = spinnerProvince.getSelectedItem().toString();
        strPackage_type = spinnerPackageType.getSelectedItem().toString();
        strLanguage = spinnerLanguage.getSelectedItem().toString();
        PackageData packageData = new PackageData(guideID,strGuideName,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"ยังไม่สมบูรณ์","ยังไม่สมบูรณ์_"+strPackage_type,strLat,strLng,strLocationName,"0.0");
        mReference.child("Packages").child(packageID).setValue(packageData);
        viewPager.setCurrentItem(getItem(+1),true);
        return true;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() +1;
    }

}
