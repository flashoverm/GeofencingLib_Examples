package de.geofencing.area.areacounteradmin.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.geofencing.area.areacounteradmin.R;
import de.geofencing.area.areacounteradmin.Settings.SettingsActivity;
import de.geofencing.area.areacounteradmin.System.SystemActivity;
import de.geofencing.client.GeofencingAdminLogin;
import de.geofencing.client.handler.InfoHandler;
import de.geofencing.client.handler.LogHandler;
import de.geofencing.client.handler.LoginHandler;
import de.geofencing.log.LogEntry;

public class LoginActivity extends AppCompatActivity {

    public static final int INTENT_CODE_LOGIN = 100;
    private static final int INTENT_CODE_SETTINGS = 110;
    public static final String INTENT_PASSWORD = "password";

    private GeofencingAdminLogin adminLogin;
    private String serviceURL;
    private EditText passwordText;
    private Button settings;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = (Button) findViewById(R.id.login_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSettings(adminLogin.getRestServiceURL());
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordText = (EditText) findViewById(R.id.password);
                adminLogin.checkCredentials(passwordText.getText().toString(), new LoginHandler() {
                    @Override
                    public void onLogin() {
                        Intent login = new Intent(LoginActivity.this.getApplicationContext(), SystemActivity.class);
                        login.putExtra(INTENT_PASSWORD , passwordText.getText().toString());
                        login.putExtra(SettingsActivity.INTENT_URL, serviceURL);
                        startActivityForResult(login, INTENT_CODE_LOGIN);
                    }
                });
            }
        });

        adminLogin = new GeofencingAdminLogin(this);
        adminLogin.allowAllCertificates();
        adminLogin.setLogHandler(new LogHandler() {
            @Override
            public void onLogEntry(LogEntry logEntry) {
                Log.v("GeoAdminClient", logEntry.getLogEntry());
            }
        });
        adminLogin.setInfoHandler(new InfoHandler() {
            @Override
            public void onInfo(String s) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        adminLogin.start();
    }

    private void switchToSettings(String serviceURL){
        Intent settings = new Intent(this.getApplicationContext(), SettingsActivity.class);
        settings.putExtra(SettingsActivity.INTENT_URL, serviceURL);
        startActivityForResult(settings, INTENT_CODE_SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (INTENT_CODE_SETTINGS) : {
                if (resultCode == Activity.RESULT_OK) {
                    serviceURL = data.getStringExtra(SettingsActivity.INTENT_URL);
                    boolean check = data.getBooleanExtra(SettingsActivity.INTENT_CHECK, false);

                    if(!check){
                        adminLogin.setServiceURL(serviceURL);
                    }
                    else{
                        adminLogin.checkServiceURL(serviceURL);
                        switchToSettings(serviceURL);
                    }
                }
                break;
            }
        }
    }
}

