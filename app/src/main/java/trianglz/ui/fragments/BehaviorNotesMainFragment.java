package trianglz.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;
import trianglz.ui.activities.StudentMainActivity;

/**
 * Created by Farah A. Moniem on 05/09/2019.
 */
public class BehaviorNotesMainFragment extends Fragment {

    private View rootView;
    private StudentMainActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_behavior_notes, container, false);
        activity = (StudentMainActivity) getActivity();
     //   onBackPress();
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        FragmentUtils.createFragment(getActivity().getSupportFragmentManager(), BehaviorNotesFragment.newInstance(this.getArguments()), R.id.timetable_main_container );
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            getParentFragment().getChildFragmentManager().popBackStack();
        return true ;
    }

}
