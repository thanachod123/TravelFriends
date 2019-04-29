package com.finalproject.it.travelfriend.Guide;

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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.finalproject.it.travelfriend.Guide.WorkProceduresGuide.CheckPayment;
import com.finalproject.it.travelfriend.HomeActivity;
import com.finalproject.it.travelfriend.MainGuideActivity;
import com.finalproject.it.travelfriend.Model.MessageModel;
import com.finalproject.it.travelfriend.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NotificationGuideFragment extends Fragment {

    Toolbar toolbar;
    RecyclerView mRecycleviewMessage;
    DatabaseReference mReferenceMessage, mReferenceUsers, mReferencePackages, mReferenceBookings;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    String MessageId;
    FirebaseRecyclerOptions<MessageModel> detail;
    FirebaseRecyclerAdapter<MessageModel, ViewholderMessage> firebaseRecyclerAdapter;
    ImageView bgNotification;
    Dialog mDialog1;
    ProgressBar loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification_guide, container, false);
        toolbar = view.findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(getActivity(), R.style.FontForActionBar);
        toolbar.setTitle("การแจ้งเตือน");
        mRecycleviewMessage = view.findViewById(R.id.recyclerview_message);
        mRecycleviewMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        bgNotification = view.findViewById(R.id.bgnotification);
        bgNotification.setVisibility(View.VISIBLE);
        loading = view.findViewById(R.id.loadingNotification);


        mDialog1 = new Dialog(getContext());
        mDialog1.setContentView(R.layout.delete_notification_dialog);
        mDialog1.getWindow().setLayout(900, 500);
        mDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loading.setVisibility(View.GONE);

        setupRecycle();
        return view;
    }

    private void setupRecycle() {
        mReferenceUsers = mDatabase.getReference().child("Users");
        mReferencePackages = mDatabase.getReference().child("Packages");
        mReferenceMessage = mDatabase.getReference().child("MessagesGuide");
        mReferenceBookings = mDatabase.getReference().child("Booking");

        Query query = mReferenceMessage.orderByChild("guideId").equalTo(mAuth.getCurrentUser().getUid());

        detail = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MessageModel, ViewholderMessage>(detail) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewholderMessage holder, final int position, @NonNull final MessageModel model) {

                loading.setVisibility(View.GONE);

                holder.packagename.setText(model.getPackageId());
                holder.nametour.setText(model.getGuideId());
                holder.textDate.setText(model.getDate());
                holder.status.setText(model.getType());
                bgNotification.setVisibility(View.GONE);

                String touristId = model.getTouristId();
                mReferenceUsers.child(touristId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName, userImage;
                        userName = dataSnapshot.child("name").getValue().toString();
                        userImage = dataSnapshot.child("profile_image").getValue().toString();


                        holder.nametour.setText(userName);

                        Picasso.with(getActivity()).load(userImage).placeholder(R.drawable.default_profile).into(holder.imgUser);


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String packageId = model.getPackageId();
                mReferencePackages.child(packageId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nameTrip;
                        nameTrip = dataSnapshot.child("name").getValue().toString();
                        holder.packagename.setText(nameTrip);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MessageId = firebaseRecyclerAdapter.getRef(position).getKey();

                        mReferenceMessage.child(MessageId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String type1 = dataSnapshot.child("type").getValue().toString();
                                final String bookingid = dataSnapshot.child("bookingId").getValue().toString();

                                if (type1.equals("สมัครแพ็คเกจ")) {
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookingGuideFragment()).commit();
                                }

                                if (type1.equals("ชำระเงินแล้ว")) {

                                    mReferenceBookings.child(bookingid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            Intent intent = new Intent(getActivity(), CheckPayment.class);
                                            intent.putExtra("BookingId", bookingid);

                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Button btnCancel = mDialog1.findViewById(R.id.btn_cancel);
                        Button btnAccepted = mDialog1.findViewById(R.id.btn_accepted);
                        ImageView btnExit = mDialog1.findViewById(R.id.btn_exit);
                        mDialog1.show();


                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog1.dismiss();
                            }
                        });

                        btnAccepted.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loading.setVisibility(View.GONE);

                                MessageId = firebaseRecyclerAdapter.getRef(position).getKey();
                                mReferenceMessage.child(MessageId).removeValue();
                                mDialog1.dismiss();
                            }
                        });

                        btnExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog1.dismiss();
                            }
                        });

                        return false;
                    }
                });
            }

            @NonNull
            @Override
            public ViewholderMessage onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_notification, viewGroup, false);
                ViewholderMessage viewholderMessage = new ViewholderMessage(view);
                return viewholderMessage;
            }
        };
        mRecycleviewMessage.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
}