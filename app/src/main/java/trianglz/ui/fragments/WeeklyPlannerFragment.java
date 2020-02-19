package trianglz.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Day;
import trianglz.models.GeneralNote;
import trianglz.models.PlannerSubject;
import trianglz.models.Student;
import trianglz.models.WeeklyPlannerResponse;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.WeeklyPlannerAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class WeeklyPlannerFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private StudentMainActivity activity;
    public Student student;
    private SmartTabLayout tabLayout;
    private RtlViewPager viewPager;
    private WeeklyPlannerAdapter adapter;
    private DayFragment fragment;
    private WeeklyPlannerResponse weeklyPlannerResponse;
    private HashMap<String, ArrayList<PlannerSubject>> dailyNoteHashMap;
    private TextView weeklyNoteHeaderTextView, weeklyNoteContentTextView;
    private ImageView weeklyNoteImageView;
    private AvatarView studentImageView;
    private LinearLayout weeklyNoteLinearLayout;
    private IImageLoader imageLoader;
    private ImageButton backButton;
    private Button seeMoreButton;
    private FrameLayout placeholderFrameLayout, pagerFramLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_weekly_planner, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        //     onBackPress();
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            weeklyPlannerResponse = (WeeklyPlannerResponse) bundle.getSerializable(Constants.KEY_WEEKLY_PLANER);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            this.dailyNoteHashMap = getDaysOfDailyNotes(weeklyPlannerResponse);
            imageLoader = new PicassoLoader();
        }

    }

    private void bindViews() {
        activity.headerLayout.setVisibility(View.GONE);
        activity.toolbarView.setVisibility(View.GONE);
        studentImageView = rootView.findViewById(R.id.img_student);
        if (student != null) {
            setStudentImage();
        }
        tabLayout = rootView.findViewById(R.id.tab_layout);
        viewPager = rootView.findViewById(R.id.viewpager);
        adapter = new WeeklyPlannerAdapter(getChildFragmentManager(), activity);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);
        ArrayList<String> daysNameArrayList = getDaysNameArrayList();
        adapter.addFragmentArrayList(getFragmentList(), daysNameArrayList);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            TextView vgTab = (TextView) vg.getChildAt(j);
            Typeface typeface = ResourcesCompat.getFont(activity, R.font.sfui_semibold);
            vgTab.setTypeface(Typeface.DEFAULT);
            vgTab.setTypeface(typeface);
        }
        weeklyNoteContentTextView = rootView.findViewById(R.id.tv_content_weekly_note);
        weeklyNoteHeaderTextView = rootView.findViewById(R.id.tv_header_weekly_note);
        weeklyNoteImageView = rootView.findViewById(R.id.img_weekly_note);
        weeklyNoteLinearLayout = rootView.findViewById(R.id.layout_weekly_note);
        pagerFramLayout = rootView.findViewById(R.id.pager_frame_layout);
        if (weeklyPlannerResponse != null && weeklyPlannerResponse.weeklyPlans.size() > 0 &&
                weeklyPlannerResponse.weeklyPlans.get(0).dailyNotes != null &&
                weeklyPlannerResponse.weeklyPlans.get(0).dailyNotes.days.size() != 0) {
            hidePlaceHolder();
        } else {
            showPlaceHolder();
        }


        if (weeklyPlannerResponse != null) {
            if (weeklyPlannerResponse.weeklyPlans.size() > 0) {
                GeneralNote generalNote = weeklyPlannerResponse.weeklyPlans.get(0).generalNote;
                if (generalNote != null) {
                    weeklyNoteHeaderTextView.setText(Html.fromHtml(generalNote.title));
                    weeklyNoteContentTextView.setText(Html.fromHtml(generalNote.description));
                    if (generalNote.image.url != null && !generalNote.image.url.isEmpty() && !generalNote.image.url.equals("null")) {
                        weeklyNoteImageView.setVisibility(View.VISIBLE);
                        Picasso.with(activity)
                                .load(generalNote.image.url)
                                .centerCrop()
                                .fit()
                                .into(weeklyNoteImageView);
                    } else {
                        weeklyNoteImageView.setVisibility(View.GONE);
                    }
                } else {
                    weeklyNoteLinearLayout.setVisibility(View.GONE);
                }
            } else {
                weeklyNoteLinearLayout.setVisibility(View.GONE);
            }
        } else {
            weeklyNoteLinearLayout.setVisibility(View.GONE);
        }
        backButton = rootView.findViewById(R.id.btn_back);
        seeMoreButton = rootView.findViewById(R.id.btn_see_more);
    }

    private void hidePlaceHolder() {
        pagerFramLayout.setVisibility(View.VISIBLE);
        weeklyNoteLinearLayout.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        placeholderFrameLayout.setVisibility(View.GONE);
    }

    private void showPlaceHolder() {
        pagerFramLayout.setVisibility(View.GONE);
        weeklyNoteLinearLayout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        placeholderFrameLayout.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        seeMoreButton.setOnClickListener(this);
    }

    private ArrayList<Fragment> getFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_saturday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_saturday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_saturday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_sunday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_sunday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_sunday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_monday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_monday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_monday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_tuesday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_tuesday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_tuesday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_wednesday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_wednesday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_wednesday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_thursday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_thursday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_thursday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_friday))) {
            Day day = new Day();
            day.day = getResources().getString(R.string.weekly_friday);
            day.plannerSubjectArrayList = dailyNoteHashMap.get(getResources().getString(R.string.weekly_friday));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        return fragmentArrayList;
    }

    private ArrayList<String> getDaysNameArrayList() {
        ArrayList<String> daysNameStringArrayList = new ArrayList<>();
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_saturday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_saturday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_sunday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_sunday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_monday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_monday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_tuesday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_tuesday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_wednesday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_wednesday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_thursday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_thursday));
        }
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_friday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_friday));
        }


        return daysNameStringArrayList;
    }


    private HashMap<String, ArrayList<PlannerSubject>> getDaysOfDailyNotes(WeeklyPlannerResponse weeklyPlannerResponse) {
        HashMap<String, ArrayList<PlannerSubject>> dailyNoteHashMap = new HashMap<>();
        if (weeklyPlannerResponse != null) {
            if (weeklyPlannerResponse.weeklyPlans.size() > 0) {
                if (weeklyPlannerResponse.weeklyPlans.get(0).dailyNotes == null ||
                        weeklyPlannerResponse.weeklyPlans.get(0).dailyNotes.days.size() == 0) {
                    return dailyNoteHashMap;
                }
                ArrayList<Day> days = weeklyPlannerResponse.weeklyPlans.get(0).dailyNotes.days;
                for (int i = 0; i < days.size(); i++) {
                    String dayName = days.get(i).day;
                    dailyNoteHashMap.put(dayName, days.get(i).plannerSubjectArrayList);
                }
            }
        }
        return dailyNoteHashMap;
    }

    private void setStudentImage() {
        final String imageUrl = student.getAvatar();
        final String name = student.firstName + " " + student.lastName;
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    private void openWeeklyNoteActivity() {
        AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
        Bundle bundle = new Bundle();
        GeneralNote generalNote = weeklyPlannerResponse.weeklyPlans.get(0).generalNote;
        bundle.putSerializable(Constants.KEY_WEEKLY_NOTE, generalNote);
        announcementDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, announcementDetailFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.toolbarView.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.btn_see_more:
                openWeeklyNoteActivity();
                break;
        }
    }
}
