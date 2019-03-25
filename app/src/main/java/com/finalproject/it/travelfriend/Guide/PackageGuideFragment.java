package com.finalproject.it.travelfriend.Guide;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Guide.CreatePackage.CreatePackageGuide;
import com.finalproject.it.travelfriend.Guide.ViewHolderPackageGuide;
import com.finalproject.it.travelfriend.Model.PackageData;
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

public class PackageGuideFragment extends Fragment {
    TextView tv_package1, tv_package2;
    Button btn_CreatePackage;
    RecyclerView recyclerViewPackage;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData,ViewHolderPackageGuide> packageAdapter;
    String guideID;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_package_guide, container, false);
        tv_package1 = view.findViewById(R.id.tv_package1);
        tv_package2 = view.findViewById(R.id.tv_package2);
        btn_CreatePackage = view.findViewById(R.id.btn_CreatePackage);
        recyclerViewPackage = view.findViewById(R.id.recyclerViewPackage);
        recyclerViewPackage.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Packages");
        guideID = mAuth.getCurrentUser().getUid();

        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReference.orderByChild("guideId").equalTo(guideID),PackageData.class).build();
        packageAdapter = new FirebaseRecyclerAdapter<PackageData, ViewHolderPackageGuide>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPackageGuide holder, final int position, @NonNull PackageData model) {
                holder.txtNamePackage.setText(model.getName());
                holder.txtDescPackage.setText(model.getDescription());
                holder.txtProvincePackage.setText(model.getProvince());
                holder.txtStatusPackage.setText(model.getPackage_status());
                Picasso.with(getActivity()).load(model.getImage()).placeholder(R.drawable.package_image).into(holder.imgPackage);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentEditPackage = new Intent(getActivity(),CreatePackageGuide.class);
                        String packageId = packageAdapter.getRef(position).getKey();
                        intentEditPackage.putExtra("PackageID",packageId);
                        startActivity(intentEditPackage);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderPackageGuide onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_package_guide,parent,false);
                return new ViewHolderPackageGuide(view);
            }
        };
        recyclerViewPackage.setLayoutManager(new LinearLayoutManager(getActivity()));
        packageAdapter.startListening();
        recyclerViewPackage.setAdapter(packageAdapter);
        updatePackageAdapter();

        setFont();
        btn_CreatePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreatePackage = new Intent(getActivity(),CreatePackageGuide.class);
                String packageId = mReference.push().getKey();
                intentCreatePackage.putExtra("PackageID",packageId);
                PackageData packageData = new PackageData(guideID,"เพิ่มชื่อแพ็คเกจของคุณที่นี่","เพิ่มรายละเอียดกแพ็คเกจของคุณ","default","","","","","","","","","","ยังไม่สมบูรณ์");
                mReference.child(packageId).setValue(packageData);
                startActivity(intentCreatePackage);
            }
        });
        return view;
    }

    private void updatePackageAdapter() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (packageAdapter!=null)
                    packageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFont() {
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        tv_package1.setTypeface(myCustomFont);
        tv_package2.setTypeface(myCustomFont);
        btn_CreatePackage.setTypeface(myCustomFont);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(packageAdapter != null)
            packageAdapter.startListening();
    }

    @Override
    public void onStop() {
        if (packageAdapter != null)
            packageAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(packageAdapter != null)
            packageAdapter.startListening();
    }
}
