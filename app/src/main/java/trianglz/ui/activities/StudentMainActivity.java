package trianglz.ui.activities;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.components.CustomRtlViewPager;
import trianglz.managers.SessionManager;
import trianglz.models.Actor;
import trianglz.models.Student;
import trianglz.ui.adapters.StudentMainPagerAdapter;
import trianglz.ui.fragments.AnnouncementsFragment;
import trianglz.ui.fragments.MenuFragment;
import trianglz.ui.fragments.MessagesFragment;
import trianglz.ui.fragments.NotificationsFragment;
import trianglz.utils.Constants;

public class StudentMainActivity extends SuperActivity implements View.OnClickListener {

    private LinearLayout coursesLayout, firstLayout, secondLayout, thirdLayout, fourthLayout;
    private ImageView coursesImageView, firstTabImageView, secondTabImageView, thirdTabImageView, fourthTabImageView;
    private TextView coursesTextView, firstTextView, secondTextView, thirdTextView, fourthTextView;

    private CustomRtlViewPager pager;
    private StudentMainPagerAdapter pagerAdapter;

    // fragments
    private AnnouncementsFragment announcementsFragment;
    private MessagesFragment messagesFragment;
    private NotificationsFragment notificationsFragment;
    private MenuFragment menuFragment;
    public Student student;
    public String attendance;
    public Actor actor;

