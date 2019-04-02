package com.finalproject.it.travelfriend.Guide.CreatePackage;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.finalproject.it.travelfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaeger.library.StatusBarUtil;

public class CreatePackageGuide extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    NonSwipeableViewPager nonSwipeableViewPager;
    CreatePackagePageAdapter createPackagePageAdapter;
    TabItem stepOne,stepTwo,stepThree,stepFour,stepFive;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    String packageID,guideID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package_guide);
        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Packages");
        guideID = mAuth.getCurrentUser().getUid();
        packageID = getIntent().getExtras().getString("PackageID");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_app_bar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
        StatusBarUtil.setTransparent(CreatePackageGuide.this);

        tabLayout = findViewById(R.id.tabLayout);
        stepOne = findViewById(R.id.stepOne);
        stepTwo = findViewById(R.id.stepTwo);
        stepThree = findViewById(R.id.stepThree);
        stepFour = findViewById(R.id.stepFour);
        stepFive = findViewById(R.id.stepFive);
        nonSwipeableViewPager = findViewById(R.id.view_pager);
        createPackagePageAdapter = new CreatePackagePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        nonSwipeableViewPager.setAdapter(createPackagePageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                nonSwipeableViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for (int i=0; i<tabStrip.getChildCount(); i++){
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        nonSwipeableViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_package_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mReference.child(packageID).removeValue();
        finish();
        return true;
    }
}
