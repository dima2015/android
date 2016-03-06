package com.plunner.plunner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by giorgiopea on 01/03/16.
 *
 * A singleton class that provides adaptation of timeslots between the backend and the backend
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
    /**
     * Adapts a timeslot between the backend and the frontend
     * @param startDate the start date of the backend timeslot
     * @param endDate the end dare of the backend timeslot
     * @return A map that represent the adapted timeslot
     */
    public Map<String, Calendar> adapt(String startDate, String endDate) throws ParseException {
        Date parsedOne, parsedTwo;
        Map<String, Calendar> toReturn = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        Calendar calendar_one = java.util.Calendar.getInstance();
        Calendar calendar_two = (java.util.Calendar) calendar_one.clone();
        parsedOne = sdf.parse(startDate);
        parsedTwo = sdf.parse(endDate);
        calendar_one.setTime(parsedOne);
        calendar_two.setTime(parsedTwo);
        toReturn.put("startTime", calendar_one);
        toReturn.put("endTime", calendar_two);
        return toReturn;

    }
}
