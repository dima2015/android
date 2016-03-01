package com.plunner.plunner.utils;

import com.plunner.plunner.models.models.employee.Timeslot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by giorgiopea on 01/03/16.
 */
public class TimeslotFrontEndAdapter {

    private static TimeslotFrontEndAdapter instance;

    public static TimeslotFrontEndAdapter getInstance() {
        if (instance == null) {
            instance = new TimeslotFrontEndAdapter();
        }
        return instance;
    }

    private TimeslotFrontEndAdapter() {
    }

    public Map<String, Calendar> adapt(Timeslot timeslot) throws ParseException {
        Date parsedOne, parsedTwo;
        Map<String, Calendar> toReturn = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        Calendar calendar_one = java.util.Calendar.getInstance();
        Calendar calendar_two = (java.util.Calendar) calendar_one.clone();
        parsedOne = sdf.parse(timeslot.getTimeStart());
        parsedTwo = sdf.parse(timeslot.getTimeEnd());
        calendar_one.setTime(parsedOne);
        calendar_two.setTime(parsedTwo);
        toReturn.put("startTime", calendar_one);
        toReturn.put("endTime", calendar_two);
        return toReturn;

    }
}
