package com.plunner.plunner.activities.Adapters;

import android.content.Context;
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

            convertView = layoutInflater.inflate(R.layout.schedules_row, null);

            this.listItem  = new ListItem();

            this.listItem.title = (TextView) convertView.
                    findViewById(R.id.schedules_row_title);
            this.listItem.updatedAt = (TextView) convertView
                    .findViewById(R.id.schedules_row_updated_at);
            this.listItem.status = (TextView) convertView.findViewById(R.id.schedules_row_status);


            convertView.setTag(listItem);

        } else {
            this.listItem = (ListItem) convertView.getTag();
        }


        this.listItem.title.setText(schedule.getName());
        this.listItem.updatedAt.setText(schedule.getUpdatedAt());
        this.listItem.status.setText(schedule.getEnabled());

        return convertView;

    }

    static class ListItem {
        TextView title;
        TextView updatedAt;
        TextView status;

    }
}