package de.geofencing.products.productnotificationadmin.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.geofencing.products.productnotificationadmin.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText serverAddress;
    private Button saveSettings;
    private Button checkConnection;

    public static final String INTENT_URL = "serviceURL";
    public static final String INTENT_CHECK = "check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        serverAddress = (EditText) findViewById(R.id.serverAddress);
        Intent settings = getIntent();
        if(serverAddress != null){
            serverAddress.setText(settings.getStringExtra(INTENT_URL));
        }

        saveSettings = (Button) findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_CHECK, false);
                resultIntent.putExtra(INTENT_URL, serverAddress.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        checkConnection = (Button) findViewById(R.id.checkConnection);
        checkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_CHECK, true);
                resultIntent.putExtra(INTENT_URL, serverAddress.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
