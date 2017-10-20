package de.geofencing.products.productnotificationadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;

public class BeaconAdapter extends ArrayAdapter<SystemBeacon> {
    private Context context;

    public BeaconAdapter(Context context, int resource, SystemBeacons beacons) {
        super(context, resource, beacons.getBeaconList());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_beacon, parent, false);
        TextView location = (TextView) rowView.findViewById(R.id.beaconLocation);
        TextView uuidView = (TextView) rowView.findViewById(R.id.beaconUUID);
        TextView majorView = (TextView) rowView.findViewById(R.id.beaconMajor);
        TextView minorView = (TextView) rowView.findViewById(R.id.beaconMinor);

        SystemBeacon beacon = getItem(position);

        if(beacon.getLocation() == null){
            location.setText(" - ");
        } else {
            location.setText(beacon.getLocation());
        }
        uuidView.setText(beacon.getUUID().toString());
        majorView.setText("" + beacon.getMajor());
        minorView.setText("" + beacon.getMinor());

        return rowView;
    }
}
