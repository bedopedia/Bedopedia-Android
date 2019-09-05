package trianglz.ui.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.CustomRtlViewPager;
import trianglz.managers.SessionManager;
import trianglz.models.Student;
import trianglz.ui.adapters.BehaviorNotesFragmentAdapter;
import trianglz.models.BehaviorNote;
import trianglz.utils.Constants;
import trianglz.utils.Util;

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
    private CustomRtlViewPager mViewPager;

    List<BehaviorNote> positiveNotesList;
    List<BehaviorNote> negativeNotesList;
    List<BehaviorNote> otherNoteList;
    private TextView positiveTv;
    private TextView negativeTv;
    private TextView otherTv;
    private View rootView;
    private ImageButton backBtn;
    private Student student;
    private AvatarView studentImage;
    private IImageLoader imageLoader;

    private SegmentedGroup segmentedGroup;

    private RadioButton positiveButton, negativeButton, otherButton;

    public BehaviorNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BehaviorNotesFragment.
     */

    public static BehaviorNotesFragment newInstance(Bundle args) {
        BehaviorNotesFragment behaviorNotesFragment = new BehaviorNotesFragment();
        behaviorNotesFragment.setArguments(args);
        return behaviorNotesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getActivity().getIntent().getExtras();
        context = getActivity();
        //   Bundle bundle = getActivity().getIntent().getBundleExtra(Constants.KEY_BUNDLE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            positiveNotesList = (List<BehaviorNote>) bundle.getSerializable(Constants.KEY_POSITIVE_NOTES_LIST);
            negativeNotesList = (List<BehaviorNote>) bundle.getSerializable(Constants.KEY_NEGATIVE_NOTES_LIST);
            otherNoteList = (List<BehaviorNote>) bundle.getSerializable(Constants.KEY_OTHER_NOTES_LIST);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            studentId = bundle.getInt(studentIdKey) + "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_behavior_notes, container, false);
        bindViews();
        setListeners();
        increaseButtonsHitArea();
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindViews() {
        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getActivity().getSupportFragmentManager(), positiveNotesList, negativeNotesList, otherNoteList, getActivity());
        mViewPager = rootView.findViewById(R.id.behavior_notes_container);
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        positiveTv = rootView.findViewById(R.id.tv_positive);
        negativeTv = rootView.findViewById(R.id.tv_negative);
        otherTv = rootView.findViewById(R.id.tv_other);
        backBtn = rootView.findViewById(R.id.back_btn);
        studentImage = rootView.findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        String name = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), name);
        setHeader();

        segmentedGroup = rootView.findViewById(R.id.segmented);
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        // radio buttons
        positiveButton = rootView.findViewById(R.id.btn_positive);
        negativeButton = rootView.findViewById(R.id.btn_negative);
        otherButton = rootView.findViewById(R.id.btn_other);
        segmentedGroup.check(positiveButton.getId());
    }

    private void setListeners() {
        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);
        otherTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        otherButton.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTextBackgrounds(0);
                } else if (position == 1) {
                    setTextBackgrounds(1);
                } else {
                    setTextBackgrounds(2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void increaseButtonsHitArea() {
        final View parent = (View) backBtn.getParent();
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backBtn.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, backBtn));
            }
        });
    }

    private void setHeader() {
        if (Util.getLocale(getActivity()).equals("ar")) {
            positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
            otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
            negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
        } else {
            setTextBackgrounds(1);
            setTextBackgrounds(0);
        }
    }

    private void setTextBackgrounds(int pageNumber) {
        if (Util.getLocale(getActivity()).equals("ar")) {
            if (pageNumber == 0) {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
                positiveTv.setTextColor(getResources().getColor(R.color.white));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
                otherTv.setTextColor(getResources().getColor(R.color.jade_green));

            } else if (pageNumber == 1) {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.solid_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.white));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
            } else {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_left_green));
                otherTv.setTextColor(getResources().getColor(R.color.white));
            }

        } else {
            if (pageNumber == 0) {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_left_green));
                positiveTv.setTextColor(getResources().getColor(R.color.white));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
                otherTv.setTextColor(getResources().getColor(R.color.jade_green));

            } else if (pageNumber == 1) {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.solid_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.white));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
            } else {
                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
                otherTv.setTextColor(getResources().getColor(R.color.white));
            }
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
            case R.id.tv_other:
                mViewPager.setCurrentItem(2);
                setTextBackgrounds(2);
                break;
            case R.id.back_btn:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            case R.id.btn_positive:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.btn_negative:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.btn_other:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }
}
