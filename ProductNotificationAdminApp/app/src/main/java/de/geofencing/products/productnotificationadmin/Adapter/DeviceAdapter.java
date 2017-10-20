package de.geofencing.products.productnotificationadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.device.Device;
import de.geofencing.system.device.Devices;

public class DeviceAdapter extends ArrayAdapter<Device> {
    private Context context;

    public DeviceAdapter(Context context, int resource, Devices devices) {
        super(context, resource, devices.getDeviceList());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_device, parent, false);
        TextView idView = (TextView) rowView.findViewById(R.id.deviceID);
        TextView beaconView = (TextView) rowView.findViewById(R.id.deviceBeacons);
        TextView lastUpdate = (TextView) rowView.findViewById(R.id.deviceLastUpdate);

        Device device = getItem(position);

        idView.setText(""+device.getDeviceID());
        beaconView.setText(device.getBeacons().beaconCount()+" in range");
        lastUpdate.setText(device.getLastTimeUpdated().toString());

        return rowView;
    }
}
