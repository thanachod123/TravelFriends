package com.finalproject.it.travelfriend.User;

import android.app.Dialog;
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

import com.finalproject.it.travelfriend.Guide.BookingPackage.UpcomingPackageFragment;
import com.finalproject.it.travelfriend.Guide.ViewholderMessage;
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

public class NotificationUserFragment extends Fragment {

    Toolbar toolbar;
    RecyclerView mRecycleviewMessage;
    DatabaseReference mReferenceMessage, mReferenceUsers, mReferencePackages;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    String MessageId;
    FirebaseRecyclerOptions<MessageModel> options;
    FirebaseRecyclerAdapter<MessageModel, ViewholderMessage> firebaseRecyclerAdapter;
    ImageView bgNotification;
    Dialog mDialog1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification_user, container, false);
        toolbar = view.findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(getActivity(), R.style.FontForActionBar);
        toolbar.setTitle("แจ้งเตือน");

        mDialog1 = new Dialog(getContext());
        mDialog1.setContentView(R.layout.delete_notification_dialog);
        mDialog1.getWindow().setLayout(900, 500);
        mDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bgNotification = view.findViewById(R.id.bgnotification);
        mRecycleviewMessage = view.findViewById(R.id.recyclerview);
        mRecycleviewMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mReferenceMessage = mDatabase.getReference().child("MessagesUser");
        mReferenceUsers = mDatabase.getReference().child("Users");
        mReferencePackages = mDatabase.getReference().child("Packages");

        bgNotification.setVisibility(View.VISIBLE);
        setupRecycle();

        return view;

    }

    private void setupRecycle() {


        Query query = mReferenceMessage.orderByChild("touristId").equalTo(mAuth.getCurrentUser().getUid());

        options = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MessageModel, ViewholderMessage>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewholderMessage holder, final int position, @NonNull MessageModel model) {


                holder.packagename.setText(model.getPackageId());
                holder.nametour.setText(model.getGuideId());
                holder.textDate.setText(model.getDate());
                holder.status.setText(model.getType());

                bgNotification.setVisibility(View.GONE);
                String guideId = model.getGuideId();
                mReferenceUsers.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Username, Userimage;
                        Username = dataSnapshot.child("name").getValue().toString();
                        Userimage = dataSnapshot.child("profile_image").getValue().toString();

                        holder.nametour.setText(Username);
                        Picasso.with(getActivity()).load(Userimage).placeholder(R.drawable.default_profile).into(holder.imgUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String packageId = model.getPackageId();
                mReferencePackages.child(packageId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Nametrip;
                        Nametrip = dataSnapshot.child("name").getValue().toString();
                        holder.packagename.setText(Nametrip);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageId = firebaseRecyclerAdapter.getRef(position).getKey();

                        mReferenceMessage.child(MessageId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String type_status = dataSnapshot.child("type").getValue().toString();

                                if (type_status.equals("ยืนยันคำขอแล้ว")) {

                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookingUserFragment()).commit();
                                }
                                if (type_status.equals("ตรวจสอบการชำระเงินแล้ว")) {
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookingUserFragment()).commit();
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
        updateBookingAdapter();

    }

    private void updateBookingAdapter() {
        mReferenceMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (firebaseRecyclerAdapter != null)
                    firebaseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}