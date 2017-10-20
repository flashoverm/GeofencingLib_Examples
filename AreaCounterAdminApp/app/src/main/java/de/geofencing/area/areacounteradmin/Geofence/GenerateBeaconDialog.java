package de.geofencing.area.areacounteradmin.Geofence;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.geofencing.area.areacounteradmin.R;
import de.geofencing.system.beacon.SystemBeacon;

public class GenerateBeaconDialog extends DialogFragment {

    public static final String DIALOG_TAG_GENBEACON = "generate";
    public static final String DIALOG_GENBEACON = "generated";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_generatebeacon, null);

        TextView uuid = (TextView) view.findViewById(R.id.gen_UUID);
        TextView major = (TextView) view.findViewById(R.id.gen_major);
        TextView minor = (TextView) view.findViewById(R.id.gen_minor);

        Bundle beacon = getArguments();
        SystemBeacon generated = (SystemBeacon)beacon.get(DIALOG_GENBEACON);

        uuid.setText(generated.getUUID().toString());
        major.setText(""+generated.getMajor());
        minor.setText(""+generated.getMinor());

        builder.setView(view)
                .setTitle("Generated Beacon")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}