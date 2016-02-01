package com.plunner.plunner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.plunner.plunner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by giorgiopea on 01/02/16.
 */
public class MonthSlider {

    private final List<String> months;
    private final List<String> computedLabels;

    public MonthSlider() {
        this.months = new ArrayList<>();
        this.computedLabels = new ArrayList<>();
        this.populateMonths();
        this.build();
    }
    private void populateMonths(){
        this.months.add("JANUARY");
        this.months.add("FEBRUARY");
        this.months.add("MARCH");
        this.months.add("APRIL");
        this.months.add("MAY");
        this.months.add("JUNE");
        this.months.add("JULY");
        this.months.add("AUGUST");
        this.months.add("SEPTEMBER");
        this.months.add("OCTOBER");
        this.months.add("NOVEMBER");
        this.months.add("DECEMBER");
    }
    private void build(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int nextYear = currentYear + 1;
        int previousYear = currentYear - 1;
        int currentMonth = calendar.get(Calendar.MONTH);
        for(int i=currentMonth; i<12; i++){
            computedLabels.add(this.months.get(i) + " " + previousYear);
        }
        for(int i=0; i<12; i++){
            computedLabels.add(this.months.get(i) + " " + currentYear);
        }
        for(int i=0; i<=currentMonth; i++){
            computedLabels.add(this.months.get(i) + " " + nextYear);
        }
    }
    public void attachToView(ViewGroup view){
        ListIterator<String> iterator = this.computedLabels.listIterator();
        Button button;
        String currentItem;

        while(iterator.hasNext()){
            currentItem = iterator.next();
            button = new Button(view.getContext());
            button.setText(currentItem);
        }
    }
}
