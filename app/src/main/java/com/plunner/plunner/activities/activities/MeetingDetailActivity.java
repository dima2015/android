package com.plunner.plunner.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.utils.DataExchanger;

public class MeetingDetailActivity extends AppCompatActivity {

    private TextView meetingTitle;
    private TextView meetingDesc;
    private TextView meetingGroup;
    private TextView meetingDuration;
    private TextView meetingStarts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meeting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meetingTitle = (TextView) findViewById(R.id.activity_meeting_detail_meeting_title);
        meetingDesc = (TextView) findViewById(R.id.activity_meeting_detail_meeting_desc);
        meetingDuration = (TextView) findViewById(R.id.activity_meeting_detail_meeting_duration);
        meetingGroup = (TextView) findViewById(R.id.activity_meeting_detail_meeting_group);
        meetingStarts = (TextView) findViewById(R.id.activity_meeting_detail_meeting_starts);
        fillData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }


    private void fillData() {
        Meeting meeting = DataExchanger.getInstance().getMeeting();
        meetingTitle.setText(meeting.getTitle());
        meetingDesc.setText(meeting.getDescription());
        int duration = Integer.parseInt(meeting.getDuration())/60;
        meetingDuration.setText(Integer.toString(duration)+" mins");
        meetingGroup.setText(meeting.getGroupName());
        if(meeting.getStartTime() != null ){
            meetingStarts.setText(meeting.getStartTime());
        }
    }
}
