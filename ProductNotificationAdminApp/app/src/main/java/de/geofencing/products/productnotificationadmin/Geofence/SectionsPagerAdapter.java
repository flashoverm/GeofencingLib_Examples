package de.geofencing.products.productnotificationadmin.Geofence;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static EventsFragment eventFragement;
    private static BeaconsFragment beaconsFragement;
    private static BeaconInRangeFragment inRangeFragment;

    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);
        eventFragement = new EventsFragment();
        beaconsFragement = new BeaconsFragment();
        inRangeFragment = new BeaconInRangeFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return eventFragement;
            case 1: return beaconsFragement;
            case 2: return inRangeFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Events";
            case 1: return "Beacons";
            case 2: return "Beacons in Range";
            default: return null;
        }
    }
}