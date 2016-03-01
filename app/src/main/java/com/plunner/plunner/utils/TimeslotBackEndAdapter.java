package com.plunner.plunner.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giorgiopea on 01/03/16.
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

    public Map<String, String> adapt(CustomWeekEvent timeslot) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("startTime", sdf.format(timeslot.getStartTime().getTime()));
        map.put("endTime", sdf.format(timeslot.getEndTime().getTime()));
        return map;
    }
}
