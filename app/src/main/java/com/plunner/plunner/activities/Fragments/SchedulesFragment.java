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
import com.plunner.plunner.activities.Adapters.SchedulesListAdapter;
import com.plunner.plunner.activities.activities.ComposeScheduleActivity;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.utils.ComManager;

import java.util.ArrayList;
import java.util.List;


/**
 * REVISED
 */
public class SchedulesFragment extends Fragment {


    private List<Calendar> composedSchedules;
    //private List<Calendar> importedSchedules;
    private List<Calendar> content;
    private ProgressBar loadingSpinner;
    private SchedulesListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    int mode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        content = new ArrayList<>();
        adapter = new SchedulesListAdapter(getActivity(), content);
        loadingSpinner = (ProgressBar) getActivity().findViewById(R.id.fragment_schedules_laoding_spinner);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComManager.getInstance().setExchangeSchedule(composedSchedules.get(position));
                Intent intent = new Intent(getActivity(), ComposeScheduleActivity.class);
                intent.putExtra("mode", "edit");
                startActivity(intent);
            }
        });

    }

    private void onRefreshCallback() {
        retrieveSchedules();
    }

    public void notifyContentChange() {
        content.clear();
        content.addAll(composedSchedules);
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

    public void switchSchedulesType(View v) {
        int tag = Integer.parseInt((String) v.getTag());
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        if (tag != mode) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setBackgroundResource(R.drawable.categorybutton);
            }
            v.setBackgroundResource(R.drawable.categorybutton_c);
            setMode(tag);
            notifyContentChange();
        }
    }

    public void setMode(int mode) throws IllegalArgumentException {
        if (mode == 1 || mode == 2) {
            this.mode = mode;
        } else {
            throw new IllegalArgumentException("mode must be either 1 or 2");
        }
    }

    public void initSequence() {
        //mode = 1;
        retrieveSchedules();
    }

    private void retrieveSchedules() {
        ComManager.getInstance().retrieveSchedules(new SchedulesCallback());
    }

    private class SchedulesCallback implements CallOnHttpError<ModelList<Calendar>>, CallOnNext<ModelList<Calendar>>, CallOnNoHttpError<ModelList<Calendar>> {
        @Override
        public void onHttpError(HttpException e) {

        }

        @Override
        public void onNext(ModelList<Calendar> calendarModelList) {
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
        public void onNoHttpError(NoHttpException e) {

        }
    }
}
