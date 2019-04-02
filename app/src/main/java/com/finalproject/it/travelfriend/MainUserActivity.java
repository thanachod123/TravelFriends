package com.finalproject.it.travelfriend;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.finalproject.it.travelfriend.Guide.BookingGuideFragment;
import com.finalproject.it.travelfriend.Guide.NotificationGuideFragment;
import com.finalproject.it.travelfriend.Guide.PackageGuideFragment;
import com.finalproject.it.travelfriend.Guide.ProfileGuideFragment;
import com.finalproject.it.travelfriend.User.BookingUserFragment;
import com.finalproject.it.travelfriend.User.HomeUserFragment;
import com.finalproject.it.travelfriend.User.ProfileUserFragment;
import com.finalproject.it.travelfriend.User.SearchUserFragment;
import com.jaeger.library.StatusBarUtil;

public class MainUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        StatusBarUtil.setColor(this,getResources().getColor(R.color.yellow));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeUserFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home_user:
                            selectedFragment = new HomeUserFragment();
                            break;
                        case R.id.navigation_booking_user:
                            selectedFragment = new BookingUserFragment();
                            break;
                        case R.id.navigation_search_user:
                            selectedFragment = new SearchUserFragment();
                            break;
                        case R.id.navigation_profile_user:
                            selectedFragment = new ProfileUserFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
