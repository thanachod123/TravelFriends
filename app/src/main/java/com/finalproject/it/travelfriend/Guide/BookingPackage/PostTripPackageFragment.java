package com.finalproject.it.travelfriend.Guide.BookingPackage;

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

import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostTripPackageFragment extends Fragment {
    RecyclerView recyclerPostTrip;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceTourist,mReferencePackage;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData,ViewHolderBookingGuide> bookingAdapter;
    String guideID;
    ImageView bg;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_post_trip_package, container, false);
        recyclerPostTrip = view.findViewById(R.id.recyclerPostTripPackage);

        mAuth = FirebaseAuth.getInstance();
        bg = view.findViewById(R.id.bgrequest);
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        guideID = mAuth.getCurrentUser().getUid();
        bg.setVisibility(View.VISIBLE);
        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_guideId").equalTo("จบลงไปแล้ว_"+guideID),BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingGuide>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingGuide holder, int position, @NonNull BookingData model) {

                bg.setVisibility(View.GONE);

                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                holder.txtBookingStatus.setText("จบลงไปแล้ว");

                final String touristId = model.getTouristId();
                final String packageId = model.getPackageId();
                mReferenceTourist.child(touristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String touristName,touristSurname,touristImage;
                        touristName = dataSnapshot.child("name").getValue().toString();
                        touristSurname = dataSnapshot.child("surname").getValue().toString();
                        touristImage = dataSnapshot.child("profile_image").getValue().toString();

                        holder.txtTouristName.setText(touristName + " " + touristSurname);
                        if (touristImage.isEmpty()){
                            touristImage = "Default";
                        }
                        Picasso.with(getActivity()).load(touristImage).placeholder(R.drawable.default_profile).into(holder.imgProfileTourist);
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
            public ViewHolderBookingGuide onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_booking_package_guide,parent,false);
                return new ViewHolderBookingGuide(view);
            }
        };
        recyclerPostTrip.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerPostTrip.setAdapter(bookingAdapter);
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
