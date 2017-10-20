package de.geofencing.area.areacounteradmin.Geofence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import de.geofencing.area.areacounteradmin.Adapter.BeaconAdapter;
import de.geofencing.area.areacounteradmin.R;
import de.geofencing.event.EventListing;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.util.Util;

public class GeofenceFragment extends Fragment {

    private ListView beaconList;
    private BeaconAdapter systemBeaconAdapter;

    private TextView counter;
    private TextView type;
    private TextView idView;
    private TextView descriptionView;
    private Button addEventButton;

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

        View rootView = inflater.inflate(R.layout.fragment_geofence, container, false);

        type = (TextView) rootView.findViewById(R.id.eventType);
        idView = (TextView) rootView.findViewById(R.id.eventID);
        descriptionView = (TextView) rootView.findViewById(R.id.eventDescription);
        counter = (TextView) rootView.findViewById(R.id.eventCounter);
        addEventButton = (Button) rootView.findViewById(R.id.addEvent);

        type.setVisibility(View.INVISIBLE);
        idView.setVisibility(View.INVISIBLE);
        descriptionView.setVisibility(View.INVISIBLE);
        counter.setVisibility(View.INVISIBLE);
        addEventButton.setVisibility(View.INVISIBLE);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GeofenceActivity) getActivity()).addEvent();
            }
        });

        beaconList = (ListView) rootView.findViewById(R.id.beaconList);
        beaconList.setAdapter(systemBeaconAdapter);
        Util.setListViewHeightBasedOnChildren(beaconList, 6);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final SystemBeacon b = (SystemBeacon) beaconList.getItemAtPosition(i);

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

    public void updateList(EventListing event, SystemBeacons systemBeacons, int counterValue){
        if(event != null){
            type.setText("GeofenceCounterEvent");
            type.setVisibility(View.VISIBLE);
            idView.setText(""+event.getEventID());
            idView.setVisibility(View.VISIBLE);
            descriptionView.setText(event.getDescription());
            descriptionView.setVisibility(View.VISIBLE);
            counter.setText("Count: " + counterValue);
            counter.setVisibility(View.VISIBLE);

            addEventButton.setVisibility(View.INVISIBLE);
        } else {
            type.setVisibility(View.INVISIBLE);
            idView.setVisibility(View.INVISIBLE);
            descriptionView.setVisibility(View.INVISIBLE);
            counter.setVisibility(View.INVISIBLE);
            addEventButton.setVisibility(View.VISIBLE);
        }
        if(systemBeaconAdapter != null){
            systemBeaconAdapter.clear();
            systemBeaconAdapter.addAll(systemBeacons.getBeaconList());
            Util.setListViewHeightBasedOnChildren(beaconList, 6);
        }
    }
}
