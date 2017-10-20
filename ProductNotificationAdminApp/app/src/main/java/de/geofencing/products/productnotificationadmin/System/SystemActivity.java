package de.geofencing.products.productnotificationadmin.System;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.geofencing.client.GeofencingAdmin;
import de.geofencing.client.handler.InfoHandler;
import de.geofencing.client.handler.LogHandler;
import de.geofencing.client.handler.SystemDataHandler;
import de.geofencing.event.EventList;
import de.geofencing.log.LogEntry;
import de.geofencing.products.productnotificationadmin.Geofence.GeofenceActivity;
import de.geofencing.products.productnotificationadmin.Login.LoginActivity;
import de.geofencing.products.productnotificationadmin.R;
import de.geofencing.products.productnotificationadmin.Settings.SettingsActivity;
import de.geofencing.system.beacon.SystemBeacons;
import de.geofencing.system.device.Devices;
import de.geofencing.system.geofence.GeofenceList;
import de.geofencing.system.geofence.GeofenceListing;

public class SystemActivity extends AppCompatActivity implements AddGeofenceDialog.OnCompleteListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static final int INTENT_CODE_GEOFENCE = 120;
    public static final String INTENT_GEOFENCE = "geofence";

    private GeofencingAdmin geofencingAdmin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Intent settings = getIntent();
        password = settings.getStringExtra(LoginActivity.INTENT_PASSWORD);

        geofencingAdmin = new GeofencingAdmin(this);
        geofencingAdmin.setServiceURL(settings.getStringExtra(SettingsActivity.INTENT_URL));
        geofencingAdmin.setPassword(password);
        geofencingAdmin.allowAllCertificates();

        geofencingAdmin.setLogHandler(new LogHandler() {
            @Override
            public void onLogEntry(LogEntry logEntry) {
                Log.v("GeoAdminClient", logEntry.getLogEntry());
            }
        });
        geofencingAdmin.setInfoHandler(new InfoHandler() {
            @Override
            public void onInfo(String s) {
                Toast.makeText(SystemActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });


        geofencingAdmin.setSystemDataHandler(new SystemDataHandler() {
            @Override
            public void onUpdateGeofences(GeofenceList geofenceList) {
                ((GeofencesFragment)mSectionsPagerAdapter.getItem(0)).updateList(geofenceList);
            }
            @Override
            public void onUpdateDevices(Devices devices) {
                ((DevicesFragment)mSectionsPagerAdapter.getItem(1)).updateList(devices);
            }
            @Override
            public void onUpdateGeofence(GeofenceListing geofence, EventList events, SystemBeacons beacons) {
                Intent geofenceIntent = new Intent(
                        SystemActivity.this.getApplicationContext(),
                        GeofenceActivity.class);
                geofenceIntent.putExtra(INTENT_GEOFENCE, geofence);
                geofenceIntent.putExtra(SettingsActivity.INTENT_URL, geofencingAdmin.getRestServiceURL());
                geofenceIntent.putExtra(LoginActivity.INTENT_PASSWORD, password);
                startActivityForResult(geofenceIntent, INTENT_CODE_GEOFENCE);
            }
        });

        geofencingAdmin.start();
        geofencingAdmin.getSystemData();
    }

    public GeofencingAdmin getGeofencingAdmin(){
        return this.geofencingAdmin;
    }

    public void onComplete(String description) {
        geofencingAdmin.addGeofence(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_system, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_update){
            geofencingAdmin.getSystemData();
            return true;
        }
        if(id == R.id.menu_addGeofence){
            DialogFragment dialog = new AddGeofenceDialog();
            dialog.show(this.getSupportFragmentManager(), AddGeofenceDialog.DIALOG_TAG_ADDGEOFENCE);
            return true;
        }
        if(id == R.id.menu_logout){
            geofencingAdmin.stop();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
