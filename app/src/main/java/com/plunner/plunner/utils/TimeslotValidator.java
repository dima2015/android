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

    public boolean validate(Calendar calendar){
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if(today.after(calendar)){
            return false;
        }
        else if(today.get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH) ){
            return false;
        }
        else if(today.get(Calendar.WEEK_OF_MONTH) + 1 == calendar.get(Calendar.WEEK_OF_MONTH)){
            if(today.get(Calendar.DAY_OF_WEEK) == 1 && calendar.get(Calendar.DAY_OF_WEEK)!=1){
                return false;
            }
        }
        return true;
    }
}
