package com.plunner.plunner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.plunner.plunner.models.Employee;
import com.plunner.plunner.models.Model;
import com.plunner.plunner.models.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Model.AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoiZW4iLCJzdWIiOiIzNCIsImlzcyI6Imh0dHA6XC9cL2FwaS5wbHVubmVyLmNvbVwvZW1wbG95ZWVzXC9ncm91cHMiLCJpYXQiOiIxNDUwNDg4OTkwIiwiZXhwIjoiMTQ1MDQ5MjU5MCIsIm5iZiI6IjE0NTA0ODg5OTAiLCJqdGkiOiJhODMwMzI0ZGNiMTU4MjA4NWMwYjUyZGQ0MDU0ZjdiOSJ9.P6PecWOQi_JByd9fKiNWBxouaq-leQDbVid2kLQRbjU";
                Employee.getEmployee(new Subscriber() {
                    @Override
                    public void onNext(Model model) {
                        super.onNext(model);
                        Employee employee = (Employee) model;
                        Snackbar.make(view, "Name " + employee.getName(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
