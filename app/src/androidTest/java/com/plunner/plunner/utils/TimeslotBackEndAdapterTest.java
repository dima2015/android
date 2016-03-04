package com.plunner.plunner.utils;

import com.plunner.plunner.utils.CustomWeekEvent;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by giorgiopea on 02/03/16.
 *
 */
public class TimeslotBackEndAdapterTest extends TestCase {

    public void testAdaptMethod(){
        Map<String, String> adaptedTimeslot;
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        CustomWeekEvent customWeekEvent;
        startTime.set(Calendar.YEAR, 8888);
        startTime.set(Calendar.MONTH, 8);
        startTime.set(Calendar.DAY_OF_MONTH,8);
        startTime.set(Calendar.HOUR_OF_DAY,8);
        startTime.set(Calendar.MINUTE,8);
        startTime.set(Calendar.SECOND,8);
        customWeekEvent = new CustomWeekEvent(0,"",startTime, startTime, false, false);
        adaptedTimeslot = TimeslotBackEndAdapter.getInstance().adapt(customWeekEvent);
        assertEquals(adaptedTimeslot.get("startTime"), "8888-09-08 08:08:08");
        assertEquals(adaptedTimeslot.get("endTime"),"8888-09-08 08:08:08");
        endTime.set(Calendar.YEAR, 2222);
        endTime.set(Calendar.MONTH, 2);
        endTime.set(Calendar.DAY_OF_MONTH, 22);
        endTime.set(Calendar.HOUR_OF_DAY, 22);
        endTime.set(Calendar.MINUTE, 22);
        endTime.set(Calendar.SECOND, 22);
        customWeekEvent = new CustomWeekEvent(0,"", startTime, endTime, false, false);
        adaptedTimeslot = TimeslotBackEndAdapter.getInstance().adapt(customWeekEvent);
        assertEquals(adaptedTimeslot.get("startTime"), "8888-09-08 08:08:08");
        assertEquals(adaptedTimeslot.get("endTime"),"2222-03-22 22:22:22");
    }
}
