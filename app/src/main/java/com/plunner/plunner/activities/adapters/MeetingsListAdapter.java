package com.plunner.plunner.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.models.models.employee.Meeting;

import java.util.List;

/**
 * Created by giorgiopea on 04/01/16.
 */
public class MeetingsListAdapter extends ArrayAdapter<Meeting> {

    private ListItem listItem;

    public MeetingsListAdapter(Context context, List<Meeting> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern

        Meeting meeting = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.meeting_item, null);

            //Binds values to view
            this.listItem = new ListItem();
            this.listItem.title = (TextView) convertView.
                    findViewById(R.id.meeting_item_title);
            this.listItem.description = (TextView) convertView
                    .findViewById(R.id.meeting_item_desc);


            convertView.setTag(listItem);

        } else {
            this.listItem = (ListItem) convertView.getTag();
        }


        this.listItem.title.setText(meeting.getTitle());
        this.listItem.description.setText(meeting.getDescription());

        return convertView;

    }

    static class ListItem {
        TextView title;
        TextView description;

    }
}