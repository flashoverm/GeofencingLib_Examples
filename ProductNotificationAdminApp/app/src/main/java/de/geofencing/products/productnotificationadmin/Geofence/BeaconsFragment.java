package de.geofencing.products.productnotificationadmin.Geofence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.geofencing.products.productnotificationadmin.Adapter.BeaconAdapter;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.util.Util;

public class BeaconsFragment extends Fragment {

    private ListView beaconList;
    private BeaconAdapter systemBeaconAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        systemBeaconAdapter = new BeaconAdapter(
                this.getContext(), R.layout.listitem_beacon, new SystemBeacons()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_beacons, container, false);
        beaconList = (ListView) rootView.findViewById(R.id.beaconList);

        beaconList.setAdapter(systemBeaconAdapter);
        Util.setListViewHeightBasedOnChildren(beaconList, 6);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SystemBeacon b = (SystemBeacon) beaconList.getItemAtPosition(i);

                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_delete, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete){
                            ((GeofenceActivity)getActivity()).getGeofencingAdmin().removeBeacon(b);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        return rootView;
    }

    public void updateList(SystemBeacons systemBeacons){
        if(systemBeaconAdapter != null){
            systemBeaconAdapter.clear();
            systemBeaconAdapter.addAll(systemBeacons.getBeaconList());
            Util.setListViewHeightBasedOnChildren(beaconList, 6);
        }
    }
}
