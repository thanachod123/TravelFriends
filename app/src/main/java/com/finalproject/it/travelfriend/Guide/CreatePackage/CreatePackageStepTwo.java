package com.finalproject.it.travelfriend.Guide.CreatePackage;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
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
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class CreatePackageStepTwo extends Fragment {
    private static final int request_Code_PackageIMG = 55;
    Button btnSaveNext,btnBack;
    EditText edt_nameTrip,edt_summaryTrip;
    ImageView img_upload;
    NonSwipeableViewPager viewPager;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    StorageReference mStorage;

    String strProvince,strPackage_type,strVehicle_type,strBank,strBank_number,strDescription,strImage,strName,strNumberTourist,strPrice_per_person,strSchedule,strLanguage;
    String guideID,packageID;
    Uri Package_imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_package_two, container, false);
        btnSaveNext = view.findViewById(R.id.btn_save_next);
        btnBack = view.findViewById(R.id.btn_back);
        edt_nameTrip = view.findViewById(R.id.edt_nametrip);
        edt_summaryTrip = view.findViewById(R.id.edt_summarytrip);
        img_upload = view.findViewById(R.id.img_upload);
        viewPager = getActivity().findViewById(R.id.view_pager);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        guideID = mAuth.getCurrentUser().getUid();
        getPackageData();

        btnSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPackageStepTwo();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(getItemForprevious(-1),true);
            }
        });
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,request_Code_PackageIMG);
            }
        });
        return view;
    }

    private boolean createPackageStepTwo() {
        strName = edt_nameTrip.getText().toString();
        strDescription = edt_summaryTrip.getText().toString();

        if (strName.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุชื่อสแพ็คเกจ", Toast.LENGTH_SHORT).show();
            edt_nameTrip.requestFocus();
            return false;
        }
        if (strDescription.trim().isEmpty()){
            Toast.makeText(getActivity(), "โปรดระบุรายละเอียดแพ็คเกจ", Toast.LENGTH_SHORT).show();
            edt_summaryTrip.requestFocus();
            return false;
        }
        PackageData packageData = new PackageData(guideID,strName,strDescription,strImage,strProvince,strPackage_type,strVehicle_type,strSchedule,strNumberTourist,strPrice_per_person,strBank,strBank_number,strLanguage,"ยังไม่สมบูรณ์","ยังไม่สมบูรณ์_"+strPackage_type);
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

                        edt_nameTrip.setText(strName);
                        edt_summaryTrip.setText(strDescription);
                        Picasso.with(getActivity()).load(strImage).placeholder(R.drawable.package_image).into(img_upload);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code_PackageIMG && resultCode == RESULT_OK
                && data != null &&data.getData() != null){
            Package_imageUri = data.getData();
            Picasso.with(getActivity()).load(Package_imageUri).into(img_upload);

            StorageReference imagePackagePath = mStorage.child("Packages").child(guideID).child(packageID).child("ImagePackage.jpg");
            imagePackagePath.putFile(Package_imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrlImagePackage = urlTask.getResult();
                    final String strPackageImage = String.valueOf(downloadUrlImagePackage);
                    mReference.child(getString(R.string.packages)).child(packageID).child("image").setValue(strPackageImage);
                }
            });
        }
    }

    private int getItemFornext(int i) {
        return viewPager.getCurrentItem() +1;
    }

    private int getItemForprevious(int i) {
        return viewPager.getCurrentItem() -1;
    }
}
