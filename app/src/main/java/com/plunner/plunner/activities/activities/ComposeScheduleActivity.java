package com.plunner.plunner.activities.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.plunner.plunner.R;
import com.plunner.plunner.activities.Fragments.CalendarFragment;
import com.plunner.plunner.activities.Fragments.EventDetailFragment;
import com.plunner.plunner.utils.CalendarPickers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ComposeScheduleActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener {


    private WeekView mWeekView;
    private LinearLayout monthsPicker;
    private LinearLayout daysPicker;
    private ActionBar actionBar;
    private EditText scheduleNameInput;
    private TextView scheduleStatus;
    private Button currentDayBtn;
    private Button currentMonthBtn;
    private Map<Button, Calendar> monthsBtnCalendarMap;

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
        final Switch enabledSwitch = (Switch) findViewById(R.id.compose_schedule_enabled_switch);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        monthsBtnCalendarMap = new HashMap<>();
        actionBar = getSupportActionBar();
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
                enabledSwitchOnCheck(isChecked);
            }
        });


// Set an action when any event is clicked.


// Set long press listener for events.


    }

    private void enabledSwitchOnCheck(boolean isChecked) {
        scheduleNameInput.clearFocus();
        if (isChecked) {
            scheduleStatus.setText(getResources().getText(R.string.enabled));
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
        } else {
            scheduleStatus.setText("DISABLED");
            scheduleStatus.setTextColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.light_gray));
        }
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
                handleFragment();
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
                List<WeekViewEvent> events = new ArrayList<>();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 15);
                WeekViewEvent event = new WeekViewEvent(1, startTime.toString(), startTime, endTime);
                event.setColor(ContextCompat.getColor(ComposeScheduleActivity.this, R.color.colorPrimary));
                events.add(event);


                return events;
            }

        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new EventDetailFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                actionBar.hide();
                transaction.add(R.id.compose_schedule_root, fragment)
                        .addToBackStack(null).commit();


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
                        switchMonthWhenScroll(newMonth, newYearIndex);

                    }


                }

            }
        });
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.UK);
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


    private void handleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.lapal);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            CalendarFragment fragment_1 = new CalendarFragment();
            transaction.add(R.id.lapal, fragment_1);
            transaction.commit();
        } else {
            if (fragment.isHidden()) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }

    }


    @Override
    public void onCalendarDateSelected(Calendar date) {
        mWeekView.goToDate(date);
        handleFragment();
    }

    private void createCalendarPickersView(int month) {
        Button monthBtn, dayBtn;
        Calendar calendar = Calendar.getInstance();
        CalendarPickers calendarPickers = CalendarPickers.getInstance();
        List<Integer> days = calendarPickers.computeDays(calendar);
        //Gets days and months labels to be inserted in the activity view
        List<Calendar> monthsCompositeList = calendarPickers.buildMonths(month);
        LayoutInflater inflater = getLayoutInflater();
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayYear = calendar.get(Calendar.YEAR);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar currentCompositeMonth;
        int currentMonth, currentYear, currentDay;


        for (int i = 0; i < monthsCompositeList.size(); i++) {
            currentCompositeMonth = monthsCompositeList.get(i);
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
        for (int i = 0; i < days.size(); i++) {
            currentDay = days.get(i);
            dayBtn = (Button) inflater.inflate(R.layout.day_button, daysPicker, false);
            dayBtn.setText(Integer.toString(currentDay));
            if (currentDay == todayDay) {
                imposeDayBtnAppearance(dayBtn);
                currentDayBtn = dayBtn;
            }
            daysPicker.addView(dayBtn);
        }

    }

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

    public void changeMonth(View v) throws ParseException {
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

    private void switchMonthWhenScroll(int newMonth, int newYearIndex) {
        Button currentButton;
        int currentMonth, currentYear;
        Calendar currentDate;
        for (int i = 0; i < monthsPicker.getChildCount(); i++) {
            currentButton = (Button) monthsPicker.getChildAt(i);
            currentDate = monthsBtnCalendarMap.get(currentButton);
            currentMonth = currentDate.get(Calendar.MONTH);
            currentYear = currentDate.get(Calendar.YEAR);
            if (currentMonth == newMonth && currentYear == newYearIndex) {
                try {
                    changeMonth(currentButton);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawDaysBtns(List<Integer> days, int daySelected) {
        LayoutInflater inflater = getLayoutInflater();
        Button dayBtn;
        int currentDay;
        daysPicker.removeAllViews();
        for (int i = 0; i < days.size(); i++) {
            currentDay = days.get(i);
            dayBtn = (Button) inflater.inflate(R.layout.day_button, daysPicker, false);
            dayBtn.setText(Integer.toString(currentDay));
            if (currentDay == daySelected) {
                imposeDayBtnAppearance(dayBtn);
                currentDayBtn = dayBtn;
            }
            daysPicker.addView(dayBtn);
        }
    }

    private void imposeDayBtnAppearance(Button button) {
        button.setBackgroundResource(R.drawable.day_button_bkgnd);
        button.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void imposeMonthBtnAppearance(Button button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    private void cancelDayBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        btn.setBackgroundResource(0);
        btn.setBackgroundColor(Color.TRANSPARENT);
    }

    private void resetMonthBtnAppearance(Button btn) {
        btn.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
    }

    public void editDate(View v){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

            }

        };
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void editHour(View v){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        };
        new TimePickerDialog(this,time,calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}

