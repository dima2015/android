package com.plunner.plunner.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by giorgiopea on 01/03/16.
 *
 */
public class TimeslotValidator {

    private static TimeslotValidator instance;

    public static TimeslotValidator getInstance(){
        if(instance == null){
            instance = new TimeslotValidator();
        }
        return instance;
    }

    private TimeslotValidator() {
    }

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
