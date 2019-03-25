package com.finalproject.it.travelfriend.Guide.CreatePackage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CreatePackagePageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public CreatePackagePageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0 :
                CreatePackageStepOne createPackageStepOne = new CreatePackageStepOne();
                return createPackageStepOne;
            case 1 :
                CreatePackageStepTwo createPackageStepTwo = new CreatePackageStepTwo();
                return createPackageStepTwo;
            case 2 :
                CreatePackageStepThree createPackageStepThree = new CreatePackageStepThree();
                return  createPackageStepThree;
            case 3 :
                CreatePackageStepFour createPackageStepFour = new CreatePackageStepFour();
                return  createPackageStepFour;
            case 4 :
                CreatePackageStepFive createPackageStepFive = new CreatePackageStepFive();
                return createPackageStepFive;
             default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
