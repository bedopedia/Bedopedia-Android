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

    private LinearLayout firstLayout, secondLayout, thirdLayout, fourthLayout;
    private ImageView firstTabImageView, secondTabImageView, thirdTabImageView, fourthTabImageView;
    private TextView firstTextView, secondTextView, thirdTextView, fourthTextView;

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

    // student profile case variables
    boolean isStudent;

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
        if(SessionManager.getInstance().getUserType()){
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
    }

    private void bindViews() {
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
        pager.setOffscreenPageLimit(4);

        // set default tab if student
        if (isStudent) {
            pager.setCurrentItem(0);
            handleStudentTabs(1);
        } else {
            pager.setCurrentItem(4);
        }

    }

    private void setUpAdapterAccordingToUserType() {
        if (isStudent) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getStudentFragmentList());
        } else {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getParentFragmentList());
        }
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
            case R.id.ll_announcment_tab:
                pager.setCurrentItem(0,true);
                handleTabsClicking(1);
                break;
            case R.id.ll_messages_tab:
                pager.setCurrentItem(1,true);
                handleTabsClicking(2);
                break;
            case R.id.ll_notifications_tab:
                pager.setCurrentItem(2,true);
                handleTabsClicking(3);
                break;
            case R.id.ll_menu_tab:
                pager.setCurrentItem(3,true);
                handleTabsClicking(4);
                break;
        }
    }

    private void handleTabsClicking(int tabNumber) {
        if (SessionManager.getInstance().getStudentAccount()) {
            handleStudentTabs(tabNumber);
        } else
            handleParentTabs(tabNumber);
    }

    private void handleParentTabs(int tabNumber) {
        switch (tabNumber) {
            case 1:
                firstTabImageView.setImageResource(R.drawable.ic_announcment_selected_teacher);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 2:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_selected_teacher);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 3:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notification_selected_teacher);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 4:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_menu_parent_selected);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void handleStudentTabs(int tabNumber) {
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
            case 1:
                firstTabImageView.setImageResource(R.drawable.ic_home_selected_student);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 2:
                firstTabImageView.setImageResource(R.drawable.ic_home_student_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_selected_student);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 3:
                firstTabImageView.setImageResource(R.drawable.ic_home_student_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_selected_student);
                fourthTabImageView.setImageResource(R.drawable.ic_announcments_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                fourthTextView.setVisibility(View.GONE);
                break;
            case 4:
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


    @Override
    public void onBackPressed() {
        if(SessionManager.getInstance().getUserType()){
            if(!SessionManager.getInstance().getStudentAccount()){
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
