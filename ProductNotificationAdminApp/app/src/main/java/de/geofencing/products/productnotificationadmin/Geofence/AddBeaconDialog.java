package de.geofencing.products.productnotificationadmin.Geofence;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.geofencing.products.productnotificationadmin.R;

public class AddBeaconDialog extends DialogFragment {

    private Intent intent;

    public static final String DIALOG_TAG_ADDBEACON = "addBeacon";
    public static final String INTENT_LOCATION = "location";
    public static final int INTENT_CODE_ADDBEACON = 130;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addbeacon, null);

        EditText location = (EditText) view.findViewById(R.id.locationEdit);

        builder.setView(view)
                .setTitle("Add Beacon to System")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent();
                        intent.putExtra(INTENT_LOCATION, location.getText().toString());
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(), INTENT_CODE_ADDBEACON , intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
