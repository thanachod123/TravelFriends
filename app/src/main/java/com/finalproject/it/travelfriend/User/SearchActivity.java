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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.Category.Nature;
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

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView mSearch;
    private RecyclerView mResultlist;
    private DatabaseReference mReferenceGuide, mReferencePackage,mReferenceFavorite;
    ImageButton mcancel;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser> packageAdapter;
    Dialog mDialog;
    String touristId;
    String favoriteStatus;
    String packageId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);
        toolbar.setTitle("ค้นหา");
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
        mDialog.getWindow().setLayout(900, 500);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mReferencePackage = FirebaseDatabase.getInstance().getReference().child("Packages");
        mReferenceGuide = FirebaseDatabase.getInstance().getReference().child("Users");
        mReferenceFavorite = FirebaseDatabase.getInstance().getReference().child("Favorites");

        mAuth = FirebaseAuth.getInstance();
        touristId = mAuth.getUid();
        mSearch = findViewById(R.id.search_field);
        mcancel = findViewById(R.id.buttonCancel);
        mAuth = FirebaseAuth.getInstance();
        mResultlist = findViewById(R.id.recycleDetail_search);
        mResultlist.setLayoutManager(new LinearLayoutManager(this));

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mcancel.setVisibility(View.VISIBLE);
                } else {
                    mcancel.setVisibility(View.GONE);
                }


                final   String searchText = mSearch.getText().toString();
                if (searchText.equals("")) {
                    packageAdapter.stopListening();

                } else {
                    firebaseUserSearch(searchText);
                }
                mcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSearch.setText("");
                    }
                });
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void firebaseUserSearch(String searchText) {


        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReferencePackage.orderByChild("province")
                        .startAt(searchText)
                        .endAt(searchText + "\uf8ff"), PackageData.class).build();

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
                    Picasso.with(SearchActivity.this).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Motorcycle")){
                    Picasso.with(SearchActivity.this).load(R.drawable.scooter)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Van")){
                    Picasso.with(SearchActivity.this).load(R.drawable.van)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Jetski")){
                    Picasso.with(SearchActivity.this).load(R.drawable.jetboating)
                            .into(holder.img_vehicle_type);
                }else {
                    Picasso.with(SearchActivity.this).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                }
                Picasso.with(SearchActivity.this).load(model.getImage()).placeholder(R.drawable.package_image).into(holder.imgPackage);
                String guideId = model.getGuideId();
                mReferenceGuide.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String guideName,guideImage,guideSurname;
                        guideName = dataSnapshot.child("name").getValue().toString();
                        guideSurname = dataSnapshot.child("surname").getValue().toString();
                        holder.txtNameGuide.setText(guideName + " " + guideSurname);
                        guideImage = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.with(SearchActivity.this).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgGuide);
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

                                if ("".equalsIgnoreCase(strTouristName) | "".equalsIgnoreCase(strTouristSurname) | "".equalsIgnoreCase(strProvince) | "".equalsIgnoreCase(strProfile_image) | "".equalsIgnoreCase(strPhone) | "".equalsIgnoreCase(strDistrict) | "".equalsIgnoreCase(strCitizen_image)){
                                    setupDialog();
                                } else {
                                    Intent intentEditPackage = new Intent(SearchActivity.this,DetailPackage.class);
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
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_package_user, viewGroup, false);
                return new ViewHolderPackageUser(view);
            }
        };

        packageAdapter.startListening();
        mResultlist.setAdapter(packageAdapter);
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
                Intent intentEditUser = new Intent(SearchActivity.this,EditProfileUser.class);
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
}
