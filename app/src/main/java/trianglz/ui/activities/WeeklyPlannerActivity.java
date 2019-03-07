package trianglz.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import trianglz.ui.adapters.WeeklyPlannerAdapter;
import trianglz.ui.fragments.DayFragment;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class WeeklyPlannerActivity extends SuperActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        rootClass = (RootClass) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_WEEKLY_PLANER);
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        this.dailyNoteHashMap = getDaysOfDailyNotes(rootClass);
        imageLoader = new PicassoLoader();
    }

    private void bindViews() {
        studentImageView = findViewById(R.id.img_student);
        if (student != null) {
            setStudentImage();
        }
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        adapter = new WeeklyPlannerAdapter(getSupportFragmentManager(), this);
        ArrayList<String> daysNameArrayList = getDaysNameArrayList();
        adapter.addFragmentArrayList(getFragmentList(),daysNameArrayList);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            TextView vgTab = (TextView) vg.getChildAt(j);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.sfui_semibold);
            vgTab.setTypeface(Typeface.DEFAULT);
            vgTab.setTypeface(typeface);
        }
        weeklyNoteContentTextView = findViewById(R.id.tv_content_weekly_note);
        weeklyNoteHeaderTextView = findViewById(R.id.tv_header_weekly_note);
        weeklyNoteImageView = findViewById(R.id.img_weekly_note);
        weeklyNoteLinearLayout = findViewById(R.id.layout_weekly_note);
        if (rootClass != null) {
            if (rootClass.getWeeklyPlans().size() > 0) {
                ArrayList<WeeklyNote> weeklyNoteArrayList = rootClass.getWeeklyPlans().get(0).getWeeklyNotes();
                if (weeklyNoteArrayList.size() > 0) {
                    WeeklyNote weeklyNote = weeklyNoteArrayList.get(0);
                    weeklyNoteHeaderTextView.setText(Html.fromHtml(weeklyNote.getTitle()));
                    weeklyNoteContentTextView.setText(Html.fromHtml(weeklyNote.getDescription()));
                    if (weeklyNote.getImageUrl() != null && !weeklyNote.getImageUrl().isEmpty() && !weeklyNote.getImageUrl().equals("null")) {
                        weeklyNoteImageView.setVisibility(View.VISIBLE);
                        Picasso.with(this)
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
        backButton = findViewById(R.id.btn_back);
        seeMoreButton = findViewById(R.id.btn_see_more);
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
        ArrayList<DailyNote> dailyNoteArrayList = rootClass.getWeeklyPlans().get(0).getDailyNotes();
        HashMap<String, ArrayList<DailyNote>> dailyNoteHashMap = new HashMap<>();
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
            Picasso.with(this)
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_see_more:
                openWeeklyNoteActivity();
                break;
        }
    }

    private void openWeeklyNoteActivity() {
        Intent intent = new Intent(this,AnnouncementDetailActivity.class);
        Bundle bundle = new Bundle();
        WeeklyNote weeklyNote = rootClass.getWeeklyPlans().get(0).getWeeklyNotes().get(0);
        bundle.putSerializable(Constants.KEY_WEEKLY_NOTE,weeklyNote);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }
}