    // booleans
    boolean isStudent, isParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        getValueFromIntent();
        bindViews();
        setListeners();
    }
    private void getValueFromIntent() {
        isStudent = SessionManager.getInstance().getStudentAccount();
        isParent = SessionManager.getInstance().getUserType();
        if(isParent){
            student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
            attendance = (String) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ATTENDANCE);
        }else {
            actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
        }

    }
    private void setListeners() {
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thirdLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);
        if (coursesLayout != null) coursesLayout.setOnClickListener(this);
    }

    private void bindViews() {

        if (!isStudent && !isParent) {
            coursesLayout = findViewById (R.id.ll_courses_tab);
            coursesImageView = findViewById (R.id.iv_courses);
            coursesTextView = findViewById (R.id.tv_courses);
            coursesLayout.setVisibility(View.VISIBLE);
        }
        // tabs
        firstLayout = findViewById(R.id.ll_announcment_tab);
        secondLayout = findViewById(R.id.ll_messages_tab);
        thirdLayout = findViewById(R.id.ll_notifications_tab);
        fourthLayout = findViewById(R.id.ll_menu_tab);

        // image views
        firstTabImageView = findViewById(R.id.iv_announcements);
        secondTabImageView = findViewById(R.id.iv_messages);
        thirdTabImageView = findViewById (R.id.iv_notifcations);
        fourthTabImageView = findViewById(R.id.iv_menu);

        // text views
        firstTextView = findViewById(R.id.tv_announcements);
        secondTextView = findViewById(R.id.tv_messages);
        thirdTextView = findViewById(R.id.tv_notifications);
        fourthTextView = findViewById (R.id.tv_menu);

        // init fragments
        announcementsFragment = new AnnouncementsFragment();
        messagesFragment = new MessagesFragment();
        notificationsFragment = new NotificationsFragment();
        menuFragment = new MenuFragment();

        // pager
        pager = findViewById(R.id.pager);
        pager.setPagingEnabled(false);
        setUpAdapterAccordingToUserType();
        pager.setAdapter(pagerAdapter);

        // set default tab according to user type
        if (isStudent) {
            pager.setOffscreenPageLimit(4);
            pager.setCurrentItem(0);
            handleStudentTabs(pagerAdapter.getCount() - 4);
        } else if (isParent) {
            pager.setOffscreenPageLimit(4);
            pager.setCurrentItem(4);
        } else {
            pager.setOffscreenPageLimit(5);
            pager.setCurrentItem(4);
            handleTeacherTabs(4);
        }

    }

    private void setUpAdapterAccordingToUserType() {
        if (isStudent) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getStudentFragmentList());
        } else if (isParent) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getParentFragmentList());
        } else {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getTeacherFragmentList());
        }
    }

    private ArrayList<Fragment> getTeacherFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new AnnouncementsFragment());
        fragmentArrayList.add(announcementsFragment);
        fragmentArrayList.add(messagesFragment);
        fragmentArrayList.add(notificationsFragment);
        fragmentArrayList.add(menuFragment);
        return fragmentArrayList;
    }

    private ArrayList<Fragment> getParentFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(announcementsFragment);
        fragmentArrayList.add(messagesFragment);
        fragmentArrayList.add(notificationsFragment);
        fragmentArrayList.add(menuFragment);
        return fragmentArrayList;
    }
    private ArrayList<Fragment> getStudentFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(menuFragment);
        fragmentArrayList.add(messagesFragment);
        fragmentArrayList.add(notificationsFragment);
        fragmentArrayList.add(announcementsFragment);
        return fragmentArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_courses_tab:
                handleTabsClicking(pagerAdapter.getCount() - 5);
                break;
            case R.id.ll_announcment_tab:
                handleTabsClicking(pagerAdapter.getCount() - 4);
                break;
            case R.id.ll_messages_tab:
                handleTabsClicking(pagerAdapter.getCount() - 3);
                break;
            case R.id.ll_notifications_tab:
                handleTabsClicking(pagerAdapter.getCount() - 2);
                break;
            case R.id.ll_menu_tab:
                handleTabsClicking(pagerAdapter.getCount() - 1);
                break;
        }
    }

    private void handleTabsClicking(int tabNumber) {
        if (isStudent) {
            handleStudentTabs(tabNumber);
        } else if (isParent) {
            handleParentTabs(tabNumber);
        } else {
            handleTeacherTabs(tabNumber);
        }

    }

    private void handleParentTabs(int tabNumber) {
        pager.setCurrentItem(tabNumber);
        switch (tabNumber) {
            case 0:
                firstTabImageView.setImageResource(R.drawable.ic_announcements_selected_parent);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 1:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_message_selected_parent);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 2:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_selected_parent);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 3:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_selected_parent);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void handleStudentTabs(int tabNumber) {
        pager.setCurrentItem(tabNumber);
        // text view content
        firstTextView.setText(getResources().getString(R.string.home));
        secondTextView.setText(getResources().getString(R.string.messages));
        thirdTextView.setText(getResources().getString(R.string.notifications));
        fourthTextView.setText(getResources().getString(R.string.announcements));
        // text view text color
        int color = Color.parseColor("#fd8268");
        firstTextView.setTextColor(color);
        secondTextView.setTextColor(color);
        thirdTextView.setTextColor(color);
        fourthTextView.setTextColor(color);
        switch (tabNumber) {
            case 0:
                firstTabImageView.setImageResource(R.drawable.ic_home_selected_student);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 1:
                firstTabImageView.setImageResource(R.drawable.ic_home_student_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_selected_student);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 2:
                firstTabImageView.setImageResource(R.drawable.ic_home_student_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_selected_student);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 3:
                firstTabImageView.setImageResource(R.drawable.ic_home_student_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_announcements_selected_student);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void handleTeacherTabs(int tabNumber) {
        pager.setCurrentItem(tabNumber);
        // text view content
        coursesTextView.setText(getResources().getString(R.string.courses));
        firstTextView.setText(getResources().getString(R.string.announcements));
        secondTextView.setText(getResources().getString(R.string.messages));
        thirdTextView.setText(getResources().getString(R.string.notifications));
        fourthTextView.setText(getResources().getString(R.string.menu));
        // text view text color
        int color = Color.parseColor("#007ee5");
        firstTextView.setTextColor(color);
        secondTextView.setTextColor(color);
        thirdTextView.setTextColor(color);
        fourthTextView.setTextColor(color);
        switch (tabNumber) {
            case 0:
                coursesImageView.setImageResource(R.drawable.ic_courses_selected_teacher);
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                coursesTextView.setVisibility(View.VISIBLE);
                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 1:
                coursesImageView.setImageResource(R.drawable.ic_home_student_tab);
                firstTabImageView.setImageResource(R.drawable.ic_announcment_selected_teacher);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                coursesTextView.setVisibility(View.GONE);
                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 2:
                coursesImageView.setImageResource(R.drawable.ic_home_student_tab);
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_selected_teacher);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                coursesTextView.setVisibility(View.GONE);
                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 3:
                coursesImageView.setImageResource(R.drawable.ic_home_student_tab);
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notification_selected_teacher);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                coursesTextView.setVisibility(View.GONE);
                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 4:
                coursesImageView.setImageResource(R.drawable.ic_home_student_tab);
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_selected_teacher);

                coursesTextView.setVisibility(View.GONE);
                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if(isParent){
            if(!isStudent){
                super.onBackPressed();
            }
        }
    }

    public Student getStudent() {
        return student;
    }
    public Actor getActor() {
        return actor;
    }
    public String getAttendance() {
        return attendance;
    }
}
