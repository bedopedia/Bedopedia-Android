package trianglz.ui.activities.fragments;

/** file modified by gemy */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.models.TimeTableSlot;
import trianglz.ui.activities.adapters.TimetableAdapter;
import trianglz.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {
    FragmentManager fragmentManager;


    private TimetableAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    List<TimeTableSlot> tomorrowSlots;
    List<TimeTableSlot> todaySlots;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TimeTableFragment.
     */
    public static Fragment newInstance() {
        Fragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.KEY_BUNDLE);
        tomorrowSlots = (ArrayList<TimeTableSlot>) bundle.getSerializable(Constants.KEY_TOMORROW);
        todaySlots = (ArrayList<TimeTableSlot> ) bundle.getSerializable(Constants.KEY_TODAY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_time_table, container, false);
        mViewPager = rootView.findViewById(R.id.timetable_container);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSectionsPagerAdapter = new TimetableAdapter(getActivity().getSupportFragmentManager(), tomorrowSlots, todaySlots);

        mViewPager.setAdapter(mSectionsPagerAdapter);

    }
}
