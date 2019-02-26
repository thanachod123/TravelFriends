package com.finalproject.it.travelfriend.FragmentGuide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.finalproject.it.travelfriend.HomeActivity;
import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileGuideFragment extends Fragment {
    Button mSignout;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_guide, container, false);
        mSignout = view.findViewById(R.id.btn_logout);
        mSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
//                sharedPrefManager.clear();

                startActivity(intent);
            }
        });
        return view;
    }
}
