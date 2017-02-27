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
 * Created by khaled on 2/27/17.
 */

public class ExcusedFragment  extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.excused_fragment, container, false);

        List<Attendance> dates= AttendanceActivity.excusedDates;
        ListView excusedAttendaceList = (ListView) rootView.findViewById(R.id.excused_attendace);
        AbsentLateAdapter adapter = new AbsentLateAdapter(getActivity(), R.layout.single_attendance, dates);
        excusedAttendaceList.setAdapter(adapter);

        return rootView;
    }
}
