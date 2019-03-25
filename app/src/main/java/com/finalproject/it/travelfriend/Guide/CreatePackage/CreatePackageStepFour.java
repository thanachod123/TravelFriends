package com.finalproject.it.travelfriend.Guide.CreatePackage;


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
import android.widget.Spinner;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePackageStepFour extends Fragment {
    Button btnSaveNext,btnBack;
    NonSwipeableViewPager viewPager;
    Spinner spinner_max_guest,spinner_bank;
    EditText edt_price,edt_bank_number;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    String guideID,packageID;
    String strProvince,strPackage_type,strVehicle_type,strBank,strBank_number,strDescription,strImage,strName,strNumberTourist,strPrice_per_person,strSchedule,strLanguage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_package_four, container, false);
        spinner_max_guest = view.findViewById(R.id.spinner_max_guests);
        spinner_bank = view.findViewById(R.id.spinner_bank);
        edt_price = view.findViewById(R.id.editTextPrice);
        edt_bank_number = view.findViewById(R.id.editText_bank_number);
        btnSaveNext = view.findViewById(R.id.btn_save_next);
        btnBack = view.findViewById(R.id.btn_back);
        viewPager = getActivity().findViewById(R.id.view_pager);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        guideID = mAuth.getCurrentUser().getUid();
        getPackageData();

        btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPackageStepFour();
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

    private boolean createPackageStepFour() {
        strPrice_per_person = edt_price.getText().toString();
        strBank_number = edt_bank_number.getText().toString();
        strNumberTourist = spinner_max_guest.getSelectedItem().toString();
        strBank = spinner_bank.getSelectedItem().toString();

        if (strPrice_per_person.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุราคาต่อคน", Toast.LENGTH_SHORT).show();
            edt_price.requestFocus();
            return false;
        } else if (strBank_number.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุเลขบัญชี", Toast.LENGTH_SHORT).show();
            edt_bank_number.requestFocus();
            return false;
        }
        PackageData packageData = new PackageData(guideID,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"ยังไม่สมบูรณ์");
        mReference.child("Packages").child(packageID).setValue(packageData);
        viewPager.setCurrentItem(getItemFornext(+1),true);
        return true;

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

                        spinner_max_guest.setSelection(((ArrayAdapter<String>)spinner_max_guest.getAdapter()).getPosition(strNumberTourist));
                        spinner_bank.setSelection(((ArrayAdapter<String>)spinner_bank.getAdapter()).getPosition(strBank));
                        edt_price.setText(strPrice_per_person);
                        edt_bank_number.setText(strBank_number);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private int getItemFornext(int i) {
        return viewPager.getCurrentItem() +1;
    }

    private int getItemForprevious(int i) {
        return viewPager.getCurrentItem() -1;
    }
}