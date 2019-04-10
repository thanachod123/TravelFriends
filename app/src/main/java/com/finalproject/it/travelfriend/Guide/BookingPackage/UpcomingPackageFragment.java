package com.finalproject.it.travelfriend.Guide.BookingPackage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.finalproject.it.travelfriend.Guide.WorkProceduresGuide.CheckEndTrip;
import com.finalproject.it.travelfriend.Guide.WorkProceduresGuide.DetailBookingGuide;
import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.WorkProceduresUser.DetailBooking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UpcomingPackageFragment extends Fragment {
    RecyclerView recyclerUpcomingPackage;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceTourist,mReferencePackage;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData,ViewHolderBookingGuide> bookingAdapter;
    String guideID;
    Dialog mDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_upcoming_package, container, false);
        recyclerUpcomingPackage = view.findViewById(R.id.recyclerUpcomingPackage);
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.upcoming_package_guide_dialog);
        mDialog.getWindow().setLayout(900,600);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        guideID = mAuth.getCurrentUser().getUid();

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_guideId").equalTo("กำลังจะเกิดขึ้น_"+guideID),BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingGuide>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingGuide holder, final int position, @NonNull BookingData model) {
                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                holder.txtBookingStatus.setText("กำลังจะเกิดขึ้น");

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

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String bookingId = bookingAdapter.getRef(position).getKey();
                        showDialog(bookingId);
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
        recyclerUpcomingPackage.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerUpcomingPackage.setAdapter(bookingAdapter);
        updateBookingAdapter();

        return view;
    }

    private void showDialog(final String bookingId) {
        Button btnDetail = mDialog.findViewById(R.id.btn_detail);
        Button btnEndTrip = mDialog.findViewById(R.id.btn_end_trip);
        ImageView btnExit = mDialog.findViewById(R.id.btn_exit);
        mDialog.show();

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DetailBookingGuide.class);
                intent.putExtra("BookingId",bookingId);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CheckEndTrip.class);
                intent.putExtra("BookingId",bookingId);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
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
