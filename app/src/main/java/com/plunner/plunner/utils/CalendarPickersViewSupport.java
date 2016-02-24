package com.plunner.plunner.utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.plunner.plunner.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 24/02/16.
 *
 */
public class CalendarPickersViewSupport {

    private static CalendarPickersViewSupport instance;
    private Activity activity;
    private LinearLayout monthsPicker;
    private LinearLayout daysPicker;
    private Button currentMonthBtn;
    private Button currentDayBtn;

    private Map<Button, Calendar> monthsBtnCalendarMap;


    public CalendarPickersViewSupport() {
        monthsBtnCalendarMap = new HashMap<>();
    }

    public static CalendarPickersViewSupport getInstance() {
        if (instance == null) {
            instance = new CalendarPickersViewSupport();
        }
        return instance;

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setDaysPicker(LinearLayout daysPicker) {
        this.daysPicker = daysPicker;
    }

    public void setMonthsPicker(LinearLayout monthsPicker) {
        this.monthsPicker = monthsPicker;
    }

    /**
     * Applies a style typical of day buttons to a given button
     *
     * @param btn The button to be styled
     */
    public void imposeDayBtnAppearance(Button btn) {
        btn.setBackgroundResource(R.drawable.day_button_bkgnd);
        btn.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    /**
     * Applies a style typical of month buttons to a given button
     *
     * @param btn The button to be styled
     */
    public void imposeMonthBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(activity, R.color.red));
    }

