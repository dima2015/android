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

        Intent intent = getIntent();
        if(!intent.getExtras().getBoolean("Testing")){
            retrieveFields();
        }

    }

    private void retrieveFields() {
        nameField.setText(DataExchanger.getInstance().getUser().getName());
        emailField.setText(DataExchanger.getInstance().getUser().getEmail());
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_settings, menu);
        return true;
    }*/
    public void retrieveFields(Employee employee){
        nameField.setText(employee.getName());
        emailField.setText(employee.getEmail());

    }

}
