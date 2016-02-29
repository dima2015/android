package com.plunner.plunner.activities.activities;

import android.content.Intent;
import android.graphics.RectF;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class MeetingDetailManagedActivity extends AppCompatActivity {

    private String meetingType;
    private EditText meetingTitle;
    private EditText meetingDesc;
    private EditText meetingGroup;
    private EditText meetingDuration;
    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see events for a given day
     */
    private WeekView mWeekView;
    private ActionBar actionBar;
    /*
     * Maps month buttons to {@link Calendar} instancies
     */
    private CalendarPickersViewSupport calendarPickersViewSupport;
    /**
     * A fragment used to create/edit an event in the {@link #mWeekView }
     */
    private EventDetailFragment addEventFragment;
    private List<CustomWeekEvent> composedEvents;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String data = intent.getExtras().getString("data");
        Log.i("1", data);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail_managed);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meeting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillData(data);
    }

    private void fillData(String data) {
        String[] tokenizedString = data.split(",");
        List<String> stringFields = new ArrayList<>();
        String[] subSplittedString;
        for (int i = 0; i < tokenizedString.length; i++) {
            subSplittedString = tokenizedString[i].split("=");
            stringFields.add(subSplittedString[1]);

        }
        ((EditText) findViewById(R.id.meeting_detail_title)).setText(stringFields.get(1));
        ((EditText) findViewById(R.id.meeting_detail_description)).setText(stringFields.get(2));
        ((TextView) findViewById(R.id.meeting_detail_group)).setText(stringFields.get(3));
        ((TextView) findViewById(R.id.meeting_detail_duration)).setText(stringFields.get(4));
        if (!stringFields.get(5).equals("null")) {
            ((TextView) findViewById(R.id.meeting_detail_starts)).setText(stringFields.get(4));
        }
        Log.i("2", stringFields.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                //handleFragment();
                return true;
            case R.id.meeting_detail_edit:
                //;
                return true;
            case R.id.meeting_detail_delete:
                //;
                return true;
            case R.id.meeting_detail_save:
                //;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void sendData() {
        int adj_duration;
        if (validateData()) {
            pBar.setVisibility(View.VISIBLE);
            adj_duration = Integer.parseInt(meetingDuration.getText().toString());
            adj_duration = adj_duration * 60;
            Meeting meeting = new Meeting();
            meeting.setTitle(meetingTitle.getText().toString());
            meeting.setDescription(meetingDesc.getText().toString());
            meeting.setDuration(Integer.toString(adj_duration));
            //meeting.setFatherParameters(selectedGroup.getId());
            //meeting.save(new MeetingSaveCallback());
        }
    }

    private boolean validateData() {
        boolean toReturn = true;
        /*//TextInputLayout tt = (TextInputLayout) findViewById(R.id.titleInput);
        if (meetingTitle.getText().toString().equals("")) {
            tt.setErrorEnabled(true);
            tt.setError("Please insert a title for the meeting");
            toReturn = false;
        } else if (composedEvents.size() == 0) {
            tt.setErrorEnabled(false);
            toReturn = false;
            createSnackBar("Insert at least one timeslot for the meeting");
        } else {
            tt.setErrorEnabled(false);
            tt.setError(null);
        }
            */
        return toReturn;

    }

    private void createSnackBar(String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(R.id.add_meeting_root), message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snackbar.show();
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
            //addEventFragment.setNewEventContent(time);
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

    private void sendEvents(String meetingId) {
        MeetingTimeslot timeslot;
        Map<String, String> adaptedEvent;
        for (int i = 0; i < composedEvents.size(); i++) {
            timeslot = new MeetingTimeslot();
            adaptedEvent = eventFormatAdapter(composedEvents.get(i));
            timeslot.setTimeStart(adaptedEvent.get("startTime"));
            timeslot.setTimeEnd(adaptedEvent.get("endTime"));
            //timeslot.setFatherParameters(selectedGroup.getId(), meetingId);
            timeslot.save(new saveTimeslotsCallback(i + 1, composedEvents.size()));
        }
    }

    private Map<String, String> eventFormatAdapter(CustomWeekEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("startTime", sdf.format(event.getStartTime().getTime()));
        map.put("endTime", sdf.format(event.getEndTime().getTime()));
        return map;
    }

    private class saveTimeslotsCallback implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        private int index;
        private int total;

        public saveTimeslotsCallback(int index, int total) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void onHttpError(HttpException e) {
            MeetingDetailManagedActivity.this.createSnackBar(e.getErrorBody());
            Log.w("1", e.getErrorBody());
        }

        @Override
        public void onNext(MeetingTimeslot meeting) {
            if (index == total) {
                pBar.setVisibility(View.GONE);
                MeetingDetailManagedActivity.this.createSnackBar("Finished adding timeslots");
            }

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }


}

