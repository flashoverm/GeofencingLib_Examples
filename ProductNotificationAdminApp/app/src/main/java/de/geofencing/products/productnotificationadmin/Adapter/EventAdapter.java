package de.geofencing.products.productnotificationadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.geofencing.event.EventList;
import de.geofencing.event.EventListing;
import de.geofencing.products.productnotificationadmin.R;

public class EventAdapter extends ArrayAdapter<EventListing> {
    private Context context;

    public EventAdapter(Context context, int resource, EventList events) {
        super(context, resource, events.getEventList());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_event, parent, false);
        TextView type = (TextView) rowView.findViewById(R.id.eventType);
        TextView idView = (TextView) rowView.findViewById(R.id.eventID);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.eventDescription);
        TextView triggerView = (TextView) rowView.findViewById(R.id.eventTrigger);

        EventListing event = getItem(position);

        type.setText("DeviceNotificationEvent");
        idView.setText(""+event.getEventID());
        descriptionView.setText(event.getDescription());

        if(event.getTrigger() != null){
            if(event.getTrigger().getDelay() == 0) {
                triggerView.setText(event.getTrigger().getDirection().toString());
            } else {
                triggerView.setText(event.getTrigger().getDirection().toString()
                        + " after " + event.getTrigger().getDelay() + "seconds");
            }
        } else {
            triggerView.setText(" - ");
        }

        return rowView;
    }
}
