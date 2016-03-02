package com.plunner.plunner.utils;

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by giorgiopea on 02/03/16.
 */
public class CalendarPickersTest extends TestCase {

    private Calendar calendar;

    @Override
    protected void setUp(){
        calendar = Calendar.getInstance();
    }

    public void testComputeDays(){
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        boolean testResult = true;
        List<Integer> days = CalendarPickers.getInstance().computeDays(calendar);
        assertEquals(days.size(), 31);
        for (int i=0; i<days.size(); i++){
            testResult = testResult && (days.get(i) == (i+1));
        }
        assertEquals(testResult, true);
        calendar.set(Calendar.MONTH, Calendar.APRIL);
        testResult = true;
        days = CalendarPickers.getInstance().computeDays(calendar);
        assertEquals(days.size(), 30);
        for (int i=0; i<days.size(); i++){
            testResult = testResult && (days.get(i) == (i+1));
        }
        assertEquals(testResult, true);
    }
    public void testGetMonthIndex(){
        String monthName = "March";
        try {
            assertEquals(Calendar.MARCH,CalendarPickers.getInstance().getMonthIndex(monthName));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void testGetMonthName(){
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        assertEquals("March", CalendarPickers.getInstance().getMonthName(calendar));
    }
    public void testBuildMonths(){
        boolean testResult = true;
        List<Calendar> monthsList = CalendarPickers.getInstance().buildMonths(Calendar.MARCH);
        List<Calendar> testList = new ArrayList<>();
        Calendar cloned_calendar;
        for(int i=Calendar.MARCH; i<12; i++){
            cloned_calendar = (Calendar) calendar.clone();
            cloned_calendar.set(Calendar.MONTH, i);
            cloned_calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);
            testList.add(cloned_calendar);
        }
        for(int i=Calendar.JANUARY; i<12; i++){
            cloned_calendar = (Calendar) calendar.clone();
            cloned_calendar.set(Calendar.MONTH, i);
            cloned_calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            testList.add(cloned_calendar);
        }
        for(int i=Calendar.JANUARY; i<Calendar.APRIL; i++){
            cloned_calendar = (Calendar) calendar.clone();
            cloned_calendar.set(Calendar.MONTH, i);
            cloned_calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
            testList.add(cloned_calendar);
        }

        assertEquals(testList.size(), monthsList.size());
    }



}
