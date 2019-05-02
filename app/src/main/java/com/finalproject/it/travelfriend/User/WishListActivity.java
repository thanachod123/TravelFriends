package com.finalproject.it.travelfriend.User;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;

import com.finalproject.it.travelfriend.User.Category.ViewHolderPackageUser;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

public class WishListActivity extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferencePackage,mReferenceGuide,mReferenceWishList;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData,ViewHolderPackageUser> packageAdapter;
    RecyclerView recyclerWishList;
    FirebaseAuth mAuth;
    String touristId,favoriteStatus;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
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

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.check_detail_user_dialog);
        mDialog.getWindow().setLayout(900, 600);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceGuide = mDatabase.getReference().child("Users");
        mReferenceWishList = mDatabase.getReference().child("Favorites");
        recyclerWishList = findViewById(R.id.recyclerWishList);
        touristId = mAuth.getUid();
        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReferenceWishList.child(touristId).orderByChild("status").equalTo("true"),PackageData.class).build();
        packageAdapter = new FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPackageUser holder, final int position, @NonNull PackageData model) {
                final String packageId2 = packageAdapter.getRef(position).getKey();
                mReferencePackage.child(packageId2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String strNamePackage = dataSnapshot.child("name").getValue(String.class);
                        String strProvincePackage = dataSnapshot.child("province").getValue(String.class);
                        String strActivity = dataSnapshot.child("package_type").getValue(String.class);
                        String strPrice = dataSnapshot.child("price_per_person").getValue(String.class);
                        String strVehicleType = dataSnapshot.child("vehicle_type").getValue(String.class);
                        String strAverageRating = dataSnapshot.child("average_rating").getValue(String.class);
                        String strGuideId = dataSnapshot.child("guideId").getValue(String.class);
                        String strImage = dataSnapshot.child("image").getValue(String.class);

                        if (strVehicleType.equals("Car")){
                            Picasso.with(WishListActivity.this).load(R.drawable.car)
                                    .into(holder.img_vehicle_type);
                        } else if (strVehicleType.equals("Motorcycle")){
                            Picasso.with(WishListActivity.this).load(R.drawable.scooter)
                                    .into(holder.img_vehicle_type);
                        }else if (strVehicleType.equals("Van")){
                            Picasso.with(WishListActivity.this).load(R.drawable.van)
                                    .into(holder.img_vehicle_type);
                        }else if (strVehicleType.equals("Jetski")){
                            Picasso.with(WishListActivity.this).load(R.drawable.jetboating)
                                    .into(holder.img_vehicle_type);
                        }else {
                            Picasso.with(WishListActivity.this).load(R.drawable.car)
                                    .into(holder.img_vehicle_type);
                        }
                        holder.txtNamePackage.setText(strNamePackage);
                        holder.txtProvincePackage.setText(strProvincePackage);
                        holder.txtActivity.setText(strActivity);
                        holder.txtPrice.setText(strPrice+" THB");
                        holder.txtVehicleType.setText(strVehicleType);
                        float averageRating = Float.parseFloat(strAverageRating);
                        holder.ratingBar.setRating(averageRating);
                        Picasso.with(WishListActivity.this).load(strImage).placeholder(R.drawable.package_image).into(holder.imgPackage);

                        mReferenceWishList.child(touristId).child(packageId2).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                favoriteStatus = dataSnapshot.child("status").getValue(String.class);
                                if ("true".equalsIgnoreCase(favoriteStatus)){
                                    holder.img_wish.setImageResource(R.drawable.love);
                                }else if ("false".equalsIgnoreCase(favoriteStatus)){
                                    holder.img_wish.setImageResource(R.drawable.unlove);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        holder.img_wish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("true".equalsIgnoreCase(favoriteStatus)){
                                    mReferenceWishList.child(touristId).child(packageId2).removeValue();
                                    holder.img_wish.setImageResource(R.drawable.unlove);
                                } else {
                                    mReferenceWishList.child(touristId).child(packageId2).child("status").setValue("true");
                                }


                            }
                        });

                        mReferenceGuide.child(strGuideId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String guideName,guideImage,guideSurname;
                                guideName = dataSnapshot.child("name").getValue().toString();
                                guideSurname = dataSnapshot.child("surname").getValue().toString();
                                holder.txtNameGuide.setText(guideName + " " + guideSurname);
                                guideImage = dataSnapshot.child("profile_image").getValue().toString();
                                Picasso.with(WishListActivity.this).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgGuide);
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mReferenceGuide.child(touristId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String strTouristName, strTouristSurname, strProvince, strProfile_image, strPhone, strDistrict, strCitizen_image;
                                strTouristName = dataSnapshot.child("name").getValue(String.class);
                                strTouristSurname = dataSnapshot.child("surname").getValue(String.class);
                                strProvince = dataSnapshot.child("province").getValue(String.class);
                                strProfile_image = dataSnapshot.child("profile_image").getValue(String.class);
                                strPhone = dataSnapshot.child("phone").getValue(String.class);
                                strDistrict = dataSnapshot.child("district").getValue(String.class);
                                strCitizen_image = dataSnapshot.child("citizen_image").getValue(String.class);

                                if ("".equalsIgnoreCase(strTouristName) | "".equalsIgnoreCase(strTouristSurname) | "".equalsIgnoreCase(strProvince) | "default".equalsIgnoreCase(strProfile_image) | "".equalsIgnoreCase(strPhone) | "".equalsIgnoreCase(strDistrict) | "default".equalsIgnoreCase(strCitizen_image)){                                    setupDialog();
                                } else {
                                    Intent intentEditPackage = new Intent(WishListActivity.this,DetailPackage.class);
                                    intentEditPackage.putExtra("PackageID",packageId2);
                                    startActivity(intentEditPackage);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderPackageUser onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_package_user,viewGroup,false);
                return new ViewHolderPackageUser(view);
            }
        };
        recyclerWishList.setLayoutManager(new LinearLayoutManager(WishListActivity.this));
        packageAdapter.startListening();
        recyclerWishList.setAdapter(packageAdapter);
        updatePackageAdapter();
    }

    private void setupDialog() {

        Button btnOk = mDialog.findViewById(R.id.btn_ok);
        ImageView btnExit = mDialog.findViewById(R.id.btn_exit);

        mDialog.show();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditUser = new Intent(WishListActivity.this,EditProfileUser.class);
                startActivity(intentEditUser);
                mDialog.dismiss();
            }
        });
    }

    private void updatePackageAdapter() {
        mReferencePackage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (packageAdapter!=null)
                    packageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(packageAdapter != null)
            packageAdapter.startListening();
    }

    @Override
    public void onStop() {
        if (packageAdapter != null)
            packageAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(packageAdapter != null)
            packageAdapter.startListening();
    }
}
