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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.adapters.SchedulesListAdapter;
import com.plunner.plunner.activities.activities.ScheduleActivity;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.utils.DataExchanger;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment that provides the logic and the view for see the list of the composed schedules
 */
public class SchedulesListFragment extends Fragment {


    private List<Calendar> composedSchedules;
    //private List<Calendar> importedSchedules;
    private List<Calendar> content;
    private ProgressBar loadingSpinner;
    private SchedulesListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean showLoadingBar;
    private TextView emptyState;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            showLoadingBar = true;
        }
        else{
            showLoadingBar = false;
        }
        content = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        ListView listView = (ListView) getActivity().findViewById(R.id.schedulesList);
        emptyState = (TextView) getActivity().findViewById(R.id.empty_state_schedules);
        //LinearLayout horizontalScrollView = (LinearLayout) getActivity().findViewById(R.id.fragment_schedules_top_menu);
        // horizontalScrollView.getChildAt(0).setBackgroundResource(R.drawable.categorybutton_c);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.fragment_schedules_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        onRefreshCallback();
                    }
                }
        );
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.colorPrimary, R.color.colorPrimaryDark);

        adapter = new SchedulesListAdapter(getActivity(), content);
        loadingSpinner = (ProgressBar) getActivity().findViewById(R.id.fragment_schedules_laoding_spinner);
        if(showLoadingBar){
            loadingSpinner.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataExchanger.getInstance().setSchedule(composedSchedules.get(position));
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                intent.putExtra("mode", "edit");
                startActivity(intent);
            }
        });

    }

    private void onRefreshCallback() {
        retrieveSchedules(true);
    }

    public void notifyContentChange() {
        content.clear();
        content.addAll(composedSchedules);
        checkEmptyState();
        /*
        switch (mode) {
            case 1:
                content.clear();
                content.addAll(composedSchedules);
                break;
            case 2:
                content.clear();
                content.addAll(importedSchedules);
                break;
        }*/
        adapter.notifyDataSetChanged();
    }

    private void checkEmptyState() {
        if(content.size() == 0){
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
            emptyState.setVisibility(View.GONE);
        }
    }


    public void initSequence() {
        //mode = 1;
        retrieveSchedules(false);
    }

    private void retrieveSchedules(Boolean toRefresh) {
        ModelList<Calendar> calendars = (ModelList<Calendar>) DataExchanger.getInstance().getUser().getCalendars().getInstance();
        if(calendars.getModels().size() == 0 || toRefresh){
            DataExchanger.getInstance().getUser().getCalendars().load(new SchedulesCallback());
        }
        else{
            insertSchedules(calendars);
        }

    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        onRefreshCallback();
    }

    private class SchedulesCallback implements CallOnHttpError<ModelList<Calendar>>, CallOnNext<ModelList<Calendar>>, CallOnNoHttpError<ModelList<Calendar>> {
        @Override
        public void onHttpError(HttpException e) {
            LoginManager.getInstance().reLogin(e, getActivity(), null);
            String msg;
            if (e.getCause().code() == 500) {
                msg = "Internal Server Error, please try again later";

            } else {
                msg = "Communication Error, please try again later";
            }
            SchedulesListFragment.this.createSnackBar(msg);
        }

        @Override
        public void onNext(final ModelList<Calendar> calendarModelList) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    insertSchedules(calendarModelList);
                }
            });
        }

        @Override
        public void onNoHttpError(NoHttpException e) {
            checkError();
        }
    }

    private void insertSchedules(ModelList<Calendar> calendarModelList){
        List<Calendar> schedulesList = calendarModelList.getModels();
        //List<Calendar> lImportedSchedules = new ArrayList<>();
        List<Calendar> lComposedSchedules = new ArrayList<>();
        Calendar currentSchedule;
        for (int i = 0; i < schedulesList.size(); i++) {
            currentSchedule = schedulesList.get(i);
            if (currentSchedule.getCaldav() == null) {
                lComposedSchedules.add(currentSchedule);
            } else {
                // lImportedSchedules.add(currentSchedule);
            }
        }
        //importedSchedules = lImportedSchedules;
        composedSchedules = lComposedSchedules;
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        loadingSpinner.setVisibility(View.GONE);
        notifyContentChange();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        // Note: getValues() is a method in your ArrayAdapter subclass
        savedState.putInt("mode", 0);

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