    /**
     * Removes the day button style to the given button
     *
     * @param btn The button whose whose style must be reset
     */
    public void cancelDayBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(activity, R.color.light_gray));
        btn.setBackgroundResource(0);
        btn.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Removes the month button style to the given button
     *
     * @param btn The button whose whose style must be reset
     */
    public void resetMonthBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(activity, R.color.light_gray));
    }

    /**
     * Populates the {@link #daysPicker} and the {@link #monthsPicker}
     *
     * @param month The central month button to be considered. If is equal to -1 today's month is considered
     */

    public void createViews(int month) {
        Button monthBtn, dayBtn;
        Calendar currentCompositeMonth;
        int currentMonth, currentYear, currentDay;
        Calendar calendar = Calendar.getInstance();
        CalendarPickers calendarPickers = CalendarPickers.getInstance();
        //Gets the days in the current month
        List<Integer> daysList = calendarPickers.computeDays(calendar);
        //Gets the months that can be selected by the user from a given central month
        List<Calendar> monthsList = calendarPickers.buildMonths(month);
        LayoutInflater inflater = activity.getLayoutInflater();
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayYear = calendar.get(Calendar.YEAR);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);


        //Inflates month buttons and highlights the button that corresponds to today's month
        for (int i = 0; i < monthsList.size(); i++) {
            currentCompositeMonth = monthsList.get(i);
            currentMonth = currentCompositeMonth.get(Calendar.MONTH);
            currentYear = currentCompositeMonth.get(Calendar.YEAR);
            monthBtn = (Button) inflater.inflate(R.layout.month_button, monthsPicker, false);
            monthBtn.setText(calendarPickers.getMonthName(currentCompositeMonth) + " " + currentYear);
            if (currentMonth == todayMonth && currentYear == todayYear) {
                imposeMonthBtnAppearance(monthBtn);
                currentMonthBtn = monthBtn;
            }
            monthsPicker.addView(monthBtn);
            monthsBtnCalendarMap.put(monthBtn, currentCompositeMonth);
        }
        //Inflates days buttons and highlights the button that corresponds to today's day
        for (int i = 0; i < daysList.size(); i++) {
            currentDay = daysList.get(i);
            dayBtn = (Button) inflater.inflate(R.layout.day_button, daysPicker, false);
            dayBtn.setText(Integer.toString(currentDay));
            if (currentDay == todayDay) {
                imposeDayBtnAppearance(dayBtn);
                currentDayBtn = dayBtn;
            }
            daysPicker.addView(dayBtn);
        }
    }

    /**
     * Adds to the daysPicker as much dayButtons as the integers specified in the days parameter. The text of these dayButtons
     * is equal to the integer values contained in the days parameter
     *
     * @param days        A list of integer that represent the days numbers to be rendered as buttons
     * @param selectedDay The day to be highlighted
     * @see #daysPicker
     * @see #currentDayBtn
     */
    private void drawDaysBtns(List<Integer> days, int selectedDay) {
        Button dayBtn;
        int currentDay;
        LayoutInflater inflater = activity.getLayoutInflater();
        daysPicker.removeAllViews();
        for (int i = 0; i < days.size(); i++) {
            currentDay = days.get(i);
            dayBtn = (Button) inflater.inflate(R.layout.day_button, daysPicker, false);
            dayBtn.setText(Integer.toString(currentDay));
            if (currentDay == selectedDay) {
                imposeDayBtnAppearance(dayBtn);
                currentDayBtn = dayBtn;
            }
            daysPicker.addView(dayBtn);
        }
    }

    public Calendar changeMonth(View v) {
        //scheduleNameInput.clearFocus();
        Button pressedMonthBtn = (Button) v;
        Calendar associatedDate = monthsBtnCalendarMap.get(v);
        List<Integer> days = CalendarPickers.getInstance().computeDays(associatedDate);
        resetMonthBtnAppearance(currentMonthBtn);
        imposeMonthBtnAppearance(pressedMonthBtn);
        currentMonthBtn = pressedMonthBtn;
        if (days.size() != daysPicker.getChildCount()) {
            drawDaysBtns(days, Integer.parseInt(currentDayBtn.getText().toString()));
        }
        associatedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(currentDayBtn.getText().toString()));
        return associatedDate;
        //mWeekView.goToDate(associatedDate);
    }

    public Calendar changeDay(View v) {
        //scheduleNameInput.clearFocus();
        Calendar calendar = Calendar.getInstance();
        Button pressedDayBtn = (Button) v;
        int dayPressed = Integer.parseInt(pressedDayBtn.getText().toString());
        calendar.set(Calendar.DAY_OF_MONTH, dayPressed);
        calendar.set(Calendar.MONTH, monthsBtnCalendarMap.get(currentMonthBtn).get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, monthsBtnCalendarMap.get(currentMonthBtn).get(Calendar.YEAR));
        cancelDayBtnAppearance(currentDayBtn);
        imposeDayBtnAppearance(pressedDayBtn);
        currentDayBtn = pressedDayBtn;
        return calendar;

    }

    /**
     * Highlights the button in the {@link #
     * monthsPicker} that corresponds to the given month and year
     *
     * @param month The month whose corresponding button in the {@link #monthsPicker} must be highlighted
     * @param year  The year of the month whose corresponding button in the {monthsPicker} must be highlighted
     */

    public void scrollSwitchMonth(int month, int year) {
        Button currentButton;
        int currentMonth, currentYear;
        Calendar currentDate;
        for (int i = 0; i < monthsPicker.getChildCount(); i++) {
            currentButton = (Button) monthsPicker.getChildAt(i);
            currentDate = monthsBtnCalendarMap.get(currentButton);
            currentMonth = currentDate.get(Calendar.MONTH);
            currentYear = currentDate.get(Calendar.YEAR);
            if (currentMonth == month && currentYear == year) {
                changeMonth(currentButton);
            }
        }
    }

    public void scrollDayScroll(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
        int oldMonth = monthsBtnCalendarMap.get(currentMonthBtn).get(Calendar.MONTH);
        int newDayIndex, newYearIndex, newMonth;
        if (oldFirstVisibleDay != null) {
            newDayIndex = newFirstVisibleDay.get(Calendar.DAY_OF_MONTH);
            newMonth = newFirstVisibleDay.get(Calendar.MONTH);
            newYearIndex = newFirstVisibleDay.get(Calendar.YEAR);
            cancelDayBtnAppearance(currentDayBtn);
            currentDayBtn = (Button) daysPicker.getChildAt(newDayIndex - 1);
            imposeDayBtnAppearance(currentDayBtn);
            if (oldMonth != newMonth) {
                scrollSwitchMonth(newMonth, newYearIndex);
            }
        }
    }
}
