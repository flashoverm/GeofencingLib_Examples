package de.geofencing.products.productnotificationadmin.System;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.geofencing.products.productnotificationadmin.R;

public class AddGeofenceDialog extends DialogFragment {

    public interface OnCompleteListener {
        public void onComplete(String description);
    }

    private OnCompleteListener onComplete;

    public static final String DIALOG_TAG_ADDGEOFENCE = "addGeofence";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addgeofence, null);

        try {
            this.onComplete = (OnCompleteListener)getActivity();
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }

        EditText description = (EditText) view.findViewById(R.id.gfAddDescritpion);
        builder.setView(view)
                .setTitle("Add Geofence to System")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onComplete.onComplete(description.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
