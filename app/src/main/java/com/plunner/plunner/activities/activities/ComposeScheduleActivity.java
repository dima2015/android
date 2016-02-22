package com.plunner.plunner.activities.activities;


import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.utils.CalendarPickers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An activity that lets users compose a schedule by inserting a name for the schedule and some busy time slots
 */
public class ComposeScheduleActivity extends AppCompatActivity{

    /**
     * Holds a reference to the WeekView of the activity, that is to say a calendar view that lets users
     * see events for a given day
     */
    private WeekView mWeekView;
    /**
     * Holds a reference to a view that contains buttons that indicates months, pressing on one of these
     * buttons causes the {@link #mWeekView} to go to the current day of the selected month
     */
    private LinearLayout monthsPicker;
    /**
     * Holds a reference to a view that contains buttons that indicates days of the current selected month, pressing on one of these
     * buttons causes the {@link #mWeekView} to go to the selected day of the current month
     */
    private LinearLayout daysPicker;
    private ActionBar actionBar;
    private EditText scheduleNameInput;
    private TextView scheduleStatus;
    /**
     * Current highligthed button of the {@link #daysPicker}
     */
    private Button currentDayBtn;
    /**
     * Current highligthed button of the {@link #monthsPicker}
     */
    private Button currentMonthBtn;
    /**
     * Maps month buttons to {@link Calendar} instancies
     */
    private Map<Button, Calendar> monthsBtnCalendarMap;
    /**
     * A fragment used to create/edit an event in the {@link #mWeekView }
     */
    private EventDetailFragment addEventFragment;
    private Switch enabledSwitch ;
    private List<WeekViewEvent> composedEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_schedule);
        //Tolbar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        //View elements retrieval
        scheduleStatus = (TextView) findViewById(R.id.compose_schedule_status);
        monthsPicker = (LinearLayout) findViewById(R.id.months_picker);
        daysPicker = (LinearLayout) findViewById(R.id.days_picker);
        scheduleNameInput = (EditText) findViewById(R.id.compose_schedule_schedule_name);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        monthsBtnCalendarMap = new HashMap<>();
        actionBar = getSupportActionBar();
        enabledSwitch = (Switch) findViewById(R.id.compose_schedule_enabled_switch);
        //This activity can come back to the main activity
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Pickers init
        createCalendarPickersView(-1);
        //Event view init
        setWeekView();
        mWeekView.goToDate(Calendar.getInstance());
        //enabled switch
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEnabledSwitchStatusChange(isChecked);
            }
        });
        composedEvents = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compose_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                //handleFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setWeekView() {
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {

            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
            }
        });

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return composedEvents;
            }

        });
        mWeekView.notifyDatasetChanged();
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(addEventFragment == null){
                    addEventFragment = new EventDetailFragment();
                    addEventFragment.setInitialDate(time);

                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity
                    actionBar.hide();

                    transaction.add(R.id.compose_schedule_root, addEventFragment)
                            .addToBackStack(null).commit();
                }
                else{
                    actionBar.hide();
                    addEventFragment.setInitialDate(time);
                    transaction.show(addEventFragment).commit();

                }



            }
        });
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
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
        });
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.UK);
                    return sdf.format(date.getTime()).toUpperCase();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            public String interpretTime(int hour) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
                    return sdf.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        });
    }


    public void showToolbar() {
        actionBar.show();
    }

    /**
     * Populates the {@link #daysPicker} and the {@link #monthsPicker}
     * @param month The central month button to be considered. If is equal to -1 today's month is considered
     */
    private void createCalendarPickersView(int month) {
        Button monthBtn, dayBtn;
        Calendar currentCompositeMonth;
        int currentMonth, currentYear, currentDay;
        Calendar calendar = Calendar.getInstance();
        CalendarPickers calendarPickers = CalendarPickers.getInstance();
        //Gets the days in the current month
        List<Integer> daysList = calendarPickers.computeDays(calendar);
        //Gets the months that can be selected by the user from a given central month
        List<Calendar> monthsList = calendarPickers.buildMonths(month);
        LayoutInflater inflater = getLayoutInflater();
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
     * Changes the current day in the {@link #daysPicker}, updating also the {@link #mWeekView}
     * @param v The pressed dayBtn
     * @see #currentMonthBtn
     */
    public void changeDay(View v) {
        scheduleNameInput.clearFocus();
        Calendar calendar = Calendar.getInstance();
        Button pressedDayBtn = (Button) v;
        int dayPressed = Integer.parseInt(pressedDayBtn.getText().toString());
        calendar.set(Calendar.DAY_OF_MONTH, dayPressed);
        calendar.set(Calendar.MONTH, monthsBtnCalendarMap.get(currentMonthBtn).get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, monthsBtnCalendarMap.get(currentMonthBtn).get(Calendar.YEAR));
        cancelDayBtnAppearance(currentDayBtn);
        imposeDayBtnAppearance(pressedDayBtn);
        currentDayBtn = pressedDayBtn;
        mWeekView.goToDate(calendar);
    }

    /**
     * Changes the current month in the monthPicker, recomputing also the days associated to it in the daysPicker
     * @param v The pressed monthButton
     * @see #daysPicker
     * @see #monthsPicker
     * @see #currentMonthBtn
     * @see #mWeekView
     */
    public void changeMonth(View v) {
        scheduleNameInput.clearFocus();
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
        mWeekView.goToDate(associatedDate);
    }

    /**
     * Highlights the button in the {@link #monthsPicker} that corresponds to the given month and year
     * @param month The month whose corresponding button in the {@link #monthsPicker} must be highlighted
     * @param year  The year of the month whose corresponding button in the {@link #monthsPicker} must be highlighted
     */
    private void scrollSwitchMonth(int month, int year) {
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

    /**
     * Adds to the daysPicker as much dayButtons as the integers specified in the days parameter. The text of these dayButtons
     * is equal to the integer values contained in the days parameter
     * @param days A list of integer that represent the days numbers to be rendered as buttons
     * @param selectedDay The day to be highlighted
     * @see #daysPicker
     * @see #currentDayBtn
     */
    private void drawDaysBtns(List<Integer> days, int selectedDay) {
        Button dayBtn;
        int currentDay;
        LayoutInflater inflater = getLayoutInflater();
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

    /**
     * Changes the text near the enabledSwitch according to the status of the switch
     * @param isChecked The status of the switch
     */
    private void onEnabledSwitchStatusChange(boolean isChecked) {
        //Resets the focus on the editext relative to the name of the schedule
        scheduleNameInput.clearFocus();
        if (isChecked) {
            scheduleStatus.setText(getResources().getText(R.string.enabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
        } else {
            scheduleStatus.setText(getResources().getText(R.string.disabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.light_gray));
        }
    }
    /**
     * Applies a style typical of day buttons to a given button
     * @param btn The button to be styled
     */
    private void imposeDayBtnAppearance(Button btn) {
        btn.setBackgroundResource(R.drawable.day_button_bkgnd);
        btn.setTextColor(ContextCompat.getColor(this, R.color.white));
    }
    /**
     * Applies a style typical of month buttons to a given button
     * @param btn The button to be styled
     */
    private void imposeMonthBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    /**
     * Removes the day button style to the given button
     * @param btn The button whose whose style must be reset
     */
    private void cancelDayBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        btn.setBackgroundResource(0);
        btn.setBackgroundColor(Color.TRANSPARENT);
    }
    /**
     * Removes the month button style to the given button
     * @param btn The button whose whose style must be reset
     */
    private void resetMonthBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
    }

    public void hideFragment(View v){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(addEventFragment != null){
            fragmentTransaction.hide(addEventFragment).commit();
            showToolbar();
        }

    }


    public void addEventDateChange(View v){
        addEventFragment.showDatePicker((Integer) v.getTag());
    }
    public void addEventTimeChange(View v){
        addEventFragment.showTimePicker((Integer) v.getTag());
    }

    public void saveEvent(View v) {
        List<Calendar> event = addEventFragment.save();
        if(event != null){
            hideFragment(null);
            composedEvents.add(new WeekViewEvent(1, "", event.get(0), event.get(1)));
            mWeekView.notifyDatasetChanged();
        }
    }
}

