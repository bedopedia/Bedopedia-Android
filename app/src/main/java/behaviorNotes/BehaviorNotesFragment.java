package behaviorNotes;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import behaviorNotes.Adapters.BehaviorNotesFragmentAdapter;
import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BehaviorNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorNotesFragment extends Fragment {

    String studentId, id;
    String studentIdKey = "student_id";
    public static Context context;
    TabLayout tabLayout;
    private BehaviorNotesFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    TextView positiveTitle;
    TextView positiveCounter;
    TextView negativeTitle;
    TextView negativeCounter;
    View positiveTab;
    View negativeTab;
    List<BehaviorNote> positiveNotesList;
    List<BehaviorNote> negativeNotesList;

    public BehaviorNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BehaviorNotesFragment.
     */

    public static BehaviorNotesFragment newInstance() {
        BehaviorNotesFragment fragment = new BehaviorNotesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= this.getActivity().getIntent().getExtras();
        studentId = extras.getString(studentIdKey);

        context = getActivity();
        Intent intent = this.getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        positiveNotesList = (List<BehaviorNote>) bundle.getSerializable(PositiveFragment.KEY_NAME);
        negativeNotesList = (List<BehaviorNote>) bundle.getSerializable(NegativeFragment.KEY_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_behavior_notes, container, false);




        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getActivity().getSupportFragmentManager(), positiveNotesList, negativeNotesList,getActivity());

        mViewPager = (ViewPager) view.findViewById(R.id.behavior_notes_container);

        tabLayout = (TabLayout) view.findViewById(R.id.behavior_notes_tabs);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        positiveTab = mSectionsPagerAdapter.getTabView(0);
        negativeTab = mSectionsPagerAdapter.getTabView(1);
        tabLayout.getTabAt(0).setCustomView(positiveTab);
        tabLayout.getTabAt(1).setCustomView(negativeTab);

        positiveTitle = (TextView) positiveTab.findViewById(R.id.tab_title);
        positiveCounter = (TextView) positiveTab.findViewById(R.id.behavior_notes_counter);
        positiveTitle.setText(R.string.positiveBehaviorNotes);


        negativeTitle = (TextView) negativeTab.findViewById(R.id.tab_title);
        negativeCounter = (TextView) negativeTab.findViewById(R.id.behavior_notes_counter);
        negativeTitle.setText(R.string.negativeBehaviorNotes);
        positiveTitle.setTextColor(Color.parseColor("#ffffff"));
        positiveCounter.setBackgroundResource(R.drawable.notes_selected_counter);

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
    }
}
