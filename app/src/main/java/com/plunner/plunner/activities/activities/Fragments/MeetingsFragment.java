package com.plunner.plunner.activities.activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.plunner.plunner.activities.activities.Adapters.MeetingsListAdapter;

import com.plunner.plunner.R;

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
        List<String> strings = new ArrayList<>();
        ListView listView = (ListView) getActivity().findViewById(R.id.meetingsList);
        for(int i=0; i<5; i++){
            strings.add("String "+i);
        }
        MeetingsListAdapter listAdapter = new MeetingsListAdapter(getActivity(), strings);
        listView.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.meetingsFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());
            }
        });



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
}
