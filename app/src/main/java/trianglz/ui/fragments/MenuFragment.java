package trianglz.ui.fragments;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import attendance.Attendance;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.LocalHelper;
import trianglz.components.SettingsDialog;
import trianglz.core.presenters.StudentDetailPresenter;
import trianglz.core.views.StudentDetailView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.Announcement;
import trianglz.models.BehaviorNote;
import trianglz.models.CourseGroup;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Notification;
import trianglz.models.RootClass;
import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.ui.activities.ContactTeacherActivity;
import trianglz.ui.activities.NotificationsActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements StudentDetailPresenter,
        View.OnClickListener, SettingsDialog.SettingsDialogInterface, StudentMainActivity.OnBackPressedInterface {

    //fragment root view
    private View rootView;
    // parent activity
    private StudentMainActivity activity;
    private View appBarView;
    private List<TimeTableSlot> todaySlots;
    private List<TimeTableSlot> tomorrowSlots;
    private List<BehaviorNote> positiveBehaviorNotes;
    private List<BehaviorNote> negativeBehaviorNotes;
    private List<BehaviorNote> otherBehaviorNotes;
    private ArrayList<CourseGroup> courseGroups;
    private Student student;
    private AvatarView studentImage;
    private ArrayList<Student> studentArrayList;
    private ArrayList<Attendance> attendanceArrayList;
    private TextView nameTextView, levelTextView, nextSlotTextView, studentGradeTextView,
            positiveCounterTextView, negativeCounterTextView, otherCounterTextView, attendanceTextView, teacherNextSlotTextView;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private String studentName = "";
    private StudentDetailView studentDetailView;
    private LinearLayout attendanceLayout, timeTableLayout, gradesLayout, behaviourNotesLayout, weeklyPlannerLayout, assignmentsLayout, postsLayout, quizzesLayout, calendarLayout, teacherTimeTableLayout;
    private String attendance, nextSlot;
    private int absentDays;
    private com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar progressBar;
    private ArrayList<JSONArray> attendanceList;
    private AppBarLayout appBarLayout;


    private LinearLayout parentLayout, teacherLayout;
    private Actor actor;
    private String actorName = "";
    private SettingsDialog settingsDialog;
    private RootClass rootClass;
    private TextView weeklyPlannerTextView;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        setListeners();
        setParentActorView();
        if (SessionManager.getInstance().getUserType()) {
            String courseUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/course_groups";
            if (Util.isNetworkAvailable(getParentActivity())) {
                studentDetailView.getStudentCourses(courseUrl);
                activity.isCalling = true;
                getParentActivity().showLoadingDialog();
            } else {
                Util.showNoInternetConnectionDialog(getParentActivity());
            }
        } else {
            checkVersionOnStore();
        }
        return rootView;

    }


    // this method is to reduce the amount of calling getParentActivity()
    private StudentMainActivity getParentActivity() {
        if (activity == null) {
            activity = (StudentMainActivity) getActivity();
            return activity;
        } else {
            return activity;
        }
    }

    private void bindViews() {

        todaySlots = new ArrayList<>();
        tomorrowSlots = new ArrayList<>();
        positiveBehaviorNotes = new ArrayList<>();
        negativeBehaviorNotes = new ArrayList<>();
        otherBehaviorNotes = new ArrayList<>();
        courseGroups = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        appBarView = rootView.findViewById(R.id.appbar_view);
        appBarLayout = rootView.findViewById(R.id.app_bar_layout);
        nameTextView = rootView.findViewById(R.id.tv_name);
        levelTextView = rootView.findViewById(R.id.tv_level);
        studentImageView = rootView.findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        nextSlotTextView = rootView.findViewById(R.id.tv_time_table);
        teacherNextSlotTextView = rootView.findViewById(R.id.tv_time_table_teacher);
        studentGradeTextView = rootView.findViewById(R.id.tv_grade);
        attendanceLayout = rootView.findViewById(R.id.layout_attendance);
        weeklyPlannerLayout = rootView.findViewById(R.id.layout_weekly_planner);
        timeTableLayout = rootView.findViewById(R.id.layout_timetable);
        gradesLayout = rootView.findViewById(R.id.layout_grades);
        calendarLayout = rootView.findViewById(R.id.layout_calendar);
        behaviourNotesLayout = rootView.findViewById(R.id.layout_behavior_notes);
        studentDetailView = new StudentDetailView(getParentActivity(), this);
        positiveCounterTextView = rootView.findViewById(R.id.tv_positive_counter);
        negativeCounterTextView = rootView.findViewById(R.id.tv_negative_counter);
        otherCounterTextView = rootView.findViewById(R.id.tv_other_counter);
        progressBar = rootView.findViewById(R.id.progress_bar);
        attendanceTextView = rootView.findViewById(R.id.tv_attendance);
        parentLayout = rootView.findViewById(R.id.layout_parent);
        teacherLayout = rootView.findViewById(R.id.layout_teacher);
        teacherTimeTableLayout = rootView.findViewById(R.id.layout_timetable_teacher);
        settingsDialog = new SettingsDialog(getParentActivity(), R.style.SettingsDialog, this);
        weeklyPlannerTextView = rootView.findViewById(R.id.tv_weekly_planner);

        student = getParentActivity().getStudent();
        actor = getParentActivity().getActor();
        attendance = getParentActivity().getAttendance();

        // new assignments layout
        assignmentsLayout = rootView.findViewById(R.id.layout_assignments);

        // new posts layout
        postsLayout = rootView.findViewById(R.id.layout_posts);

        // new quizzes layout
        quizzesLayout = rootView.findViewById(R.id.layout_quizzes);

    }

    private void setListeners() {
        attendanceLayout.setOnClickListener(this);
        weeklyPlannerLayout.setOnClickListener(this);
        timeTableLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        behaviourNotesLayout.setOnClickListener(this);
        assignmentsLayout.setOnClickListener(this);
        postsLayout.setOnClickListener(this);
        calendarLayout.setOnClickListener(this);
        quizzesLayout.setOnClickListener(this);
        teacherTimeTableLayout.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    appBarView.setVisibility(View.GONE);
                    //  appBarView.animate().alpha(0.0f);
                    //    activity.toolbarView.setVisibility(View.VISIBLE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
//                    appBarView.animate().alpha(1.0f);
                    appBarView.setVisibility(View.VISIBLE);
                    //  activity.toolbarView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setParentActorView() {
        if (!SessionManager.getInstance().getUserType()) {
            parentLayout.setVisibility(View.GONE);
            teacherLayout.setVisibility(View.VISIBLE);
            actorName = actor.firstName + " " + actor.lastName;
            setStudentImage(actor.imageUrl, actorName);
            nameTextView.setText(actorName);
            levelTextView.setText(actor.actableType);
            String timeTableUrl = SessionManager.getInstance().getBaseUrl() + "/api/teachers/" + SessionManager.getInstance().getId() + "/timetable";
            activity.isCalling = true;
            studentDetailView.getStudentTimeTable(timeTableUrl);
            getParentActivity().showLoadingDialog();
            String notificationText = SessionManager.getInstance().getNotficiationCounter() + " " + getParentActivity().getResources().getString(R.string.unread_notifications);
        } else {
            parentLayout.setVisibility(View.VISIBLE);
            teacherLayout.setVisibility(View.GONE);
            studentName = student.firstName + " " + student.lastName;
            setAttendance();
            setStudentImage(student.getAvatar(), studentName);
            nameTextView.setText(studentName);
            levelTextView.setText(student.level);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (!SessionManager.getInstance().getUserType()) {
//            if (Util.isNetworkAvailable(getParentActivity())) {
//                getNotifications(false);
//            } else {
//                Util.showNoInternetConnectionDialog(getParentActivity());
//            }
//        }
    }


    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(getParentActivity())
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

    private void setAttendance() {
        try {
            JSONArray jsonArray = new JSONArray(attendance);
            Set<Date> attendanceDates = new HashSet<>();
            absentDays = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject day = jsonArray.optJSONObject(i);
                Date date = new Date();
                date.setTime(day.optLong(Constants.KEY_DATE));
                if (!attendanceDates.contains(date)) {
                    if (day.optString(Constants.KEY_STATUS).equals(Constants.KEY_ABSENT))
                        absentDays++;
                }
                attendanceDates.add(date);
            }
            if (attendanceDates.size() != 0)
                progressBar.setProgress(((attendanceDates.size() - absentDays) * 100) / attendanceDates.size());
            String attendance = getParentActivity().getResources().getString(R.string.attend) + " " + (
                    attendanceDates.size() - absentDays) +
                    " " + getParentActivity().getResources().getString(R.string.out) + " " + attendanceDates.size() + " " +
                    getParentActivity().getResources().getString(R.string.days);
            attendanceTextView.setText(attendance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetStudentCourseGroupSuccess(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
        String url = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/grade_certificate";
     //   studentDetailView.getStudentGrades(url, courseGroups);
        String timeTableUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/timetable";
        studentDetailView.getStudentTimeTable(timeTableUrl);
    }

    @Override
    public void onGetStudentCourseGroupFailure(String message, int errorCode) {
        activity.isCalling = false;
        if (getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void onGetStudentGradesSuccess(ArrayList<trianglz.models.CourseGroup> courseGroups, String totalGrade) {
        activity.isCalling = true;
        this.courseGroups = courseGroups;
        totalGrade = getParentActivity().getResources().getString(R.string.average_grade) + " " + totalGrade;
      //  studentGradeTextView.setVisibility(View.VISIBLE);
        studentGradeTextView.setText(totalGrade);
        String timeTableUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/timetable";
        studentDetailView.getStudentTimeTable(timeTableUrl);
    }

    @Override
    public void onGetStudentGradesFailure(String message, int errorCode) {
        activity.isCalling = false;
        if (getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void oneGetTimeTableSuccess(ArrayList<Object> timeTableData) {

        if (!SessionManager.getInstance().getUserType()) {
            activity.isCalling = false;
        } else {
            activity.isCalling = true;
        }
        nextSlot = (String) timeTableData.get(2);

        todaySlots = (List<TimeTableSlot>) timeTableData.get(0);
        tomorrowSlots = (List<TimeTableSlot>) timeTableData.get(1);
        if (!nextSlot.isEmpty()) {
            nextSlotTextView.setVisibility(View.VISIBLE);
            teacherNextSlotTextView.setVisibility(View.VISIBLE);
            nextSlotTextView.setText(nextSlot);
            teacherNextSlotTextView.setText(nextSlot);
        }
        if (SessionManager.getInstance().getStudentAccount() || SessionManager.getInstance().getUserType()) {
            String url = SessionManager.getInstance().getBaseUrl() + "/api/behavior_notes";
            studentDetailView.getStudentBehavioursNotes(url, student.getId() + "");
        } else {
            getParentActivity().progress.dismiss();
        }
    }

    @Override
    public void onGetTimeTableFailure(String message, int errorCode) {
        activity.isCalling = false;
        if (getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void onGetBehaviorNotesSuccess(HashMap<String, List<BehaviorNote>> behaviorNoteHashMap) {
        activity.isCalling = true;
        positiveBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_POSITIVE);
        negativeBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_NEGATIVE);
        otherBehaviorNotes = behaviorNoteHashMap.get(Constants.OTHER);
        String positiveCounter = positiveBehaviorNotes.size() + "";
        String negativeCounter = negativeBehaviorNotes.size() + "";
        String otherCounter = otherBehaviorNotes.size() + "";
        positiveCounterTextView.setText(positiveCounter);
        negativeCounterTextView.setText(negativeCounter);
        otherCounterTextView.setText(otherCounter);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
        Date date = new Date();
        String currentDate = (dateFormat.format(date));
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getWeeklyPlanerUrl(currentDate);
        studentDetailView.getWeeklyPlanner(url);

    }

    @Override
    public void onGetBehaviorNotesFailure(String message, int errorCode) {
        activity.isCalling = false;
        if (getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");


    }

    @Override
    public void onGetWeeklyPlannerSuccess(RootClass rootClass) {
        activity.isCalling = false;
        this.rootClass = rootClass;
        if (rootClass.getWeeklyPlans().size() > 0) {
            weeklyPlannerTextView.setVisibility(View.VISIBLE);
            weeklyPlannerTextView.setText(Util.getWeeklPlannerText(rootClass.getWeeklyPlans().get(0).getStartDate(),
                    rootClass.getWeeklyPlans().get(0).getEndDate(), getParentActivity()));
        } else {
            //  weeklyPlannerTextView.setText(R.string.there_is_no_weekly_planner);
        }
        if (getParentActivity().progress.isShowing())
            getParentActivity().progress.dismiss();

    }

    @Override
    public void onGetWeeklyPlannerFailure(String message, int errorCode) {
        activity.isCalling = false;
        if (getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notificationArrayList) {
        if (notificationArrayList.size() > 0) {
            Notification notification = notificationArrayList.get(0);
        } else {
        }
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
        studentDetailView.getMessages(url, SessionManager.getInstance().getId());
    }

    @Override
    public void onGetNotificationFailure(String message, int errorCode) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
        studentDetailView.getMessages(url, SessionManager.getInstance().getId());
        if (!activity.isCalling) {
            if (getParentActivity().progress.isShowing()) {
                getParentActivity().progress.dismiss();
            }
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void onGetMessagesSuccess(ArrayList<MessageThread> messageArrayList, int unreadMessageCount) {
        if (messageArrayList.size() > 0) {
            MessageThread messageThread = messageArrayList.get(0);
            if (messageThread.messageArrayList.size() > 0) {
                Message latestMessage = messageThread.messageArrayList.get(0);
                String body = android.text.Html.fromHtml(latestMessage.body).toString();
                body = StringEscapeUtils.unescapeJava(body);
            }
        } else {
        }
    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        activity.showErrorDialog(activity, errorCode,"");
    }


    @Override
    public void onGetAnnouncementsSuccess(ArrayList<Announcement> announcementArrayList) {
        if (announcementArrayList.size() > 0) {
            Announcement announcement = announcementArrayList.get(0);
            String body = android.text.Html.fromHtml(announcement.body).toString();
            body = StringEscapeUtils.unescapeJava(body);
        } else {
        }
        if (!activity.isCalling) {
            if (getParentActivity().progress.isShowing()) {
                getParentActivity().progress.dismiss();
            }
        }
    }

    @Override
    public void onGetAnnouncementsFailure(String message, int errorCode) {
        if (!activity.isCalling) {
            if (getParentActivity().progress.isShowing()) {
                getParentActivity().progress.dismiss();
            }
        }
        activity.showErrorDialog(activity, errorCode,"");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_timetable:
                openTimeTableActivity();
                break;
            case R.id.btn_back:
                getParentActivity().onBackPressed();
                break;
            case R.id.layout_attendance:
                openAttendanceActivity();
                break;
            case R.id.layout_behavior_notes:
                openBehaviourNotesActivity();
                break;
            case R.id.layout_grades:
                openGradesActivity();
                break;
            case R.id.layout_calendar:
                openCalendarActivity();
                break;
            case R.id.btn_messages:
                openMessagesActivity();
                break;
            case R.id.btn_notification:
                openNotificationsActivity();
                break;
            case R.id.layout_notification:
                openNotificationsActivity();
                break;
            case R.id.layout_messages:
                openMessagesActivity();
                break;
            case R.id.layout_annoucment:
                //   openAnnouncement();
                break;
            case R.id.btn_setting:
                settingsDialog.show();
                break;
            case R.id.layout_weekly_planner:
                openWeeklyPlannerActivity();
                break;
            case R.id.btn_setting_student:
                if (!settingsDialog.isShowing()) {
                    settingsDialog.show();
                }
                break;
            case R.id.tv_assignment:
                openAssignmentDetailActivity();
                break;
            case R.id.layout_assignments:
                openAssignmentDetailActivity();
                break;
            case R.id.layout_posts:
                appBarLayout.setExpanded(true);
                PostsFragment postsFragment = new PostsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STUDENT, student);
                bundle.putInt(Constants.KEY_STUDENT_ID, student.getId());
                postsFragment.setArguments(bundle);
                getChildFragmentManager().
                        beginTransaction().add(R.id.menu_fragment_root, postsFragment, "MenuFragments").
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                        addToBackStack(null).commit();
                break;
            case R.id.layout_quizzes:
                appBarLayout.setExpanded(true);
                OnlineQuizzesFragment onlineQuizzesFragment = new OnlineQuizzesFragment();
                Bundle bundleQuiz = new Bundle();
                bundleQuiz.putString(Constants.STUDENT, student.toString());
                onlineQuizzesFragment.setArguments(bundleQuiz);
                getChildFragmentManager().
                        beginTransaction().add(R.id.menu_fragment_root, onlineQuizzesFragment, "MenuFragments").
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                        addToBackStack(null).commit();
                break;
            case R.id.layout_timetable_teacher:
                openTeacherTimeTableActivity();
                break;
        }
    }

    private void openWeeklyPlannerActivity() {
        if (rootClass.getWeeklyPlans().size() > 0) {
            appBarLayout.setExpanded(true);
            WeeklyPlannerFragment weeklyPlannerFragment = new WeeklyPlannerFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_WEEKLY_PLANER, rootClass);
            bundle.putSerializable(Constants.STUDENT, student);
            weeklyPlannerFragment.setArguments(bundle);
            getChildFragmentManager().
                    beginTransaction().add(R.id.menu_fragment_root, weeklyPlannerFragment, "MenuFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        } else {
            activity.showErrorDialog(activity,-3,getParentActivity().getResources().getString(R.string.there_is_no_weekly_planner));
        }
    }

    private void openMessagesActivity() {
        Intent intent = new Intent(getActivity(), ContactTeacherActivity.class);
        Bundle bundle = new Bundle();
        if (SessionManager.getInstance().getUserType()) {
            bundle.putSerializable(Constants.STUDENT, student);
        } else {
            bundle.putSerializable(Constants.KEY_ACTOR, actor);
        }
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    private void openGradesActivity() {

        GradesFragment gradesFragment = new GradesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, courseGroups);
        gradesFragment.setArguments(bundle);
        appBarLayout.setExpanded(true);
        getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, gradesFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void openCalendarActivity() {
        appBarLayout.setExpanded(true);
        CalendarFragment calendarFragment = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        calendarFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, calendarFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void openTimeTableActivity() {
        if (nextSlot.isEmpty()) {
            activity.showErrorDialog(activity,-3,getParentActivity().getResources().getString(R.string.there_is_no_time_table));
        } else {
            appBarLayout.setExpanded(true);
            TimetableMainFragment timetableMainFragment = new TimetableMainFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_TOMORROW, (Serializable) tomorrowSlots);
            bundle.putSerializable(Constants.KEY_TODAY, (Serializable) todaySlots);
            bundle.putSerializable(Constants.STUDENT, student);
            timetableMainFragment.setArguments(bundle);
            getChildFragmentManager().
                    beginTransaction().add(R.id.menu_fragment_root, timetableMainFragment, "MenuFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        }
    }

    private void openBehaviourNotesActivity() {
        appBarLayout.setExpanded(true);
        BehaviorNotesMainFragment behaviorNotesMainFragment = new BehaviorNotesMainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_STUDENT_ID, student.getId());
        bundle.putSerializable(Constants.KEY_POSITIVE_NOTES_LIST, (Serializable) positiveBehaviorNotes);
        bundle.putSerializable(Constants.KEY_NEGATIVE_NOTES_LIST, (Serializable) negativeBehaviorNotes);
        bundle.putSerializable(Constants.KEY_OTHER_NOTES_LIST, (Serializable) otherBehaviorNotes);
        bundle.putInt(Constants.KEY_STUDENT_ID, student.getId());
        bundle.putSerializable(Constants.STUDENT, student);
        behaviorNotesMainFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, behaviorNotesMainFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void openAttendanceActivity() {
        appBarLayout.setExpanded(true);
        AttendanceFragment attendanceFragment = new AttendanceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        bundle.putString(Constants.KEY_ATTENDANCE, attendance);
        attendanceFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, attendanceFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }


    private void openNotificationsActivity() {
        Intent myIntent = new Intent(getActivity(), NotificationsActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onChangeLanguageClicked() {
        changeLanguage();
    }

    @Override
    public void onSignOutClicked() {
        getParentActivity().logoutUser(getParentActivity());
    }


    private void changeLanguage() {
        if (LocalHelper.getLanguage(getParentActivity()).equals("ar")) {
            updateViews("en");
        } else {
            updateViews("ar");
        }
    }


    private void updateViews(String languageCode) {

        LocalHelper.setLocale(getParentActivity(), languageCode);
        LocalHelper.getLanguage(getParentActivity());
        restartApp();
    }

    public void restartApp() {
        Intent intent = getParentActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getParentActivity().getBaseContext().getPackageName());
        if (intent != null)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (getParentActivity() != null) {
            (this).getParentActivity().finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 0);

    }

//    private void openAnnouncement() {
//        Intent intent = new Intent(getActivity(), AnnouncementActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.KEY_ACTOR, actor);
//        intent.putExtra(Constants.KEY_BUNDLE, bundle);
//        startActivity(intent);
//    }
//
//
//    private void getNotifications(boolean pagination) {
//        if (!activity.isCalling) {
//            getParentActivity().showLoadingDialog();
//        }
//        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
//                SessionManager.getInstance().getUserId() + "/notifications";
//        studentDetailView.getNotifications(url, 1, 1);
//    }
//
//    private void getAnnouncement() {
//        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnnouncementUrl(1, actor.actableType, 1);
//        studentDetailView.getAnnouncement(url);
//    }

    private void checkVersionOnStore() {
        AppUpdater appUpdater = new AppUpdater(getParentActivity())
                .setDisplay(Display.DIALOG)
                .showEvery(1)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable(getParentActivity().getResources().getString(R.string.update_is_available))
                .setContentOnUpdateAvailable(getParentActivity().getResources().getString(R.string.check_latest_version))
                .setButtonUpdate(getParentActivity().getResources().getString(R.string.cancel))
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setButtonDismiss("")
                .setButtonDoNotShowAgain(getParentActivity().getResources().getString(R.string.update_now))
                .setButtonDoNotShowAgainClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openStore();
                    }
                })
                .setCancelable(false); // Dialog could not be;
        appUpdater.start();
    }

    private void openStore() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getParentActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getParentActivity().getPackageName())));
        }
    }

    private void openAssignmentDetailActivity() {
        CourseAssignmentFragment courseAssignmentFragment = new CourseAssignmentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        courseAssignmentFragment.setArguments(bundle);
        appBarLayout.setExpanded(true);
        getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, courseAssignmentFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void openTeacherTimeTableActivity() {
        if (nextSlot.isEmpty()) {
            activity.showErrorDialog(activity,-3,getParentActivity().getResources().getString(R.string.there_is_no_time_table));
        } else {
            appBarLayout.setExpanded(true);
            TimetableMainFragment timetableMainFragment = new TimetableMainFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_TOMORROW, (Serializable) tomorrowSlots);
            bundle.putSerializable(Constants.KEY_TODAY, (Serializable) todaySlots);
            bundle.putSerializable(Constants.STUDENT, student);
            timetableMainFragment.setArguments(bundle);
            getChildFragmentManager().
                    beginTransaction().add(R.id.menu_fragment_root, timetableMainFragment, "MenuFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Boolean isStudent = SessionManager.getInstance().getStudentAccount();
        Boolean isParent = SessionManager.getInstance().getUserType() && !isStudent;
        if (isStudent) {
            if (activity.pager.getCurrentItem() == 0) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if (isParent) {
            if (activity.pager.getCurrentItem() == 3) {
                if(getChildFragmentManager().getFragments().size()==0){
                    getActivity().finish();
                    return;
                }
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (activity.pager.getCurrentItem() == 4) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
