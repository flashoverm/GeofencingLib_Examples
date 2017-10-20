package de.geofencing.area.areacounteradmin.System;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static GeofencesFragment geofencesFragement;
    private static DevicesFragment systemPhoneFragment;

    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);
        geofencesFragement = new GeofencesFragment();
        systemPhoneFragment = new DevicesFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return geofencesFragement;
            case 1: return systemPhoneFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Geofences";
            case 1: return "Devices";
            default: return null;
        }
    }
}