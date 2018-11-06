package behaviorNotes;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.List;
import java.util.Objects;

import behaviorNotes.Adapters.BehaviorNotesFragmentAdapter;
import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;
import trianglz.models.BehaviorNote;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BehaviorNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * modified by gemy
 */
public class BehaviorNotesFragment extends Fragment implements View.OnClickListener {

    String studentId;
    final String studentIdKey = "student_id";
    public static Context context;
    private BehaviorNotesFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    List<BehaviorNote> positiveNotesList;
    List<BehaviorNote> negativeNotesList;
    private TextView positiveTv;
    private TextView negativeTv;
    private View rootView;
    private ImageButton backBtn;

    public BehaviorNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BehaviorNotesFragment.
     */

    public static BehaviorNotesFragment newInstance() {
        BehaviorNotesFragment behaviorNotesFragment = new BehaviorNotesFragment();
        Bundle args = new Bundle();
        behaviorNotesFragment.setArguments(args);
        return behaviorNotesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getActivity().getIntent().getExtras();
        studentId = extras.getString(studentIdKey);
        context = getActivity();
        Bundle bundle = getActivity().getIntent().getExtras();
        positiveNotesList = (List<BehaviorNote>) bundle.getSerializable(PositiveFragment.KEY_NAME);
        negativeNotesList = (List<BehaviorNote>) bundle.getSerializable(NegativeFragment.KEY_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_behavior_notes, container, false);
        bindViews();
        setListeners();
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindViews() {
        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getActivity().getSupportFragmentManager(), positiveNotesList, negativeNotesList, getActivity());
        mViewPager = rootView.findViewById(R.id.behavior_notes_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        positiveTv = rootView.findViewById(R.id.tv_positive);
        negativeTv = rootView.findViewById(R.id.tv_negative);
        backBtn = rootView.findViewById(R.id.back_btn);
    }

    private void setListeners() {
        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTextBackgrounds(0);
                } else {
                    setTextBackgrounds(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTextBackgrounds(int pageNumber) {
        if (pageNumber == 0) {
            positiveTv.setBackground(getResources().getDrawable(R.drawable.text_solid_background));
            positiveTv.setTextColor(getResources().getColor(R.color.white));
            negativeTv.setBackground(rootView.getBackground());
            negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
        } else {
            positiveTv.setBackground(rootView.getBackground());
            negativeTv.setBackground(getResources().getDrawable(R.drawable.text_solid_background));
            negativeTv.setTextColor(getResources().getColor(R.color.white));
            positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_positive:
                mViewPager.setCurrentItem(0);
                setTextBackgrounds(0);
                break;
            case R.id.tv_negative:
                mViewPager.setCurrentItem(1);
                setTextBackgrounds(1);
                break;
            case R.id.back_btn:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;

        }
    }
}
