package trianglz.ui.fragments;

/**
 * file modified by gemy
 */

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

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.CustomRtlViewPager;
import trianglz.managers.SessionManager;
import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TimetableAdapter;
import trianglz.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener {


    private TimetableAdapter mSectionsPagerAdapter;
    private CustomRtlViewPager mViewPager;
    List<TimeTableSlot> tomorrowSlots;
    List<TimeTableSlot> todaySlots;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImage;
    private Student student;
    private RadioButton todayButton, tomorrowButton;
    private SegmentedGroup segmentedGroup;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimeTableFragment.
     */
    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new TimeTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Intent intent = getActivity().getIntent();
        // Bundle bundle = intent.getBundleExtra(Constants.KEY_BUNDLE);
        Bundle bundle = this.getArguments();
        tomorrowSlots = (ArrayList<TimeTableSlot>) bundle.getSerializable(Constants.KEY_TOMORROW);
        todaySlots = (ArrayList<TimeTableSlot>) bundle.getSerializable(Constants.KEY_TODAY);
        student = (Student) bundle.getSerializable(Constants.STUDENT);
    }

    private void setListeners() {
        todayButton.setOnClickListener(this);
        tomorrowButton.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    segmentedGroup.check(todayButton.getId());
                } else {
                    segmentedGroup.check(tomorrowButton.getId());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_time_table, container, false);
        mViewPager = rootView.findViewById(R.id.timetable_container);
        mViewPager.setPagingEnabled(true);
        bindViews();
        setListeners();
        if (SessionManager.getInstance().getUserType()) {
            setStudentImage();
        }
        increaseButtonsHitArea();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSectionsPagerAdapter = new TimetableAdapter(getChildFragmentManager(), tomorrowSlots, todaySlots);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setHorizontalScrollBarEnabled(false);

    }

    private void bindViews() {
        todayButton = rootView.findViewById(R.id.btn_today);
        tomorrowButton = rootView.findViewById(R.id.btn_tomorrow);
        backBtn = rootView.findViewById(R.id.back_btn);
        studentImage = rootView.findViewById(R.id.img_student);
        // radio button for segment control
        segmentedGroup = rootView.findViewById(R.id.segmented);
        segmentedGroup.check(todayButton.getId());
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            studentImage.setVisibility(View.INVISIBLE);
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_today:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.btn_tomorrow:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.back_btn:
                StudentMainActivity activity = (StudentMainActivity) getActivity();
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.onBackPressed();
                //  Objects.requireNonNull(getActivity()).onBackPressed();
                break;

        }
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

    private void setStudentImage() {
        String imageUrl = student.getAvatar();
        final String name = student.firstName + " " + student.lastName;
        final IImageLoader[] imageLoader = {new PicassoLoader()};
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader[0] = new PicassoLoader();
            imageLoader[0].loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader[0] = new PicassoLoader();
            imageLoader[0].loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
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
                            imageLoader[0] = new PicassoLoader();
                            imageLoader[0].loadImage(studentImage, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

}
