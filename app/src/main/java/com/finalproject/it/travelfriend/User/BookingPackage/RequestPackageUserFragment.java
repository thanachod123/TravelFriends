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
import android.widget.ImageView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.RegisterPackage.RequestPackageStepTwo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RequestPackageUserFragment extends Fragment {
    RecyclerView recyclerRequestPackageUser;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferenceTourist,mReferencePackage;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData,ViewHolderBookingUser> bookingAdapter;
    String touristID;
    Dialog mDialog1,mDialog2,mDialog3;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_request_user_package, container, false);
        recyclerRequestPackageUser = view.findViewById(R.id.recyclerRequestPackageUser);
        mDialog1 = new Dialog(getContext());
        mDialog1.setContentView(R.layout.request_package_user_dialog_one);
        mDialog1.getWindow().setLayout(900,500);
        mDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog2 = new Dialog(getContext());
        mDialog2.setContentView(R.layout.request_package_user_dialog_two);
        mDialog2.getWindow().setLayout(900,500);
        mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog3 = new Dialog(getContext());
        mDialog3.setContentView(R.layout.request_package_user_dialog_three);
        mDialog3.getWindow().setLayout(900,500);
        mDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        touristID = mAuth.getCurrentUser().getUid();

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_touristId").equalTo("รอการตอบรับ_"+touristID) ,BookingData.class).build();

        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingUser holder, final int position, @NonNull BookingData model) {
                final String guideId = model.getGuideId();
                final String packageId = model.getPackageId();
                final String requestStatus = model.getRequest_status();

                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist()+" คน");
                if ("not_purchase_booking".equalsIgnoreCase(requestStatus)){
                    holder.txtBookingStatus.setText("รอการชำระเงิน");
                } else if ("not_accept_booking".equalsIgnoreCase(requestStatus)){
                    holder.txtBookingStatus.setText("รอการตอบรับ");
                } else if ("wait_check_payment".equalsIgnoreCase(requestStatus)){
                    holder.txtBookingStatus.setText("รอการตรวจสอบการชำระเงิน");
                }

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
                    public void onClick(View view) {
                        if ("not_accept_booking".equalsIgnoreCase(requestStatus)){
                            String bookingId = bookingAdapter.getRef(position).getKey();
                            showDialog1(bookingId);

                        } else if ("not_purchase_booking".equalsIgnoreCase(requestStatus)){
                            String bookingId = bookingAdapter.getRef(position).getKey();
                            showDialog2(bookingId);

                        } else if ("wait_check_payment".equalsIgnoreCase(requestStatus)){
                            String bookingId = bookingAdapter.getRef(position).getKey();
                            showDialog3(bookingId);
                        }
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
        recyclerRequestPackageUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerRequestPackageUser.setAdapter(bookingAdapter);
        updateBookingAdapter();
        return view;
    }

    private void showDialog1(final String bookingId) {
        Button btnCancel = mDialog1.findViewById(R.id.btn_cancel);
        ImageView btnExit = mDialog1.findViewById(R.id.btn_exit);
        mDialog1.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReferenceBooking.child(bookingId).removeValue();
                mDialog1.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog1.dismiss();
            }
        });
    }

    private void showDialog2(final String bookingId) {
        Button btnPurchase = mDialog2.findViewById(R.id.btn_purchase);
        ImageView btnExit = mDialog2.findViewById(R.id.btn_exit);
        mDialog2.show();
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),RequestPackageStepTwo.class);
                intent.putExtra("BookingId",bookingId);
                startActivity(intent);
                mDialog1.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog2.dismiss();
            }
        });
    }

    private void showDialog3(final String bookingId) {
        Button btnOk = mDialog3.findViewById(R.id.btn_ok);
        ImageView btnExit = mDialog3.findViewById(R.id.btn_exit);
        mDialog3.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog3.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog3.dismiss();
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
