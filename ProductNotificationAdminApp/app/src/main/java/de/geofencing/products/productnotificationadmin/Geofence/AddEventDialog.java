package de.geofencing.products.productnotificationadmin.Geofence;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import de.geofencing.event.Event;
import de.geofencing.event.Trigger;
import de.geofencing.event.notification.DeviceNotificationEvent;
import de.geofencing.products.productnotificationadmin.R;

public class AddEventDialog extends DialogFragment {

    public interface OnCompleteListener {
        public void onComplete(Event event);
    }

    private OnCompleteListener onComplete;

    public static final String DIALOG_TAG_ADDEVENT = "addEvent";
    public static final String DIALOG_MINOR = "geofenceMinor";

    private int gfMinor;
    private String selectedTrigger;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addevent, null);

        try {
            this.onComplete = (OnCompleteListener)getActivity();
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }

        EditText eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        Spinner triggerSpinner = (Spinner) view.findViewById(R.id.triggerList);
        NumberPicker seconds = (NumberPicker) view.findViewById(R.id.eventSeconds);
        seconds.setMaxValue(3600);
        seconds.setMinValue(0);
        seconds.setWrapSelectorWheel(true);
        seconds.setVisibility(View.INVISIBLE);

        Bundle arguments = this.getArguments();
        gfMinor = arguments.getInt(DIALOG_MINOR);

        ArrayAdapter<CharSequence> triggerAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.trigger, android.R.layout.simple_spinner_item);
        triggerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        triggerSpinner.setAdapter(triggerAdapter);

        triggerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTrigger = (String)adapterView.getSelectedItem();

                if(selectedTrigger.contains("for")){
                    seconds.setVisibility(View.VISIBLE);
                }
                else{
                    seconds.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        EditText message = (EditText) view.findViewById(R.id.eventMessage);
        EditText title = (EditText) view.findViewById(R.id.eventTitle);

        builder.setView(view)
                .setTitle("Add Event")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Trigger trigger;
                        if(selectedTrigger.equals("Enter") || selectedTrigger.equals("Enter for")){
                            trigger = new Trigger(
                                    Trigger.Direction.Enter ,
                                    seconds.getValue());
                        }
                        else{
                            trigger = new Trigger(
                                    Trigger.Direction.Leave ,
                                    seconds.getValue());
                        }
                        Event event = new DeviceNotificationEvent(
                                    eventDescription.getText().toString(),
                                    gfMinor,
                                    trigger,
                                    title.getText().toString(),
                                    message.getText().toString());

                        onComplete.onComplete(event);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
