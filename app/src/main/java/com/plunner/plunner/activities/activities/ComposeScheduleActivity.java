package com.plunner.plunner.activities.activities;

import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.CalendarFragment;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ComposeScheduleActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener {


    private WeekView mWeekView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*MaterialCalendarView widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                NavUtils.navigateUpFromSameTask(ComposeScheduleActivity.this);
            }
        });
        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });*/

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CalendarFragment fragment = new CalendarFragment();
        transaction.add(R.id.lapal, fragment);
        transaction.commit();*/


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);
        setWeekView();
// Set an action when any event is clicked.



// Set long press listener for events.



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compose, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                handleFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setWeekView() {
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {

            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
            }
        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 1);
                endTime.set(Calendar.MONTH, newMonth - 1);
                WeekViewEvent event = new WeekViewEvent(1, startTime.toString(), startTime, endTime);
                event.setColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, 4);
                endTime.set(Calendar.MINUTE, 30);
                endTime.set(Calendar.MONTH, newMonth - 1);
                event = new WeekViewEvent(10, startTime.toString(), startTime, endTime);
                event.setColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 4);
                startTime.set(Calendar.MINUTE, 20);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, 5);
                endTime.set(Calendar.MINUTE, 0);
                event = new WeekViewEvent(10, startTime.toString(), startTime, endTime);
                event.setColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 5);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 2);
                endTime.set(Calendar.MONTH, newMonth - 1);
                event = new WeekViewEvent(2, startTime.toString(), startTime, endTime);
                event.setColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
                events.add(event);


                return events;
            }

        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new EventDetailFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                actionBar.hide();
                transaction.add(R.id.best, fragment)
                        .addToBackStack(null).commit();


            }
        });
    }

    public void showToolbar() {
        actionBar.show();
    }


    private void handleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.lapal);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            CalendarFragment fragment_1 = new CalendarFragment();
            transaction.add(R.id.lapal, fragment_1);
            transaction.commit();
        } else {
            if (fragment.isHidden()) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }

    }




    @Override
    public void onCalendarDateSelected(Calendar date) {
        mWeekView.goToDate(date);
        handleFragment();
    }
}
