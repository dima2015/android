package com.plunner.plunner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by giorgiopea on 20/12/15.
 */
public class meetingsArrayAdapter extends ArrayAdapter<Meeting> {

    public meetingsArrayAdapter(Context context, List<Meeting> meetingsList) {
        super(context, 0, meetingsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern

        Meeting meeting = getItem(position);


        MeetingHolder meetingHolder;

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.meeting_row, null);

            meetingHolder = new MeetingHolder();

            meetingHolder.meetingTitle = (TextView) convertView.
                    findViewById(R.id.meeting_title);
            meetingHolder.meetingDesc = (TextView) convertView
                    .findViewById(R.id.meeting_desc);

            convertView.setTag(meetingHolder);

        }
        else {
            meetingHolder = (MeetingHolder) convertView.getTag();
        }


        meetingHolder.meetingTitle.setText(meeting.getTitle());
        meetingHolder.meetingDesc.setText(meeting.getDesc());

        return convertView;

    }

    static class MeetingHolder {
        TextView meetingTitle;
        TextView meetingDesc;
    }
}
