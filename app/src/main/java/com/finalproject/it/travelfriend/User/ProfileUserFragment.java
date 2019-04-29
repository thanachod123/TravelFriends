package com.finalproject.it.travelfriend.User;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.finalproject.it.travelfriend.Guide.AdapterProfileGuide;
import com.finalproject.it.travelfriend.Guide.ChangePasswordGuide;
import com.finalproject.it.travelfriend.Guide.EditProfileGuide;
import com.finalproject.it.travelfriend.HomeActivity;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.Utility.Firebase_guide_method;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserFragment extends Fragment {
    CircleImageView iv_profile_image;
    TextView mName;
    Firebase_guide_method firebase_guide_method;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;

    String userId,name,surname,password,profile_image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        iv_profile_image = view.findViewById(R.id.iv_profile_image);
        mName = view.findViewById(R.id.txt_name);

        mAuth = FirebaseAuth.getInstance();
        firebase_guide_method = new Firebase_guide_method(getContext());
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        userId = mAuth.getCurrentUser().getUid();
        setFont();
        getUser_Profile_Data();

        int[] resId = {R.drawable.resume
                , R.drawable.power_button};

        String[] list = { "แก้ไขโปรไฟล์","ออกจากระบบ"};

        AdapterProfileUser adapterProfileUser  = new AdapterProfileUser(getContext(),list,resId);
        ListView listView = view.findViewById(R.id.listViewSetting);
        listView.setAdapter(adapterProfileUser);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intentEditProfile = new Intent(getActivity(),EditProfileUser.class);
                        startActivity(intentEditProfile);
                        break;
                    case 1:
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        Intent signOut = new Intent(getActivity(), HomeActivity.class);
                        startActivity(signOut);
                }
            }
        });
        return view;
    }

    private void getUser_Profile_Data() {
        mReference.child(getString(R.string.users)).child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name = dataSnapshot.child("name").getValue().toString();
                        surname = dataSnapshot.child("surname").getValue().toString();
                        profile_image = dataSnapshot.child("profile_image").getValue().toString();
                        mName.setText(name+" "+surname);
                        if ("".equalsIgnoreCase(profile_image)){
                            profile_image = "default";
                        }
                        Picasso.with(getContext()).load(profile_image).placeholder(R.drawable.default_profile).into(iv_profile_image);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setFont() {
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        mName.setTypeface(myCustomFont);
    }
}
