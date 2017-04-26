package timetable;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bedopedia.bedopedia_android.R;

import java.io.Serializable;
import java.util.List;

import timetable.Fragments.TomorrowFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {
    FragmentManager fragmentManager;


    private TimetableAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_time_table, container, false);


        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        List<TimetableSlot> tomorrowSlots = ( List<TimetableSlot> ) bundle.getSerializable(TomorrowFragment.KEY_NAME);
        List<TimetableSlot> todaySlots = ( List<TimetableSlot> ) bundle.getSerializable(TomorrowFragment.KEY_NAME);
        mSectionsPagerAdapter = new TimetableAdapter(getActivity().getSupportFragmentManager(), tomorrowSlots, todaySlots);

        mViewPager = (ViewPager) rootView.findViewById(R.id.timetable_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.timetable_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

}
