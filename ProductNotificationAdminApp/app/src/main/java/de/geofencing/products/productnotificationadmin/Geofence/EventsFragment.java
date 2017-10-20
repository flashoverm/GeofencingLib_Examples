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

import de.geofencing.event.EventList;
import de.geofencing.event.EventListing;
import de.geofencing.products.productnotificationadmin.Adapter.EventAdapter;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.util.Util;

public class EventsFragment extends Fragment {

    private ListView eventList;
    private EventAdapter eventAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        eventAdapter = new EventAdapter(
                this.getContext(), R.layout.listitem_event, new EventList()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        eventList = (ListView) rootView.findViewById(R.id.eventList);

        eventList.setAdapter(eventAdapter);
        Util.setListViewHeightBasedOnChildren(eventList, 6);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EventListing event = (EventListing) eventList.getItemAtPosition(i);

                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_delete, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete){
                            ((GeofenceActivity)getActivity()).getGeofencingAdmin()
                                    .removeEvent(event.getMinor(), event.getEventID());
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    public void updateList(EventList events){
        if(eventAdapter != null){
            eventAdapter.clear();
            eventAdapter.addAll(events.getEventList());
            Util.setListViewHeightBasedOnChildren(eventList, 6);
        }
    }
}
