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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.plunner.plunner.models.models.employee.planner.Group;
import com.plunner.plunner.models.models.employee.planner.Meeting;
import com.plunner.plunner.models.models.employee.planner.MeetingTimeslot;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.utils.CalendarPickersViewSupport;
import com.plunner.plunner.utils.DataExchanger;
import com.plunner.plunner.utils.CustomWeekEvent;
import com.plunner.plunner.utils.TimeslotBackEndAdapter;
import com.plunner.plunner.utils.TimeslotFrontEndAdapter;
import com.plunner.plunner.utils.MeetingTimeslotValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An activity that provides the logic and the view for edit or create a meeting
 *
 * @author Giorgio Pea
 */
public class MeetingActivity extends AppCompatActivity {
    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see timeslots for a given day
     */
    private WeekView mWeekView;
    private ActionBar actionBar;
    private EditText meetingTitle;
    private EditText meetingDesc;
    private TextView meetingDuration;
    private Spinner groupsSpinner;
    private SeekBar seekBar;
    private List<Group> currentGroups;
    private Group selectedGroup;
    private Meeting selectedMeeting;
    /**
     * @see CalendarPickersViewSupport
     */
    private CalendarPickersViewSupport calendarPickersViewSupport;
    /**
     * @see TimeslotDetailFragment
     */
    private TimeslotDetailFragment timeslotDetail;
    private List<CustomWeekEvent> composedTimeslots;
    private List<CustomWeekEvent> deletedTimeslots;
    /**
     * Maps timeslots id with timeslots objects
     */
    private Map<String, MeetingTimeslot> idTimeslots;
    private ProgressDialog progressDialog;
    private boolean editMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        //Checks if i'm editing a meeting or not
        Intent intent = getIntent();
        editMode = intent.getExtras() != null;
        //Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_meeting_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //View elements catch
        meetingTitle = (EditText) findViewById(R.id.activity_meeting_title);
        meetingDesc = (EditText) findViewById(R.id.activity_meeting_desc);
        meetingDuration = (TextView) findViewById(R.id.activity_meeting_duration);
        groupsSpinner = (Spinner) findViewById(R.id.add_meeting_spinner);
        //Seekbar setup
        seekBar = (SeekBar) findViewById(R.id.activity_meeting_duration_seek);
        seekBar.setMax(300);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Display the progress of the seekbar in a textview
                meetingDuration.setText(Integer.toString(progress + 15));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
        //Timeslot day widget and calendar picker setup start
        calendarPickersViewSupport = CalendarPickersViewSupport.getInstance();
        calendarPickersViewSupport.setActivity(this);
        calendarPickersViewSupport.setDaysPicker((LinearLayout) findViewById(R.id.activity_meeting_days_picker));
        calendarPickersViewSupport.setMonthsPicker((LinearLayout) findViewById(R.id.activity_meeting_months_picker));
        mWeekView = (WeekView) findViewById(R.id.activity_meeting_mweekview);
        //Pickers init
        calendarPickersViewSupport.createViews(-1);
        //createCalendarPickersView(-1);
        //Event view init
        setWeekView();
        mWeekView.goToDate(Calendar.getInstance());
        //Composed t
        composedTimeslots = new ArrayList<>();
        deletedTimeslots = new ArrayList<>();
        idTimeslots = new HashMap<>();
        //If i'm editing a meeting i want to grab that meeting information
        if (editMode) {
            setTitle(getResources().getText(R.string.edit_meeting));
            selectedMeeting = (Meeting) DataExchanger.getInstance().getMeeting();
            retrieveMeetingInformation();
        }
        //In any case i need the groups managed by the planner
        retrieveGroups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_add_meeting_delete) {
            //createLoadingDialog();
            deleteMeeting();
            return true;
        } else if (id == R.id.menu_add_meeting_send) {
            saveMeeting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!editMode) {
            menu.removeItem(R.id.menu_add_meeting_delete);
        }
        return true;
    }

    /**
     * Retrieves the groups managed by the planner
     */
    private void retrieveGroups() {
        Planner planner = (Planner) DataExchanger.getInstance().getUser();
        //If the groups are already loaded i skip the loading
        if (planner.getGroupsManaged().getInstance().getModels().size() == 0) {
            planner.getGroupsManaged().load(new ManagedGroupsCallback());
        } else {
            insertGroups(planner.getGroupsManaged().getInstance());
        }

    }

    /**
     * Deletes the meeting the planner is editing
     */
    private void deleteMeeting() {
        //Asks user confirmation
        AlertDialog.Builder alertDialogB = new AlertDialog.Builder(this);
        alertDialogB.setTitle("Delete Meeting");
        alertDialogB.setMessage("Are you sure you want to delete this meeting?");
        alertDialogB.setNegativeButton("ABORT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = ProgressDialog.show(MeetingActivity.this, "", "Deleting meeting", true);
                //Sends delete request to backend
                selectedMeeting.delete(new DeleteMeetingCallback());
            }
        });
        alertDialogB.show();
    }

    /**
     * Save the changes to the meeting the planner is editing or creating
     */
    private void saveMeeting() {
        int adj_duration;
        Meeting meeting;
        //the id of the group this meeting is being planned for
        String groupId;
        if (validateData()) {
            String msg;
            if (!editMode) {
                msg = "Adding meeting";
                groupId = selectedGroup.getId();
                meeting = new Meeting();
            } else {
                msg = "Saving changes";
                groupId = selectedMeeting.getGroupId();
                meeting = selectedMeeting;
            }
            progressDialog = ProgressDialog.show(this, "", msg, true);
            //The backend needs the duration of a meeting in minutes and as integer
            adj_duration = Integer.parseInt(meetingDuration.getText().toString());
            adj_duration = adj_duration * 60;
            //Properties setting
            meeting.setTitle(meetingTitle.getText().toString());
            meeting.setDescription(meetingDesc.getText().toString());
            meeting.setDuration(Integer.toString(adj_duration));
            meeting.setFatherParameters(groupId);
            //Save request to back end
            meeting.save(new saveMeetingCallback());
        }
    }

    /**
     * Validates the inserted properties for the meeting the planner is editing or creating
     *
     * @return true if the inserted properties are valid, otherwise false
     */
    private boolean validateData() {
        boolean toReturn = true;
        //Error message for the meeting
        TextView errorMsg = (TextView) findViewById(R.id.activity_meeting_title_err);

        if (meetingTitle.getText().toString().equals("")) {
            errorMsg.setVisibility(View.VISIBLE);
            toReturn = false;
        } else if (composedTimeslots.size() == 0) {
            //Shows alert snackbar
            createSnackBar("Insert at least one timeslot for the meeting");
            errorMsg.setVisibility(View.GONE);
            toReturn = false;
        } else {
            errorMsg.setVisibility(View.GONE);
        }
        return toReturn;

    }

    /**
     * Creates an alert snackbar with the given message(the created snackbar has a red background)
     *
     * @param message The message to be displayed in the created snackbar
     */
    private void createSnackBar(String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(R.id.add_meeting_root), message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snackbar.show();
    }

    /**
     * Sets some action listeners for the TimeslotsDay widget
     *
     * @see WeekView
     */
    private void setWeekView() {
        //Listener for clicking on a timeslot
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
        //Listener for loading timeslots
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return composedTimeslots;
            }

        });
        //Listener for clicking on a empty area
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
        //Listener for horizontal scroll
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
    //Actions performed in response to different view event of mWeekView

    /**
     * Response to click on a timeslot in the {@link #mWeekView}
     *
     * @param timeslot The clicked timeslot
     */
    private void onTimeslotClick(WeekViewEvent timeslot) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (timeslotDetail == null) {
            timeslotDetail = new TimeslotDetailFragment();
            timeslotDetail.setCurrentEventId(timeslot.getId());
            timeslotDetail.setInitialDate(timeslot.getStartTime());
            timeslotDetail.setMode(false);
            //timeslotDetail.setEditEventContent(timeslot.getStartTime(), timeslot.getEndTime());
            actionBar.hide();
            transaction.add(R.id.add_meeting_root, timeslotDetail)
                    .addToBackStack(null).commit();
        } else {
            actionBar.hide();
            timeslotDetail.setCurrentEventId(timeslot.getId());
            timeslotDetail.setMode(false);
            timeslotDetail.setEditEventContent(timeslot.getStartTime(), timeslot.getEndTime());
            transaction.show(timeslotDetail).commit();

        }
    }

    /**
     * Response to click on a empty area in the {@link #mWeekView}
     *
     * @param time The date that corresponds to that area
     */
    private void onEmptyAreaClick(Calendar time) {
        if (MeetingTimeslotValidator.getInstance().validate(time, null)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            actionBar.hide();

            if (timeslotDetail == null) {
                timeslotDetail = new TimeslotDetailFragment();
                timeslotDetail.setInitialDate(time);
                timeslotDetail.setMode(true);
                transaction.add(R.id.add_meeting_root, timeslotDetail)
                        .addToBackStack(null).commit();

            } else {
                timeslotDetail.setNewEventContent(time, true);
                transaction.show(timeslotDetail).commit();
            }
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
        Calendar associatedDate = calendarPickersViewSupport.changeDay(v);
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Invoked when the user clicks on a month button of the CalendarPicker widget
     *
     * @param v The day button pressed
     */
    public void changeMonth(View v) {
        Calendar associatedDate = calendarPickersViewSupport.changeMonth(v);
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Invoked when the user dismisses the {@link TimeslotDetailFragment}
     *
     * @param v The button clicked to dismiss the {@link TimeslotDetailFragment}
     */
    public void hideFragment(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (timeslotDetail != null) {
            fragmentTransaction.hide(timeslotDetail).commit();
            actionBar.show();
        }

    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#showDatePicker(int)
     */
    public void showDatePicker(View v) {
        timeslotDetail.showDatePicker((Integer) v.getTag());
    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#showTimePicker(int)
     */
    public void showTimePicker(View v) {
        timeslotDetail.showTimePicker((Integer) v.getTag());
    }

    /**
     * @param v The view that triggered this action
     * @see TimeslotDetailFragment#save()
     */
    public void saveTimeslot(View v) {
        CustomWeekEvent timeslot = timeslotDetail.save();
        int position;
        if (timeslot != null) {
            hideFragment(null);
            if (timeslot.isNew() && !timeslot.isEdited()) {
                composedTimeslots.add(timeslot);
            } else if (timeslot.isEdited()) {
                //Removes the old timeslot
                position = findTimeslotById(timeslot.getId());
                composedTimeslots.remove(position);
                //Replaces it with the new one
                composedTimeslots.add(timeslot);
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
        long eventId = timeslotDetail.getCurrentEventId();
        int position = findTimeslotById(eventId);
        deletedTimeslots.add(composedTimeslots.get(position));
        composedTimeslots.remove(position);
        mWeekView.notifyDatasetChanged();
        hideFragment(null);

    }


    /**
     * Saves the timeslots associated to the edited or created meeting
     *
     * @param meetingId The id of the edited/created meeting
     */
    private void saveTimeslots(String meetingId) {
        MeetingTimeslot timeslot;
        Map<String, String> adaptedTimeslot;
        List<CustomWeekEvent> newTimeslots = new ArrayList<>();
        CustomWeekEvent currentTimeslot;
        //Gets only new timeslots
        for (int i = 0; i < composedTimeslots.size(); i++) {
            currentTimeslot = composedTimeslots.get(i);
            if (currentTimeslot.isNew()) {
                newTimeslots.add(currentTimeslot);
            }
        }
        //Dismisses loading dialog
        if (newTimeslots.size() == 0) {
            saveUpdatedTimeslots();
        }
        for (int i = 0; i < newTimeslots.size(); i++) {
            timeslot = new MeetingTimeslot();
            //Backend adaptation
            adaptedTimeslot = TimeslotBackEndAdapter.getInstance().adapt(newTimeslots.get(i));
            timeslot.setTimeStart(adaptedTimeslot.get("startTime"));
            timeslot.setTimeEnd(adaptedTimeslot.get("endTime"));
            timeslot.setFatherParameters(selectedGroup.getId(), meetingId);
            //Performs save request to bacjend
            timeslot.save(new SaveTimeslotsCallback(i + 1, newTimeslots.size()));
        }
    }

    /**
     * Callback class for the response to {@link #saveTimeslots(String)}
     */
    private class SaveTimeslotsCallback implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        private int index;
        private int total;

        public SaveTimeslotsCallback(int index, int total) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(MeetingTimeslot meeting) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == total) {
                        if (editMode) {
                            saveUpdatedTimeslots();
                        } else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(MeetingActivity.this, DashboardActivity.class);
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
     * Callback class for the response to {@link #retrieveGroups()}
     */
    private class ManagedGroupsCallback implements CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<Group> groups) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertGroups(groups);
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Callback class for the response to {@link #saveMeeting()}
     */
    private class saveMeetingCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final com.plunner.plunner.models.models.employee.planner.Meeting meeting) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    saveTimeslots(meeting.getId());
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Saves the timeslots edited by the user (not the new ones)
     */
    private void saveUpdatedTimeslots() {
        List<CustomWeekEvent> updatedTimeslots = new ArrayList<>();
        CustomWeekEvent currentEvent;
        //Gets edited timeslots
        for (int i = 0; i < composedTimeslots.size(); i++) {
            currentEvent = composedTimeslots.get(i);
            if (currentEvent.isEdited() && !currentEvent.isNew()) {
                updatedTimeslots.add(currentEvent);
            }
        }
        if (updatedTimeslots.size() == 0) {
            saveDeletedTimeslots();
        }
        for (int i = 0; i < updatedTimeslots.size(); i++) {
            currentEvent = updatedTimeslots.get(i);
            //Save request to backend
            idTimeslots.get(Integer.toString((int) currentEvent.getId())).save(new saveUpdatedTimeslotsCallback(i + 1, updatedTimeslots.size()));
        }

    }

    /**
     * Save the deletion of timeslots by the user
     */
    private void saveDeletedTimeslots() {
        if (deletedTimeslots.size() == 0) {
            progressDialog.dismiss();
            startActivity(new Intent(MeetingActivity.this, DashboardActivity.class));
        }
        for (int i = 0; i < deletedTimeslots.size(); i++) {
            //Delete request to backend
            idTimeslots.get(Integer.toString((int) deletedTimeslots.get(i).getId())).delete(new saveDeletedTimeslots(i + 1, deletedTimeslots.size()));
        }
    }

    /**
     * Inserts the timeslots in the {@link #mWeekView}
     *
     * @param meetingModelList The list of {@link MeetingTimeslot} retrieved that must be inserted
     */
    private void insertTimeslots(ModelList<MeetingTimeslot> meetingModelList) {
        List<MeetingTimeslot> timeslots = meetingModelList.getModels();
        MeetingTimeslot meetingTimeslot;
        Map<String, Calendar> adaptedTimeslot;
        for (int i = 0; i < timeslots.size(); i++) {
            meetingTimeslot = timeslots.get(i);
            try {
                adaptedTimeslot = TimeslotFrontEndAdapter.getInstance().adapt(meetingTimeslot.getTimeStart(), meetingTimeslot.getTimeEnd());
                idTimeslots.put(meetingTimeslot.getId(), meetingTimeslot);
                composedTimeslots.add(new CustomWeekEvent(Integer.parseInt(meetingTimeslot.getId()), "", adaptedTimeslot.get("startTime"), adaptedTimeslot.get("endTime"), false, false));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mWeekView.notifyDatasetChanged();
    }

    /**
     * Inserts the groups managed by the user in the {@link #groupsSpinner}
     *
     * @param groups The list of {@link Group} retrieved that must be inserted
     */
    private void insertGroups(ModelList<Group> groups) {
        Group currentGroup;
        int position = 0;
        currentGroups = groups.getModels();
        selectedGroup = currentGroups.get(0);
        List<String> stringedGroups = new ArrayList<>();
        for (int i = 0; i < currentGroups.size(); i++) {
            currentGroup = currentGroups.get(i);
            if (editMode) {
                if (currentGroup.getId().equals(selectedMeeting.getGroupId())) {
                    position = i;
                }
            }
            stringedGroups.add(currentGroups.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MeetingActivity.this, android.R.layout.simple_spinner_dropdown_item, stringedGroups);
        groupsSpinner.setAdapter(adapter);
        groupsSpinner.setSelection(position);
        groupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = currentGroups.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Retrieves the information of the meeting the user is editing
     */
    private void retrieveMeetingInformation() {
        meetingTitle.setText(selectedMeeting.getTitle());
        meetingDesc.setText(selectedMeeting.getDescription());
        int duration = Integer.parseInt(selectedMeeting.getDuration());
        meetingDuration.setText(Integer.toString(duration / 60));
        seekBar.setProgress(duration / 60 - 15);
        retrieveMeetingTimeslots();
    }

    /**
     * Retrieves the timeslots associated with the meeting the user is editing
     */
    private void retrieveMeetingTimeslots() {
        if (selectedMeeting.getMeetingsTimeslotManaged().getInstance().getModels().size() == 0) {
            selectedMeeting.getMeetingsTimeslotManaged().load(new retrieveMeetingTimeslotsCallback());
        } else {
            insertTimeslots(selectedMeeting.getMeetingsTimeslotManaged().getInstance());
        }

    }

    /**
     * Callback class for the response to {@link #retrieveMeetingTimeslots()} ()}
     */
    private class retrieveMeetingTimeslotsCallback implements CallOnHttpError<ModelList<MeetingTimeslot>>, CallOnNext<ModelList<MeetingTimeslot>>, CallOnNoHttpError<ModelList<MeetingTimeslot>> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<MeetingTimeslot> meetingModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertTimeslots(meetingModelList);
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Callback class for the response to {@link #deleteMeeting()} ()}
     */
    private class DeleteMeetingCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(Meeting meeting) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(MeetingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    /**
     * Callback class for the response to {@link #saveDeletedTimeslots()} ()}
     */
    private class saveDeletedTimeslots implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        private int index;
        private int total;

        public saveDeletedTimeslots(int index, int total) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(MeetingTimeslot timeslot) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (index == total) {
                        progressDialog.dismiss();
                        startActivity(new Intent(MeetingActivity.this, DashboardActivity.class));
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
     * Callback class for the response to {@link #saveUpdatedTimeslots()} ()}
     */
    private class saveUpdatedTimeslotsCallback implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        int index, tot;

        public saveUpdatedTimeslotsCallback(int index, int tot) {
            this.index = index;
            this.tot = tot;
        }

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, MeetingActivity.this, null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingActivity.this.createSnackBar(msg);
        }

        @Override
        public void onNext(MeetingTimeslot timeslot) {
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
}
