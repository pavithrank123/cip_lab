package com.siva.needred;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

class SectionsPagerAdapter extends FragmentPagerAdapter{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private boolean check=false;
    @Override
    public Fragment getItem(int position) {
    switch (position)
    {

        case 0: DonorListFragment donorListFragment=new DonorListFragment();
            return donorListFragment;
        case 1: NeedHelpFragment needHelpFragment=new NeedHelpFragment();
            return needHelpFragment;

        default:return  null;
    }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        this.check=MainActivity.getAdminStatus();
        Log.e("String is ", String.valueOf(MainActivity.getAdminStatus()));
        Log.e("String is ", String.valueOf(check));
        if(check==true)
        {
            switch (position){
                case 0:
                    return "HOSPITALS";
                case 1:
                    return "PATIENTS";
                default: return null;

            }
        }
        else
        {
            switch (position){
                case 0:
                    return "HOSPITALS";
                case 1:
                    return "EMERGENCIES";
                default: return null;

            }
        }

    }
}
