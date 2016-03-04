package com.plunner.plunner.activities.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.plunner.plunner.R;
import com.plunner.plunner.utils.CustomWeekEvent;
import com.plunner.plunner.utils.idFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class TimeslotDetailFragment extends Fragment {

    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
    private TextView endTime;
    private TextView fragmentTitle;
    private Map<TextView, Calendar> textViewCalendarMap;
    private RelativeLayout deleteView;
    private boolean isNewEvent;
    private boolean isEditedEvent;
    private long currentEventId;
    private Calendar initialDate;
    private boolean mode;


    public TimeslotDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }


    public void showDatePicker(int tag) {
        DatePickerDialog.OnDateSetListener dateListenerOne, dateListenerTwo;
        Calendar calendar_start_date, calendar_end_date;
        if (textViewCalendarMap.get(startDate) == null) {
            calendar_start_date = Calendar.getInstance();
        } else {
            calendar_start_date = textViewCalendarMap.get(startDate);
        }
        if (textViewCalendarMap.get(endDate) == null) {
            calendar_end_date = Calendar.getInstance();
        } else {
            calendar_end_date = textViewCalendarMap.get(endDate);
        }
        dateListenerOne = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                onDateSetM(year, monthOfYear, dayOfMonth, true);

            }

        };
        dateListenerTwo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                onDateSetM(year, monthOfYear, dayOfMonth, false);
            }
        };
        if (tag == 1) {

            DatePickerDialog datePickerStarts = new DatePickerDialog(getContext(), dateListenerOne, calendar_start_date
                    .get(Calendar.YEAR), calendar_start_date.get(Calendar.MONTH),
                    calendar_start_date.get(Calendar.DAY_OF_MONTH));
            datePickerStarts.getDatePicker().setMinDate(minDate().getTimeInMillis());
            datePickerStarts.show();
        } else if (tag == 2) {

            DatePickerDialog datePickerEnds = new DatePickerDialog(getContext(), dateListenerTwo, calendar_end_date
                    .get(Calendar.YEAR), calendar_end_date.get(Calendar.MONTH),
                    calendar_end_date.get(Calendar.DAY_OF_MONTH));
            datePickerEnds.getDatePicker().setMinDate(textViewCalendarMap.get(startDate).getTimeInMillis());
            datePickerEnds.setTitle("");
            datePickerEnds.show();
        }
    }

    public void showTimePicker(int tag) {
        TimePickerDialog.OnTimeSetListener timeListenerOne, timeListenerTwo;
        Calendar calendar_start_time, calendar_end_time;
        if (textViewCalendarMap.get(startDate) == null) {
            calendar_start_time = Calendar.getInstance();
        } else {
            calendar_start_time = textViewCalendarMap.get(startDate);
        }
        if (textViewCalendarMap.get(endDate) == null) {
            calendar_end_time = Calendar.getInstance();
        } else {
            calendar_end_time = textViewCalendarMap.get(endDate);
        }
        timeListenerOne = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                onTimeSetM(hour, minute, true);

            }

        };
        timeListenerTwo = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                onTimeSetM(hour, minute, false);

            }

        };
        if (tag == 1) {

            TimePickerDialog timePickerStarts = new TimePickerDialog(getContext(), timeListenerOne, calendar_start_time
                    .get(Calendar.HOUR_OF_DAY), calendar_start_time.get(Calendar.MINUTE), true);
            timePickerStarts.show();
        } else if (tag == 2) {

            TimePickerDialog timePickerEnds = new TimePickerDialog(getContext(), timeListenerTwo, calendar_end_time
                    .get(Calendar.HOUR_OF_DAY), calendar_end_time.get(Calendar.MINUTE), true);

            timePickerEnds.show();
        }
    }

    public void eventStarts(Calendar calendar, int switchValue) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
        textViewCalendarMap.put(startDate, calendar);
        if (switchValue == 0 || switchValue == 2) {
            startDate.setText(formatter.format(calendar.getTime()));
        }
        if (switchValue == 1 || switchValue == 2) {
            formatter.applyPattern("kk:mm");
            startTime.setText(formatter.format(calendar.getTime()));
        }

    }

    public void eventEnds(Calendar calendar, int switchValue) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
        textViewCalendarMap.put(endDate, calendar);
        if (switchValue == 0 || switchValue == 2) {
            endDate.setText(formatter.format(calendar.getTime()));
        }
        if (switchValue == 1 || switchValue == 2) {
            formatter.applyPattern("kk:mm");
            endTime.setText(formatter.format(calendar.getTime()));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        textViewCalendarMap = new HashMap<>();
        Activity activity =  getActivity();
        fragmentTitle = (TextView) activity.findViewById(R.id.add_event_title);
        deleteView = (RelativeLayout) activity.findViewById(R.id.add_event_delete);
        startDate = (TextView) activity.findViewById(R.id.add_event_starts_date);
        startDate.setTag(1);
        startTime = (TextView) activity.findViewById(R.id.add_event_starts_time);
        startTime.setTag(1);
        endDate = (TextView) activity.findViewById(R.id.add_event_ends_date);
        endDate.setTag(2);
        endTime = (TextView) activity.findViewById(R.id.add_event_ends_time);
        endTime.setTag(2);
        if (mode) {
            fragmentTitle.setText(getResources().getText(R.string.add_event));
            deleteView.setVisibility(View.GONE);
        }
        if (!mode || isEditedEvent) {
            fragmentTitle.setText(getResources().getText(R.string.edit_event));
            deleteView.setVisibility(View.VISIBLE);

        }
        setNewEventContent(initialDate,mode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (isNewEvent) {
                fragmentTitle.setText(getResources().getText(R.string.add_event));
                deleteView.setVisibility(View.GONE);
            }
            if (isEditedEvent) {

                fragmentTitle.setText(getResources().getText(R.string.edit_event));
                deleteView.setVisibility(View.VISIBLE);
            }
        }

    }

    public CustomWeekEvent save() {
        Snackbar snackbar;
        Calendar evStartDate = textViewCalendarMap.get(startDate);
        Calendar evEndDate = textViewCalendarMap.get(endDate);
        int startDay = evStartDate.get(Calendar.DAY_OF_MONTH);
        int endDay = evEndDate.get(Calendar.DAY_OF_MONTH);
        int startHour = evStartDate.get(Calendar.HOUR_OF_DAY);
        int startMinutes = evStartDate.get(Calendar.MINUTE);
        int endHour = evEndDate.get(Calendar.HOUR_OF_DAY);
        int endMinutes = evEndDate.get(Calendar.MINUTE);

        if (startDay == endDay) {
            if (endHour < startHour || startHour == endHour && startMinutes > endMinutes) {
                snackbar = Snackbar.make(getActivity().findViewById(R.id.add_event_fragment), "You must insert a valid end time", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                snackbar.show();
                return null;
            }
        }

        return new CustomWeekEvent(currentEventId, "", evStartDate, evEndDate, isNewEvent, isEditedEvent);
    }


    public long getCurrentEventId() {
        return currentEventId;
    }

    public void setCurrentEventId(long currentEventId) {
        this.currentEventId = currentEventId;
    }
    public void setInitialDate(Calendar time){
        initialDate = time;
    }
    public void setNewEventContent(Calendar time,boolean mode) {
        Calendar calendar;
        if(mode == true){
            isNewEvent = true;
            isEditedEvent = false;
            currentEventId = idFactory.generate();
        }
        else{
            isEditedEvent = false;
        }
        eventStarts(time, 2);
        calendar = (Calendar) time.clone();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        eventEnds(calendar, 2);
    }
    public void setMode(boolean mode){
        this.mode = mode;
    }

    public void setEditEventContent(Calendar startTime, Calendar endTime) {
        isEditedEvent = true;
        eventStarts(startTime, 2);
        eventEnds(endTime, 2);
    }

    private void onTimeSetM(int hour, int minute, boolean forEventStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        if (forEventStart) {
            eventStarts(calendar, 1);
        } else {
            eventEnds(calendar, 1);
        }

    }

    private void onDateSetM(int year, int monthOfYear, int dayOfMonth, boolean forEventStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        if (forEventStart) {
            eventStarts(calendar, 0);
        } else {
            eventEnds(calendar, 0);
        }

    }
    private Calendar minDate(){
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar target = (Calendar) today.clone();
        if(today.get(Calendar.DAY_OF_WEEK) <= 7){
            target.set(Calendar.DAY_OF_WEEK,2);
            target.add(Calendar.WEEK_OF_MONTH,1);
        }
        else{
            target.set(Calendar.DAY_OF_WEEK,2);
            target.add(Calendar.WEEK_OF_MONTH,2);
        }
        return target;
    }
}
