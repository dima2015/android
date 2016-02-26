package com.plunner.plunner.activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.Adapters.SchedulesListAdapter;
import com.plunner.plunner.models.models.employee.Calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class SchedulesFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private List<Calendar> composedSchedules;
    private List<Calendar> importedSchedules;
    private List<Calendar> content;
    private SchedulesListAdapter adapter;


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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onStart(){
        super.onStart();
        ListView listView = (ListView) getActivity().findViewById(R.id.schedulesList);
        content = new ArrayList<>();
        adapter = new SchedulesListAdapter(getActivity(), content);
        listView.setAdapter(adapter);



    }

    public void setComposedSchedules(List<Calendar> composedSchedules) {
        this.composedSchedules = composedSchedules;
    }

    public void setImportedSchedules(List<Calendar> importedSchedules) {
        this.importedSchedules = importedSchedules;
    }

    public void notifyContentChange(int type){
        switch (type){
            case 1:
                content.clear();
                content.addAll(composedSchedules);
                break;
            case 2:
                content.clear();
                content.addAll(importedSchedules);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
