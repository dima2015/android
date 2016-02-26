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
import com.plunner.plunner.activities.Adapters.MeetingsListAdapter;
import com.plunner.plunner.models.models.employee.Meeting;

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
    private List<Meeting> mMetings;
    private List<Meeting> content;
    private MeetingsListAdapter adapter;
    private int lastEditType;


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
        content = new ArrayList<>();
        adapter = new MeetingsListAdapter(getActivity(), content);
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

    public void setTbpMeetings(List<Meeting> meetingList){
        tbpMeetings = meetingList;
    }

    public void setpMeetings(List<Meeting> pMeetings) {
        this.pMeetings = pMeetings;
    }

    public void setmMetings(List<Meeting> mMetings) {
        this.mMetings = mMetings;
    }
    public void notifyContentChange(int type){
        switch (type){
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
                content.addAll(mMetings);
                break;
        }
        adapter.notifyDataSetChanged();
    }
    public boolean isContentEmpty(int type){
        boolean returnResult;
        switch (type){
            case 1:
                returnResult = (tbpMeetings == null);
                break;
            case 2:
                returnResult = (pMeetings == null);
                break;
            case 3:
                returnResult = (mMetings == null);
                break;
            default:
                returnResult = false;
        }
        return returnResult;
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
