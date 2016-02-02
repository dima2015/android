package com.plunner.plunner.activities.activities;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.CalendarFragment;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.utils.CalendarPickers;
import com.plunner.plunner.utils.DayMonthStructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ComposeScheduleActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener {


    private WeekView mWeekView;
    private LinearLayout monthsPicker;
    private LinearLayout daysPicker;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        monthsPicker = (LinearLayout) findViewById(R.id.months_picker);
        daysPicker = (LinearLayout) findViewById(R.id.days_picker);

        createCalendarPickersView(-1);
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
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 15);
                WeekViewEvent event = new WeekViewEvent(1, startTime.toString(), startTime, endTime);
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

    private void createCalendarPickersView(int month){
        TextView monthView, dayView;
        Map<String, List<DayMonthStructure>> labelsLists = CalendarPickers.getInstance().build(month);
        List<DayMonthStructure> monthsLabels = labelsLists.get("months");
        List<DayMonthStructure> daysLabels = labelsLists.get("days");
        LayoutInflater inflater = getLayoutInflater();
        String currentDay = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        DayMonthStructure currentItem;

        for(int i=0; i<monthsLabels.size(); i++){
            monthView = (TextView) inflater.inflate(R.layout.month_item, monthsPicker, false);
            currentItem = monthsLabels.get(i);
            monthView.setText(currentItem.getValue());
            if(month == - 1 && currentItem.getIsCurrent()){
                monthView.setTextColor(ContextCompat.getColor(this, R.color.red));
            }
            monthsPicker.addView(monthView);
        }
        for(int i=0; i<daysLabels.size(); i++){
            dayView = (TextView) inflater.inflate(R.layout.day_item, daysPicker, false);
            currentItem = daysLabels.get(i);
            dayView.setText(currentItem.getValue());
            if(month == -1 && currentItem.getIsCurrent()){
                dayView.setTextColor(ContextCompat.getColor(this, R.color.red));
            }

            daysPicker.addView(dayView);
        }

    }

    public void changeDay(View v){
        Calendar calendar = Calendar.getInstance();
        TextView textView = (TextView) v;
        int day = Integer.parseInt(textView.getText().toString());
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int childCount = daysPicker.getChildCount();
        for(int i=0; i<childCount; i++){
            ((TextView) daysPicker.getChildAt(i)).setTextColor(ContextCompat.getColor(this,R.color.light_gray));
        }
        textView.setTextColor(ContextCompat.getColor(this, R.color.red));
        mWeekView.goToDate(calendar);
    }

    public void changeMonth(View v){
        Calendar calendar = Calendar.getInstance();
        TextView textView = (TextView) v;
        String[] split = textView.getText().toString().split(" ");
        String month = split[0];
        String year = split[1];
        int monthIndex = CalendarPickers.getInstance().getMonthIndex(month);
        int yearIndex = Integer.parseInt(year);
        calendar.set(Calendar.MONTH, monthIndex-1);
        calendar.set(Calendar.YEAR, yearIndex);
        int childCount = monthsPicker.getChildCount();
        for(int i=0; i<childCount; i++){
            ((TextView) monthsPicker.getChildAt(i)).setTextColor(ContextCompat.getColor(this,R.color.light_gray));
        }
        ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.red));
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.YEAR);
        mWeekView.goToDate(calendar);

    }
}

