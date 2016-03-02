package com.plunner.plunner.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.adapters.FragmentsTabViewAdapter;
import com.plunner.plunner.activities.fragments.MeetingsFragment;
import com.plunner.plunner.activities.fragments.SchedulesFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.utils.ComManager;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * CODE REVISED, NEED DOCUMENT
 */
public class DashboardActivity extends AppCompatActivity {


    private MeetingsFragment meetingsFragment;
    private SchedulesFragment schedulesFragment;
    private ComManager comManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Action bar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.w("2","I'm started");
        //Fab setting
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.dashboard_activity_fab);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard_activity_fab_add_meeting:
                        switchToAddMeetingActivity();
                        return true;
                    case R.id.dashboard_activity_fab_add_schedule:
                        switchToScopedAddActivity();
                        return true;
                    default:
                        return super.onMenuItemSelected(menuItem);
                }
            }

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                try {
                    if (!comManager.isUserPlanner()) {
                        navigationMenu.removeItem(R.id.dashboard_activity_fab_add_meeting);
                    }
                } catch (NullPointerException e) {
                    navigationMenu.removeItem(R.id.dashboard_activity_fab_add_meeting);
                }
                return true;

            }
        });
        //Tab layout setting
        TabLayout tabLayout = (TabLayout) findViewById(R.id.dashboard_activity_tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.dashboard_activity_view_pager);
        comManager = ComManager.getInstance();
        comManager.login(this, new LoginManager.storeTokenCallback() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_dashboard_activity_user) {
            switchToUserProfileActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.w("1", "I'm resumed");
        if(ComManager.getInstance().getUser() != null){

        }

    }

    private void switchToUserProfileActivity() {
        startActivity(new Intent(this, UserSettingsActivity.class));
    }

    private void switchToAddMeetingActivity() {
        Intent intent = new Intent(this, AddMeeting.class);
        startActivity(intent);
    }

    private void switchToScopedAddActivity() {
        Intent intent = new Intent(this, ComposeScheduleActivity.class);
        startActivity(intent);
    }


    public void switchMeetingsType(View v) {
        meetingsFragment.switchMeetingsType(v);
    }


    private class retrieveUserCallaback implements CallOnHttpError<Employee>, CallOnNext<Employee>, CallOnNoHttpError<Employee> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(final Employee employee) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    comManager.setUser(employee);
                    meetingsFragment.initSequence();
                    schedulesFragment.initSequence();
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

}
