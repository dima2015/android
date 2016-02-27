package com.plunner.plunner.activities.activities;

import android.graphics.RectF;
import android.os.Bundle;
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
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.planner.Group;
import com.plunner.plunner.models.models.employee.planner.Meeting;
import com.plunner.plunner.models.models.employee.planner.MeetingTimeslot;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.utils.CalendarPickersViewSupport;
import com.plunner.plunner.utils.ComManager;
import com.plunner.plunner.utils.CustomWeekEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddMeeting extends AppCompatActivity {
    private TextView tprogress;
    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see events for a given day
     */
    private WeekView mWeekView;
    private ActionBar actionBar;
    private EditText meetingTitle;
    private EditText meetingDesc;
    private TextView meetingDuration;
    private Spinner spinner;
    private List<Group> currentGroups;
    private Group selectedGroup;
    /**
     * Maps month buttons to {@link Calendar} instancies
     */
    private CalendarPickersViewSupport calendarPickersViewSupport;
    /**
     * A fragment used to create/edit an event in the {@link #mWeekView }
     */
    private EventDetailFragment addEventFragment;
    private List<CustomWeekEvent> composedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_meeting_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.add_meeting_spinner);
        retrieveGroups();
        tprogress = (TextView) findViewById(R.id.add_meeting_duration);
        SeekBar seekBar = (SeekBar) findViewById(R.id.duration_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeProgressIndicator(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calendarPickersViewSupport = CalendarPickersViewSupport.getInstance();
        calendarPickersViewSupport.setActivity(this);
        calendarPickersViewSupport.setDaysPicker((LinearLayout) findViewById(R.id.days_picker));
        calendarPickersViewSupport.setMonthsPicker((LinearLayout) findViewById(R.id.months_picker));
        mWeekView = (WeekView) findViewById(R.id.weekView);
        //Pickers init
        calendarPickersViewSupport.createViews(-1);
        //createCalendarPickersView(-1);
        //Event view init
        setWeekView();
        mWeekView.goToDate(Calendar.getInstance());
        //enabled switch
        composedEvents = new ArrayList<>();
        meetingTitle = (EditText) findViewById(R.id.add_meeting_title);
        meetingDesc = (EditText) findViewById(R.id.add_meeting_desc);
        meetingDuration = (TextView) findViewById(R.id.add_meeting_duration);


    }

    private void retrieveGroups() {
        Planner planner = (Planner) ComManager.getInstance().getUser();
        planner.getGroupsManaged().load(new ManagedGroupsCallback());

    }

    private void changeProgressIndicator(int progress) {
        tprogress.setText(Integer.toString(progress));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_add_meeting_send) {
            sendData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendData() {
        if (validateData()) {
            Meeting meeting = new Meeting();
            meeting.setTitle(meetingTitle.getText().toString());
            meeting.setDescription(meetingDesc.getText().toString());
            meeting.setDuration(meetingDuration.getText().toString());
            meeting.setFatherParameters(selectedGroup.getId());
            meeting.save(new MeetingSaveCallback());
        }
    }

    private boolean validateData() {
        boolean toReturn = true;
        if (meetingTitle.getText().equals("")) {
            meetingTitle.setError("Please insert a title for the meeting");
            toReturn = false;
        } else if (composedEvents.size() == 0) {
            toReturn = false;
        }
        return toReturn;

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
        Calendar associatedDate = calendarPickersViewSupport.changeMonth(v);
        mWeekView.goToDate(associatedDate);
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
        //deletedEvents.add(composedEvents.get(position));
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
            transaction.add(R.id.add_meeting_root, addEventFragment)
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
            transaction.add(R.id.add_meeting_root, addEventFragment)
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
    private class addMeetingCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting>{

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Meeting meeting) {
            sendEvents(meeting.getId());
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void sendEvents(String id) {
        MeetingTimeslot timeslot;
        Map<String,String> adaptedEvent;
        for(int i=0; i<composedEvents.size(); i++){
            timeslot = new MeetingTimeslot();
            adaptedEvent = eventFormatAdapter(composedEvents.get(i));
            timeslot.setTimeStart(adaptedEvent.get("startTime"));
            timeslot.setTimeEnd(adaptedEvent.get("endTime"));
            timeslot.setFatherParameters();
            //timeslot.save(new saveTimeslotsCallback(index, total));
        }
    }
    private Map<String, String> eventFormatAdapter(CustomWeekEvent event){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,String> map = new HashMap<>();
        map.put("startTime", sdf.format(event.getStartTime().getTime()));
        map.put("endTime", sdf.format(event.getEndTime().getTime()));
        return map;
    }

    private class saveTimeslotsCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting> {
        private int index;
        private int total;

        public saveTimeslotsCallback(int index) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(Meeting meeting) {
            if(index == total){
                //removeLoading();
            }

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class ManagedGroupsCallback implements CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Group> groups) {
            currentGroups = groups.getModels();
            selectedGroup = currentGroups.get(0);
            List<String> stringedGroups = new ArrayList<>();
            for (int i=0; i<currentGroups.size();i++){
                stringedGroups.add(currentGroups.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMeeting.this, android.R.layout.simple_spinner_dropdown_item, stringedGroups);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedGroup = currentGroups.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class MeetingSaveCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting>{
        @Override
        public void onHttpError(HttpException e) {

            Snackbar snackbar = Snackbar.make(findViewById(R.id.add_meeting_root), e.getErrorBody(), Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(AddMeeting.this, R.color.red));
            snackbar.show();
        }

        @Override
        public void onNext(com.plunner.plunner.models.models.employee.planner.Meeting meeting) {
            sendEvents(meeting.getId());
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
}
