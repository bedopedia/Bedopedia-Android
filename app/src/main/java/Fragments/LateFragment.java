package Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bedopedia.bedopedia_android.AttendanceActivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapters.AbsentLateAdapter;

/**
 * Created by khaled on 2/21/17.
 */

public class LateFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.late_fragment, container, false);

        List<Date> dates= AttendanceActivity.lateDates;
        ListView lateAttendaceList = (ListView) rootView.findViewById(R.id.late_attendace);
        AbsentLateAdapter adapter = new AbsentLateAdapter(getActivity(), R.layout.single_attendance, dates);
        lateAttendaceList.setAdapter(adapter);

        return rootView;
    }
}
