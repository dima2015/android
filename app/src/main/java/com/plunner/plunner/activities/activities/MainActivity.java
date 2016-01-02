package com.plunner.plunner.activities.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.plunner.plunner.R;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.SetModel;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.Employee;


public class MainActivity extends AppCompatActivity implements SetModel<Employee>, CallOnHttpError, CallOnNext<Employee> {

    Employee employee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this); //TODO remove
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        LoginManager.loginByData("testInit", "testEmp@test.com", "test");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (employee != null) {
                    employee.fresh();
                    Snackbar.make(view, "Already loaded name " + employee.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    (new Employee()).get(new Subscriber<Employee>(MainActivity.this) {
                        @Override
                        public void onNext(Employee employee) {
                            super.onNext(employee);
                            Snackbar.make(view, "Name " + employee.getName(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
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

    @Override
    public void setModel(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void onHttpError(HttpException e) {
        retrofit.HttpException response = e.getCause();
        int code = response.code(); //HTTP code
        String errorBody = e.getErrorBody();
        //TODO error, eventually ask the login
        //TODO automatically try to get token by long token
    }

    @Override
    public void onNext(Employee employee) {
        ;
        //TODO implement or remove
    }
}
