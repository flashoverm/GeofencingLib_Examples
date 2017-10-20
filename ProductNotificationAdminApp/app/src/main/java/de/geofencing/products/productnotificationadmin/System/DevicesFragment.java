package de.geofencing.products.productnotificationadmin.System;

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

import de.geofencing.products.productnotificationadmin.Adapter.DeviceAdapter;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.device.Device;
import de.geofencing.system.device.Devices;
import de.geofencing.util.Util;

public class DevicesFragment extends Fragment {
    private ListView deviceList;
    private DeviceAdapter deviceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceAdapter = new DeviceAdapter(
                this.getContext(), R.layout.listitem_device, new Devices()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        deviceList = (ListView) rootView.findViewById(R.id.deviceList);
        deviceList.setAdapter(deviceAdapter);
        Util.setListViewHeightBasedOnChildren(deviceList, 6);

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Device device = (Device) deviceList.getItemAtPosition(i);

                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_delete, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete) {
                            ((SystemActivity) getActivity()).getGeofencingAdmin()
                                    .removeDevice(device);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    public void updateList(Devices systemDevices) {
        if (deviceAdapter != null) {
            deviceAdapter.clear();
            deviceAdapter.addAll(systemDevices.getDeviceList());
            Util.setListViewHeightBasedOnChildren(deviceList, 6);
        }
    }
}
