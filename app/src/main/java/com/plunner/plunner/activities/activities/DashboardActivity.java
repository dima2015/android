package com.plunner.plunner.activities.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.adapters.FragmentsTabViewAdapter;
import com.plunner.plunner.activities.fragments.MeetingsListFragment;
import com.plunner.plunner.activities.fragments.SchedulesFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.utils.DataExchanger;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Entry point activity of the application, gives access to the creation and visualitation of meetings/schedules,
 * it also lets the users see their settings(email, name, pwd)
 *
 * @author Giorgio Pea
 *
 */
public class DashboardActivity extends AppCompatActivity {

    /**
     * @see MeetingsListFragment
     */
    private MeetingsListFragment meetingsListFragment;
    /**
     * @see SchedulesFragment
     */
    private SchedulesFragment schedulesFragment;
    /**
     * @see DataExchanger
     */
    private DataExchanger dataExchanger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Action bar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Fab setting
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.dashboard_activity_fab);
        dataExchanger = DataExchanger.getInstance();
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard_activity_fab_add_meeting:
                        switchToMeetingActivity();
                        return true;
                    case R.id.dashboard_activity_fab_add_schedule:
                        switchToScheduleActivity();
                        return true;
                    default:
                        return super.onMenuItemSelected(menuItem);
                }
            }

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                try {
                    //I can plan a meeting iff i'm a planner
                    if (!dataExchanger.getUser().isPlanner()) {
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

        //Login, if the user is not authenticated the Login activity is started
        /**
         * @see LoginManager#storeToken(Activity)
         */
        LoginManager.getInstance().storeToken(this, new LoginManager.storeTokenCallback() {
            @Override
            public void onError(Throwable e) {
                //TODO manage

            }

            @Override
            public void onOk(String authtoken) {
                (new Employee<>()).getFactory(new retrieveUserCallback());
            }
        });
        //TabViewAdapter
        FragmentsTabViewAdapter fragmentsTabViewAdapter = new FragmentsTabViewAdapter(getSupportFragmentManager());
        //Binding tabs to fragments
        meetingsListFragment = new MeetingsListFragment();
        schedulesFragment = new SchedulesFragment();
        fragmentsTabViewAdapter.addFragment(meetingsListFragment, "Meetings");
        fragmentsTabViewAdapter.addFragment(schedulesFragment, "Schedules");
        viewPager.setAdapter(fragmentsTabViewAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_dashboard_activity_user) {
            switchToSettingsActivity();
            return true;
        }
        else if(id == R.id.menu_dashboard_activityrefresh_meetings){
            meetingsListFragment.refresh();
            return true;
        }
        else if(id == R.id.menu_dashboard_activity_refresh_schedules){
            schedulesFragment.refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts SettingsActivity
     *
     * @see SettingsActivity
     */
    private void switchToSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
    /**
     * Starts MeetingActivity
     *
     * @see MeetingActivity
     */
    private void switchToMeetingActivity() {

        startActivity(new Intent(this, MeetingActivity.class));
    }
    /**
     * Starts ScheduleActivity
     *
     * @see ScheduleActivity
     */
    private void switchToScheduleActivity() {
        startActivity(new Intent(this, ScheduleActivity.class));
    }

    /**
     * @see MeetingsListFragment#switchMeetingsType(View)
     */
    public void switchMeetingsType(View v) {
        meetingsListFragment.switchMeetingsType(v);
    }

    /**
     * A callback class that manages the response to the request of retrieving user information
     */
    private class retrieveUserCallback implements CallOnHttpError<Employee>, CallOnNext<Employee>, CallOnNoHttpError<Employee> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, DashboardActivity.this, null);
        }

        @Override
        public void onNext(final Employee employee) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Set the user information as globally av
                    dataExchanger.setUser(employee);
                    //Populates the meeting list with meetings
                    meetingsListFragment.initSequence();
                    //Populates the schedules list with schedules
                    schedulesFragment.initSequence();
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }


}
