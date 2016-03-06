package com.plunner.plunner.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * A singleton class that provides validation of a meeting timeslots against the following rule:
 * If today is not sunday then a meeting timeslots can start any day from the monday of the next week,
 * otherwise it can start any day from the monday of the next next week
 *
 * @author Giorgio Pea
 */
public class MeetingTimeslotValidator {
    //Instance
    private static MeetingTimeslotValidator instance;

    public static MeetingTimeslotValidator getInstance(){
        if(instance == null){
            instance = new MeetingTimeslotValidator();
        }
        return instance;
    }

    private MeetingTimeslotValidator() {
    }

    /**
     * Validates a meeting timeslot
     * @param calendar The start time of the meeting timeslot to validate
     * @param todayDate An optional date for today (used for testing purposes)
     * @return true If the meeting timeslot is valid, otherwise false
     */
    public boolean validate(Calendar calendar, Calendar todayDate){
        Calendar today, cloned_today;
        if(todayDate == null){
            today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        }
        else{
            today = todayDate;
        }
        cloned_today = (Calendar) today.clone();
        if(today.after(calendar)){
            return false;
        }
        else if(today.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) ){
            return false;
        }
        else{
            cloned_today.add(Calendar.WEEK_OF_YEAR,1);
            if(cloned_today.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && today.get(Calendar.DAY_OF_WEEK)==1){
                return false;
            }

            return true;
        }

    }
}
