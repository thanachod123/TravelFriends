package com.finalproject.it.travelfriend.User.BookingPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.Category.Adventure;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostTripPackageUserFragment extends Fragment {
    RecyclerView recyclerPostTripPackageUser;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceTourist,mReferencePackage;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData,ViewHolderBookingUser> bookingAdapter;
    String touristID;
    ImageView imageView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_post_trip_user_package, container, false);
        recyclerPostTripPackageUser = view.findViewById(R.id.recyclerPostTripUser);

//        progressBarFirst = view.findViewById(R.id.progressBarFirst);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        imageView = view.findViewById(R.id.bgrequest);
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        touristID = mAuth.getCurrentUser().getUid();
        imageView.setVisibility(View.VISIBLE);

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_touristId").equalTo("จบลงไปแล้ว_"+touristID),BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingUser holder, int position, @NonNull BookingData model) {
                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                holder.txtBookingStatus.setText("จบลงไปแล้ว");

                imageView.setVisibility(View.GONE);

                final String guideId = model.getGuideId();
                final String packageId = model.getPackageId();

                mReferenceTourist.child(guideId).addValueEventListener(new ValueEventListener() {
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
                        Picasso.with(getActivity()).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgProfileGuide);
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentEditPackage = new Intent(getActivity(), DetailPackage2.class);
                        intentEditPackage.putExtra("PackageID",packageId);
                        startActivity(intentEditPackage);
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
        recyclerPostTripPackageUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerPostTripPackageUser.setAdapter(bookingAdapter);
        updateBookingAdapter();

        return view;
    }

    private void updateBookingAdapter() {
        mReferenceBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (bookingAdapter!=null)
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
        if(bookingAdapter != null)
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
        if(bookingAdapter != null)
            bookingAdapter.startListening();
    }
}