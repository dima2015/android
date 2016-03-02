package com.plunner.plunner.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.utils.ComManager;

public class UserSettingsActivity extends AppCompatActivity {

    private TextView nameField;
    private TextView emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_user_settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameField = (TextView) findViewById(R.id.activity_user_settings_name);
        emailField = (TextView) findViewById(R.id.activity_user_settings_email);

        retrieveFields();
    }

    private void retrieveFields() {
        nameField.setText(ComManager.getInstance().getUser().getName());
        emailField.setText(ComManager.getInstance().getUser().getEmail());
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_settings, menu);
        return true;
    }*/

}
