package com.plunner.plunner.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giorgiopea on 01/03/16.
 *
 * A singleton class that provides adaptation of timeslots beetwen the frontend and the backend
 */
public class TimeslotBackEndAdapter {

    private static TimeslotBackEndAdapter instance;

    public static TimeslotBackEndAdapter getInstance() {
        if (instance == null) {
            instance = new TimeslotBackEndAdapter();
        }
        return instance;
    }

    private TimeslotBackEndAdapter() {
    }

    /**
     * Adapts a timeslot between the frontend and the backend
     * @param timeslot The timeslot to be adapted
     * @return A map that represent the adapted timeslot
     */
    public Map<String, String> adapt(CustomWeekEvent timeslot) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("startTime", sdf.format(timeslot.getStartTime().getTime()));
        map.put("endTime", sdf.format(timeslot.getEndTime().getTime()));
        return map;
    }
}
