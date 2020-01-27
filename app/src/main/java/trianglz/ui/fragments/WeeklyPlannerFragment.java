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
import trianglz.components.CustomRtlViewPager;
import trianglz.models.DailyNote;
import trianglz.models.Day;
import trianglz.models.RootClass;
import trianglz.models.Student;
import trianglz.models.WeeklyNote;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.WeeklyPlannerAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class WeeklyPlannerFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private StudentMainActivity activity;
    public Student student;
    private SmartTabLayout tabLayout;
    private CustomRtlViewPager viewPager;
    private WeeklyPlannerAdapter adapter;
    private DayFragment fragment;
    private RootClass rootClass;
    private HashMap<String, ArrayList<DailyNote>> dailyNoteHashMap;
    private TextView weeklyNoteHeaderTextView, weeklyNoteContentTextView;
    private ImageView weeklyNoteImageView;
    private AvatarView studentImageView;
    private LinearLayout weeklyNoteLinearLayout;
    private IImageLoader imageLoader;
    private ImageButton backButton;
    private Button seeMoreButton;
    private FrameLayout listFrameLayout, placeholderFrameLayout;

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
            rootClass = (RootClass) bundle.getSerializable(Constants.KEY_WEEKLY_PLANER);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            this.dailyNoteHashMap = getDaysOfDailyNotes(rootClass);
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
        adapter = new WeeklyPlannerAdapter(activity.getSupportFragmentManager(), activity);
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
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
        if (rootClass != null && rootClass.getWeeklyPlans().size() > 0) {
            listFrameLayout.setVisibility(View.VISIBLE);
            placeholderFrameLayout.setVisibility(View.GONE);
        } else {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        }


        if (rootClass != null) {
            if (rootClass.getWeeklyPlans().size() > 0) {
                ArrayList<WeeklyNote> weeklyNoteArrayList = rootClass.getWeeklyPlans().get(0).getWeeklyNotes();
                if (weeklyNoteArrayList.size() > 0) {
                    WeeklyNote weeklyNote = weeklyNoteArrayList.get(0);
                    weeklyNoteHeaderTextView.setText(Html.fromHtml(weeklyNote.getTitle()));
                    weeklyNoteContentTextView.setText(Html.fromHtml(weeklyNote.getDescription()));
                    if (weeklyNote.getImageUrl() != null && !weeklyNote.getImageUrl().isEmpty() && !weeklyNote.getImageUrl().equals("null")) {
                        weeklyNoteImageView.setVisibility(View.VISIBLE);
                        Picasso.with(activity)
                                .load(weeklyNote.getImageUrl())
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

    private void setListeners() {
        backButton.setOnClickListener(this);
        seeMoreButton.setOnClickListener(this);
    }

    private ArrayList<Fragment> getFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_sunday))) {
            Day day = new Day(getResources().getString(R.string.weekly_sunday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_sunday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_monday))) {
            Day day = new Day(getResources().getString(R.string.weekly_monday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_monday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_tuesday))) {
            Day day = new Day(getResources().getString(R.string.weekly_tuesday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_tuesday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_wednesday))) {
            Day day = new Day(getResources().getString(R.string.weekly_wednesday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_wednesday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_thursday))) {
            Day day = new Day(getResources().getString(R.string.weekly_thursday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_thursday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_friday))) {
            Day day = new Day(getResources().getString(R.string.weekly_friday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_friday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }

        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_saturday))) {
            Day day = new Day(getResources().getString(R.string.weekly_saturday)
                    , dailyNoteHashMap.get(getResources().getString(R.string.weekly_saturday)));
            fragmentArrayList.add(DayFragment.newInstance(day, student));
        }
        return fragmentArrayList;
    }

    private ArrayList<String> getDaysNameArrayList() {
        ArrayList<String> daysNameStringArrayList = new ArrayList<>();
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
        if (dailyNoteHashMap.containsKey(getResources().getString(R.string.weekly_saturday))) {
            daysNameStringArrayList.add(getResources().getString(R.string.weekly_saturday));
        }

        return daysNameStringArrayList;
    }


    private HashMap<String, ArrayList<DailyNote>> getDaysOfDailyNotes(RootClass rootClass) {
        HashMap<String, ArrayList<DailyNote>> dailyNoteHashMap = new HashMap<>();
        if (rootClass != null) {
            if (rootClass.getWeeklyPlans().size() > 0) {
                ArrayList<DailyNote> dailyNoteArrayList = rootClass.getWeeklyPlans().get(0).getDailyNotes();
                for (int i = 0; i < dailyNoteArrayList.size(); i++) {
                    DailyNote dailyNote = dailyNoteArrayList.get(i);
                    String dayName = Util.getDayName(dailyNote.getDate());
                    if (!dayName.isEmpty()) {
                        if (dailyNoteHashMap.containsKey(dayName)) {
                            ArrayList<DailyNote> dailyNotes = dailyNoteHashMap.get(dayName);
                            dailyNotes.add(dailyNote);
                            dailyNoteHashMap.put(dayName, dailyNotes);
                        } else {
                            ArrayList<DailyNote> dailyNotes = new ArrayList<>();
                            dailyNotes.add(dailyNote);
                            dailyNoteHashMap.put(dayName, dailyNotes);
                        }

                    }

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
        WeeklyNote weeklyNote = rootClass.getWeeklyPlans().get(0).getWeeklyNotes().get(0);
        bundle.putSerializable(Constants.KEY_WEEKLY_NOTE, weeklyNote);
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
