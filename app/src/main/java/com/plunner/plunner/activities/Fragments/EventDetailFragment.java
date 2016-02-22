package com.plunner.plunner.activities.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.ComposeScheduleActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerStarts;
    private DatePickerDialog datePickerEnds;
    private TimePickerDialog timePickerStarts;
    private TimePickerDialog timePickerEnds;
    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
    private TextView endTime;
    private Calendar initialDate;


    public EventDetailFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailFragment newInstance(String param1, String param2) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void setInitialDate(Calendar calendar){
        initialDate = calendar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }


    public void showDatePicker(int type){
        DatePickerDialog.OnDateSetListener dateListener;
        if(type == 1){
            if(datePickerStarts == null){
                dateListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        eventStarts(calendar, true);

                    }

                };
                Calendar calendar = Calendar.getInstance();
                datePickerStarts = new DatePickerDialog(getContext(), dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
            datePickerStarts.show();
        }
        else if(type==2){
            if(datePickerEnds == null){
                dateListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        eventEnds(calendar, true);

                    }

                };
                Calendar calendar = Calendar.getInstance();
                datePickerEnds = new DatePickerDialog(getContext(), dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
            datePickerEnds.show();
        }
    }
    public void showTimePicker(int type){
        TimePickerDialog.OnTimeSetListener timeListener;
        if(type == 1){
            if(timePickerStarts == null){
                timeListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        eventStarts(calendar, false);

                    }

                };
                Calendar calendar = Calendar.getInstance();
                timePickerStarts = new TimePickerDialog(getContext(), timeListener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            }
            timePickerStarts.show();
        }
        else if(type==2){
            if(timePickerEnds == null){
                timeListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        eventEnds(calendar, false);

                    }

                };
                Calendar calendar = Calendar.getInstance();
                timePickerEnds = new TimePickerDialog(getContext(), timeListener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            }
            timePickerEnds.show();
        }
    }


    public void eventStarts(Calendar calendar, boolean switchValue){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
        if(switchValue){
            startDate.setText(formatter.format(calendar.getTime()));
        }
        else{
            formatter.applyPattern("kk:mm");
            startTime.setText(formatter.format(calendar.getTime()));
        }

    }
    public void eventEnds(Calendar calendar, boolean switchValue){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
        if(switchValue){
            endDate.setText(formatter.format(calendar.getTime()));
        }
        else{
            formatter.applyPattern("kk:mm");
            endTime.setText(formatter.format(calendar.getTime()));
        }
    }



    @Override
    public void onStart(){
        super.onStart();
        ComposeScheduleActivity activity = (ComposeScheduleActivity) getActivity();
        startDate = (TextView) activity.findViewById(R.id.add_event_starts_date);
        startDate.setTag(1);
        startTime = (TextView) activity.findViewById(R.id.add_event_starts_time);
        startTime.setTag(1);
        endDate = (TextView) activity.findViewById(R.id.add_event_ends_date);
        endDate.setTag(2);
        endTime = (TextView) activity.findViewById(R.id.add_event_ends_time);
        endTime.setTag(2);
        eventStarts(initialDate, true);
        initialDate.set(Calendar.HOUR_OF_DAY, 10);
        eventStarts(initialDate, false);
        eventEnds(initialDate, true);
        initialDate.set(Calendar.HOUR_OF_DAY, 12);
        eventEnds(initialDate,false);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //do when hidden
        } else {
            eventStarts(initialDate, true);
            initialDate.set(Calendar.HOUR_OF_DAY, 10);
            eventStarts(initialDate, false);
            eventEnds(initialDate, true);
            initialDate.set(Calendar.HOUR_OF_DAY, 12);
            eventEnds(initialDate, false);
        }
    }
}
