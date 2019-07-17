package trianglz.ui.fragments;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import trianglz.ui.activities.AnnouncementActivity;
import trianglz.ui.activities.AttendanceActivity;
import trianglz.ui.activities.BehaviorNotesActivity;
import trianglz.ui.activities.ContactTeacherActivity;
import trianglz.ui.activities.CourseAssignmentActivity;
import trianglz.ui.activities.GradesActivity;
import trianglz.ui.activities.NotificationsActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.activities.TimetableActivity;
import trianglz.ui.activities.WeeklyPlannerActivity;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements StudentDetailPresenter,
        View.OnClickListener,SettingsDialog.SettingsDialogInterface {
    
    //fragment root view
    private View rootView;

    // parent activity 
    private  StudentMainActivity activity;
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
            positiveCounterTextView, negativeCounterTextView,otherCounterTextView, attendanceTextView;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private String studentName = "";
    private StudentDetailView studentDetailView;
    private LinearLayout attendanceLayout, timeTableLayout, gradesLayout, behaviourNotesLayout,weeklyPlannerLayout, assignmentsLayout;
    private String attendance;
    private int absentDays;
    private com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar progressBar;
    private ArrayList<JSONArray> attendanceList;


    private LinearLayout parentLayout,teacherLayout;
    private TextView notificationTextView,annoucmentTextView,messagesTextView;
    private LinearLayout notificationLayout,annoucmentLayout,messagesLayout;
    private Actor actor;
    private String actorName = "";
    private ImageButton settingsBtn;
    private RelativeLayout studentHeaderLayout,actorHeaderLayout;
    private SettingsDialog settingsDialog;
    private TextView notifcationCounterTextView,announcementCounterTextView,messagesCounterTextView;
    private RootClass rootClass;
    private TextView weeklyPlannerTextView;
    private ImageButton studentSettingsButton;


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
        if(SessionManager.getInstance().getUserType()){
            String courseUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/course_groups";
            if (Util.isNetworkAvailable(getActivity())) {
                studentDetailView.getStudentCourses(courseUrl);
                activity.showLoadingDialog();
            } else {
                Util.showNoInternetConnectionDialog(getActivity());
            }
        }else{
            checkVersionOnStore();
        }
        return rootView;
        
    }
    

    private void bindViews() {

        todaySlots = new ArrayList<>();
        tomorrowSlots = new ArrayList<>();
        positiveBehaviorNotes = new ArrayList<>();
        negativeBehaviorNotes = new ArrayList<>();
        otherBehaviorNotes = new ArrayList<>();
        courseGroups = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        nameTextView = rootView.findViewById(R.id.tv_name);
        levelTextView = rootView.findViewById(R.id.tv_level);
        studentImageView = rootView.findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        nextSlotTextView = rootView.findViewById(R.id.tv_time_table);
        studentGradeTextView = rootView.findViewById(R.id.tv_grade);
        attendanceLayout = rootView.findViewById(R.id.layout_attendance);
        weeklyPlannerLayout = rootView.findViewById(R.id.layout_weekly_planner);
        timeTableLayout = rootView.findViewById(R.id.layout_timetable);
        gradesLayout = rootView.findViewById(R.id.layout_grades);
        behaviourNotesLayout = rootView.findViewById(R.id.layout_behavior_notes);
        studentDetailView = new StudentDetailView(getActivity(), this);
        positiveCounterTextView = rootView.findViewById(R.id.tv_positive_counter);
        negativeCounterTextView = rootView.findViewById(R.id.tv_negative_counter);
        otherCounterTextView = rootView.findViewById(R.id.tv_other_counter);
        progressBar = rootView.findViewById(R.id.progress_bar);
        attendanceTextView = rootView.findViewById(R.id.tv_attendance);
        parentLayout = rootView.findViewById(R.id.layout_parent);
        teacherLayout = rootView.findViewById(R.id.layout_teacher);
        notificationTextView = rootView.findViewById(R.id.tv_notification);
        notificationLayout = rootView.findViewById(R.id.layout_notification);
        messagesTextView = rootView.findViewById(R.id.tv_message);
        messagesLayout = rootView.findViewById(R.id.layout_messages);
        annoucmentLayout = rootView.findViewById(R.id.layout_annoucment);
        annoucmentTextView = rootView.findViewById(R.id.tv_annoucment);
        settingsBtn = rootView.findViewById(R.id.btn_setting);
        studentHeaderLayout = rootView.findViewById(R.id.layout_student_header);
        actorHeaderLayout = rootView.findViewById(R.id.layout_actor_header);
        settingsDialog = new SettingsDialog(getActivity(), R.style.SettingsDialog, this);
        notifcationCounterTextView = rootView.findViewById(R.id.tv_notifcation_counter);
        messagesCounterTextView = rootView.findViewById(R.id.tv_messages_counter);
        announcementCounterTextView = rootView.findViewById(R.id.tv_announcement_counter);
        weeklyPlannerTextView = rootView.findViewById(R.id.tv_weekly_planner);
        studentSettingsButton = rootView.findViewById(R.id.btn_setting_student);

        student = activity.getStudent();
        actor = activity.getActor();
        attendance = activity.getAttendance();

        // new assigmnets layout
        assignmentsLayout = rootView.findViewById (R.id.layout_assignments);
    }

    private void setListeners() {
        attendanceLayout.setOnClickListener(this);
        weeklyPlannerLayout.setOnClickListener(this);
        timeTableLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        behaviourNotesLayout.setOnClickListener(this);
        assignmentsLayout.setOnClickListener(this);

        //actor listeners
        notificationLayout.setOnClickListener(this);
        messagesLayout.setOnClickListener(this);
        annoucmentLayout.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
        studentSettingsButton.setOnClickListener(this);
    }

    private void setParentActorView() {
        if(!SessionManager.getInstance().getUserType()){
            parentLayout.setVisibility(View.GONE);
            teacherLayout.setVisibility(View.VISIBLE);
            actorName = actor.firstName + " " + actor.lastName;
            setStudentImage(actor.imageUrl,actorName);
            nameTextView.setText(actorName);
            levelTextView.setText(actor.actableType);
            actorHeaderLayout.setVisibility(View.VISIBLE);
            studentHeaderLayout.setVisibility(View.GONE);
            String notificationText = SessionManager.getInstance().getNotficiationCounter() + " "+getResources().getString(R.string.unread_notifications);
            notificationTextView.setText(notificationText);
            if(SessionManager.getInstance().getNotficiationCounter() > 0){
                notifcationCounterTextView.setVisibility(View.VISIBLE);
                notifcationCounterTextView.setText(SessionManager.getInstance().getNotficiationCounter()+"");
            }else {
                notifcationCounterTextView.setVisibility(View.INVISIBLE);
            }
        }else {
            parentLayout.setVisibility(View.VISIBLE);
            teacherLayout.setVisibility(View.GONE);
            studentName = student.firstName + " " + student.lastName;
            setAttendance();
            setStudentImage(student.getAvatar(), studentName);
            nameTextView.setText(studentName);
            levelTextView.setText(student.level);
            actorHeaderLayout.setVisibility(View.GONE);
            studentHeaderLayout.setVisibility(View.VISIBLE);
            if(SessionManager.getInstance().getStudentAccount()){
                studentSettingsButton.setVisibility(View.VISIBLE);
            }else {
                studentSettingsButton.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!SessionManager.getInstance().getUserType()){
            if(Util.isNetworkAvailable(getActivity())){
                getNotifications(false);
                activity.showLoadingDialog();
            }else {
                Util.showNoInternetConnectionDialog(getActivity());
            }
        }
        if (!SessionManager.getInstance().getUserType()) {
            if (SessionManager.getInstance().getNotficiationCounter() > 0) {
                notifcationCounterTextView.setVisibility(View.VISIBLE);
                notifcationCounterTextView.setText(SessionManager.getInstance().getNotficiationCounter()+"");
            }else {
                notifcationCounterTextView.setVisibility(View.INVISIBLE);
            }

        }
    }
    

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(getActivity())
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
                progressBar.setProgress(((attendanceDates.size()-absentDays) * 100) / attendanceDates.size());
            String attendance = getResources().getString(R.string.attend) + " " + (
                    attendanceDates.size()-absentDays)  +
                    " " + getResources().getString(R.string.out) + " " + attendanceDates.size() + " " +
                    getResources().getString(R.string.days);
            attendanceTextView.setText(attendance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetStudentCourseGroupSuccess(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
        String url = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/grade_certificate";
        studentDetailView.getStudentGrades(url, courseGroups);
    }

    @Override
    public void onGetStudentCourseGroupFailure(String message, int errorCode) {
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }

    @Override
    public void onGetStudentGradesSuccess(ArrayList<trianglz.models.CourseGroup> courseGroups, String totalGrade) {
        this.courseGroups = courseGroups;
        totalGrade = getResources().getString(R.string.average_grade) + " " + totalGrade;
        studentGradeTextView.setText(totalGrade);
        String timeTableUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/timetable";
        studentDetailView.getStudentTimeTable(timeTableUrl);
    }

    @Override
    public void onGetStudentGradesFailure(String message, int errorCode) {
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }

    @Override
    public void oneGetTimeTableSuccess(ArrayList<Object> timeTableData) {
        String nextSlot = (String) timeTableData.get(2);
        todaySlots = (List<TimeTableSlot>) timeTableData.get(0);
        tomorrowSlots = (List<TimeTableSlot>) timeTableData.get(1);
        if(nextSlot.isEmpty()){
            nextSlotTextView.setText(getResources().getString(R.string.there_is_no_time_table));
            timeTableLayout.setClickable(false);
        }else {
            timeTableLayout.setClickable(true);
            nextSlotTextView.setText(nextSlot);
        }

        String url = SessionManager.getInstance().getBaseUrl() + "/api/behavior_notes";
        studentDetailView.getStudentBehavioursNotes(url, student.getId() + "");
    }

    @Override
    public void onGetTimeTableFailure(String message, int errorCode) {
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }

    @Override
    public void onGetBehaviorNotesSuccess(HashMap<String, List<BehaviorNote>> behaviorNoteHashMap) {
        positiveBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_POSITIVE);
        negativeBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_NEGATIVE);
        otherBehaviorNotes = behaviorNoteHashMap.get(Constants.OTHER);
        String positiveCounter = positiveBehaviorNotes.size() + "";
        String negativeCounter = negativeBehaviorNotes.size() + "";
        String otherCounter = otherBehaviorNotes.size() +"";
        positiveCounterTextView.setText(positiveCounter);
        negativeCounterTextView.setText(negativeCounter);
        otherCounterTextView.setText(otherCounter);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",new Locale("en"));
        Date date = new Date();
        String currentDate = (dateFormat.format(date));
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getWeeklyPlanerUrl(currentDate);
        studentDetailView.getWeeklyPlanner(url);

    }

    @Override
    public void onGetBehaviorNotesFailure(String message, int errorCode) {
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity. showErrorDialog(getActivity());
        }

    }

    @Override
    public void onGetWeeklyPlannerSuccess(RootClass rootClass) {
        this.rootClass = rootClass;
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if(rootClass.getWeeklyPlans().size() > 0){
            weeklyPlannerTextView.setText(Util.getWeeklPlannerText(rootClass.getWeeklyPlans().get(0).getStartDate(),
                    rootClass.getWeeklyPlans().get(0).getEndDate(),getActivity()));
        }else {
            weeklyPlannerTextView.setText(R.string.there_is_no_weekly_planner);
        }

    }

    @Override
    public void onGetWeeklyPlannerFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(getActivity());
        } else {
            activity.showErrorDialog(getActivity());
        }
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notificationArrayList) {
        if(notificationArrayList.size()>0){
            Notification notification = notificationArrayList.get(0);
            notificationTextView.setText(notification.getMessage());
        }else {
            notificationTextView.setText(getResources().getString(R.string.there_is_no_notifications));
        }

        if(Util.isNetworkAvailable(getActivity())){
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
            studentDetailView.getMessages(url,SessionManager.getInstance().getId());
        }else {
            if(activity.progress.isShowing()){
                activity. progress.dismiss();
            }
            Util.showNoInternetConnectionDialog(getActivity());
        }


    }

    @Override
    public void onGetNotificationFailure(String message, int errorCode) {
        if(Util.isNetworkAvailable(getActivity())){
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
            studentDetailView.getMessages(url,SessionManager.getInstance().getId());
        }else {
            if(activity.progress.isShowing()){
                activity.progress.dismiss();
            }
            Util.showNoInternetConnectionDialog(getActivity());
        }

        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<MessageThread> messageArrayList, int unreadMessageCount) {
        if(messageArrayList.size() > 0){
            MessageThread messageThread = messageArrayList.get(0);
            if(messageThread.messageArrayList.size()>0){
                Message latestMessage = messageThread.messageArrayList.get(0);
                String body = android.text.Html.fromHtml(latestMessage.body).toString();
                body = StringEscapeUtils.unescapeJava(body);
                messagesTextView.setText(body);
            }
        }else {
            messagesTextView.setText(getResources().getString(R.string.there_is_no_messages));
        }
        if (unreadMessageCount> 0) {
            messagesCounterTextView.setVisibility(View.VISIBLE);
            messagesCounterTextView.setText(unreadMessageCount+"");
        }else {
            messagesCounterTextView.setVisibility(View.INVISIBLE);
        }
        if(Util.isNetworkAvailable(getActivity())){
            getAnnouncement();
        }else {
            if(activity.progress.isShowing()){
                activity.progress.dismiss();
            }
            Util.showNoInternetConnectionDialog(getActivity());
        }

    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        if(Util.isNetworkAvailable(getActivity())){
            getAnnouncement();
        }else {
            if(activity.progress.isShowing()){
                activity.progress.dismiss();
            }
            Util.showNoInternetConnectionDialog(getActivity());
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }


    @Override
    public void onGetAnnouncementsSuccess(ArrayList<Announcement> announcementArrayList) {
        if(announcementArrayList.size()>0){
            Announcement announcement = announcementArrayList.get(0);
            String body = android.text.Html.fromHtml(announcement.body).toString();
            body = StringEscapeUtils.unescapeJava(body);
            annoucmentTextView.setText(body);
        }else {
            annoucmentTextView.setText(getResources().getString(R.string.there_is_no_announcements));
        }
        if(activity.progress.isShowing()){
            activity. progress.dismiss();
        }
    }

    @Override
    public void onGetAnnouncementsFailure(String message, int errorCode) {
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            activity.logoutUser(getActivity());
        }else {
            activity.showErrorDialog(getActivity());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_timetable:
                openTimeTableActivity();
                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
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
                openAnnouncement();
                break;
            case R.id.btn_setting:
                settingsDialog.show();
                break;
            case R.id.layout_weekly_planner:
                openWeeklyPlannerActivity();
                break;
            case R.id.btn_setting_student:
                if(!settingsDialog.isShowing()){
                    settingsDialog.show();
                }
                break;
            case R.id.tv_assignment:
                openAssignmentDetailActivity();
                break;
            case R.id.layout_assignments:
                openAssignmentDetailActivity();
                break;
        }
    }

    private void openWeeklyPlannerActivity() {
        if(rootClass.getWeeklyPlans().size() > 0 ) {
            Intent myIntent = new Intent(getActivity(), WeeklyPlannerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_WEEKLY_PLANER, rootClass);
            bundle.putSerializable(Constants.STUDENT, student);
            myIntent.putExtra(Constants.KEY_BUNDLE, bundle);
            startActivity(myIntent);
        }
    }

    private void openMessagesActivity() {
        Intent intent = new Intent(getActivity(), ContactTeacherActivity.class);
        Bundle bundle = new Bundle();
        if(SessionManager.getInstance().getUserType()){
            bundle.putSerializable(Constants.STUDENT,student);
        }else {
            bundle.putSerializable(Constants.KEY_ACTOR,actor);
        }
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }

    private void openGradesActivity() {
        Intent gradesIntent = new Intent(getActivity(), GradesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, courseGroups);
        gradesIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(gradesIntent);
    }

    private void openTimeTableActivity() {
        Intent timeTableIntent = new Intent(getActivity(), TimetableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_TOMORROW, (Serializable) tomorrowSlots);
        bundle.putSerializable(Constants.KEY_TODAY, (Serializable) todaySlots);
        bundle.putSerializable(Constants.STUDENT, student);
        timeTableIntent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(timeTableIntent);

    }

    private void openBehaviourNotesActivity() {
        Intent behaviorNotesIntent = new Intent(getActivity(), BehaviorNotesActivity.class);
        behaviorNotesIntent.putExtra(Constants.KEY_STUDENT_ID, student.getId());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_POSITIVE_NOTES_LIST, (Serializable) positiveBehaviorNotes);
        bundle.putSerializable(Constants.KEY_NEGATIVE_NOTES_LIST, (Serializable) negativeBehaviorNotes);
        bundle.putSerializable(Constants.KEY_OTHER_NOTES_LIST, (Serializable) otherBehaviorNotes);
        bundle.putInt(Constants.KEY_STUDENT_ID, student.getId());
        bundle.putSerializable(Constants.STUDENT,student);
        behaviorNotesIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(behaviorNotesIntent);
    }

    private void openAttendanceActivity() {
        Intent attendanceIntent = new Intent(getActivity(), AttendanceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        bundle.putString(Constants.KEY_ATTENDANCE, attendance);
        attendanceIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(attendanceIntent);
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
        activity.logoutUser(getActivity());
    }


    private void changeLanguage() {
        if (LocalHelper.getLanguage(getActivity()).equals("ar")) {
            updateViews("en");
        } else {
            updateViews("ar");
        }
    }


    private void updateViews(String languageCode) {

        LocalHelper.setLocale(getActivity(), languageCode);
        LocalHelper.getLanguage(getActivity());
        restartApp();
    }

    public void restartApp() {
        Intent intent = getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (getActivity() instanceof Activity) {
            (this).getActivity().finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 0);

    }



    private void openAnnouncement() {
        Intent intent = new Intent(getActivity(), AnnouncementActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ACTOR,actor);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }


    private void getNotifications(boolean pagination) {
        activity.showLoadingDialog();
        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                SessionManager.getInstance().getUserId() + "/notifications";
        studentDetailView.getNotifications(url, 1, 1);
    }

    private void getAnnouncement() {

        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnnouncementUrl(1,actor.actableType,1);
        studentDetailView.getAnnouncement(url);

    }


    private void checkVersionOnStore() {
        AppUpdater appUpdater = new AppUpdater(getActivity())
                .setDisplay(Display.DIALOG)
                .showEvery(1)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable(getResources().getString(R.string.update_is_available))
                .setContentOnUpdateAvailable(getResources().getString(R.string.check_latest_version))
                .setButtonUpdate(getResources().getString(R.string.cancel))
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setButtonDismiss("")
                .setButtonDoNotShowAgain(getResources().getString(R.string.update_now))
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
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + activity.getPackageName())));
        }
    }

    private void openAssignmentDetailActivity(){
        Intent intent = new Intent(getActivity(), CourseAssignmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }
}
