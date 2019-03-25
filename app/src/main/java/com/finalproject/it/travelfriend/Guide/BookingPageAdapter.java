package com.finalproject.it.travelfriend.Guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.finalproject.it.travelfriend.Guide.BookingPackage.PostTripPackageFragment;
import com.finalproject.it.travelfriend.Guide.BookingPackage.RequestPackageFragment;
import com.finalproject.it.travelfriend.Guide.BookingPackage.UpcomingPackageFragment;

public class BookingPageAdapter extends FragmentPagerAdapter {

    private  int numOfTabs;

    public BookingPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RequestPackageFragment();
            case 1:
                return new UpcomingPackageFragment();
            case 2:
                return new PostTripPackageFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
