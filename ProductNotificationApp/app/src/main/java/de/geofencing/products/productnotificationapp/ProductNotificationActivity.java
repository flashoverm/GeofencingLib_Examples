package de.geofencing.products.productnotificationapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.geofencing.client.GeofencingDevice;
import de.geofencing.client.beaconScanner.BeaconChangeHandler;
import de.geofencing.client.handler.InfoHandler;
import de.geofencing.client.handler.LogHandler;
import de.geofencing.log.LogEntry;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.system.device.Device;

public class ProductNotificationActivity extends AppCompatActivity {

    private TextView deviceIDView;
    private TextView beaconsInRange;
    private Button update;
    private GeofencingDevice geofencingDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_notification);

        deviceIDView = (TextView) findViewById(R.id.deviceID);
        beaconsInRange = (TextView) findViewById(R.id.inRange);
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               updateDeviceID();
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.GET_ACCOUNTS}, 666);
            }
        } else {
            runGeofencing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 666 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runGeofencing();
        }
    }

    private void updateDeviceID(){
        Device clientDevice = geofencingDevice.getClientDevice();
        if(clientDevice != null){
            deviceIDView.setText(clientDevice.getDeviceID()+"");
        }
    }

    private void runGeofencing(){
        geofencingDevice = new GeofencingDevice(
                this,
                "https://hs.geofencing.thral.de/ProductNotification/",
                new LogHandler() {
                    @Override
                    public void onLogEntry(LogEntry logEntry) {
                        System.out.println(logEntry.getLogEntry());
                    }
                });
        geofencingDevice.allowAllCertificates();
        geofencingDevice.setInfoHandler(new InfoHandler() {
            @Override
            public void onInfo(String s) {
                Toast.makeText(ProductNotificationActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        geofencingDevice.setBeaconChangeHandler(new BeaconChangeHandler() {
            @Override
            public void onBeaconsChangeDetected(SystemBeacons beacons) {
                beaconsInRange.setText(beacons.beaconCount()+"");
            }
        });
        geofencingDevice.start();
    }
}
