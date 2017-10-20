package de.geofencing.products.productnotificationadmin.System;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.geofencing.products.productnotificationadmin.Adapter.GeofenceAdapter;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.geofence.GeofenceList;
import de.geofencing.system.geofence.GeofenceListing;
import de.geofencing.util.Util;

public class GeofencesFragment extends Fragment {

    private ListView geofenceList;
    private GeofenceAdapter geofenceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        geofenceAdapter = new GeofenceAdapter(
                this.getContext(), R.layout.listitem_geofence, new GeofenceList()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_geofences, container, false);
        geofenceList = (ListView) rootView.findViewById(R.id.geofenceList);

        geofenceList.setAdapter(geofenceAdapter);
        Util.setListViewHeightBasedOnChildren(geofenceList, 6);

        geofenceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GeofenceListing geofence = (GeofenceListing) geofenceList.getItemAtPosition(i);
                ((SystemActivity)getActivity()).getGeofencingAdmin()
                        .getGeofence(geofence);
            }
        });

        return rootView;
    }

    public void updateList(GeofenceList geofences){
        if(geofenceAdapter != null){
            geofenceAdapter.clear();
            geofenceAdapter.addAll(geofences.getGeofenceList());
            Util.setListViewHeightBasedOnChildren(geofenceList, 6);
        }
    }
}