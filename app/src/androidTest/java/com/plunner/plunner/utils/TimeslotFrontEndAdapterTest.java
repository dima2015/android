package com.plunner.plunner.utils;

import com.plunner.plunner.utils.CustomWeekEvent;
import com.plunner.plunner.utils.TimeslotFrontEndAdapter;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by giorgiopea on 02/03/16.
 *
 */
public class TimeslotFrontEndAdapterTest extends TestCase {

    public void testAdaptMethod(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Calendar> adaptedTimeslot;
        String startDate = "8888-08-08 08:08:08";
        try{
            adaptedTimeslot = TimeslotFrontEndAdapter.getInstance().adapt(startDate,startDate);
            assertEquals(sdf.format(adaptedTimeslot.get("startTime").getTime()),startDate);
            assertEquals(sdf.format(adaptedTimeslot.get("endTime").getTime()),startDate);
            String endDate = "2222-02-02 02:02:02";
            adaptedTimeslot = TimeslotFrontEndAdapter.getInstance().adapt(startDate,endDate);
            assertEquals(sdf.format(adaptedTimeslot.get("startTime").getTime()),startDate);
            assertEquals(sdf.format(adaptedTimeslot.get("endTime").getTime()),endDate);
        }
        catch(ParseException e){

        }

    }
}
