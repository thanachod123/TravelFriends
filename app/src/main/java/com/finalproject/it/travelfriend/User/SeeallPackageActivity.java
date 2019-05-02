package com.finalproject.it.travelfriend.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.Category.ViewHolderPackageUser;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SeeallPackageActivity extends AppCompatActivity {

    RecyclerView mrecyclerView;
    FirebaseDatabase mDatabase;
    Toolbar toolbar;
    DatabaseReference mReferencePackage, mReferenceGuide;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser> packageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeall_package);

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);
        toolbar.setTitle("แพ็คเกจนำเที่ยวแนะนำ");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_app_bar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mrecyclerView = findViewById(R.id.RecyclerviewSeeall);
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceGuide = mDatabase.getReference().child("Users");


        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReferencePackage.orderByChild("average_rating").equalTo("5"), PackageData.class).build();

        packageAdapter = new FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPackageUser holder, final int position, @NonNull PackageData model) {


                holder.txtProvincePackage.setText(model.getProvince());
                holder.txtActivity.setText(model.getPackage_type());
                holder.txtPrice.setText(model.getPrice_per_person() + " THB");
                holder.txtVehicleType.setText(model.getVehicle_type());

                float averageRating = Float.parseFloat(model.getAverage_rating());
                holder.ratingBar.setRating(averageRating);

                if (model.getVehicle_type().equals("Car")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Motorcycle")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.scooter)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Van")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.van)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Jetski")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.jetboating)
                            .into(holder.img_vehicle_type);
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                }
                Picasso.with(getApplicationContext()).load(model.getImage()).placeholder(R.drawable.package_image).into(holder.imgPackage);
                String guideId = model.getGuideId();
                mReferenceGuide.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String guideName, guideImage, guideSurname;
                        guideName = dataSnapshot.child("name").getValue().toString();
                        guideSurname = dataSnapshot.child("surname").getValue().toString();
                        holder.txtNameGuide.setText(guideName + " " + guideSurname);
                        guideImage = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.with(getApplication()).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgGuide);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentEditPackage = new Intent(getApplicationContext(), DetailPackage.class);
                        String packageId = packageAdapter.getRef(position).getKey();
                        intentEditPackage.putExtra("PackageID", packageId);
                        startActivity(intentEditPackage);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mrecyclerView.setLayoutManager(layoutManager);

        packageAdapter.startListening();
        mrecyclerView.setAdapter(packageAdapter);
        updatePackageAdapter();
    }

    private void updatePackageAdapter() {
        mReferencePackage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (packageAdapter != null)
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
        if (packageAdapter != null)
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
        if (packageAdapter != null)
            packageAdapter.startListening();
    }
}

