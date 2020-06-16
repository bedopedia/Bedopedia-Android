package trianglz.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import trianglz.components.CustomRtlViewPager;
import trianglz.components.ErrorDialog;
import trianglz.components.SettingsDialog;
import trianglz.managers.SessionManager;
import trianglz.models.Actor;
import trianglz.models.Attendances;
import trianglz.models.Student;
import trianglz.ui.adapters.StudentMainPagerAdapter;
import trianglz.ui.fragments.AnnouncementsFragment;
import trianglz.ui.fragments.MenuFragment;
import trianglz.ui.fragments.MessagesFragment;
import trianglz.ui.fragments.NotificationsFragment;
import trianglz.ui.fragments.TeacherCoursesFragment;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class StudentMainActivity extends SuperActivity implements View.OnClickListener {

    private LinearLayout coursesLayout, secondLayout;
    private ImageView coursesImageView, firstTabImageView, secondTabImageView, thirdTabImageView, fourthTabImageView, notificationBadgeImageView, announcementBadgeImageView, studentannouncementBadgeImageView;
    private TextView coursesTextView, firstTextView, secondTextView, thirdTextView, fourthTextView;
    private FrameLayout thirdLayout, firstLayout, fourthLayout;
    public View toolbarView;
    public Boolean isCalling = false;
    public CustomRtlViewPager pager;
    private StudentMainPagerAdapter pagerAdapter;
    private ErrorDialog errorDialogue;
    // header
    private ImageButton settingsBtn, addNewMessageButton, backBtn;
    private SettingsDialog settingsDialog;
    public RelativeLayout headerLayout;

    // fragments
    private AnnouncementsFragment announcementsFragment;
    private MessagesFragment messagesFragment;
    private NotificationsFragment notificationsFragment;
    private MenuFragment menuFragment;
    private TeacherCoursesFragment teacherCoursesFragment;
    public Student student;
    public ArrayList<Attendances> attendance;
    public Actor actor;
    public boolean isHod = false;

    // booleans

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationCheck();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()))
            studentAnnouncementCheck();
        else
            announcementCheck();
    }

    private void getValueFromIntent() {
        //       isStudent = SessionManager.getInstance().getStudentAccount();
        //     isParent = SessionManager.getInstance().getUserType();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            student = getIntent().getBundleExtra(Constants.KEY_BUNDLE).getParcelable(Constants.STUDENT);
            attendance = student.attendances;
        } else {
            actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
        }

    }

    private void setListeners() {
        settingsBtn.setOnClickListener(this);
        addNewMessageButton.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thirdLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);
        if (coursesLayout != null) coursesLayout.setOnClickListener(this);

        final int fragmentsCount = pagerAdapter.getCount();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showHideToolBar(pagerAdapter.getItem(position));
                if (fragmentsCount == 4) {
                    if (position == 1) {
                        addNewMessageButton.setVisibility(View.VISIBLE);
                    } else {
                        addNewMessageButton.setVisibility(View.GONE);
                    }
                } else {
                    addNewMessageButton.setVisibility(View.GONE);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void bindViews() {

        // tabs
        firstLayout = findViewById(R.id.ll_announcment_tab);
        secondLayout = findViewById(R.id.ll_messages_tab);
        thirdLayout = findViewById(R.id.ll_notifications_tab);
        fourthLayout = findViewById(R.id.ll_menu_tab);

        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            teacherCoursesFragment = new TeacherCoursesFragment();
            coursesLayout = findViewById(R.id.ll_courses_tab);
            coursesImageView = findViewById(R.id.iv_courses);
            coursesTextView = findViewById(R.id.tv_courses);
            coursesLayout.setVisibility(View.VISIBLE);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            fourthLayout.setVisibility(View.GONE);
            isHod = true;
        }

        // image views
        firstTabImageView = findViewById(R.id.iv_announcements);
        secondTabImageView = findViewById(R.id.iv_messages);
        thirdTabImageView = findViewById(R.id.iv_notifcations);
        fourthTabImageView = findViewById(R.id.iv_menu);
        notificationBadgeImageView = findViewById(R.id.img_red_circle);
        announcementBadgeImageView = findViewById(R.id.img_announcement_circle);
        studentannouncementBadgeImageView = findViewById(R.id.student_announcement_circle);

        // text views
        firstTextView = findViewById(R.id.tv_announcements);
        secondTextView = findViewById(R.id.tv_messages);
        thirdTextView = findViewById(R.id.tv_notifications);
        fourthTextView = findViewById(R.id.tv_menu);

        //views
        toolbarView = findViewById(R.id.toolbar_view);

        // header
        headerLayout = findViewById(R.id.rl_header);
        settingsBtn = findViewById(R.id.btn_setting_student);
        addNewMessageButton = findViewById(R.id.btn_new_message);
        addNewMessageButton.setVisibility(View.GONE);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setVisibility(View.GONE);
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            settingsBtn.setVisibility(View.VISIBLE);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            settingsBtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addNewMessageButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            addNewMessageButton.setLayoutParams(params);
            backBtn.setVisibility(View.VISIBLE);
        } else {
            settingsBtn.setVisibility(View.VISIBLE);
        }

        // init fragments
        menuFragment = new MenuFragment();
        announcementsFragment = new AnnouncementsFragment();
        messagesFragment = new MessagesFragment();
        notificationsFragment = new NotificationsFragment();

        // pager
        pager = findViewById(R.id.pager);
        pager.setPagingEnabled(false);
        setUpAdapterAccordingToUserType();
        pager.setAdapter(pagerAdapter);
        String h = SessionManager.getInstance().getUserType();
        // set default tab according to user type
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            pager.setOffscreenPageLimit(4);
            pager.setCurrentItem(0);
            handleStudentTabs(pagerAdapter.getCount() - 4);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            pager.setOffscreenPageLimit(4);
            pager.setCurrentItem(4);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            pager.setOffscreenPageLimit(5);
            pager.setCurrentItem(4);
            handleTeacherTabs(4);
        } else {
            pager.setOffscreenPageLimit(3);
            pager.setCurrentItem(2);
            handleHodTabs(0);
        }
    }

    private void setUpAdapterAccordingToUserType() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getStudentFragmentList());
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getParentFragmentList());
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getTeacherFragmentList());
        } else {
            pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getHodFragmentList());
        }
    }

    private ArrayList<Fragment> getTeacherFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(teacherCoursesFragment);
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

    private ArrayList<Fragment> getHodFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(announcementsFragment);
        fragmentArrayList.add(messagesFragment);
        fragmentArrayList.add(notificationsFragment);
        return fragmentArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_courses_tab:
                returnToRootFragment(pagerAdapter.getCount() - 5);
                handleTabsClicking(pagerAdapter.getCount() - 5);
                break;
            case R.id.ll_announcment_tab:
                if (isHod) {
                    returnToRootFragment(pagerAdapter.getCount() - 3);
                    handleTabsClicking(pagerAdapter.getCount() - 3);
                } else {
                    returnToRootFragment(pagerAdapter.getCount() - 4);
                    handleTabsClicking(pagerAdapter.getCount() - 4);
                }
                break;
            case R.id.ll_messages_tab:
                if (isHod) {
                    returnToRootFragment(pagerAdapter.getCount() - 2);
                    handleTabsClicking(pagerAdapter.getCount() - 2);
                } else {
                    returnToRootFragment(pagerAdapter.getCount() - 3);
                    handleTabsClicking(pagerAdapter.getCount() - 3);
                }
                break;
            case R.id.ll_notifications_tab:
                SessionManager.getInstance().setNotificationCounterToZero();
                notificationBadgeImageView.setVisibility(View.GONE);
                notificationCheck();
                if (isHod) {
                    returnToRootFragment(pagerAdapter.getCount() - 1);
                    handleTabsClicking(pagerAdapter.getCount() - 1);
                } else {
                    returnToRootFragment(pagerAdapter.getCount() - 2);
                    handleTabsClicking(pagerAdapter.getCount() - 2);
                }
                break;
            case R.id.ll_menu_tab:
                returnToRootFragment(pagerAdapter.getCount() - 1);
                handleTabsClicking(pagerAdapter.getCount() - 1);
                break;
            case R.id.btn_setting_student:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.btn_new_message:
                if (Util.isNetworkAvailable(this)) {
                    messagesFragment.openNewMessageActivity();
                } else {
                    Util.showNoInternetConnectionDialog(this);
                }
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    private void handleTabsClicking(int tabNumber) {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            handleStudentTabs(tabNumber);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            handleParentTabs(tabNumber);
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            handleTeacherTabs(tabNumber);
        } else {
            handleHodTabs(tabNumber);
        }

    }

    private void handleParentTabs(int tabNumber) {
        pager.setCurrentItem(tabNumber);
        fourthTextView.setText(getResources().getString(R.string.profile));
        //  showHideToolBar(pagerAdapter.getItem(tabNumber));
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
        firstTextView.setText(getResources().getString(R.string.profile));
        secondTextView.setText(getResources().getString(R.string.messages));
        thirdTextView.setText(getResources().getString(R.string.notifications));
        fourthTextView.setText(getResources().getString(R.string.announcements));
        // text view text color
        int color = Color.parseColor("#fd8268");
        notificationBadgeImageView.setColorFilter(ContextCompat.getColor(this, R.color.salmon), android.graphics.PorterDuff.Mode.SRC_IN);
        studentannouncementBadgeImageView.setColorFilter(ContextCompat.getColor(this, R.color.salmon), android.graphics.PorterDuff.Mode.SRC_IN);
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
        // showHideToolBar(pagerAdapter.getItem(tabNumber));
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

    private void handleHodTabs(int tabNumber) {
        pager.setCurrentItem(tabNumber);
        // showHideToolBar(pagerAdapter.getItem(tabNumber));
        // text view content
        firstTextView.setText(getResources().getString(R.string.announcements));
        secondTextView.setText(getResources().getString(R.string.messages));
        thirdTextView.setText(getResources().getString(R.string.notifications));
        // text view text color
        int color = Color.parseColor("#007ee5");
        firstTextView.setTextColor(color);
        secondTextView.setTextColor(color);
        thirdTextView.setTextColor(color);
        notificationBadgeImageView.setColorFilter(ContextCompat.getColor(this, R.color.cerulean_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        announcementBadgeImageView.setColorFilter(ContextCompat.getColor(this, R.color.cerulean_blue), android.graphics.PorterDuff.Mode.SRC_IN);

        switch (tabNumber) {
            case 0:
                firstTabImageView.setImageResource(R.drawable.ic_announcment_selected_teacher);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);
                firstTextView.setVisibility(View.VISIBLE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.GONE);
                break;
            case 1:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_selected_teacher);
                thirdTabImageView.setImageResource(R.drawable.ic_notifications_tab);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.VISIBLE);
                thirdTextView.setVisibility(View.GONE);
                break;
            case 2:
                firstTabImageView.setImageResource(R.drawable.ic_announcments_tab);
                secondTabImageView.setImageResource(R.drawable.ic_messages_tab);
                thirdTabImageView.setImageResource(R.drawable.ic_notification_selected_teacher);

                firstTextView.setVisibility(View.GONE);
                secondTextView.setVisibility(View.GONE);
                thirdTextView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedInterface) {
                    ((OnBackPressedInterface) fragment).onBackPressed();
                }
            }
        }
    }

    public Student getStudent() {
        return student;
    }

    public Actor getActor() {
        return actor;
    }

    public ArrayList<Attendances> getAttendance() {
        return attendance;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public interface OnBackPressedInterface {
        void onBackPressed();
    }

    private void showHideToolBar(Fragment fragment) {
        if (!fragment.isAdded()) {
            return;
        }
        if (fragment.getChildFragmentManager() != null) {
            if (fragment.getChildFragmentManager().getFragments().size() > 0) {
                headerLayout.setVisibility(View.GONE);
                toolbarView.setVisibility(View.GONE);
            } else {
                headerLayout.setVisibility(View.VISIBLE);
                toolbarView.setVisibility(View.VISIBLE);
            }
        }

    }

    private void returnToRootFragment(int tabNumber) {
        if (tabNumber == pager.getCurrentItem()) {
            if (pagerAdapter.getItem(tabNumber).isAdded()) {
                pagerAdapter.getItem(tabNumber).getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                headerLayout.setVisibility(View.VISIBLE);
                toolbarView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void notificationCheck() {
        if (SessionManager.getInstance().getNotficiationCounter() > 0) {
            notificationBadgeImageView.setVisibility(View.VISIBLE);
        } else {
            notificationBadgeImageView.setVisibility(View.GONE);
        }
    }

    private void announcementCheck() {
        if (SessionManager.getInstance().getAnnouncementCounter() > 0) {
            announcementBadgeImageView.setVisibility(View.VISIBLE);
        } else {
            announcementBadgeImageView.setVisibility(View.GONE);
        }
    }

    private void studentAnnouncementCheck() {
        if (SessionManager.getInstance().getAnnouncementCounter() > 0) {
            studentannouncementBadgeImageView.setVisibility(View.VISIBLE);
        } else {
            studentannouncementBadgeImageView.setVisibility(View.GONE);
        }
    }
}
