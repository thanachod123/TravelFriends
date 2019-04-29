package com.finalproject.it.travelfriend.User.RegisterPackage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.it.travelfriend.Guide.BookingPackage.ViewHolderBookingGuide;
import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.BookingPackage.ViewHolderBookingUser;
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

public class ShowHistoryGuide extends AppCompatActivity {

    String GuideId;
    RecyclerView recyclerShowhistory;
    FirebaseAuth mAuth;
    DatabaseReference mReferenceBooking, mReferencePackage, mReferenceUser;
    FirebaseDatabase mDatabase;
    Toolbar toolbar;

    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData, ViewHolderBookingUser> bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_guide);

        recyclerShowhistory = findViewById(R.id.recyclerShowhistory);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);
        toolbar.setTitle("ประวัติการนำเที่ยว");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_app_bar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        StatusBarUtil.setColor(this,getResources().getColor(R.color.yellow));

        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceUser = mDatabase.getReference().child("Users");


        GuideId = getIntent().getExtras().getString("GuideId");

        setupRecyclerview();

    }

    private void setupRecyclerview() {

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_guideId").equalTo("จบลงไปแล้ว_"+GuideId),BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingUser holder, int position, @NonNull BookingData model) {
                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                holder.txtBookingStatus.setText("จบลงไปแล้ว");


                final String guideId = model.getGuideId();
                final String packageId = model.getPackageId();

                mReferenceUser.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String guideName,guideSurname,guideImage;
                        guideName = dataSnapshot.child("name").getValue().toString();
                        guideSurname = dataSnapshot.child("surname").getValue().toString();
                        guideImage = dataSnapshot.child("profile_image").getValue().toString();

                        holder.txtGuideName.setText(guideName + " " + guideSurname);
                        if (guideImage.isEmpty()){
                            guideImage = "Default";
                        }
                        Picasso.with(getApplication()).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgProfileGuide);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mReferencePackage.child(packageId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String packageName,packageDescription;
                        packageName = dataSnapshot.child("name").getValue().toString();
                        packageDescription = dataSnapshot.child("description").getValue().toString();

                        holder.txtNameTrip.setText(packageName);
                        holder.txtDescription.setText(packageDescription);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public ViewHolderBookingUser onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_booking_package_user,parent,false);
                return new ViewHolderBookingUser(view);
            }
        };

        recyclerShowhistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerShowhistory.setAdapter(bookingAdapter);
        bookingAdapter.startListening();

        updateBookingAdapter();
    }


    private void updateBookingAdapter() {

        mReferenceBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (bookingAdapter != null)
                    bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (bookingAdapter != null)
            bookingAdapter.startListening();
    }

    @Override
    public void onStop() {
        if (bookingAdapter != null)
            bookingAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookingAdapter != null)
            bookingAdapter.startListening();
    }
}

