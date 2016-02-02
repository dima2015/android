package com.plunner.plunner.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 02/02/16.
 */
public class CalendarPickers {

    private static CalendarPickers instance;
    private Map<Integer, String> monthsList;
    private Map<String, Integer> inverseMonthList;
    private List<DayMonthStructure> daysLabelList;
    private List<DayMonthStructure> monthsLabelList;

    public static CalendarPickers getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new CalendarPickers();
            return instance;
        }
    }

    /**
     * Cretes object of this class by initializing their private fields and
     * populating the list of the names of the months with the names of the months
     */
    private CalendarPickers() {
        this.monthsList = new HashMap<>();
        this.daysLabelList = new ArrayList<>();
        this.monthsLabelList = new ArrayList<>();
        this.inverseMonthList = new HashMap<>();
        populateMonthsMaps();
    }

    /**
     * Populates the monthsList with the names of the months
     */
    private void populateMonthsMaps() {
        //Populating month_index -> month_name map
        this.monthsList.put(1, "January");
        this.monthsList.put(2, "February");
        this.monthsList.put(3, "March");
        this.monthsList.put(4, "April");
        this.monthsList.put(5, "May");
        this.monthsList.put(6, "June");
        this.monthsList.put(7, "July");
        this.monthsList.put(8, "August");
        this.monthsList.put(9, "September");
        this.monthsList.put(10, "October");
        this.monthsList.put(11, "November");
        this.monthsList.put(12, "December");

        //Populating month_name -> month_index map
        this.inverseMonthList.put("January", 1);
        this.inverseMonthList.put("February", 2);
        this.inverseMonthList.put("March", 3);
        this.inverseMonthList.put("April", 4);
        this.inverseMonthList.put("May", 5);
        this.inverseMonthList.put("June", 6);
        this.inverseMonthList.put("July", 7);
        this.inverseMonthList.put("August", 8);
        this.inverseMonthList.put("September", 9);
        this.inverseMonthList.put("October", 10);
        this.inverseMonthList.put("November", 11);
        this.inverseMonthList.put("December", 12);
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
     * @see DayMonthStructure
     */
    public Map<String, List<DayMonthStructure>> build(int month) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int nextYear = currentYear + 1;
        int previousYear = currentYear - 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int daysInCurrentOrGivenMonth;
        Map<String, List<DayMonthStructure>> returnValues = new HashMap<>();

        if (month == -1 || this.monthsLabelList.isEmpty()) {
            this.monthsLabelList.clear();
            daysInCurrentOrGivenMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = currentMonth; i <= 12; i++) {
                monthsLabelList.add(new DayMonthStructure(this.monthsList.get(i) + " " + previousYear, false));
            }
            for (int i = 1; i <= 12; i++) {
                if (i == currentMonth) {
                    monthsLabelList.add(new DayMonthStructure(this.monthsList.get(i) + " " + currentYear, true));
                } else {
                    monthsLabelList.add(new DayMonthStructure(this.monthsList.get(i) + " " + currentYear, false));
                }

            }
            for (int i = 1; i <= currentMonth; i++) {
                monthsLabelList.add(new DayMonthStructure(this.monthsList.get(i) + " " + nextYear, false));
            }
        } else {
            daysInCurrentOrGivenMonth = calendar.getActualMaximum(month);
        }
        daysLabelList.clear();
        for (int i = 1; i <= daysInCurrentOrGivenMonth; i++) {
            if (i == currentDay) {
                daysLabelList.add(new DayMonthStructure(Integer.toString(i), true));
            } else {
                daysLabelList.add(new DayMonthStructure(Integer.toString(i), false));
            }
        }
        returnValues.put("months", monthsLabelList);
        returnValues.put("days", daysLabelList);
        return returnValues;
    }

    public  int getMonthIndex(String month){
        return this.inverseMonthList.get(month);
    }

}
