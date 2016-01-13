package com.plunner.plunner.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plunner.plunner.R;

import java.util.List;

/**
 * Created by giorgiopea on 04/01/16.
 */
public class MeetingsListAdapter extends ArrayAdapter<String> {

    private ListItem listItem;

    public MeetingsListAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern

        String text = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.meetings_row, null);

            //Binds values to view
            this.listItem = new ListItem();
            this.listItem.title = (TextView) convertView.
                    findViewById(R.id.meetingTitle);
            this.listItem.description = (TextView) convertView
                    .findViewById(R.id.meetingDescription);


            convertView.setTag(listItem);

        } else {
            this.listItem = (ListItem) convertView.getTag();
        }


        this.listItem.title.setText(text+"meetings");
        this.listItem.description.setText(text + " subtitle");

        return convertView;

    }

    static class ListItem {
        TextView title;
        TextView description;

    }
}