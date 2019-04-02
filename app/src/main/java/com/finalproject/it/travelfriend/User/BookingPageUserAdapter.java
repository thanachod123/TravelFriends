package com.finalproject.it.travelfriend.User;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.finalproject.it.travelfriend.Guide.BookingPackage.PostTripPackageFragment;
import com.finalproject.it.travelfriend.Guide.BookingPackage.RequestPackageFragment;
import com.finalproject.it.travelfriend.Guide.BookingPackage.UpcomingPackageFragment;
import com.finalproject.it.travelfriend.User.BookingPackage.PostTripPackageUserFragment;
import com.finalproject.it.travelfriend.User.BookingPackage.RequestPackageUserFragment;
import com.finalproject.it.travelfriend.User.BookingPackage.UpcomingPackageUserFragment;

public class BookingPageUserAdapter extends FragmentPagerAdapter {

    private  int numOfTabs;

    public BookingPageUserAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RequestPackageUserFragment();
            case 1:
                return new UpcomingPackageUserFragment();
            case 2:
                return new PostTripPackageUserFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
