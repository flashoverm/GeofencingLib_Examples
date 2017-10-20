package de.geofencing.products.productnotificationadmin.Geofence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import de.geofencing.client.GeofencingAdmin;
import de.geofencing.client.beaconScanner.BeaconChangeHandler;
import de.geofencing.client.handler.GeneratedBeaconHandler;
import de.geofencing.client.handler.InfoHandler;
import de.geofencing.client.handler.LogHandler;
import de.geofencing.client.handler.SystemDataHandler;
import de.geofencing.event.Event;
import de.geofencing.event.EventList;
import de.geofencing.log.LogEntry;
import de.geofencing.products.productnotificationadmin.Login.LoginActivity;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.products.productnotificationadmin.Settings.SettingsActivity;
import de.geofencing.products.productnotificationadmin.System.SystemActivity;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.system.device.Devices;
import de.geofencing.system.geofence.GeofenceList;
import de.geofencing.system.geofence.GeofenceListing;

public class GeofenceActivity extends AppCompatActivity implements AddEventDialog.OnCompleteListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private GeofencingAdmin geofencingAdmin;
    private GeofenceListing selectedGeofence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Intent geofenceIntent = getIntent();
        selectedGeofence = (GeofenceListing)geofenceIntent.getExtras().get(SystemActivity.INTENT_GEOFENCE);
        ((TextView)findViewById(R.id.gfMinorBar))
                .setText("Geofence Minor: "+selectedGeofence.getMinor());

        geofencingAdmin = new GeofencingAdmin(this);
        geofencingAdmin.allowAllCertificates();
        geofencingAdmin.setPassword(geofenceIntent.getStringExtra(LoginActivity.INTENT_PASSWORD));
        geofencingAdmin.setServiceURL(geofenceIntent.getStringExtra(SettingsActivity.INTENT_URL));
        geofencingAdmin.setLogHandler(new LogHandler() {
            @Override
            public void onLogEntry(LogEntry logEntry) {
                System.out.println(logEntry.getLogEntry());
                Log.v("GeoAdminClient", logEntry.getLogEntry());
                Toast.makeText(GeofenceActivity.this, logEntry.getLogEntry(), Toast.LENGTH_LONG).show();
            }
        });
        geofencingAdmin.setInfoHandler(new InfoHandler() {
            @Override
            public void onInfo(String s) {
                Toast.makeText(GeofenceActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        geofencingAdmin.setSystemDataHandler(new SystemDataHandler() {
            @Override
            public void onUpdateGeofences(GeofenceList geofenceList) {}
            @Override
            public void onUpdateDevices(Devices devices) {}

            @Override
            public void onUpdateGeofence(GeofenceListing geofence, EventList events, SystemBeacons beacons) {
                selectedGeofence = geofence;
                ((BeaconsFragment)mSectionsPagerAdapter.getItem(1))
                        .updateList(beacons);
                ((EventsFragment)mSectionsPagerAdapter.getItem(0))
                        .updateList(events);
            }
        });
        geofencingAdmin.setGeneratedBeaconHandler(new GeneratedBeaconHandler() {
            @Override
            public void onBeaconGenerated(SystemBeacon systemBeacon) {
                GenerateBeaconDialog generate = new GenerateBeaconDialog();
                Bundle beacon = new Bundle();
                beacon.putSerializable(GenerateBeaconDialog.DIALOG_GENBEACON, systemBeacon);
                generate.setArguments(beacon);
                generate.show(getSupportFragmentManager(), GenerateBeaconDialog.DIALOG_TAG_GENBEACON);
            }
        });
        geofencingAdmin.setBeaconChangeHandler(new BeaconChangeHandler() {
            @Override
            public void onBeaconsChangeDetected(SystemBeacons beacons) {
                ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(2))
                        .updateList(beacons);
            }
        });

        geofencingAdmin.start();
        geofencingAdmin.getGeofence(selectedGeofence);
    }

    public GeofencingAdmin getGeofencingAdmin(){
        return this.geofencingAdmin;
    }

    public void onComplete(Event event) {
        geofencingAdmin.addEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_geofence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_update){
            geofencingAdmin.getGeofence(selectedGeofence);
            return true;
        }
        if(id == R.id.menu_scan) {
            if(item.isChecked()){
                if(geofencingAdmin.stopScanning()){
                    item.setChecked(false);
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(2))
                            .onScanStop();
                }
            }
            else{
                if(geofencingAdmin.startScanning()){
                    item.setChecked(true);
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(2))
                            .onScanRunning();
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(2))
                            .updateList(new SystemBeacons());
                }
            }
            return true;
        }
        if(id == R.id.menu_generate){
            geofencingAdmin.generateBeacon(selectedGeofence.getMinor());
            return true;
        }
        if(id == R.id.menu_addEvent){
            AddEventDialog addEvent = new AddEventDialog();
            Bundle eventAdd = new Bundle();
            eventAdd.putInt(AddEventDialog.DIALOG_MINOR, selectedGeofence.getMinor());
            addEvent.setArguments(eventAdd);
            addEvent.show(getSupportFragmentManager(), AddEventDialog.DIALOG_TAG_ADDEVENT);
            return true;
        }
        if(id == R.id.menu_deleteGeofence){
            geofencingAdmin.removeGeofence(selectedGeofence.getMinor());
            return stop();
        }
        if(id == R.id.menu_back){
            return stop();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean stop(){
        geofencingAdmin.stopScanning();
        finish();
        return true;
    }
}
