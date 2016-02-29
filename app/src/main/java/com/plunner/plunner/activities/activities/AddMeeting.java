package com.plunner.plunner.activities.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AddMeeting extends AppCompatActivity {
    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see events for a given day
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
     * Maps month buttons to {@link Calendar} instancies
     */
    private CalendarPickersViewSupport calendarPickersViewSupport;
    /**
     * A fragment used to create/edit an event in the {@link #mWeekView }
     */
    private EventDetailFragment addEventFragment;
    private List<CustomWeekEvent> composedEvents;
    private List<CustomWeekEvent> deletedEvents;
    private ProgressBar pBar;
    private String id;
    private String groupId;
    private Map<String, MeetingTimeslot> idTimeslots;
    private ProgressDialog progressDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        if(intent.getExtras() != null){

            id = intent.getExtras().getString("meeting_id");
            groupId = intent.getExtras().getString("group_id");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_meeting_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        meetingTitle = (EditText) findViewById(R.id.add_meeting_title);
        meetingDesc = (EditText) findViewById(R.id.add_meeting_desc);
        meetingDuration = (TextView) findViewById(R.id.add_meeting_duration);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        groupsSpinner = (Spinner) findViewById(R.id.add_meeting_spinner);


        seekBar = (SeekBar) findViewById(R.id.duration_seek);
        seekBar.setMax(300);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                meetingDuration.setText(Integer.toString(progress+15));
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
        calendarPickersViewSupport.setDaysPicker((LinearLayout) findViewById(R.id.add_meeting_days_picker));
        calendarPickersViewSupport.setMonthsPicker((LinearLayout) findViewById(R.id.add_meeting_months_picker));
        mWeekView = (WeekView) findViewById(R.id.add_meeting_weekView);
        //Pickers init
        calendarPickersViewSupport.createViews(-1);
        //createCalendarPickersView(-1);
        //Event view init
        setWeekView();
        mWeekView.goToDate(Calendar.getInstance());
        //enabled switch
        composedEvents = new ArrayList<>();
        deletedEvents = new ArrayList<>();
        idTimeslots = new HashMap<>();
        retrieveGroups();
    }



    private void retrieveGroups() {
        Planner planner = (Planner) ComManager.getInstance().getUser();
        planner.getGroupsManaged().load(new ManagedGroupsCallback(id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.meeting_detail_save) {
            //createLoadingDialog();
            sendData();
            return true;
        }
        else if(id == R.id.meeting_detail_delete){
            createDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        if(id == null && groupId == null){
            menu.removeItem(R.id.meeting_detail_delete);
            menu.removeItem(R.id.meeting_detail_edit);
        }
        return true;
    }

    private void sendData() {
        int adj_duration;
        if (validateData()) {
            //pBar.setVisibility(View.VISIBLE);
            String msg;
            if(id == null && groupId == null){
                msg = "Adding meeting";
            }
            else{
                msg = "Saving changes";
            }
            progressDialog = ProgressDialog.show(this, "",
                    msg, true);

            adj_duration = Integer.parseInt(meetingDuration.getText().toString());
            adj_duration = adj_duration*60;
            Meeting meeting;
            String groupId;
            if(id != null && this.groupId != null){
                meeting = selectedMeeting;
                groupId = this.groupId;
            }
            else{
                meeting = new Meeting();
                groupId = selectedGroup.getId();
            }
            meeting.setTitle(meetingTitle.getText().toString());
            meeting.setDescription(meetingDesc.getText().toString());
            meeting.setDuration(Integer.toString(adj_duration));
            meeting.setFatherParameters(groupId);
            meeting.save(new MeetingSaveCallback());
        }
    }

    private boolean rulesValidator(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if(today.after(calendar)){
            return false;
        }
        else if(today.get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH) ){
            return false;
        }
        else if(today.get(Calendar.WEEK_OF_MONTH) + 1 == calendar.get(Calendar.WEEK_OF_MONTH)){
            if(today.get(Calendar.DAY_OF_WEEK) == 1 && calendar.get(Calendar.DAY_OF_WEEK)!=1){
                return false;
            }
        }
        return true;
    }

    private boolean validateData() {
        boolean toReturn = true;
        TextInputLayout tt = (TextInputLayout) findViewById(R.id.titleInput);
        if (meetingTitle.getText().toString().equals("")) {
            tt.setErrorEnabled(true);
            tt.setError("Please insert a title for the meeting");
            toReturn = false;
        } else if (composedEvents.size() == 0) {
            tt.setErrorEnabled(false);
            toReturn = false;
            createSnackBar("Insert at least one timeslot for the meeting");
        }
        else{
            tt.setErrorEnabled(false);
            tt.setError(null);
        }

        return toReturn;

    }

    private void createSnackBar(String message){
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(R.id.add_meeting_root),message,Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,R.color.red));
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
                int a = 3;
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
            addEventFragment.setInitialDate(event.getStartTime());
            addEventFragment.setMode(false);
            //addEventFragment.setEditEventContent(event.getStartTime(), event.getEndTime());
            actionBar.hide();
            transaction.add(R.id.add_meeting_root, addEventFragment)
                    .addToBackStack(null).commit();
        } else {
            actionBar.hide();
            addEventFragment.setCurrentEventId(event.getId());
            addEventFragment.setMode(false);
            addEventFragment.setEditEventContent(event.getStartTime(), event.getEndTime());
            transaction.show(addEventFragment).commit();

        }
    }

    private void onEmptySpaceClick(Calendar time) {
        if(rulesValidator(time)){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            actionBar.hide();

            if (addEventFragment == null) {
                addEventFragment = new EventDetailFragment();
                addEventFragment.setInitialDate(time);
                addEventFragment.setMode(true);
                transaction.add(R.id.add_meeting_root, addEventFragment)
                        .addToBackStack(null).commit();

            } else {
                addEventFragment.setNewEventContent(time,true);
                transaction.show(addEventFragment).commit();
            }
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
        Map<String,String> adaptedEvent;
        List<CustomWeekEvent> newEvents = new ArrayList<>();
        CustomWeekEvent currentEvent;
        for (int i=0; i<composedEvents.size(); i++){
            currentEvent = composedEvents.get(i);
            if(currentEvent.isNew()){
                newEvents.add(currentEvent);
            }
        }
        for(int i=0; i<newEvents.size(); i++){
            timeslot = new MeetingTimeslot();
            adaptedEvent = eventFormatAdapter(newEvents.get(i));
            timeslot.setTimeStart(adaptedEvent.get("startTime"));
            timeslot.setTimeEnd(adaptedEvent.get("endTime"));
            timeslot.setFatherParameters(selectedGroup.getId(), meetingId);
            timeslot.save(new saveTimeslotsCallback(i+1, composedEvents.size()));
        }
    }
    private Map<String, String> eventFormatAdapter(CustomWeekEvent event){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,String> map = new HashMap<>();
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
            AddMeeting.this.createSnackBar(e.getErrorBody());
            Log.w("1", e.getErrorBody());
        }

        @Override
        public void onNext(MeetingTimeslot meeting) {
            if(index == total){
                if(id!=null){
                    sendUpdatedEvents();
                }
                else{
                    progressDialog.dismiss();
                    Intent intent = new Intent(AddMeeting.this, DashboardActivity.class);
                    startActivity(intent);
                }

            }

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class ManagedGroupsCallback implements CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {

        String id;

        public ManagedGroupsCallback(String id) {
            this.id = id;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Group> groups) {
            Group currentGroup;
            currentGroups = groups.getModels();
            selectedGroup = currentGroups.get(0);
            List<String> stringedGroups = new ArrayList<>();
            for (int i=0; i<currentGroups.size();i++){
                currentGroup = currentGroups.get(i);
                if(groupId != null & currentGroup.getId().equals(groupId)){
                    currentGroup.getMeetingsManaged().load(new MeetingGetCallback());
                }
                stringedGroups.add(currentGroups.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMeeting.this, android.R.layout.simple_spinner_dropdown_item, stringedGroups);
            groupsSpinner.setAdapter(adapter);
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

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class MeetingSaveCallback implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting>{
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(com.plunner.plunner.models.models.employee.planner.Meeting meeting) {

            if(id == null && groupId == null){
                sendEvents(meeting.getId());
            }
            else{
                sendUpdatedEvents();
            }


        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void sendUpdatedEvents() {
        List<CustomWeekEvent> updatedEvents = new ArrayList<>();
        CustomWeekEvent currentEvent;
        for(int i=0; i<composedEvents.size(); i++){
            currentEvent = composedEvents.get(i);
            if(currentEvent.isEdited() && !currentEvent.isNew()){
                composedEvents.remove(i);
                updatedEvents.add(currentEvent);
            }
        }
        for(int i=0; i<updatedEvents.size(); i++){
            currentEvent = updatedEvents.get(i);
            idTimeslots.get(currentEvent.getId()).save(new UpdateTimeslotCallback(i, updatedEvents.size()));
        }

    }

    private void sendDeletedEvents() {
        if(id != null && groupId != null){
            for(int i=0; i<deletedEvents.size(); i++){
                idTimeslots.get(deletedEvents.get(i).getId()).delete(new DeleteTimeslotCallback(i,deletedEvents.size()));
            }
        }
    }

    private class MeetingGetCallback implements CallOnHttpError<ModelList<Meeting>>, CallOnNext<ModelList<Meeting>>, CallOnNoHttpError<ModelList<Meeting>>{
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Meeting> meetingsList) {
            List<Meeting> meetings = meetingsList.getModels();
            Meeting currentMeeting;
            for(int i=0; i<meetings.size(); i++){
                currentMeeting = meetings.get(i);
                if(currentMeeting.getId().equals(id)){
                    selectedMeeting = currentMeeting;
                    fillData();
                    break;
                }
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void fillData() {
        meetingTitle.setText(selectedMeeting.getTitle());
        meetingDesc.setText(selectedMeeting.getDescription());
        meetingDuration.setText(selectedMeeting.getDuration());
        seekBar.setProgress(Integer.parseInt(selectedMeeting.getDuration())/60);
        getTimeslots();
    }

    private void getTimeslots(){
        selectedMeeting.getMeetingsTimeslotManaged().load(new TimeslotsGetCallback());
    }

    private class TimeslotsGetCallback implements CallOnHttpError<ModelList<MeetingTimeslot>>, CallOnNext<ModelList<MeetingTimeslot>>, CallOnNoHttpError<ModelList<MeetingTimeslot>> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<MeetingTimeslot> meetingModelList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            List<MeetingTimeslot> timeslots = meetingModelList.getModels();
            MeetingTimeslot meetingTimeslot;
            Calendar calendar_one = Calendar.getInstance();
            Calendar calendar_two = (Calendar) calendar_one.clone();
            Date parsedOne, parsedTwo;
            for(int i=0; i<timeslots.size(); i++){
                meetingTimeslot = timeslots.get(i);
                idTimeslots.put(meetingTimeslot.getId(),meetingTimeslot);
                try {
                    parsedOne = sdf.parse(meetingTimeslot.getTimeStart());
                    parsedTwo = sdf.parse(meetingTimeslot.getTimeEnd());
                    calendar_one.setTime(parsedOne);
                    calendar_two.setTime(parsedTwo);
                    composedEvents.add(new CustomWeekEvent(Integer.parseInt(meetingTimeslot.getId()),"", calendar_one,calendar_two ,false,false));
                    mWeekView.notifyDatasetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class DeleteMeetingCallback  implements CallOnHttpError<Meeting>, CallOnNext<Meeting>, CallOnNoHttpError<Meeting>{
        @Override
        public void onHttpError(HttpException e) {
            createSnackBar(e.getErrorBody());
        }

        @Override
        public void onNext(Meeting meeting) {
            Intent intent = new Intent(AddMeeting.this, DashboardActivity.class);
            startActivity(intent);
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void createDialog(){
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
                selectedMeeting.delete(new DeleteMeetingCallback());
            }
        });
        alertDialogB.show();
    }
    private void createLoadingDialog(){
        ProgressDialog progress = ProgressDialog.show(this, "",
                "Saving changes", true);
    }

    private class DeleteTimeslotCallback implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        private int index;
        private int total;

        public DeleteTimeslotCallback(int index, int total) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(MeetingTimeslot timeslot) {
            if(index == total){
                sendEvents(id);
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private class UpdateTimeslotCallback implements CallOnHttpError<MeetingTimeslot>, CallOnNext<MeetingTimeslot>, CallOnNoHttpError<MeetingTimeslot> {
        int index, tot;

        public UpdateTimeslotCallback(int index, int tot) {
            this.index = index;
            this.tot = tot;
        }

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(MeetingTimeslot timeslot) {
            if(index == tot){
                sendDeletedEvents();
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
}