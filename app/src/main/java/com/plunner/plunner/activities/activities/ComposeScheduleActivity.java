package com.plunner.plunner.activities.activities;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Timeslot;
import com.plunner.plunner.utils.CalendarPickersViewSupport;
import com.plunner.plunner.utils.ComManager;
import com.plunner.plunner.utils.CustomWeekEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private Map<String, Timeslot> fromIdToTimeslot;
    private com.plunner.plunner.models.models.employee.Calendar exchangedSchedule;
    private boolean editMode;
    private ProgressDialog progressDialog;
    private Switch enabledSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_schedule);
        Intent intent = getIntent();
        editMode = false;
        if (intent.getExtras() != null) {
            String mode = intent.getExtras().getString("mode");
            if(mode.equals("edit")){
                editMode = true;
            }
        }
        //Tolbar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //View elements retrieval
        scheduleStatus = (TextView) findViewById(R.id.compose_schedule_status);
        calendarPickersViewSupport = CalendarPickersViewSupport.getInstance();
        calendarPickersViewSupport.setActivity(this);
        calendarPickersViewSupport.setDaysPicker((LinearLayout) findViewById(R.id.days_picker));
        calendarPickersViewSupport.setMonthsPicker((LinearLayout) findViewById(R.id.months_picker));
        scheduleNameInput = (EditText) findViewById(R.id.compose_schedule_schedule_name);
        mWeekView = (WeekView) findViewById(R.id.weekView);

        enabledSwitch = (Switch) findViewById(R.id.compose_schedule_enabled_switch);
        //This activity can come back to the main activity

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
        fromIdToTimeslot = new HashMap<>();
        if (editMode) {
            exchangedSchedule = ComManager.getInstance().getExchangeSchedule();
            scheduleNameInput.setText(exchangedSchedule.getName());
            setTitle(getResources().getText(R.string.edit_schedule));
            if (exchangedSchedule.getEnabled().equals("0")) {
                enabledSwitch.setChecked(false);
                onEnabledSwitchStatusChange(false);
            }
            retrieveScheduleTimslots();
        }

    }

    private void retrieveScheduleTimslots() {
        exchangedSchedule.getTimeslots().load(new GetTimeslotsCallback());
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
                sendData();
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
            addEventFragment.setMode(false);
            addEventFragment.setInitialDate(event.getStartTime());
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
            addEventFragment.setMode(true);
            addEventFragment.setInitialDate(time);
            transaction.add(R.id.compose_schedule_root, addEventFragment)
                    .addToBackStack(null).commit();

        } else {
            addEventFragment.setNewEventContent(time,true);
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


    private class GetTimeslotsCallback implements CallOnHttpError<ModelList<Timeslot>>, CallOnNext<ModelList<Timeslot>>, CallOnNoHttpError<ModelList<Timeslot>> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Timeslot> timeslotModelList) {
            List<Timeslot> timeslots = timeslotModelList.getModels();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            Timeslot timeslot;
            Calendar calendar_one = Calendar.getInstance();
            Calendar calendar_two = (Calendar) calendar_one.clone();
            List<CustomWeekEvent> provList = new ArrayList<>();
            Date parsedOne, parsedTwo;
            for (int i = 0; i < timeslots.size(); i++) {
                timeslot = timeslots.get(i);
                fromIdToTimeslot.put(timeslot.getId(), timeslot);
                try {
                    parsedOne = sdf.parse(timeslot.getTimeStart());
                    parsedTwo = sdf.parse(timeslot.getTimeEnd());
                    calendar_one.setTime(parsedOne);
                    calendar_two.setTime(parsedTwo);
                    provList.add(new CustomWeekEvent(Integer.parseInt(timeslot.getId()), "", calendar_one, calendar_two, false, false));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            composedEvents.clear();
            composedEvents.addAll(provList);
            mWeekView.notifyDatasetChanged();
            mWeekView.notifyDatasetChanged();
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private Map<String, String> eventFormatAdapter(CustomWeekEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("startTime", sdf.format(event.getStartTime().getTime()));
        map.put("endTime", sdf.format(event.getEndTime().getTime()));
        return map;
    }

    private void sendData() {
        com.plunner.plunner.models.models.employee.Calendar schedule;
        if (!editMode) {
            progressDialog = ProgressDialog.show(this,"","Adding schedule",true);
            schedule = new com.plunner.plunner.models.models.employee.Calendar();
        } else {
            schedule = exchangedSchedule;
            progressDialog = ProgressDialog.show(this,"","Saving changes", true);
        }
        schedule.setName(scheduleNameInput.getText().toString());
        if(enabledSwitch.isChecked()){
            schedule.setEnabled("1");
        }
        else{
            schedule.setEnabled("0");
        }

        schedule.save(new SaveScheduleCallback());
    }

    private class SaveScheduleCallback implements CallOnHttpError<com.plunner.plunner.models.models.employee.Calendar>, CallOnNext<com.plunner.plunner.models.models.employee.Calendar>, CallOnNoHttpError<com.plunner.plunner.models.models.employee.Calendar> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(com.plunner.plunner.models.models.employee.Calendar calendar) {
            sendTimeslots(calendar.getId());
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void sendTimeslots(String id) {
        Timeslot timeslot;
        CustomWeekEvent currentEvent;
        List<CustomWeekEvent> newEvents = new ArrayList<>();

        Map<String, String> eventMap;
        for (int i = 0; i < composedEvents.size(); i++) {
            currentEvent = composedEvents.get(i);
            if (currentEvent.isNew()) {
                newEvents.add(currentEvent);
            }
        }
        for (int i = 0; i < newEvents.size(); i++) {
            eventMap = eventFormatAdapter(composedEvents.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(id);
            timeslot.setTimeStart(eventMap.get("startTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            timeslot.save(new SaveTimeslotCallback(i, composedEvents.size()));
        }
        if(newEvents.size() == 0){
            progressDialog.dismiss();
        }
    }

    private void sendUpdatedEvents() {
        List<CustomWeekEvent> events = new ArrayList<>();
        CustomWeekEvent currentEvent;
        Map<String, String> eventMap;
        Timeslot timeslot;
        for (int i = 0; i < composedEvents.size(); i++) {
            currentEvent = composedEvents.get(i);
            if (currentEvent.isEdited() && !currentEvent.isNew()) {
                events.add(currentEvent);
            }
        }
        if(events.size() == 0){
            progressDialog.dismiss();
        }
        for (int i = 0; i < events.size(); i++) {
            eventMap = eventFormatAdapter(events.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(ComManager.getInstance().getExchangeSchedule().getId());
            timeslot.setTimeStart(eventMap.get("starTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            timeslot.save(new UpdateTimeslotsCallback(i, events.size()));
        }



    }

    private class SaveTimeslotCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        int index, tot;

        public SaveTimeslotCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Timeslot timeslot) {
            if (index == tot) {
                if (editMode) {
                    sendUpdatedEvents();
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ComposeScheduleActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class UpdateTimeslotsCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        private int index, tot;

        public UpdateTimeslotsCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Timeslot timeslot) {
            if (index == tot) {
                deleteTimeslots();
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void deleteTimeslots() {
        for (int i = 0; i < deletedEvents.size(); i++) {
            fromIdToTimeslot.get(deletedEvents.get(i)).delete(new DeleteTimeslotCallback(i, deletedEvents.size()));
        }
        if(deletedEvents.size() == 0){
            progressDialog.dismiss();
        }
    }

    private class DeleteTimeslotCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        private int index, tot;

        public DeleteTimeslotCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Timeslot timeslot) {
            if (index == tot) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
}

