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
import android.widget.Toast;

import com.finalproject.it.travelfriend.Guide.WorkProceduresGuide.CheckPayment;
import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.Model.MessageModel;
import com.finalproject.it.travelfriend.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RequestPackageFragment extends Fragment {
    RecyclerView recyclerRequestPackageGuide;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking, mReferenceTourist, mReferencePackage, mReferenceNotifition, mReferenceNotiCancel, mReferencemessage;
    FirebaseRecyclerOptions<BookingData> options;
    FirebaseRecyclerAdapter<BookingData, ViewHolderBookingGuide> bookingAdapter;
    String guideID, strMessage;
    Dialog mDialog1, mDialog2, mDialog3;
    ImageView bg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_request_package, container, false);
        recyclerRequestPackageGuide = view.findViewById(R.id.recyclerRequestPackage);
        bg = view.findViewById(R.id.bgrequest);
        mDialog1 = new Dialog(getContext());
        mDialog1.setContentView(R.layout.request_package_guide_dialog_one);
        mDialog1.getWindow().setLayout(900, 500);
        mDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bg.setVisibility(View.VISIBLE);
//        mDialog2 = new Dialog(getContext());
//        mDialog2.setContentView(R.layout.request_package_user_dialog_two);
//        mDialog2.getWindow().setLayout(900,500);
//        mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        mDialog3 = new Dialog(getContext());
//        mDialog3.setContentView(R.layout.request_package_user_dialog_three);
//        mDialog3.getWindow().setLayout(900,500);
//        mDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceTourist = mDatabase.getReference().child("Users");
        mReferencemessage = mDatabase.getReference().child("MessagesUser");
        mReferenceNotifition = mDatabase.getReference().child("Notification").child("NotificationAccepts");
        mReferenceNotiCancel = mDatabase.getReference().child("Notification").child("NotificationCancel");
        guideID = mAuth.getCurrentUser().getUid();
        strMessage = mReferencemessage.push().getKey();

        options = new FirebaseRecyclerOptions.Builder<BookingData>()
                .setQuery(mReferenceBooking.orderByChild("status_guideId").equalTo("รอการตอบรับ_" + guideID), BookingData.class).build();
        bookingAdapter = new FirebaseRecyclerAdapter<BookingData, ViewHolderBookingGuide>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBookingGuide holder, final int position, @NonNull BookingData model) {
                holder.txtDay.setText(model.getBooking_date());
                holder.txtNumTourist.setText(model.getBooking_number_tourist() + " คน");
                holder.txtBookingStatus.setText("รอการตอบรับ");
                bg.setVisibility(View.GONE);
                final String touristId = model.getTouristId();
                final String packageId = model.getPackageId();
                final String requestStatus = model.getRequest_status();

                if ("not_purchase_booking".equalsIgnoreCase(requestStatus)) {
                    holder.txtBookingStatus.setText("รอการชำระเงิน");
                } else if ("not_accept_booking".equalsIgnoreCase(requestStatus)) {
                    holder.txtBookingStatus.setText("รอการตอบรับ");
                } else if ("wait_check_payment".equalsIgnoreCase(requestStatus)) {
                    holder.txtBookingStatus.setText("รอการตรวจสอบการชำระเงิน");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ("not_accept_booking".equalsIgnoreCase(requestStatus)) {
                            String bookingId = bookingAdapter.getRef(position).getKey();
                            showDialog1(bookingId);

                        } else if ("wait_check_payment".equalsIgnoreCase(requestStatus)) {
                            String bookingId = bookingAdapter.getRef(position).getKey();
                            Intent intent = new Intent(getActivity(), CheckPayment.class);
                            intent.putExtra("BookingId", bookingId);
                            startActivity(intent);
                        }
                    }
                });
                mReferenceTourist.child(touristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String touristName, touristSurname, touristImage;
                        touristName = dataSnapshot.child("name").getValue().toString();
                        touristSurname = dataSnapshot.child("surname").getValue().toString();
                        touristImage = dataSnapshot.child("profile_image").getValue().toString();

                        holder.txtTouristName.setText(touristName + " " + touristSurname);
                        if (touristImage.isEmpty()) {
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
                        String packageName, packageDescription;
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_booking_package_guide, parent, false);
                return new ViewHolderBookingGuide(view);
            }
        };
        recyclerRequestPackageGuide.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingAdapter.startListening();
        recyclerRequestPackageGuide.setAdapter(bookingAdapter);
        updateBookingAdapter();

        return view;
    }

    private void showDialog1(final String bookingId) {
        Button btnCancel = mDialog1.findViewById(R.id.btn_cancel);
        Button btnAccepted = mDialog1.findViewById(R.id.btn_accepted);
        ImageView btnExit = mDialog1.findViewById(R.id.btn_exit);
        mDialog1.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReferenceBooking.child(bookingId).removeValue();
                mDialog1.dismiss();

//                        HashMap<String, String> notification = new HashMap<>();
//                        notification.put("from : ", guideID);
//
//                        mReferenceNotiCancel.child().push()
//                                .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(getActivity() , "send notification !! " , Toast.LENGTH_SHORT).show();
//                            }
//                        });
//

            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog1.dismiss();
            }
        });
        btnAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReferenceBooking.child(bookingId).child("request_status").setValue("not_purchase_booking");
                mDialog1.dismiss();

                mReferenceBooking.child(bookingId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String packageID = dataSnapshot.child("packageId").getValue().toString();
                        String touristId = dataSnapshot.child("touristId").getValue().toString();
                        String type = "ยืนยันคำขอแล้ว";

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String strDate = dateFormat.format(date);

                        final String tid = dataSnapshot.child("touristId").getValue().toString();
                        HashMap<String, String> notification = new HashMap<>();
                        notification.put("from : ", guideID);


                        final MessageModel message = new MessageModel(packageID, guideID, bookingId, touristId, strDate, type);
                        mReferencemessage.child(strMessage).setValue(message);

                        mReferenceNotifition.child(tid).push()
                                .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "send notification !! ", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

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
