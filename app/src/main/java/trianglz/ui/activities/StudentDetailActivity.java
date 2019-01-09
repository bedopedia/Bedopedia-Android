package trianglz.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import trianglz.models.Actor;
import trianglz.models.BehaviorNote;
import trianglz.models.CourseGroup;
import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class StudentDetailActivity extends SuperActivity implements StudentDetailPresenter,
        View.OnClickListener,SettingsDialog.SettingsDialogInterface {

    private List<TimeTableSlot> todaySlots;
    private List<TimeTableSlot> tomorrowSlots;
    private List<BehaviorNote> positiveBehaviorNotes;
    private List<BehaviorNote> negativeBehaviorNotes;
    private List<BehaviorNote> otherBehaviorNotes;
    private ArrayList<trianglz.models.CourseGroup> courseGroups;
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
    private LinearLayout attendanceLayout, timeTableLayout, gradesLayout, behaviourNotesLayout;
    private ImageButton backBtn;
    private Button messagesBtn;
    private String attendance;
    private int absentDays;
    private com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar progressBar;
    private ArrayList<JSONArray> attendanceList;
    private TextView quizzesTextView, assignmentsTextView, eventsTextView;
    private ImageButton notificationBtn;
    private ImageView redCircleImageView;


    private LinearLayout parentLayout,teacherLayout;
    private TextView notificationTextView,annoucmentTextView,messagesTextView;
    private LinearLayout notificationLayout,annoucmentLayout,messagesLayout;
    private RelativeLayout bottomLayout;
    private Actor actor;
    private String actorName = "";
    private ImageButton settingsBtn;
    private LinearLayout studentHeaderLayout,actorHeaderLayout;
    private SettingsDialog settingsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
        setParentActorView();
        if(SessionManager.getInstance().getUserType()){
            String courseUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/course_groups";
            if (Util.isNetworkAvailable(this)) {
                studentDetailView.getStudentCourses(courseUrl);
                showLoadingDialog();
            } else {
                Util.showNoInternetConnectionDialog(this);
            }
        }else {
            // TODO: 1/9/19 call new end point
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
        nameTextView = findViewById(R.id.tv_name);
        levelTextView = findViewById(R.id.tv_level);
        studentImageView = findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        nextSlotTextView = findViewById(R.id.tv_time_table);
        studentGradeTextView = findViewById(R.id.tv_grade);
        attendanceLayout = findViewById(R.id.layout_attendance);
        timeTableLayout = findViewById(R.id.layout_timetable);
        gradesLayout = findViewById(R.id.layout_grades);
        behaviourNotesLayout = findViewById(R.id.layout_behavior_notes);
        studentDetailView = new StudentDetailView(this, this);
        backBtn = findViewById(R.id.btn_back);
        positiveCounterTextView = findViewById(R.id.tv_positive_counter);
        negativeCounterTextView = findViewById(R.id.tv_negative_counter);
        otherCounterTextView = findViewById(R.id.tv_other_counter);
        progressBar = findViewById(R.id.progress_bar);
        attendanceTextView = findViewById(R.id.tv_attendance);
        quizzesTextView = findViewById(R.id.tv_quizzes);
        assignmentsTextView = findViewById(R.id.tv_assignment);
        eventsTextView = findViewById(R.id.tv_events);
        messagesBtn = findViewById(R.id.btn_messages);
        notificationBtn = findViewById(R.id.btn_notification);
        redCircleImageView = findViewById(R.id.img_red_circle);
        parentLayout = findViewById(R.id.layout_parent);
        teacherLayout = findViewById(R.id.layout_teacher);
        notificationTextView = findViewById(R.id.tv_notification);
        notificationLayout = findViewById(R.id.layout_notification);
        messagesTextView = findViewById(R.id.tv_message);
        messagesLayout = findViewById(R.id.layout_messages);
        annoucmentLayout = findViewById(R.id.layout_annoucment);
        annoucmentTextView = findViewById(R.id.tv_annoucment);
        bottomLayout = findViewById(R.id.layout_bottom);
        settingsBtn = findViewById(R.id.btn_setting);
        studentHeaderLayout = findViewById(R.id.layout_student_header);
        actorHeaderLayout = findViewById(R.id.layout_actor_header);
        settingsDialog = new SettingsDialog(this, R.style.SettingsDialog, this);


    }

    private void setListeners() {
        attendanceLayout.setOnClickListener(this);
        timeTableLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        behaviourNotesLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        messagesBtn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);

        //actor listeners
        notificationLayout.setOnClickListener(this);
        messagesLayout.setOnClickListener(this);
        annoucmentLayout.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    private void setParentActorView() {
        if(!SessionManager.getInstance().getUserType()){
            parentLayout.setVisibility(View.GONE);
            teacherLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
            messagesBtn.setVisibility(View.INVISIBLE);
            actorName = actor.firstName + " " + actor.lastName;
            setStudentImage(actor.imageUrl,actorName);
            nameTextView.setText(actorName);
            levelTextView.setText(actor.actableType);
            actorHeaderLayout.setVisibility(View.VISIBLE);
            studentHeaderLayout.setVisibility(View.INVISIBLE);
        }else {
            parentLayout.setVisibility(View.VISIBLE);
            teacherLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
            studentName = student.firstName + " " + student.lastName;
            setAttendance();
            setBottomText(student);
            setStudentImage(student.getAvatar(), studentName);
            nameTextView.setText(studentName);
            levelTextView.setText(student.level);
            messagesBtn.setVisibility(View.VISIBLE);
            actorHeaderLayout.setVisibility(View.INVISIBLE);
            studentHeaderLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(SessionManager.getInstance().getNotficiationCounter()>0){
            redCircleImageView.setVisibility(View.VISIBLE);
        }else {
            redCircleImageView.setVisibility(View.GONE);
        }
    }

    private void getValueFromIntent() {
        if(SessionManager.getInstance().getUserType()){
            student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
            attendance = (String) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ATTENDANCE);
        }else {
            actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
        }

    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
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
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
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
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
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
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
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
        if(progress.isShowing())
        progress.dismiss();
    }

    @Override
    public void onGetBehaviorNotesFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_timetable:
                openTimeTableActivity();
                break;
            case R.id.btn_back:
                onBackPressed();
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
                // TODO: 1/9/19 openNotificationLayout
                break;
            case R.id.layout_messages:
                // TODO: 1/9/19 open message layout
                openMessagesActivity();
                break;
            case R.id.layout_annoucment:
                // TODO: 1/9/19 open annoucment layout
                break;
            case R.id.btn_setting:
                settingsDialog.show();
                break;
        }
    }

    private void openMessagesActivity() {
        Intent intent = new Intent(this,ContactTeacherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }

    private void openGradesActivity() {
        Intent gradesIntent = new Intent(this, GradesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, courseGroups);
        gradesIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(gradesIntent);
    }

    private void openTimeTableActivity() {
        Intent timeTableIntent = new Intent(this, TimetableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_TOMORROW, (Serializable) tomorrowSlots);
        bundle.putSerializable(Constants.KEY_TODAY, (Serializable) todaySlots);
        bundle.putSerializable(Constants.STUDENT, student);
        timeTableIntent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(timeTableIntent);

    }

    private void openBehaviourNotesActivity() {
        Intent behaviorNotesIntent = new Intent(this, BehaviorNotesActivity.class);
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
        Intent attendanceIntent = new Intent(this, AttendanceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        bundle.putString(Constants.KEY_ATTENDANCE, attendance);
        attendanceIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(attendanceIntent);
    }

    private void setBottomText(Student student) {
        String quizzes = String.valueOf(student.getTodayQuizzesCount()) + " " + getResources().getString(R.string.quizzes);
        quizzesTextView.setText(quizzes);
        String assignments = String.valueOf(student.getTodayAssignmentsCount()) + " " + getResources().getString(R.string.assignments);
        assignmentsTextView.setText(assignments);
        String events = String.valueOf(student.getTodayEventsCount()) + " " + getResources().getString(R.string.events);
        eventsTextView.setText(events);
    }



    private void openNotificationsActivity() {
        Intent myIntent = new Intent(StudentDetailActivity.this, NotificationsActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onChangeLanguageClicked() {
        changeLanguage();
    }

    @Override
    public void onSignOutClicked() {
        logoutUser(this);
    }


    private void changeLanguage() {
        if (LocalHelper.getLanguage(this).equals("ar")) {
            updateViews("en");
        } else {
            updateViews("ar");
        }
    }


    private void updateViews(String languageCode) {

        LocalHelper.setLocale(this, languageCode);
        LocalHelper.getLanguage(this);
        restartApp();
    }

    public void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (this instanceof Activity) {
            (this).finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 0);

    }
}
