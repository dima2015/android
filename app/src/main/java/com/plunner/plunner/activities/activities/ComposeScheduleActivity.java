package com.plunner.plunner.activities.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
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
import com.plunner.plunner.activities.fragments.EventDetailFragment;
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
import com.plunner.plunner.utils.TimeslotBackEndAdapter;
import com.plunner.plunner.utils.TimeslotFrontEndAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private EditText scheduleName;
    private TextView scheduleStatus;
    private TextView scheduleNameError;
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
            editMode = true;
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
        scheduleName = (EditText) findViewById(R.id.compose_schedule_schedule_name);
        scheduleNameError = (TextView) findViewById(R.id.compose_schedule_schedule_name_error);
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
            scheduleName.setText(exchangedSchedule.getName());
            setTitle(getResources().getText(R.string.edit_schedule));
            if (exchangedSchedule.getEnabled().equals("0")) {
                enabledSwitch.setChecked(false);
                onEnabledSwitchStatusChange(false);
            }
            retrieveScheduleTimslots();
        }

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
            case R.id.menu_compose_schedule_send:
                sendData();
                return true;
            case R.id.menu_compose_schedule_delete:
                deleteSchedule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        if(!editMode){
            menu.removeItem(R.id.menu_compose_schedule_delete);
        }
        return true;
    }


    private void retrieveScheduleTimslots() {
        exchangedSchedule.getTimeslots().load(new GetTimeslotsCallback());
    }

    private void deleteSchedule() {
        AlertDialog.Builder alertDialogB = new AlertDialog.Builder(this);
        alertDialogB.setTitle("Delete Schedule");
        alertDialogB.setMessage("Are you sure you want to delete this schedule?");
        alertDialogB.setNegativeButton("ABORT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = ProgressDialog.show(ComposeScheduleActivity.this, "", "Deleting schedule", true);
                exchangedSchedule.delete(new DeleteScheduleCallback());
            }
        });
        alertDialogB.show();

    }



    private void setWeekView() {
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(final WeekViewEvent event, RectF eventRect) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onEventClickM(event);
                    }
                });

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
            public void onEmptyViewClicked(final Calendar time) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onEmptySpaceClick(time);
                    }
                });

            }
        });
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(final Calendar newFirstVisibleDay, final Calendar oldFirstVisibleDay) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        calendarPickersViewSupport.scrollDayScroll(newFirstVisibleDay, oldFirstVisibleDay);
                    }
                });

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
        scheduleName.clearFocus();
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
        scheduleName.clearFocus();
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
        scheduleName.clearFocus();
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
        public void onNext(final ModelList<Timeslot> timeslotModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertTimeslots(timeslotModelList);
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void insertTimeslots(ModelList<Timeslot> timeslotModelList) {
        List<Timeslot> timeslots = timeslotModelList.getModels();
        Timeslot timeslot;
        List<CustomWeekEvent> tmpList = new ArrayList<>();
        Map<String, Calendar> adaptedEvent;
        for (int i = 0; i < timeslots.size(); i++) {
            timeslot = timeslots.get(i);
            fromIdToTimeslot.put(timeslot.getId(), timeslot);
            try {
                adaptedEvent = TimeslotFrontEndAdapter.getInstance().adapt(timeslot.getTimeStart(), timeslot.getTimeEnd());
                tmpList.add(new CustomWeekEvent(Integer.parseInt(timeslot.getId()), "", adaptedEvent.get("startTime"), adaptedEvent.get("endTime"), false, false));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        composedEvents.clear();
        composedEvents.addAll(tmpList);
        mWeekView.notifyDatasetChanged();
    }
    private boolean validate(){
        boolean errorName, errorTimeslots = false;
        if(scheduleName.getText().toString().equals("")){
            errorName = true;
            scheduleNameError.setVisibility(View.VISIBLE);
        }
        else{
            errorName = false;
            scheduleNameError.setVisibility(View.GONE);
        }
        if(composedEvents.size() == 0){
            errorTimeslots = true;
            makeSnackBar("Please insert at least one busy timeslot");
        }
        return !(errorName && errorTimeslots);
    }

    private void makeSnackBar(String message){
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(R.id.compose_schedule_root), message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snackbar.show();
    }
    private void sendData() {
        com.plunner.plunner.models.models.employee.Calendar schedule;
        if(validate()){
            if (!editMode) {
                progressDialog = ProgressDialog.show(this,"","Adding schedule",true);
                schedule = new com.plunner.plunner.models.models.employee.Calendar();
            } else {
                schedule = exchangedSchedule;
                progressDialog = ProgressDialog.show(this,"","Saving changes", true);
            }
            schedule.setName(scheduleName.getText().toString());
            if(enabledSwitch.isChecked()){
                schedule.setEnabled("1");
            }
            else{
                schedule.setEnabled("0");
            }

            schedule.save(new SaveScheduleCallback());
        }
    }

    private class SaveScheduleCallback implements CallOnHttpError<com.plunner.plunner.models.models.employee.Calendar>, CallOnNext<com.plunner.plunner.models.models.employee.Calendar>, CallOnNoHttpError<com.plunner.plunner.models.models.employee.Calendar> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(final com.plunner.plunner.models.models.employee.Calendar calendar) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sendTimeslots(calendar.getId());
                }
            });

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
        if(newEvents.size() == 0){
            progressDialog.dismiss();
            startActivity(new Intent(this,DashboardActivity.class));
        }
        for (int i = 0; i < newEvents.size(); i++) {
            eventMap = TimeslotBackEndAdapter.getInstance().adapt(composedEvents.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(id);
            timeslot.setTimeStart(eventMap.get("startTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            timeslot.save(new SaveTimeslotCallback(i+1, composedEvents.size()));
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
            eventMap = TimeslotBackEndAdapter.getInstance().adapt(events.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(ComManager.getInstance().getExchangeSchedule().getId());
            timeslot.setTimeStart(eventMap.get("starTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            timeslot.save(new UpdateTimeslotsCallback(i+1, events.size()));
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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
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
            });

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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == tot) {
                        deleteTimeslots();
                    }
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void deleteTimeslots() {
        if(deletedEvents.size() == 0){
            progressDialog.dismiss();
        }
        for (int i = 0; i < deletedEvents.size(); i++) {
            fromIdToTimeslot.get(deletedEvents.get(i)).delete(new DeleteTimeslotCallback(i+1, deletedEvents.size()));
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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == tot) {
                        progressDialog.dismiss();
                        startActivity(new Intent(ComposeScheduleActivity.this, DashboardActivity.class));
                    }
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class DeleteScheduleCallback implements CallOnHttpError<com.plunner.plunner.models.models.employee.Calendar>, CallOnNext<com.plunner.plunner.models.models.employee.Calendar>, CallOnNoHttpError<com.plunner.plunner.models.models.employee.Calendar>  {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(com.plunner.plunner.models.models.employee.Calendar calendar) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    startActivity(new Intent(ComposeScheduleActivity.this, DashboardActivity.class));
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
}

