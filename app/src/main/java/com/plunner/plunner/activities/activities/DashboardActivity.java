package com.plunner.plunner.activities.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.Adapters.FragmentsTabViewAdapter;
import com.plunner.plunner.activities.Fragments.MeetingsFragment;
import com.plunner.plunner.activities.Fragments.SchedulesFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Group;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.utils.ComManager;
import com.plunner.plunner.utils.GlobalData;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MeetingsFragment.OnFragmentInteractionListener, SchedulesFragment.OnFragmentInteractionListener {


    private MeetingsFragment meetingsFragment;
    private SchedulesFragment schedulesFragment;
    private Employee userModel;
    private ComManager comManager;
    private boolean loadingFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Action bar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigation drawer setting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Fab setting
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.meetingsFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScopedAddActivity();
            }
        });
        //Tab layout setting
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        comManager = ComManager.getInstance();
        comManager.login(this, new LoginManager.storeTokenCallback() {
            //TODO of cours eit is possible override onOk
            @Override
            public void onError(Throwable e) {
                //TODO manage
                //TODO how do we proceed if the aauthenticator knwos the password but it is not able to verify it becaseu we have a connection lack?
            }

            @Override
            public void onOk(String authtoken) {
                comManager.retrieveUser(new retrieveUserCallaback());
            }
        });
        FragmentsTabViewAdapter fragmentsTabViewAdapter = new FragmentsTabViewAdapter(getSupportFragmentManager());
        //Binding tabs to fragments
        meetingsFragment = new MeetingsFragment();
        schedulesFragment = new SchedulesFragment();
        fragmentsTabViewAdapter.addFragment(meetingsFragment, "Meetings");
        fragmentsTabViewAdapter.addFragment(schedulesFragment, "Schedules");
        viewPager.setAdapter(fragmentsTabViewAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void switchToScopedAddActivity() {
        Intent intent = new Intent(this, ComposeScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void switchMeetingsType(View v) {
        meetingsFragment.switchMeetingsType(v);
    }

    public void switchSchedulesType(View v) {
        schedulesFragment.switchSchedulesType(v);
    }



    private class retrieveUserCallaback implements CallOnHttpError<Employee>, CallOnNext<Employee>, CallOnNoHttpError<Employee> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Employee employee) {
            comManager.setUser(employee);
            meetingsFragment.initSequence();
            schedulesFragment.initSequence();
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }


}
