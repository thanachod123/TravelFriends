package com.finalproject.it.travelfriend.Guide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.finalproject.it.travelfriend.R;

public class BookingGuideFragment extends Fragment {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    BookingPageAdapter bookingPageAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_booking_guide, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);
        toolbar = view.findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(getActivity(), R.style.FontForActionBar);
        toolbar.setTitle("การสมัครแพ็คเกจนำเที่ยว");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        bookingPageAdapter = new BookingPageAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(bookingPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }
}
