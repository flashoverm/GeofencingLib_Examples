package de.geofencing.area.areacounteradmin.Geofence;

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

import de.geofencing.area.areacounteradmin.Login.LoginActivity;
import de.geofencing.area.areacounteradmin.R;
import de.geofencing.area.areacounteradmin.Settings.SettingsActivity;
import de.geofencing.area.areacounteradmin.System.SystemActivity;
import de.geofencing.client.GeofencingAdmin;
import de.geofencing.client.beaconScanner.BeaconChangeHandler;
import de.geofencing.client.handler.GeneratedBeaconHandler;
import de.geofencing.client.handler.InfoHandler;
import de.geofencing.client.handler.LogHandler;
import de.geofencing.client.handler.SystemDataHandler;
import de.geofencing.client.networkTask.NetworkTask;
import de.geofencing.event.EventList;
import de.geofencing.event.EventListing;
import de.geofencing.event.counter.GeofenceCounterEvent;
import de.geofencing.event.counter.RestCounterClient;
import de.geofencing.log.LogEntry;
import de.geofencing.system.beacon.SystemBeacon;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.system.device.Devices;
import de.geofencing.system.geofence.GeofenceList;
import de.geofencing.system.geofence.GeofenceListing;

public class GeofenceActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private GeofencingAdmin geofencingAdmin;
    private GeofenceListing selectedGeofence;
    private SystemBeacons beaconsOfSelected;
    private EventListing eventOfSelected;

    private LogHandler logHandler;
    private RestCounterClient counterClient;

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

        String url = geofenceIntent.getStringExtra(SettingsActivity.INTENT_URL);
        String passwort = geofenceIntent.getStringExtra(LoginActivity.INTENT_PASSWORD);

        geofencingAdmin = new GeofencingAdmin(this);
        geofencingAdmin.setPassword(passwort);
        geofencingAdmin.allowAllCertificates();
        geofencingAdmin.setServiceURL(url);

        counterClient = new RestCounterClient(url);
        counterClient.setAuthorization(passwort);
        counterClient.allowAllCertificates();

        logHandler = new LogHandler() {
            @Override
            public void onLogEntry(LogEntry logEntry) {
                System.out.println(logEntry.getLogEntry());
                Log.v("GeoAdminClient", logEntry.getLogEntry());
            }
        };

        geofencingAdmin.setLogHandler(logHandler);
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
                beaconsOfSelected = beacons;
                try {
                    eventOfSelected = events.getEventList().get(0);
                } catch(IndexOutOfBoundsException e){
                    eventOfSelected = null;
                }
                new GetCounterValueTask().runTask(logHandler);
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
                ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(1))
                        .updateList(beacons);
            }
        });

        geofencingAdmin.start();
        geofencingAdmin.getGeofence(selectedGeofence);
    }

    public GeofencingAdmin getGeofencingAdmin(){
        return this.geofencingAdmin;
    }

    public void addEvent(){
        geofencingAdmin.addEvent(new GeofenceCounterEvent(
                selectedGeofence.getDescription(),
                selectedGeofence.getMinor()));
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
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(1))
                            .onScanStop();
                }
            }
            else{
                if(geofencingAdmin.startScanning()){
                    item.setChecked(true);
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(1))
                            .onScanRunning();
                    ((BeaconInRangeFragment)mSectionsPagerAdapter.getItem(1))
                            .updateList(new SystemBeacons());
                }
            }
            return true;
        }
        if(id == R.id.menu_generate){
            geofencingAdmin.generateBeacon(selectedGeofence.getMinor());
            return true;
        }
        if(id == R.id.menu_deleteGeofence){
            if(eventOfSelected != null){
                geofencingAdmin.removeEvent(selectedGeofence.getMinor(), eventOfSelected.getEventID());
                new RemoveCounterTask().runTask(logHandler);
            } else {
                geofencingAdmin.removeGeofence(selectedGeofence.getMinor());
                stop();
            }
            return true;
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

    private class GetCounterValueTask extends NetworkTask<Void, Void, Integer> {
        @Override
        protected Integer runInBackground(Void... params) throws Exception{
            return counterClient.getGeofenceCounterValue(selectedGeofence.getMinor());
        }
        @Override
        protected void onSuccess(Integer result) {
            ((GeofenceFragment)mSectionsPagerAdapter.getItem(0))
                    .updateList(eventOfSelected, beaconsOfSelected, result);
        }
        protected void afterFailure() {
            ((GeofenceFragment)mSectionsPagerAdapter.getItem(0))
                    .updateList(eventOfSelected, beaconsOfSelected, -1);
        }
    }

    private class RemoveCounterTask extends NetworkTask<Integer, Void, Boolean>{
        @Override
        protected Boolean runInBackground(Integer... params) throws Exception{
            return counterClient.removeCounter(params[0]);
        }
        @Override
        protected void onSuccess(Boolean result) {
            geofencingAdmin.removeGeofence(selectedGeofence.getMinor());
            stop();
        }
    }
}
