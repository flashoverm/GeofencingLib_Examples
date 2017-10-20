package de.geofencing.area.areacounteradmin.Geofence;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static GeofenceFragment geofenceFragement;
    private static BeaconInRangeFragment inRangeFragment;

    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);
        geofenceFragement = new GeofenceFragment();
        inRangeFragment = new BeaconInRangeFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return geofenceFragement;
            case 1: return inRangeFragment;
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
            case 0: return "Geofence";
            case 1: return "Beacons in Range";
            default: return null;
        }
    }
}