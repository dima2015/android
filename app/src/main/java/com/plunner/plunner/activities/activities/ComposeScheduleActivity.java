package com.plunner.plunner.activities.activities;


import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.utils.CalendarPickersViewSupport;
import com.plunner.plunner.utils.CustomWeekEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * An activity that lets users compose a schedule by inserting a name for the schedule and some busy time slots
 */
public class ComposeScheduleActivity extends AppCompatActivity {

    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see events for a given day
     */
    private WeekView mWeekView;
    private ActionBar actionBar;
    private EditText scheduleNameInput;
    private TextView scheduleStatus;
    /**
     * Maps month buttons to {@link Calendar} instancies
     */
    private CalendarPickersViewSupport calendarPickersViewSupport;
    /**
     * A fragment used to create/edit an event in the {@link #mWeekView }
     */
    private EventDetailFragment addEventFragment;
    private List<CustomWeekEvent> composedEvents;
    private List<CustomWeekEvent> deletedEvents;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_schedule);
        //Tolbar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        //View elements retrieval
        scheduleStatus = (TextView) findViewById(R.id.compose_schedule_status);
        calendarPickersViewSupport = CalendarPickersViewSupport.getInstance();
        calendarPickersViewSupport.setActivity(this);
        calendarPickersViewSupport.setDaysPicker((LinearLayout) findViewById(R.id.days_picker));
        calendarPickersViewSupport.setMonthsPicker((LinearLayout) findViewById(R.id.months_picker));
        scheduleNameInput = (EditText) findViewById(R.id.compose_schedule_schedule_name);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        actionBar = getSupportActionBar();
        Switch enabledSwitch = (Switch) findViewById(R.id.compose_schedule_enabled_switch);
        //This activity can come back to the main activity
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Pickers init
        calendarPickersViewSupport.createViews(-1);
        //createCalendarPickersView(-1);
        //Event view init
        setWeekView();
        mWeekView.goToDate(Calendar.getInstance());
        //enabled switch
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEnabledSwitchStatusChange(isChecked);
            }
        });
        composedEvents = new ArrayList<>();
        deletedEvents = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compose_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                //handleFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setWeekView() {
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                onEventClickM(event);
            }
        });
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return composedEvents;
            }

        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                onEmptySpaceClick(time);
            }
        });
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                calendarPickersViewSupport.scrollDayScroll(newFirstVisibleDay, oldFirstVisibleDay);
            }
        });
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                return interpretDateM(date);
            }

            @Override
            public String interpretTime(int hour) {
                return interpretTimeM(hour);
            }
        });
    }

    /**
     * Changes the current day in the {daysPicker}, updating also the {@link #mWeekView}
     *
     * @param v The pressed dayBtn
     */
    public void changeDay(View v) {
        scheduleNameInput.clearFocus();
        Calendar associatedDate = calendarPickersViewSupport.changeDay(v);
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Changes the current month in the monthPicker, recomputing also the days associated to it in the daysPicker
     *
     * @param v The pressed monthButton
     *          //@see #daysPicker
     *          //@see #monthsPicker
     * @see #mWeekView
     */
    public void changeMonth(View v) {
        scheduleNameInput.clearFocus();
        Calendar associatedDate = calendarPickersViewSupport.changeMonth(v);
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Changes the text near the enabledSwitch according to the status of the switch
     *
     * @param isChecked The status of the switch
     */
    private void onEnabledSwitchStatusChange(boolean isChecked) {
        //Resets the focus on the editext relative to the name of the schedule
        scheduleNameInput.clearFocus();
        if (isChecked) {
            scheduleStatus.setText(getResources().getText(R.string.enabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
        } else {
            scheduleStatus.setText(getResources().getText(R.string.disabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.light_gray));
        }
    }

    public void hideFragment(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addEventFragment != null) {
            fragmentTransaction.hide(addEventFragment).commit();
            actionBar.show();
        }

    }

    public void addEventDateChange(View v) {
        addEventFragment.showDatePicker((Integer) v.getTag());
    }

    public void addEventTimeChange(View v) {
        addEventFragment.showTimePicker((Integer) v.getTag());
    }

    public void saveEvent(View v) {
        CustomWeekEvent event = addEventFragment.save();
        int position;
        if (event != null) {
            hideFragment(null);
            if (event.isNew() && !event.isEdited()) {
                composedEvents.add(event);
            } else if (event.isEdited()) {
                position = findEventById(event.getId());
                composedEvents.remove(position);
                composedEvents.add(position, event);
            }
            mWeekView.notifyDatasetChanged();
        }
    }

    public int findEventById(long id) {
        CustomWeekEvent currentEvent;
        for (int i = 0; i < composedEvents.size(); i++) {
            currentEvent = composedEvents.get(i);
            if (currentEvent.getId() == id) {
                return i;
            }
        }
        return -1;

    }

    public void deleteEvent(View v) {
        long eventId = addEventFragment.getCurrentEventId();
        int position = findEventById(eventId);
        deletedEvents.add(composedEvents.get(position));
        composedEvents.remove(position);
        mWeekView.notifyDatasetChanged();
        hideFragment(null);

    }

    private void onEventClickM(WeekViewEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addEventFragment == null) {
            addEventFragment = new EventDetailFragment();
            addEventFragment.setCurrentEventId(event.getId());
            //addEventFragment.setEditEventContent(event.getStartTime(), event.getEndTime());
            actionBar.hide();
            transaction.add(R.id.compose_schedule_root, addEventFragment)
                    .addToBackStack(null).commit();
        } else {
            actionBar.hide();
            addEventFragment.setCurrentEventId(event.getId());
            addEventFragment.setEditEventContent(event.getStartTime(), event.getEndTime());
            transaction.show(addEventFragment).commit();

        }
    }

    private void onEmptySpaceClick(Calendar time) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        actionBar.hide();
        if (addEventFragment == null) {
            addEventFragment = new EventDetailFragment();
            addEventFragment.setInitialDate(time);
            transaction.add(R.id.compose_schedule_root, addEventFragment)
                    .addToBackStack(null).commit();

        } else {
            addEventFragment.setNewEventContent(time);
            transaction.show(addEventFragment).commit();
        }

    }

    private String interpretDateM(Calendar date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
            return sdf.format(date.getTime()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String interpretTimeM(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}

