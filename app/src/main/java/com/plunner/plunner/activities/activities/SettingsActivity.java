package com.plunner.plunner.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.utils.DataExchanger;

public class SettingsActivity extends AppCompatActivity {

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

    /**
     * Populates the view with data about the user
     */
    private void retrieveFields() {
        nameField.setText(DataExchanger.getInstance().getUser().getName());
        emailField.setText(DataExchanger.getInstance().getUser().getEmail());
    }

}
