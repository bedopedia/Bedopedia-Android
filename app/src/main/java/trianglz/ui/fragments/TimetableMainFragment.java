package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;
import trianglz.ui.activities.StudentMainActivity;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class TimetableMainFragment extends Fragment {
    private View rootView;
    private StudentMainActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_time_table, container, false);
        activity = (StudentMainActivity) getActivity();
    //    onBackPress();
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        FragmentUtils.createFragment(activity.getSupportFragmentManager(), TimeTableFragment.newInstance(this.getArguments()), R.id.timetable_main_container);
        return rootView;
    }
}
