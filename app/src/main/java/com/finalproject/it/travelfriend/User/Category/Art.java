package com.finalproject.it.travelfriend.User.Category;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.finalproject.it.travelfriend.User.EditProfileUser;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage;
import com.finalproject.it.travelfriend.User.SearchActivity;
import com.finalproject.it.travelfriend.User.WishListActivity;
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

public class Art extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferencePackage,mReferenceGuide,mReferenceFavorite;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData,ViewHolderPackageUser> packageAdapter;
    RecyclerView recyclerArt;
    FirebaseAuth mAuth;
    String touristId;
    String favoriteStatus;
    String packageId2;
    Dialog mDialog;
    FloatingActionButton floatingSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);
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
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/FC Lamoon Bold ver 1.00.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(myCustomFont);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setExpandedTitleTypeface(myCustomFont);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));
        floatingSearch = findViewById(R.id.SearchButton);

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.check_detail_user_dialog);
        mDialog.getWindow().setLayout(900, 600);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        touristId = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceGuide = mDatabase.getReference().child("Users");
        mReferenceFavorite = mDatabase.getReference().child("Favorites");
        recyclerArt = findViewById(R.id.recyclerArt);

        floatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Art.this, SearchActivity.class));
            }

        });

        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReferencePackage.orderByChild("status_type").equalTo("ตรวจสอบแล้ว_ท่องเที่ยวทางศิลปะวิทยาการ"),PackageData.class).build();
        packageAdapter = new FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPackageUser holder, final int position, @NonNull PackageData model) {
                packageId2 = packageAdapter.getRef(position).getKey();

                holder.txtNamePackage.setText(model.getName());
                holder.txtProvincePackage.setText(model.getProvince());
                holder.txtActivity.setText(model.getPackage_type());
                holder.txtPrice.setText(model.getPrice_per_person()+" THB");
                holder.txtVehicleType.setText(model.getVehicle_type());

                mReferenceFavorite.child(touristId).child(packageId2).addValueEventListener(new ValueEventListener() {
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
                            mReferenceFavorite.child(touristId).child(packageId2).removeValue();
                            holder.img_wish.setImageResource(R.drawable.unlove);
                        } else {
                            mReferenceFavorite.child(touristId).child(packageId2).child("status").setValue("true");
                        }
                    }
                });

                float averageRating = Float.parseFloat(model.getAverage_rating());
                holder.ratingBar.setRating(averageRating);

                if (model.getVehicle_type().equals("Car")){
                    Picasso.with(Art.this).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Motorcycle")){
                    Picasso.with(Art.this).load(R.drawable.scooter)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Van")){
                    Picasso.with(Art.this).load(R.drawable.van)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Jetski")){
                    Picasso.with(Art.this).load(R.drawable.jetboating)
                            .into(holder.img_vehicle_type);
                }else {
                    Picasso.with(Art.this).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                }
                Picasso.with(Art.this).load(model.getImage()).placeholder(R.drawable.package_image).into(holder.imgPackage);
                String guideId = model.getGuideId();
                mReferenceGuide.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String guideName,guideImage,guideSurname;
                        guideName = dataSnapshot.child("name").getValue().toString();
                        guideSurname = dataSnapshot.child("surname").getValue().toString();
                        holder.txtNameGuide.setText(guideName + " " + guideSurname);
                        guideImage = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.with(Art.this).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgGuide);
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
                                    Intent intentEditPackage = new Intent(Art.this,DetailPackage.class);
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
        recyclerArt.setLayoutManager(new LinearLayoutManager(Art.this));
        packageAdapter.startListening();
        recyclerArt.setAdapter(packageAdapter);
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
                Intent intentEditUser = new Intent(Art.this,EditProfileUser.class);
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
