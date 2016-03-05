package com.plunner.plunner.activities.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.plunner.plunner.activities.fragments.TimeslotDetailFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Timeslot;
import com.plunner.plunner.utils.CalendarPickersViewSupport;
import com.plunner.plunner.utils.CustomWeekEvent;
import com.plunner.plunner.utils.DataExchanger;
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
public class ScheduleActivity extends AppCompatActivity {

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
    private TimeslotDetailFragment timeslotDetailFragment;
    private List<CustomWeekEvent> composedTimeslots;
    private List<CustomWeekEvent> deletedTimeslots;
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
        //Checks if i'm editing a meeting or not
        editMode = intent.getExtras() != null;
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

        //WeekView init and pickers init
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
        composedTimeslots = new ArrayList<>();
        deletedTimeslots = new ArrayList<>();
        fromIdToTimeslot = new HashMap<>();
        if (editMode) {
            //If i'm editing a schedule i grab its information and timeslots
            exchangedSchedule = DataExchanger.getInstance().getSchedule();
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
                saveSchedule();
                return true;
            case R.id.menu_compose_schedule_delete:
                deleteSchedule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!editMode) {
            menu.removeItem(R.id.menu_compose_schedule_delete);
        }
        return true;
    }

    /**
     * Retrieves the timeslots of the schedule the user is editing
     */
    private void retrieveScheduleTimslots() {
        exchangedSchedule.getTimeslots().load(new RetrieveTimeslotsCallback());
    }

    /**
     * deletes the schedule the planner is editing
     */
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
                progressDialog = ProgressDialog.show(ScheduleActivity.this, "", "Deleting schedule", true);
                exchangedSchedule.delete(new DeleteScheduleCallback());
            }
        });
        alertDialogB.show();

    }


    /**
     * Sets some action listeners for the TimeslotsDay widget
     *
     * @see WeekView
     */
    private void setWeekView() {
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(final WeekViewEvent event, RectF eventRect) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onTimeslotClick(event);
                    }
                });

            }
        });
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return composedTimeslots;
            }

        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(final Calendar time) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onEmptyAreaClick(time);
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
     * Response to click on a timeslot in the {@link #mWeekView}
     *
     * @param timeslot The clicked timeslot
     */
    private void onTimeslotClick(WeekViewEvent timeslot) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (timeslotDetailFragment == null) {
            timeslotDetailFragment = new TimeslotDetailFragment();
            timeslotDetailFragment.setMode(false);
            timeslotDetailFragment.setInitialDate(timeslot.getStartTime());
            timeslotDetailFragment.setCurrentEventId(timeslot.getId());
            //timeslotDetailFragment.setEditEventContent(timeslot.getStartTime(), timeslot.getEndTime());
            actionBar.hide();
            transaction.add(R.id.compose_schedule_root, timeslotDetailFragment)
                    .addToBackStack(null).commit();
        } else {
            actionBar.hide();
            timeslotDetailFragment.setCurrentEventId(timeslot.getId());
            timeslotDetailFragment.setEditEventContent(timeslot.getStartTime(), timeslot.getEndTime());
            transaction.show(timeslotDetailFragment).commit();

        }
    }

    /**
     * Response to click on a empty area in the {@link #mWeekView}
     *
     * @param time The date that corresponds to that area
     */
    private void onEmptyAreaClick(Calendar time) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        actionBar.hide();
        if (timeslotDetailFragment == null) {
            timeslotDetailFragment = new TimeslotDetailFragment();
            timeslotDetailFragment.setMode(true);
            timeslotDetailFragment.setInitialDate(time);
            transaction.add(R.id.compose_schedule_root, timeslotDetailFragment)
                    .addToBackStack(null).commit();

        } else {
            timeslotDetailFragment.setNewEventContent(time, true);
            transaction.show(timeslotDetailFragment).commit();
        }

    }

    /**
     * How dates are interpreted in the context of {@link #mWeekView}
     *
     * @param date The date to be interpreted
     * @return The string representing the interpreted date
     */
    private String interpretDateM(Calendar date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
            return sdf.format(date.getTime()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * How hours are interpreted in the context of {@link #mWeekView}
     *
     * @param hour The hour to be interpreted
     * @return The string representing the interpreted time
     */
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

    /**
     * Invoked when the user clicks on a day button of the CalendarPicker widget
     *
     * @param v The day button pressed
     */
    public void changeDay(View v) {
        scheduleName.clearFocus();
        Calendar associatedDate = calendarPickersViewSupport.changeDay(v);
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Invoked when the user clicks on a month button of the CalendarPicker widget
     *
     * @param v The day button pressed
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
            scheduleStatus.setTextColor(ContextCompat.getColor(ScheduleActivity.this, R.color.colorPrimary));
        } else {
            scheduleStatus.setText(getResources().getText(R.string.disabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ScheduleActivity.this, R.color.light_gray));
        }
    }

    /**
     * Invoked when the user dismisses the {@link TimeslotDetailFragment}
     *
     * @param v The button clicked to dismiss the {@link TimeslotDetailFragment}
     */
    public void hideFragment(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (timeslotDetailFragment != null) {
            fragmentTransaction.hide(timeslotDetailFragment).commit();
            actionBar.show();
        }

    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#showDatePicker(int)
     */
    public void showDatePicker(View v) {
        timeslotDetailFragment.showDatePicker((Integer) v.getTag());
    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#showTimePicker(int)
     */
    public void showTimePicker(View v) {
        timeslotDetailFragment.showTimePicker((Integer) v.getTag());
    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#save()
     */
    public void saveTimeslot(View v) {
        CustomWeekEvent event = timeslotDetailFragment.save();
        int position;
        if (event != null) {
            hideFragment(null);
            if (event.isNew() && !event.isEdited()) {
                composedTimeslots.add(event);
            } else if (event.isEdited()) {
                position = findTimeslotById(event.getId());
                composedTimeslots.remove(position);
                composedTimeslots.add(position, event);
            }
            mWeekView.notifyDatasetChanged();
        }
    }

    /**
     * Finds a timeslot in the {@link #composedTimeslots} list by id
     *
     * @param id The id of the timeslot to be searched
     * @return The position in the {@link #composedTimeslots} list of the timeslot with the given id,
     * if the timeslot is not found -1 is returned
     */
    public int findTimeslotById(long id) {
        CustomWeekEvent currentEvent;
        for (int i = 0; i < composedTimeslots.size(); i++) {
            currentEvent = composedTimeslots.get(i);
            if (currentEvent.getId() == id) {
                return i;
            }
        }
        return -1;

    }

    /**
     * Invoked when the user deletes an event
     *
     * @param v The button clicked to delete the timeslot
     */
    public void deleteTimeslot(View v) {
        long eventId = timeslotDetailFragment.getCurrentEventId();
        int position = findTimeslotById(eventId);
        deletedTimeslots.add(composedTimeslots.get(position));
        composedTimeslots.remove(position);
        mWeekView.notifyDatasetChanged();
        hideFragment(null);

    }


    private class RetrieveTimeslotsCallback implements CallOnHttpError<ModelList<Timeslot>>, CallOnNext<ModelList<Timeslot>>, CallOnNoHttpError<ModelList<Timeslot>> {
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
        composedTimeslots.clear();
        composedTimeslots.addAll(tmpList);
        mWeekView.notifyDatasetChanged();
    }

    private boolean validate() {
        boolean errorName, errorTimeslots = false;
        if (scheduleName.getText().toString().equals("")) {
            errorName = true;
            scheduleNameError.setVisibility(View.VISIBLE);
        } else {
            errorName = false;
            scheduleNameError.setVisibility(View.GONE);
        }
        if (composedTimeslots.size() == 0) {
            errorTimeslots = true;
            createSnackBar("Please insert at least one busy timeslot");
        }
        return !(errorName && errorTimeslots);
    }

    /**
     * Saves the edited or created schedule
     */
    private void saveSchedule() {
        com.plunner.plunner.models.models.employee.Calendar schedule;
        if (validate()) {
            if (!editMode) {
                progressDialog = ProgressDialog.show(this, "", "Adding schedule", true);
                schedule = new com.plunner.plunner.models.models.employee.Calendar();
            } else {
                schedule = exchangedSchedule;
                progressDialog = ProgressDialog.show(this, "", "Saving changes", true);
            }
            schedule.setName(scheduleName.getText().toString());
            if (enabledSwitch.isChecked()) {
                schedule.setEnabled("1");
            } else {
                schedule.setEnabled("0");
            }

            schedule.save(new SaveScheduleCallback());
        }
    }

    /**
     * Callback to {@link #saveSchedule()}
     */
    private class SaveScheduleCallback implements CallOnHttpError<com.plunner.plunner.models.models.employee.Calendar>, CallOnNext<com.plunner.plunner.models.models.employee.Calendar>, CallOnNoHttpError<com.plunner.plunner.models.models.employee.Calendar> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(final com.plunner.plunner.models.models.employee.Calendar calendar) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    saveTimeslots(calendar.getId());
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    /**
     * Save the timeslots associated to the edited or created meeting
     *
     * @param id The id of the edited/created schedule
     */
    private void saveTimeslots(String id) {
        Timeslot timeslot;
        CustomWeekEvent currentEvent;
        List<CustomWeekEvent> newEvents = new ArrayList<>();

        Map<String, String> eventMap;
        for (int i = 0; i < composedTimeslots.size(); i++) {
            currentEvent = composedTimeslots.get(i);
            if (currentEvent.isNew()) {
                newEvents.add(currentEvent);
            }
        }
        if (newEvents.size() == 0) {
            saveUpdatedTimeslots();
        }
        for (int i = 0; i < newEvents.size(); i++) {
            eventMap = TimeslotBackEndAdapter.getInstance().adapt(composedTimeslots.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(id);
            timeslot.setTimeStart(eventMap.get("startTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            timeslot.save(new SaveTimeslotCallback(i + 1, composedTimeslots.size()));
        }

    }

    /**
     * Saves the edited timeslots
     */
    private void saveUpdatedTimeslots() {
        List<CustomWeekEvent> events = new ArrayList<>();
        CustomWeekEvent currentEvent;
        Map<String, String> eventMap;
        Timeslot timeslot;
        for (int i = 0; i < composedTimeslots.size(); i++) {
            currentEvent = composedTimeslots.get(i);
            if (currentEvent.isEdited() && !currentEvent.isNew()) {
                events.add(currentEvent);
            }
        }
        if (events.size() == 0) {
            saveDeletedTimeslots();
        }
        for (int i = 0; i < events.size(); i++) {
            eventMap = TimeslotBackEndAdapter.getInstance().adapt(events.get(i));
            timeslot = new Timeslot();
            timeslot.setFatherParameters(DataExchanger.getInstance().getSchedule().getId());
            timeslot.setTimeStart(eventMap.get("starTime"));
            timeslot.setTimeEnd(eventMap.get("endTime"));
            //Backend save request
            timeslot.save(new SaveUpdatedTimeslotsCallback(i + 1, events.size()));
        }


    }

    /**
     * Callback to {@link #saveTimeslots(String)}
     */
    private class SaveTimeslotCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        int index, tot;

        public SaveTimeslotCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, ScheduleActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            ScheduleActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(Timeslot timeslot) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == tot) {
                        if (editMode) {
                            saveUpdatedTimeslots();
                        } else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(ScheduleActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Callback to {@link #saveUpdatedTimeslots()}
     */
    private class SaveUpdatedTimeslotsCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        private int index, tot;

        public SaveUpdatedTimeslotsCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, ScheduleActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            ScheduleActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(Timeslot timeslot) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == tot) {
                        saveDeletedTimeslots();
                    }
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Saves the deletion of timeslots by the user
     */
    private void saveDeletedTimeslots() {
        if (deletedTimeslots.size() == 0) {
            progressDialog.dismiss();
        }
        for (int i = 0; i < deletedTimeslots.size(); i++) {
            fromIdToTimeslot.get(Integer.toString((int) deletedTimeslots.get(i).getId())).delete(new DeleteTimeslotCallback(i + 1, deletedTimeslots.size()));
        }

    }

    /**
     * Callback to {@link #deletedTimeslots}
     */
    private class DeleteTimeslotCallback implements CallOnHttpError<Timeslot>, CallOnNext<Timeslot>, CallOnNoHttpError<Timeslot> {
        private int index, tot;

        public DeleteTimeslotCallback(int i, int size) {
            index = i;
            tot = size;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, ScheduleActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            ScheduleActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(Timeslot timeslot) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == tot) {
                        progressDialog.dismiss();
                        startActivity(new Intent(ScheduleActivity.this, DashboardActivity.class));
                    }
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Callback to {@link #deleteSchedule()}
     */
    private class DeleteScheduleCallback implements CallOnHttpError<com.plunner.plunner.models.models.employee.Calendar>, CallOnNext<com.plunner.plunner.models.models.employee.Calendar>, CallOnNoHttpError<com.plunner.plunner.models.models.employee.Calendar> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, ScheduleActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            ScheduleActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(com.plunner.plunner.models.models.employee.Calendar calendar) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    startActivity(new Intent(ScheduleActivity.this, DashboardActivity.class));
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Checks the kind of non http error occured(check if it is caused by the absence of network)
     */
    private void checkError() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        String msg;
        if (!isConnected) {
            msg = "No network";
        } else {
            msg = "Communication error, please try again later";
        }
        createSnackBar(msg);
    }

    /**
     * Creates an alert snackbar with the given message(the created snackbar has a red background)
     *
     * @param message The message to be displayed in the created snackbar
     */
    private void createSnackBar(String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(R.id.compose_schedule_root), message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snackbar.show();
    }
}

