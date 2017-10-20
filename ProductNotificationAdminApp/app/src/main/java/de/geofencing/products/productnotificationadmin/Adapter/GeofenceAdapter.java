package de.geofencing.products.productnotificationadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.system.geofence.GeofenceList;
import de.geofencing.system.geofence.GeofenceListing;

public class GeofenceAdapter extends ArrayAdapter<GeofenceListing> {
    private Context context;

    public GeofenceAdapter(Context context, int resource, GeofenceList geofenceList) {
        super(context, resource, geofenceList.getGeofenceList());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_geofence, parent, false);
        TextView minorView = (TextView) rowView.findViewById(R.id.gfMinor);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.gfDescription);

        GeofenceListing geofence = getItem(position);

        minorView.setText(""+geofence.getMinor());
        descriptionView.setText(geofence.getDescription());

        return rowView;
    }
}
