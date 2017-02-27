package Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bedopedia.bedopedia_android.AttendanceActivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Adapters.AbsentLateAdapter;
import Models.Attendance;


/**
 * Created by khaled on 2/21/17.
 */

public class AbsentFragment  extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.absent_fragment, container, false);

        List<Attendance> dates= AttendanceActivity.absentDates;
        ListView absentAttendaceList = (ListView) rootView.findViewById(R.id.absent_attendace);
        AbsentLateAdapter adapter = new AbsentLateAdapter(getActivity(), R.layout.single_attendance, dates);
        absentAttendaceList.setAdapter(adapter);

        return rootView;
    }
}
