package com.plunner.plunner.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.MeetingActivity;
import com.plunner.plunner.activities.activities.MeetingDetailActivity;
import com.plunner.plunner.activities.adapters.MeetingsListAdapter;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Group;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.utils.DataExchanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A fragments that provides logic and view for see a list of the meetings planned, to be planned and managed
 */
public class MeetingsListFragment extends Fragment {

    private List<Meeting> tbpMeetings;
    private List<Meeting> pMeetings;
    private List<? extends Meeting> mMeetings;
    private List<Meeting> content;
    private ProgressBar loadingSpinner;
    private MeetingsListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout scrollView;
    private int mode;
    private boolean showLoadBar;
    private TextView emptyStateTBP;
    private TextView emptyStateP;
    private TextView emptyStateM;
    private Map<String, String> groupIdToName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt("mode");
            showLoadBar = true;
        } else {
            mode = 1;
            showLoadBar = false;
        }
        mMeetings = new ArrayList<>();
        content = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meetings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //List view that contains the meetings
        ListView listView = (ListView) getActivity().findViewById(R.id.fragment_meetings_meetings_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        meetingClickedCallback(position);
                    }
                });

            }
        });
        //Empty state messages set
        emptyStateTBP = (TextView) getActivity().findViewById(R.id.empty_state_tbp_meetings);
        emptyStateP = (TextView) getActivity().findViewById(R.id.empty_state_p_meetings);
        emptyStateM = (TextView) getActivity().findViewById(R.id.empty_state_m_meetings);
        //
        scrollView = (LinearLayout) getActivity().findViewById(R.id.fragment_meetings_top_menu);
        loadingSpinner = (ProgressBar) getActivity().findViewById(R.id.fragment_meetings_loading_spinner);
        scrollView.getChildAt(mode - 1).setBackgroundResource(R.drawable.categorybutton_c);
        if (showLoadBar) {
            loadingSpinner.setVisibility(View.VISIBLE);
        }
        groupIdToName = new HashMap<>();
        adapter = new MeetingsListAdapter(getActivity(), content);
        //SwipeRefreshLayout setting
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.fragment_meetings_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.colorPrimary, R.color.colorPrimaryDark);
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

    /**
     * Invoked when a user clicks on an item in the list of meetings
     * @param position The position of the clicked item
     */
    private void meetingClickedCallback(int position) {
        boolean isMeetingManaged;
        Meeting selectedMeeting;
        Intent intent;

        switch (mode) {
            case 1:
                selectedMeeting = tbpMeetings.get(position);
                isMeetingManaged = false;
                break;
            case 2:
                selectedMeeting = pMeetings.get(position);
                isMeetingManaged = false;
                break;
            case 3:
                selectedMeeting = mMeetings.get(position);
                isMeetingManaged = true;
                break;
            default:
                selectedMeeting = null;
                isMeetingManaged = false;
                break;
        }
        //sets meeting object to exchange
        DataExchanger.getInstance().setMeeting(selectedMeeting);

        //Checks which activity invoke
        if (isMeetingManaged) {
            intent = new Intent(getActivity(), MeetingActivity.class);
            intent.putExtra("dummy_extra", "");
        } else {
            intent = new Intent(getActivity(), MeetingDetailActivity.class);
        }
        startActivity(intent);
    }

    /**
     * Updates the content of the list of meetings in relation with {@link #mode}
     */
    public void notifyContentChange() {
        content.clear();
        switch (mode) {
            case 1:
                content.addAll(tbpMeetings);
                break;
            case 2:
                content.addAll(pMeetings);
                break;
            case 3:
                content.addAll(mMeetings);
                break;
        }
        checkEmptyState();
        adapter.notifyDataSetChanged();

    }

    /**
     * Checks if there's the necessity to show an empty state msg
     */
    private void checkEmptyState(){
        emptyStateTBP.setVisibility(View.GONE);
        emptyStateP.setVisibility(View.GONE);
        emptyStateM.setVisibility(View.GONE);
        if(content.size() == 0){

            switch (mode) {
                case 1:
                    emptyStateTBP.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    emptyStateP.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    emptyStateM.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    /**
     * Switches the type of meetings displatyed(to be planned, planned, managed)
     * @param v The button that triggered this switch
     */
    public void switchMeetingsType(View v) {
        int tag = Integer.parseInt((String) v.getTag());
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        if (tag != mode) {
            //Buttons background change
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setBackgroundResource(R.drawable.categorybutton);
            }
            v.setBackgroundResource(R.drawable.categorybutton_c);
            mode = tag;
            notifyContentChange();
        }
    }

    /**
     * Retrieves the to be planned meetings associated with the user
     * @param toRefresh A flag that indicates if there's the necessity to force a new load of these meetings
     */
    private void retrieveTBPMeetings(Boolean toRefresh) {
        ModelList<Group> groups = (ModelList<Group>) DataExchanger.getInstance().getUser().getGroups().getInstance();
        if(groups.getModels().size() == 0 || toRefresh){
            DataExchanger.getInstance().getUser().getGroups().load(new tbpMeetingsCallback());
        }
        else{
            insertTBPMeetings(groups);
        }

    }
    /**
     * Retrieves the planned meetings associated with the user
     * @param toRefresh A flag that indicates if there's the necessity to force a new load of these meetings
     */
    private void retrievePMeetings(Boolean toRefresh) {
        ModelList<Meeting> meetings = (ModelList<Meeting>) DataExchanger.getInstance().getUser().getMeetings().getInstance();
        if( meetings.getModels().size() == 0 || toRefresh){
            DataExchanger.getInstance().getUser().getMeetings().load(new pMeetingsCallback());
        }
        else{
            insertPMeetings(meetings);
        }

    }
    /**
     * Retrieves the managed meetings associated with the user
     * @param toRefresh A flag that indicates if there's the necessity to force a new load of these meetings
     */
    private void retrieveMMeetings(Boolean toRefresh) {
        ModelList<com.plunner.plunner.models.models.employee.planner.Group> groups = ((Planner) DataExchanger.getInstance().getUser()).getGroupsManaged().getInstance();
        if( groups.getModels().size() == 0 || toRefresh){
            ((Planner) DataExchanger.getInstance().getUser()).getGroupsManaged().load(new mMeetingsCallback());
        }
        else{
            elaborateManagedGroups(groups);
        }

    }

    /**
     * Public method to refresh the list of meetings
     */
    public void refresh(){
        swipeRefreshLayout.setRefreshing(true);
        onRefreshCallback();
    }

    /**
     * Callback to swipe down refresh
     */
    private void onRefreshCallback() {
        switch (mode) {
            case 1:
                retrieveTBPMeetings(true);
                break;
            case 2:
                retrievePMeetings(true);
                break;
            case 3:
                retrieveMMeetings(true);
                break;
        }
    }

    /**
     * Initial set of operations for this fragment
     */
    public void initSequence() {
        //mode = 1;
        retrieveTBPMeetings(false);
        retrievePMeetings(false);

        if (DataExchanger.getInstance().getUser().isPlanner()) {
            scrollView.getChildAt(2).setVisibility(View.VISIBLE);
            retrieveMMeetings(false);
        }
    }

    /**
     * Callback to {@link #retrieveTBPMeetings(Boolean)}
     */
    private class tbpMeetingsCallback implements CallOnHttpError<ModelList<Group>>, CallOnNext<ModelList<Group>>, CallOnNoHttpError<ModelList<Group>> {

        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(final ModelList<Group> groupModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertTBPMeetings(groupModelList);
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {

        }
    }

    private void insertTBPMeetings(ModelList<Group> groupModelList) {
        List<Group> groupList = groupModelList.getModels();
        List<Meeting> meetingList = new ArrayList<>();
        Meeting currentMeeting;
        Group currentGroup;
        List<Meeting> currentMeetings;
        for (int i = 0; i < groupList.size(); i++) {
            currentGroup = groupList.get(i);
            groupIdToName.put(currentGroup.getId(),currentGroup.getName());
            currentMeetings = currentGroup.getMeetings();
            for (int j = 0; j < currentMeetings.size(); j++) {
                currentMeeting = currentMeetings.get(j);
                currentMeeting.setGroupName(currentGroup.getName());
                meetingList.add(currentMeeting);
            }
        }
        tbpMeetings = meetingList;
        if (mode == 1) {
            notifyContentChange();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        loadingSpinner.setVisibility(View.GONE);
    }
    /**
     * Callback to {@link #retrievePMeetings(Boolean)}
     */
    private class pMeetingsCallback implements CallOnHttpError<ModelList<Meeting>>, CallOnNext<ModelList<Meeting>>, CallOnNoHttpError<ModelList<Meeting>> {

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, getActivity(), null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingsListFragment.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<Meeting> meetingModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertPMeetings(meetingModelList);

                }
            });


        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    private void insertPMeetings(ModelList<Meeting> meetingModelList) {
        pMeetings = meetingModelList.getModels();
        for(int i=0; i<pMeetings.size(); i++){
            pMeetings.get(i).setGroupName(groupIdToName.get(pMeetings.get(i).getGroupId()));
        }
        if (mode == 2) {
            notifyContentChange();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private void elaborateManagedGroups(ModelList<com.plunner.plunner.models.models.employee.planner.Group> groupModelList){
        List<com.plunner.plunner.models.models.employee.planner.Group> groupList = groupModelList.getModels();
        com.plunner.plunner.models.models.employee.planner.Group currentGroup;
        for (int i = 0; i < groupList.size(); i++) {
            currentGroup = groupList.get(i);
            if(currentGroup.getMeetingsManaged().getInstance().getModels().size() == 0){
                currentGroup.getMeetingsManaged().load(new MeetingsManagedCallabck());
            }
            else{
                insertManagedMeetings(currentGroup.getMeetingsManaged().getInstance());
            }

        }
    }
    /**
     * Callback to {@link #retrieveMMeetings(Boolean)}
     */
    private class mMeetingsCallback implements CallOnHttpError<ModelList<com.plunner.plunner.models.models.employee.planner.Group>>, CallOnNext<ModelList<com.plunner.plunner.models.models.employee.planner.Group>>, CallOnNoHttpError<ModelList<com.plunner.plunner.models.models.employee.planner.Group>> {

        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, getActivity(), null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingsListFragment.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<com.plunner.plunner.models.models.employee.planner.Group> groupModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    elaborateManagedGroups(groupModelList);
                }
            });


        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }

    }

    private void insertManagedMeetings(ModelList<com.plunner.plunner.models.models.employee.planner.Meeting> meetingsList){
        mMeetings = meetingsList.getModels();
        if (mode == 3) {
            notifyContentChange();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    /**
     * Callback to {@link #elaborateManagedGroups(ModelList)}
     */
    private class MeetingsManagedCallabck implements CallOnHttpError<ModelList<com.plunner.plunner.models.models.employee.planner.Meeting>>, CallOnNext<ModelList<com.plunner.plunner.models.models.employee.planner.Meeting>>, CallOnNoHttpError<ModelList<com.plunner.plunner.models.models.employee.planner.Meeting>> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, getActivity(), null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            MeetingsListFragment.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<com.plunner.plunner.models.models.employee.planner.Meeting> meetingsList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertManagedMeetings(meetingsList);
                }
            });


        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        // Note: getValues() is a method in your ArrayAdapter subclass
        savedState.putInt("mode", mode);

    }

    /**
     * Checks the kind of non http error occured(check if it is caused by the absence of network)
     */
    private void checkError() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        String msg;
        if (!isConnected) {
            msg = "No network";
        } else {
            msg = "Communication error, please try again later";
        }
        createSnackBar(msg);
    }

    /**
     * Creates an alert snackbar with the given message(the created snackbar has a red background)
     *
     * @param message The message to be displayed in the created snackbar
     */
    private void createSnackBar(String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(getActivity().findViewById(R.id.dashboard_activity_root), message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        snackbar.show();
    }
}
