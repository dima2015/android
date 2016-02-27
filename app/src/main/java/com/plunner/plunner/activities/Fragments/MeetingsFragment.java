package com.plunner.plunner.activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.Adapters.MeetingsListAdapter;
import com.plunner.plunner.activities.activities.MeetingDetailActivity;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Group;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.utils.ComManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MeetingsFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private List<Meeting> tbpMeetings;
    private List<Meeting> pMeetings;
    private List<Meeting> mMeetings;
    private List<Meeting> content;
    private ProgressBar loadingSpinner;
    private MeetingsListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout scrollView;
    private int mode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meetings, container, false);
    }
    @Override
    public void onStart(){
        super.onStart();
        ListView listView = (ListView) getActivity().findViewById(R.id.meetingsList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMeeting = tbpMeetings.get(position).sToString();
                Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
                intent.putExtra("data",selectedMeeting);
                intent.putExtra("type",Integer.toString(mode));
                startActivity(intent);
            }
        });
        scrollView = (LinearLayout) getActivity().findViewById(R.id.fragment_meetings_top_menu);
        loadingSpinner = (ProgressBar) getActivity().findViewById(R.id.fragment_meetings_loading_spinner);
        scrollView.getChildAt(0).setBackgroundResource(R.drawable.categorybutton_c);

        content = new ArrayList<>();
        adapter = new MeetingsListAdapter(getActivity(), content);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.fragment_meetings_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        onRefreshCallback();
                    }
                }
        );
        listView.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void notifyContentChange(){
        switch (mode){
            case 1:
                content.clear();
                content.addAll(tbpMeetings);
                break;
            case 2:
                content.clear();
                content.addAll(pMeetings);
                break;
            case 3:
                content.clear();
                content.addAll(mMeetings);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        //This method should be defined in the activity
        //and allows the communication between the fragment and the activity
        void onFragmentInteraction(Uri uri);
    }


    public void switchMeetingsType(View v){
        int tag =  Integer.parseInt((String) v.getTag());
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        if(tag != mode ){
            for(int i=0; i<viewGroup.getChildCount(); i++){
                viewGroup.getChildAt(i).setBackgroundResource(R.drawable.categorybutton);
            }
            v.setBackgroundResource(R.drawable.categorybutton_c);
            mode = tag;
            notifyContentChange();
        }
    }


    private void retrieveTBPMeetings(){
        ComManager.getInstance().retrieveGroups(new tbpMeetingsCallback());
    }
    private void retrievePMeetings(){
        ComManager.getInstance().retrievePlannedMeetings(new pMeetingsCallback());
    }
    private void retrieveMMeetings(){
        ComManager.getInstance().retrievePlannedMeetings(new mMeetingsCallback());
    }
    private void onRefreshCallback(){
        switch (mode){
            case 1:
                retrieveTBPMeetings();
                break;
            case 2:
                retrievePMeetings();
                break;
            case 3:
                retrieveMMeetings();
                break;
        }
    }
    public void initSequence(){
        if(ComManager.getInstance().isUserPlanner()){
            scrollView.getChildAt(2).setVisibility(View.VISIBLE);
        }
        mode = 1;
        retrieveTBPMeetings();
        retrievePMeetings();
        if(ComManager.getInstance().isUserPlanner()){
            retrieveMMeetings();
        }
    }
    private class tbpMeetingsCallback implements  CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Group> groupModelList) {
            List<Group> groupList = groupModelList.getModels();
            List<Meeting> meetingList = new ArrayList<>();
            Meeting currentMeeting;
            Group currentGroup;
            List<Meeting> currentMeetings;
            for (int i = 0; i < groupList.size(); i++) {
                currentGroup = groupList.get(i);
                currentMeetings = currentGroup.getMeetings();
                for(int j=0; j<currentMeetings.size(); j++){
                    currentMeeting = currentMeetings.get(j);
                    currentMeeting.setGroupName(currentGroup.getName());
                    meetingList.add(currentMeeting);
                }
            }
            tbpMeetings = meetingList;
            if(mode == 1){
                notifyContentChange();
            }
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
            loadingSpinner.setVisibility(View.GONE);
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
    private class pMeetingsCallback implements  CallOnHttpError<ModelList<Meeting>>, CallOnNext<ModelList<Meeting>>, CallOnNoHttpError<ModelList<Meeting>> {

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Meeting> meetingModelList) {
            pMeetings = meetingModelList.getModels();
            if(mode == 2){
                notifyContentChange();
            }
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }

        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
    private class mMeetingsCallback implements CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Group> groupModelList) {
            List<Group> groupList = groupModelList.getModels();
            List<Meeting> meetingList = new ArrayList<>();
            Meeting currentMeeting;
            Group currentGroup;
            List<Meeting> currentMeetings;
            for (int i = 0; i < groupList.size(); i++) {
                currentGroup = groupList.get(i);
                currentMeetings = currentGroup.getMeetings();
                for(int j=0; j<currentMeetings.size(); j++){
                    currentMeeting = currentMeetings.get(j);
                    currentMeeting.setGroupName(currentGroup.getName());
                    meetingList.add(currentMeeting);
                }
            }
            mMeetings = meetingList;
            if(mode == 3){
                notifyContentChange();
            }
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }
}
