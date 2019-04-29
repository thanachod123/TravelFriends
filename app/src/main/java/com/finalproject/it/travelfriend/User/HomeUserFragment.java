package com.finalproject.it.travelfriend.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.PackageData;
import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.Category.Adventure;
import com.finalproject.it.travelfriend.User.Category.Art;
import com.finalproject.it.travelfriend.User.Category.Culture;
import com.finalproject.it.travelfriend.User.Category.History;
import com.finalproject.it.travelfriend.User.Category.Nature;
import com.finalproject.it.travelfriend.User.Category.ViewHolderPackageUser;
import com.finalproject.it.travelfriend.User.RegisterPackage.DetailPackage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeUserFragment extends Fragment {
    CardView cardViewAdventure,cardViewCulture,cardViewArt,cardViewNature,cardViewHistory;
    FirebaseRecyclerOptions<PackageData> options;
    FirebaseRecyclerAdapter<PackageData,ViewHolderPackageUser> packageAdapter;
    RecyclerView recyclerRecommended;
    FloatingActionButton floatingSearch;
    FirebaseDatabase mDatabase;
    Toolbar toolbar;
    TextView seeall;
    DatabaseReference mReferencePackage,mReferenceGuide;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        toolbar = view.findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(getActivity(), R.style.FontForActionBar);
        toolbar.setTitle("ค้นหาเพื่อนแนะนำการท่องเที่ยว");
        cardViewAdventure = view.findViewById(R.id.cardviewAdventure);
        cardViewCulture = view.findViewById(R.id.cardviewCulture);
        cardViewArt = view.findViewById(R.id.cardviewArt);
        cardViewNature = view.findViewById(R.id.cardviewNature);
        cardViewHistory = view.findViewById(R.id.cardviewHistory);
        floatingSearch = view.findViewById(R.id.SearchButton);
        seeall = view.findViewById(R.id.seeall);

        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditPackage = new Intent(getActivity(),SeeallPackageActivity.class);

                startActivity(intentEditPackage);
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mReferencePackage = mDatabase.getReference().child("Packages");
        mReferenceGuide = mDatabase.getReference().child("Users");
        recyclerRecommended = view.findViewById(R.id.recyclerViewRecommended);

        options = new FirebaseRecyclerOptions.Builder<PackageData>()
                .setQuery(mReferencePackage.orderByChild("average_rating").equalTo("5.0"),PackageData.class).build();

        packageAdapter = new FirebaseRecyclerAdapter<PackageData, ViewHolderPackageUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPackageUser holder, final int position, @NonNull PackageData model) {
                holder.txtNamePackage.setText(model.getName());
                holder.txtProvincePackage.setText(model.getProvince());
                holder.txtActivity.setText(model.getPackage_type());
                holder.txtPrice.setText(model.getPrice_per_person()+" THB");
                holder.txtVehicleType.setText(model.getVehicle_type());

                float averageRating = Float.parseFloat(model.getAverage_rating());
                holder.ratingBar.setRating(averageRating);

                if (model.getVehicle_type().equals("Car")){
                    Picasso.with(getActivity()).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                } else if (model.getVehicle_type().equals("Motorcycle")){
                    Picasso.with(getActivity()).load(R.drawable.scooter)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Van")){
                    Picasso.with(getActivity()).load(R.drawable.van)
                            .into(holder.img_vehicle_type);
                }else if (model.getVehicle_type().equals("Jetski")){
                    Picasso.with(getActivity()).load(R.drawable.jetboating)
                            .into(holder.img_vehicle_type);
                }else {
                    Picasso.with(getActivity()).load(R.drawable.car)
                            .into(holder.img_vehicle_type);
                }
                Picasso.with(getActivity()).load(model.getImage()).placeholder(R.drawable.package_image).into(holder.imgPackage);
                String guideId = model.getGuideId();
                mReferenceGuide.child(guideId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String guideName,guideImage,guideSurname;
                        guideName = dataSnapshot.child("name").getValue().toString();
                        guideSurname = dataSnapshot.child("surname").getValue().toString();
                        holder.txtNameGuide.setText(guideName + " " + guideSurname);
                        guideImage = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.with(getActivity()).load(guideImage).placeholder(R.drawable.default_profile).into(holder.imgGuide);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentEditPackage = new Intent(getActivity(),DetailPackage.class);
                        String packageId = packageAdapter.getRef(position).getKey();
                        intentEditPackage.putExtra("PackageID",packageId);
                        startActivity(intentEditPackage);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderPackageUser onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_package_user,viewGroup,false);
                return new ViewHolderPackageUser(view);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerRecommended.setLayoutManager(layoutManager);

        packageAdapter.startListening();
        recyclerRecommended.setAdapter(packageAdapter);
        updatePackageAdapter();


        floatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }

        });

        cardViewAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Adventure.class));
            }
        });
        cardViewCulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Culture.class));
            }
        });
        cardViewArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Art.class));
            }
        });
        cardViewNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Nature.class));
            }
        });
        cardViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),History.class));
            }
        });

        return view;
    }

    private void updatePackageAdapter() {
        mReferencePackage.addValueEventListener(new ValueEventListener() {
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
