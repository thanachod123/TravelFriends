package com.finalproject.it.travelfriend.User.BookingPackage;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.RegisterPackage.RequestPackageStepTwo;
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

public class UpcomingPackageUserFragment extends Fragment {
    RecyclerView recyclerUpcomingPackageUser;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceTourist,mReferencePackage,mReferenceReview;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData,ViewHolderBookingUser> bookingAdapter;
    String touristID,guideId,packageId,guideName,guideSurname,guideImage,packageName,packageDescription,rating;
    Dialog mDialog,mDialog2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_upcoming_user_package, container, false);
        recyclerUpcomingPackageUser = view.findViewById(R.id.recyclerUpcomingPackageUser);
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.upcoming_package_user_dialog);
        mDialog.getWindow().setLayout(900,600);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog2 = new Dialog(getContext());
        mDialog2.setContentView(R.layout.rating_comment_dialog);
        mDialog2.getWindow().setLayout(900,1000);
        mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        mReferenceReview = mDatabase.getReference().child("Reviews");
        touristID = mAuth.getCurrentUser().getUid();

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_touristId").equalTo("กำลังจะเกิดขึ้น_"+touristID),BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingUser holder, final int position, @NonNull BookingData model) {
                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                holder.txtBookingStatus.setText("กำลังจะเกิดขึ้น_");

                guideId = model.getGuideId();
                packageId = model.getPackageId();
                mReferenceTourist.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        showDialog(bookingId,packageId);
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
        recyclerUpcomingPackageUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerUpcomingPackageUser.setAdapter(bookingAdapter);
        updateBookingAdapter();

        return view;
    }

    private void showDialog(final String bookingId, final String packageId) {
        Button btnDetail = mDialog.findViewById(R.id.btn_detail);
        Button btnEndTrip = mDialog.findViewById(R.id.btn_end_trip);
        ImageView btnExit = mDialog.findViewById(R.id.btn_exit);
        mDialog.show();

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DetailBooking.class);
                intent.putExtra("BookingId",bookingId);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                showDialog2(bookingId,packageId);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    private void showDialog2(final String bookingId, final String packageId) {
        mDialog2.show();
        Button btnEndTrip = mDialog2.findViewById(R.id.btn_end_trip);
        ImageView btnExit = mDialog2.findViewById(R.id.btn_exit);

        final EditText edtComment = mDialog2.findViewById(R.id.edt_comment);
        final RatingBar ratingBar = mDialog2.findViewById(R.id.rating_bar);


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog2.dismiss();
            }
        });

        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strComment = edtComment.getText().toString().trim();
                rating = String.valueOf(ratingBar.getRating());
                mReferenceReview.child(packageId).child(touristID).child("rating").setValue(rating);
                mReferenceReview.child(packageId).child(touristID).child("comment").setValue(strComment);
                mReferenceBooking.child(bookingId).child("status_touristId").setValue("จบลงไปแล้ว_"+touristID);
                setAverageRating(packageId);
                mDialog2.dismiss();
            }
        });



    }

    private void setAverageRating(final String packageId) {
        try {
            mReferenceReview.child(packageId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double total = 0.0;
                    double count = 0.0;
                    double average = 0.0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        double rating = Double.parseDouble(ds.child("rating").getValue().toString());
                        total = total + rating;
                        count = count + 1;
                        average = total / count;
                    }

                    String strAverage = String.valueOf(average);
                    mReferencePackage.child(packageId).child("average_rating").setValue(strAverage);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), "Error Exception" +e , Toast.LENGTH_SHORT).show();
        }
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
