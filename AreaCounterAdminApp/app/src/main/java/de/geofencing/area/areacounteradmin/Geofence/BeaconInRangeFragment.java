package de.geofencing.area.areacounteradmin.Geofence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import de.geofencing.area.areacounteradmin.Adapter.BeaconAdapter;
import de.geofencing.area.areacounteradmin.R;
import de.geofencing.client.GeofencingAdmin;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.util.Util;

public class BeaconInRangeFragment extends Fragment {

    private ListView beaconList;
    private TextView scanNotActive;
    private BeaconAdapter beaconAdapter;

    private SystemBeacon selectedBeacon;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        beaconAdapter = new BeaconAdapter(
                this.getContext(), R.layout.listitem_beacon, new SystemBeacons()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inrange, container, false);
        beaconList = (ListView) rootView.findViewById(R.id.beaconList);
        scanNotActive = (TextView) rootView.findViewById(R.id.scanNotAcitve);

        if(((GeofenceActivity) getActivity()).getGeofencingAdmin().isBtScanActive()){
            onScanRunning();
        }
        else {
            onScanStop();
        }

        beaconList.setAdapter(beaconAdapter);
        Util.setListViewHeightBasedOnChildren(beaconList, 6);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedBeacon = (SystemBeacon) beaconList.getItemAtPosition(i);

                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_rangebeacons, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.addBeacon){
                            DialogFragment dialog = new AddBeaconDialog();
                            dialog.setTargetFragment(BeaconInRangeFragment.this, AddBeaconDialog.INTENT_CODE_ADDBEACON);
                            dialog.show(getActivity().getSupportFragmentManager(), AddBeaconDialog.DIALOG_TAG_ADDBEACON);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    public void updateList(SystemBeacons beaconsInRange){
        if(beaconAdapter != null){
            beaconAdapter.clear();
            beaconAdapter.addAll(beaconsInRange.getBeaconList());
            Util.setListViewHeightBasedOnChildren(beaconList, 6);
        }
    }

    public void onScanStop(){
        beaconList.setVisibility(View.INVISIBLE);
        scanNotActive.setVisibility(View.VISIBLE);
    }

    public void onScanRunning(){
        beaconList.setVisibility(View.VISIBLE);
        scanNotActive.setVisibility(View.INVISIBLE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddBeaconDialog.INTENT_CODE_ADDBEACON) {
            String location = data.getStringExtra(AddBeaconDialog.INTENT_LOCATION);

            GeofenceActivity activity = (GeofenceActivity) getActivity();
            GeofencingAdmin admin = activity.getGeofencingAdmin();
            admin.addBeacon(selectedBeacon, location);
        }
    }
}
