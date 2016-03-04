package com.plunner.plunner.utils;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 * Created by giorgiopea on 02/03/16.
 */
public class MeetingTimeslotValidatorTest extends TestCase{

    public void testValidateMathod(){
        Calendar today = Calendar.getInstance();
        Calendar testDate = (Calendar) today.clone();
        testDate.set(Calendar.DAY_OF_WEEK, 6);
        assertEquals(false, MeetingTimeslotValidator.getInstance().validate(testDate, null));
        testDate.add(Calendar.WEEK_OF_YEAR, 1);
        testDate.set(Calendar.DAY_OF_WEEK, 6);
        assertEquals(true, MeetingTimeslotValidator.getInstance().validate(testDate, null));
        today.set(Calendar.DAY_OF_WEEK, 1);
        assertEquals(false, MeetingTimeslotValidator.getInstance().validate(testDate, today));
        testDate = (Calendar) today.clone();
        today.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(false, MeetingTimeslotValidator.getInstance().validate(testDate, today));
    }
}
