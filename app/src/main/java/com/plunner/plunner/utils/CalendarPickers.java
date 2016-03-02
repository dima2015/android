package com.plunner.plunner.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by giorgiopea on 02/02/16.
 */
public class CalendarPickers {

    private static CalendarPickers instance;
    private List<Integer> daysLabelList;
    private List<Calendar> monthsList;
    private SimpleDateFormat dateFormatter;

    public static CalendarPickers getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new CalendarPickers();
            return instance;
        }
    }

    /**
     * Creates objects of this class by initializing their private fields and
     * populating the list of the names of the months with the names of the months
     */
    private CalendarPickers() {
        this.daysLabelList = new ArrayList<>();
        this.monthsList = new ArrayList<>();
        this.dateFormatter = new SimpleDateFormat("MMMM",Locale.UK);

    }

    /**
     * Creates two lists of DayMonthStructure objects, one that holds all the days in the given or current month,
     * one that holds all the months with their year within a 1 year period before and after the current month.
     * These lists are encapsulated in map, the keys of the maps are just "months" and "keys" for easy
     * access.
     *
     * @param month The numeric representation of a given month(1 -> January, 12 -> December). If month is -1
     *              then the method consider the current month otherwise it considers the given month to build
     *              the list of related days
     * @return A map that encapsulates the list of the days relative the current or given month and the list
     * all the months with their year within a 1 year period before and after the current month
     */
    public List<Calendar> buildMonths(int month) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int nextYear = currentYear + 1;
        int previousYear = currentYear - 1;
        int currentMonth = calendar.get(Calendar.MONTH);

        if(month != -1){
            currentMonth = month;
        }
        this.monthsList.clear();
        for (int i = currentMonth; i < 12; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.YEAR, previousYear);
            monthsList.add(calendar);
        }
        for (int i = Calendar.JANUARY; i < 12; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i);
            monthsList.add(calendar);
        }
        for (int i = Calendar.JANUARY; i <= currentMonth; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.YEAR, nextYear);
            monthsList.add(calendar);
        }

        return monthsList;
    }
    public String getMonthName(Calendar calendar) {
        return dateFormatter.format(calendar.getTime());
    }
    public int getMonthIndex(String monthName) throws ParseException {
        Date date;
        Calendar calendar = Calendar.getInstance();
        date = dateFormatter.parse(monthName);
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }
    public List<Integer> computeDays(Calendar calendar){
        List<Integer> returnList = new ArrayList<>();
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=1; i<=days; i++){
            returnList.add(i);
        }
        return returnList;
    }


}
