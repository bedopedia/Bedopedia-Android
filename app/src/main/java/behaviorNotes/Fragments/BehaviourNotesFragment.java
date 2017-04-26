package behaviorNotes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.io.Serializable;
import java.util.List;

import attendance.AttendanceFragment;
import behaviorNotes.Adapters.BehaviorNotesFragmentAdapter;
import behaviorNotes.BehaviorNote;


public class BehaviourNotesFragment extends Fragment {

    String studentId, id;
    public static Context context;
    TabLayout tabLayout;
    private BehaviorNotesFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    TextView positiveTitle;
    TextView positiveCounter;
    TextView negativeTitle;
    TextView negativeCounter;



    List<BehaviorNote> positiveNotesList ;
    List<BehaviorNote> negativeNotesList ;






    public BehaviourNotesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            id = getArguments().getString("student_id");
            positiveNotesList = (List<BehaviorNote>) getArguments().getSerializable(PositiveFragment.KEY_NAME);
            negativeNotesList = (List<BehaviorNote>) getArguments().getSerializable(NegativeFragment.KEY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_behaviour_notes,container,false);
        mViewPager = (ViewPager) view.findViewById(R.id.behavior_notes_container);
        tabLayout = (TabLayout) view.findViewById(R.id.behavior_notes_tabs);
        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getActivity().getSupportFragmentManager(), positiveNotesList, negativeNotesList);
        View positiveTab = mSectionsPagerAdapter.getTabView(0);
        View negativeTab = mSectionsPagerAdapter.getTabView(1);
        positiveCounter = (TextView) positiveTab.findViewById(R.id.tab_counter);



        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


        tabLayout.getTabAt(0).setCustomView(positiveTab);
        tabLayout.getTabAt(1).setCustomView(negativeTab);


        negativeTitle = (TextView) negativeTab.findViewById(R.id.tab_title);
        negativeCounter = (TextView) negativeTab.findViewById(R.id.tab_counter);
        positiveTitle.setTextColor(Color.parseColor("#ffffff"));
        positiveCounter.setBackgroundResource(R.drawable.notes_selected_counter);
        positiveTitle = (TextView) positiveTab.findViewById(R.id.tab_title);


        negativeTitle.setTextColor(Color.parseColor("#b3ffffff"));
        negativeCounter.setBackgroundResource(R.drawable.notes_unselected_counter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    positiveTitle.setTextColor(Color.parseColor("#ffffff"));
                    positiveCounter.setBackgroundResource(R.drawable.notes_selected_counter);

                    negativeTitle.setTextColor(Color.parseColor("#b3ffffff"));
                    negativeCounter.setBackgroundResource(R.drawable.notes_unselected_counter);
                } else{
                    positiveTitle.setTextColor(Color.parseColor("#b3ffffff"));
                    positiveCounter.setBackgroundResource(R.drawable.notes_unselected_counter);

                    negativeTitle.setTextColor(Color.parseColor("#ffffff"));
                    negativeCounter.setBackgroundResource(R.drawable.notes_selected_counter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return  view ;
    }






    public static Fragment newInstance(String student_id , Serializable positiveNotesList , Serializable negativeNotesList) {
        BehaviourNotesFragment fragment = new BehaviourNotesFragment();
        Bundle args = new Bundle();
        args.putString("student_id", student_id);
        args.putSerializable(PositiveFragment.KEY_NAME , (Serializable) positiveNotesList);
        args.putSerializable(NegativeFragment.KEY_NAME , (Serializable) negativeNotesList);

        fragment.setArguments(args);
        return fragment;
    }



}
