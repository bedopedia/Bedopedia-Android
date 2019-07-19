package trianglz.ui.activities;

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

    private LinearLayout announcementLayout, messagesLayout, notificationsLayout, menuLayout;
    private ImageView announcementImageView, messagesImageView, notificationsImageView, menuImageView;
    private TextView announcementTextView, messagesTextView, notificationsTextView, menuTextView;

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
        isStudent = SessionManager.getInstance().getStudentAccount();
    }
    private void getValueFromIntent() {
        if(SessionManager.getInstance().getUserType()){
            student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
            attendance = (String) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ATTENDANCE);
        }else {
            actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
        }

    }
    private void setListeners() {
        announcementLayout.setOnClickListener(this);
        messagesLayout.setOnClickListener(this);
        notificationsLayout.setOnClickListener(this);
        menuLayout.setOnClickListener(this);
    }

    private void bindViews() {
        // tabs
        announcementLayout = findViewById(R.id.ll_announcment_tab);
        messagesLayout = findViewById(R.id.ll_messages_tab);
        notificationsLayout = findViewById(R.id.ll_notifications_tab);
        menuLayout = findViewById(R.id.ll_menu_tab);

        // image views
        announcementImageView = findViewById(R.id.iv_announcements);
        messagesImageView = findViewById(R.id.iv_messages);
        notificationsImageView = findViewById (R.id.iv_notifcations);
        menuImageView = findViewById(R.id.iv_menu);

        // text views
        announcementTextView = findViewById(R.id.tv_announcements);
        messagesTextView = findViewById(R.id.tv_messages);
        notificationsTextView = findViewById(R.id.tv_notifications);
        menuTextView = findViewById (R.id.tv_menu);

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
        pager.setCurrentItem(4);
    }

    private void setUpAdapterAccordingToUserType() {
        if (SessionManager.getInstance().getStudentAccount()) {
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
                announcementImageView.setImageResource(R.drawable.ic_announcment_selected_teacher);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.VISIBLE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 2:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_selected_teacher);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.VISIBLE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 3:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notification_selected_teacher);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.VISIBLE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 4:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_menu_parent_selected);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void handleStudentTabs(int tabNumber) {
        switch (tabNumber) {
            case 1:
                announcementImageView.setImageResource(R.drawable.ic_announcements_selected_student);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_home_student_tab);

                announcementTextView.setVisibility(View.VISIBLE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 2:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_selected_student);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_home_student_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.VISIBLE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 3:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_selected_student);
                menuImageView.setImageResource(R.drawable.ic_home_student_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.VISIBLE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 4:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_home_selected_student);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.VISIBLE);
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
