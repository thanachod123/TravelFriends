package com.finalproject.it.travelfriend;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.finalproject.it.travelfriend.FragmentGuide.HomeGuideFragment;
import com.finalproject.it.travelfriend.FragmentGuide.NotificationGuideFragment;
import com.finalproject.it.travelfriend.FragmentGuide.PackageGuideFragment;
import com.finalproject.it.travelfriend.FragmentGuide.ProfileGuideFragment;

public class MainGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guide);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeGuideFragment();
                            break;
                        case R.id.navigation_package:
                            selectedFragment = new PackageGuideFragment();
                            break;
                        case R.id.navigation_notification:
                            selectedFragment = new NotificationGuideFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileGuideFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
