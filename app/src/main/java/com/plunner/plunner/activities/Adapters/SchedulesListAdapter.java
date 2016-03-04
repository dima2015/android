package com.plunner.plunner.activities.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.models.models.employee.Calendar;

import java.util.List;

/**
 * Created by giorgiopea on 04/01/16.
 */
public class SchedulesListAdapter extends ArrayAdapter<Calendar> {

    private ListItem listItem;

    public SchedulesListAdapter(Context context, List<Calendar> objects) {
        super(context, 0, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern

        Calendar schedule = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.schedules_item, null);

            this.listItem  = new ListItem();

            this.listItem.title = (TextView) convertView.
                    findViewById(R.id.schedules_item_title);
            this.listItem.updatedAt = (TextView) convertView
                    .findViewById(R.id.schedules_item_updated_at);
            this.listItem.status = (TextView) convertView.findViewById(R.id.schedules_item_status);


            convertView.setTag(listItem);

        } else {
            this.listItem = (ListItem) convertView.getTag();
        }


        this.listItem.title.setText(schedule.getName());
        this.listItem.updatedAt.setText(schedule.getUpdatedAt());
        if(schedule.getEnabled().equals("1")){
            this.listItem.status.setText(getContext().getText(R.string.enabled));
            this.listItem.status.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        }
        else{
            this.listItem.status.setText(getContext().getText(R.string.disabled));
            this.listItem.status.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        }


        return convertView;

    }

    static class ListItem {
        TextView title;
        TextView updatedAt;
        TextView status;

    }
}