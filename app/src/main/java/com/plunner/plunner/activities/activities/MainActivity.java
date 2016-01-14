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


/**
 * @author Claudio Cardinale <cardi@thecsea.it>
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity implements SetModel<Employee>, CallOnHttpError, CallOnNext<Employee> {

    Employee employee = null;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this); //TODO remove
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        loginManager = LoginManager.getInstance();
        //TODO check if the token is not null before perform other actions
        loginManager.storeToken(this, new LoginManager.storeTokenCallback() {
            //TODO of cours eit is possible override onOk
            @Override
            public void onError(Throwable e) {
                //TODO manage
                //TODO how do we proceed if the aauthenticator knwos the password but it is not able to verify it becaseu we have a connection lack?
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (employee != null) {
                    employee.fresh(MainActivity.this);
                    //TODO fresh is async so this is not the correct way to use it, it's just an example
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
        loginManager.reLogin(e, this, null);//TODO reed javadoc before use
        //TODO error, eventually ask the login
        //TODO automatically try to get token by long token
    }

    @Override
    public void onNext(Employee employee) {
        //TODO implement or remove
    }
}
