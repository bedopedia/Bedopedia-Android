package trianglz.ui.fragments;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.CustomRtlViewPager;
import trianglz.models.BehaviorNote;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.BehaviorNotesFragmentAdapter;
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
    private View rootView;
    private ImageButton backBtn;
    private Student student;
    private AvatarView studentImage;
    private IImageLoader imageLoader;
    private TabLayout tabLayout;

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
        context = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            positiveNotesList = bundle.getParcelableArrayList(Constants.KEY_POSITIVE_NOTES_LIST);
            negativeNotesList = bundle.getParcelableArrayList(Constants.KEY_NEGATIVE_NOTES_LIST);
            otherNoteList = bundle.getParcelableArrayList(Constants.KEY_OTHER_NOTES_LIST);
            student = bundle.getParcelable(Constants.STUDENT);
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
        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getChildFragmentManager(), positiveNotesList, negativeNotesList, otherNoteList, getActivity());
        mViewPager = rootView.findViewById(R.id.behavior_notes_container);
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        backBtn = rootView.findViewById(R.id.back_btn);
        studentImage = rootView.findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        String name = student.firstName + " " + student.lastName;
        setStudentImage(student.avatar, name);
        //setHeader();

        tabLayout = rootView.findViewById(R.id.tab_layout);

        tabLayout.setSelectedTabIndicatorColor(getActivity().getResources().getColor(Util.checkUserColor()));
        tabLayout.setTabTextColors(getActivity().getResources().getColor(R.color.steel), getActivity().getResources().getColor(Util.checkUserColor()));

    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        tabLayout.setupWithViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {
//                    setTextBackgrounds(0);
//                } else if (position == 1) {
//                    setTextBackgrounds(1);
//                } else {
//                    setTextBackgrounds(2);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
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

//    private void setHeader() {
//        if (Util.getLocale(getActivity()).equals("ar")) {
//            positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
//            otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
//            negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
//        } else {
//            setTextBackgrounds(1);
//            setTextBackgrounds(0);
//        }
//    }

//    private void setTextBackgrounds(int pageNumber) {
//        if (Util.getLocale(getActivity()).equals("ar")) {
//            if (pageNumber == 0) {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.white));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
//                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
//
//            } else if (pageNumber == 1) {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.solid_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.white));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
//                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
//            } else {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_left_green));
//                otherTv.setTextColor(getResources().getColor(R.color.white));
//            }
//
//        } else {
//            if (pageNumber == 0) {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_left_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.white));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
//                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
//
//            } else if (pageNumber == 1) {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.solid_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.white));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_right_green));
//                otherTv.setTextColor(getResources().getColor(R.color.jade_green));
//            } else {
//                positiveTv.setBackground(getResources().getDrawable(R.drawable.curved_stroke_left_green));
//                positiveTv.setTextColor(getResources().getColor(R.color.jade_green));
//                negativeTv.setBackground(getResources().getDrawable(R.drawable.stroke_green_background));
//                negativeTv.setTextColor(getResources().getColor(R.color.jade_green));
//                otherTv.setBackground(getResources().getDrawable(R.drawable.curved_solid_right_green));
//                otherTv.setTextColor(getResources().getColor(R.color.white));
//            }
//        }
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                StudentMainActivity activity = (StudentMainActivity) getActivity();
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.onBackPressed();
                //    Objects.requireNonNull(getActivity()).onBackPressed();
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
