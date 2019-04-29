package com.finalproject.it.travelfriend.Guide;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.finalproject.it.travelfriend.Model.ReviewData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.RegisterPackage.ViewHolderPackageUserReview;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPackageGuide extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferencePackage,mReferenceGuide, mReferenceReview;
    String strProvince, strPackage_type, strVehicle_type, strDescription, strImage, strPackageName, strNumberTourist, strPrice_per_person, strSchedule, strLanguage,strLat,strLng,strLocationName,strAverageRating;
    TextView txtProvince,txtPackage_type,txtVehicle_type,txtDescription,txtPackageName, txtNumberTourist, txtPricePerPerson, txtSchedule,txtLanguage,txtNameGuide;
    ImageView img_package;
    CircleImageView img_guide_image;
    String packageID,guideID;
    String strPhone;
    Double lat;
    Double lng;

    RatingBar ratingBar;
    FirebaseRecyclerOptions<ReviewData> options;
    FirebaseRecyclerAdapter<ReviewData,ViewHolderPackageUserReview> reviewAdapter;
    RecyclerView recyclerReview;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_package_guide);
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
        toolbar.setTitleTextAppearance(DetailPackageGuide.this, R.style.FontForActionBar);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtProvince = findViewById(R.id.txt_province);
        txtPackage_type = findViewById(R.id.txt_activity);
        txtVehicle_type = findViewById(R.id.txt_vehicle_type);
        txtDescription = findViewById(R.id.txt_description);
        txtNameGuide = findViewById(R.id.txt_name_guide);
        img_package = findViewById(R.id.img_package);
        img_guide_image = findViewById(R.id.img_guide_image);
        txtPackageName = findViewById(R.id.txt_name_package);
        txtNumberTourist = findViewById(R.id.txt_max_guest);
        txtPricePerPerson = findViewById(R.id.txt_price_per_person);
        txtSchedule = findViewById(R.id.txt_schedule);
        txtLanguage = findViewById(R.id.txt_language);
        ratingBar = findViewById(R.id.rating_bar);
        recyclerReview = findViewById(R.id.recyclerReview);

        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceGuide = mDatabase.getReference().child("Users");
        mReferenceReview = mDatabase.getReference().child("Reviews");
        packageID = getIntent().getExtras().getString("PackageID");
        getPackageData();

        options = new FirebaseRecyclerOptions.Builder<ReviewData>()
                .setQuery(mReferenceReview.child(packageID),ReviewData.class).build();

        reviewAdapter = new FirebaseRecyclerAdapter<ReviewData, ViewHolderPackageUserReview>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPackageUserReview holder, int position, @NonNull ReviewData model) {
                String touristId = reviewAdapter.getRef(position).getKey();

                float clientRating = Float.parseFloat(model.getRating());
                holder.txtComment.setText(model.getComment());
                holder.ratingBar.setRating(clientRating);

                mReferenceGuide.child(touristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String strTouristName = dataSnapshot.child("name").getValue(String.class);
                        String strTouristSurName = dataSnapshot.child("surname").getValue(String.class);
                        String strProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                        strPhone = dataSnapshot.child("phone").getValue(String.class);


                        holder.txtName.setText(strTouristName+ " " +strTouristSurName);
                        Picasso.with(DetailPackageGuide.this).load(strProfileImage).placeholder(R.drawable.default_profile).into(holder.circleImageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public ViewHolderPackageUserReview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_package_review,viewGroup,false);
                return new ViewHolderPackageUserReview(view);
            }
        };
        recyclerReview.setLayoutManager(new LinearLayoutManager(DetailPackageGuide.this));
        reviewAdapter.startListening();
        recyclerReview.setAdapter(reviewAdapter);
        updatePackageAdapter();

    }

    private void updatePackageAdapter() {
        mReferenceReview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (reviewAdapter != null)
                    reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPackageData() {
        mReferencePackage.child(packageID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        strPackage_type = dataSnapshot.child("package_type").getValue(String.class);
                        strProvince = dataSnapshot.child("province").getValue(String.class);
                        strVehicle_type = dataSnapshot.child("vehicle_type").getValue(String.class);
                        strDescription = dataSnapshot.child("description").getValue(String.class);
                        strImage = dataSnapshot.child("image").getValue(String.class);
                        strPackageName = dataSnapshot.child("name").getValue(String.class);
                        strNumberTourist = dataSnapshot.child("number_tourist").getValue(String.class);
                        strPrice_per_person = dataSnapshot.child("price_per_person").getValue(String.class);
                        strSchedule = dataSnapshot.child("schedule").getValue(String.class);
                        strLanguage = dataSnapshot.child("language").getValue(String.class);
                        guideID = dataSnapshot.child("guideId").getValue(String.class);
                        strAverageRating = dataSnapshot.child("average_rating").getValue(String.class);

                        float averageRating = Float.parseFloat(strAverageRating);
                        ratingBar.setRating(averageRating);

                        toolbar.setTitle(strPackageName);
                        txtPackageName.setText(strPackageName);
                        txtDescription.setText(strDescription);
                        txtProvince.setText(strProvince);
                        txtPackage_type.setText(strPackage_type);
                        txtLanguage.setText(strLanguage);
                        txtVehicle_type.setText(strVehicle_type);
                        txtSchedule.setText(strSchedule);
                        txtNumberTourist.setText(strNumberTourist);
                        txtPricePerPerson.setText(strPrice_per_person+ " THB");

                        if ("".equalsIgnoreCase(strImage)) {
                            strImage = "default";
                        }
                        Picasso.with(DetailPackageGuide.this).load(strImage).placeholder(R.drawable.package_image).into(img_package);
                        mReferenceGuide.child(guideID)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String strProfile_image, strName, strSurname;
                                        strProfile_image = dataSnapshot.child("profile_image").getValue(String.class);
                                        strName = dataSnapshot.child("name").getValue(String.class);
                                        strSurname = dataSnapshot.child("surname").getValue(String.class);

                                        txtNameGuide.setText(strName+" "+strSurname);
                                        if ("".equalsIgnoreCase(strProfile_image)) {
                                            strProfile_image = "default";
                                        }
                                        Picasso.with(DetailPackageGuide.this).load(strProfile_image).placeholder(R.drawable.package_image).into(img_guide_image);
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
    public void onMapReady(final GoogleMap googleMap) {
        mReferencePackage.child(packageID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strLat = dataSnapshot.child("lat").getValue(String.class);
                strLng = dataSnapshot.child("lng").getValue(String.class);
                strLocationName = dataSnapshot.child("location_name").getValue(String.class);

                lat = Double.parseDouble(strLat);
                lng = Double.parseDouble(strLng);

                mMap = googleMap;
                LatLng latLng = new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(latLng).title(strLocationName));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(reviewAdapter != null)
            reviewAdapter.startListening();
    }

    @Override
    public void onStop() {
        if (reviewAdapter != null)
            reviewAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(reviewAdapter != null)
            reviewAdapter.startListening();
    }
}
