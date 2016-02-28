package com.plunner.plunner.activities.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.plunner.plunner.R;

import java.util.ArrayList;
import java.util.List;

public class MeetingDetailActivity extends AppCompatActivity {

    private String meetingType;
    private EditText meetingTitle;
    private EditText meetingDesc;
    private EditText meetingGroup;
    private EditText meetingDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String data = intent.getExtras().getString("data");
        meetingType = intent.getExtras().getString("type");
        Log.i("1", data);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meeting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillData(data);
    }

    private void fillData(String data) {
        String[] tokenizedString = data.split(",");
        List<String> stringFields = new ArrayList<>();
        String[] subSplittedString;
        for(int i=0; i<tokenizedString.length; i++){
            subSplittedString = tokenizedString[i].split("=");
            stringFields.add(subSplittedString[1]);

        }
        ((EditText) findViewById(R.id.meeting_detail_title)).setText(stringFields.get(1));
        ((EditText) findViewById(R.id.meeting_detail_description)).setText(stringFields.get(2));
        ((TextView) findViewById(R.id.meeting_detail_group)).setText(stringFields.get(3));
        ((TextView) findViewById(R.id.meeting_detail_duration)).setText(stringFields.get(4));
        if(!stringFields.get(5).equals("null")){
            ((TextView) findViewById(R.id.meeting_detail_starts)).setText(stringFields.get(4));
        }
        Log.i("2", stringFields.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                //handleFragment();
                return true;
            case R.id.meeting_detail_edit:
                //;
                return true;
            case R.id.meeting_detail_delete:
                //;
                return true;
            case R.id.meeting_detail_save:
                //;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        if(!meetingType.equals("3")){
            menu.removeItem(R.id.meeting_detail_edit);
            menu.removeItem(R.id.meeting_detail_save);
            menu.removeItem(R.id.meeting_detail_delete);

        }
        return true;

    }
}
