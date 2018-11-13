package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import trianglz.core.presenters.StudentDetailPresenter;
import trianglz.core.views.StudentDetailView;
import trianglz.managers.SessionManager;
import trianglz.models.BehaviorNote;
import trianglz.models.CourseGroup;
import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class StudentDetailActivity extends SuperActivity implements StudentDetailPresenter, View.OnClickListener {

    private List<TimeTableSlot> todaySlots;
    private List<TimeTableSlot> tomorrowSlots;
    private List<BehaviorNote> positiveBehaviorNotes;
    private List<BehaviorNote> negativeBehaviorNotes;
    private ArrayList<trianglz.models.CourseGroup> courseGroups;
    private Student student;
    private AvatarView studentImage;
    private ArrayList<Student> studentArrayList;
    private ArrayList<Attendance> attendanceArrayList;
    private TextView nameTextView, levelTextView, nextSlotTextView, studentGradeTextView,
            positiveCounterTextView, negativeCounterTextView, attendanceTextView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
        setStudentData();

    }

    private void bindViews() {
        todaySlots = new ArrayList<>();
        tomorrowSlots = new ArrayList<>();
        positiveBehaviorNotes = new ArrayList<>();
        negativeBehaviorNotes = new ArrayList<>();
        courseGroups = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        nameTextView = findViewById(R.id.tv_name);
        studentName = student.firstName + " " + student.lastName;
        nameTextView.setText(studentName);
        levelTextView = findViewById(R.id.tv_level);
        levelTextView.setText(student.level);
        studentImageView = findViewById(R.id.img_student);
        imageLoader = new PicassoLoader();
        setStudentImage(student.getAvatar(), studentName);
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
        progressBar = findViewById(R.id.progress_bar);
        attendanceTextView = findViewById(R.id.tv_attendance);
        quizzesTextView = findViewById(R.id.tv_quizzes);
        assignmentsTextView = findViewById(R.id.tv_assignment);
        eventsTextView = findViewById(R.id.tv_events);
        setAttendance();
        setBottomText(student);
        String courseUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/course_groups";
        if (Util.isNetworkAvailable(this)) {
            studentDetailView.getStudentCourses(courseUrl);
            showLoadingDialog();
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
        messagesBtn = findViewById(R.id.btn_messages);
        notificationBtn = findViewById(R.id.btn_notification);
    }

    private void setListeners() {
        attendanceLayout.setOnClickListener(this);
        timeTableLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        behaviourNotesLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        messagesBtn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);
    }

    private void setStudentData() {

    }


    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        attendance = (String) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ATTENDANCE);
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
    public void onGetStudentCourseGroupFailure(String message, int code) {
        progress.dismiss();
    }

    @Override
    public void onGetStudentGradesSuccess(ArrayList<trianglz.models.CourseGroup> courseGroups, String totalGrade) {
        totalGrade = getResources().getString(R.string.average_grade) + " " + totalGrade;
        studentGradeTextView.setText(totalGrade);
        String timeTableUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/timetable";
        studentDetailView.getStudentTimeTable(timeTableUrl);
    }

    @Override
    public void onGetStudentGradesFailure(String message, int code) {
        progress.dismiss();
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
    public void onGetTimeTableFailure(String message, int code) {
        progress.dismiss();
    }

    @Override
    public void onGetBehaviorNotesSuccess(HashMap<String, List<BehaviorNote>> behaviorNoteHashMap) {
        positiveBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_POSITIVE);
        negativeBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_NEGATIVE);
        addBehaviourNotesDummyData();
        String positiveCounter = positiveBehaviorNotes.size() + "";
        String negativeCounter = negativeBehaviorNotes.size() + "";
        positiveCounterTextView.setText(positiveCounter);
        negativeCounterTextView.setText(negativeCounter);
        progress.dismiss();
    }

    @Override
    public void onGetBehaviorNotesFailure(String message, int code) {
        progress.dismiss();

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
        behaviorNotesIntent.putExtras(bundle);
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

    private void addBehaviourNotesDummyData() {
        //adding dummy data while waiting to api to get fixed
        positiveBehaviorNotes.add(new BehaviorNote("Mr.Adham napolsi", "very good boi","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        positiveBehaviorNotes.add(new BehaviorNote("Dr.khalil el mohammady", "what a wonderful world","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        positiveBehaviorNotes.add(new BehaviorNote("jannet el maghraby", "this kid is a natural","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        positiveBehaviorNotes.add(new BehaviorNote("Mr.Adham napolsi", "very good boi","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        positiveBehaviorNotes.add(new BehaviorNote("Dr.khalil el mohammady", "what a wonderful world","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        positiveBehaviorNotes.add(new BehaviorNote("jannet el maghraby", "this kid is a natural","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("Mr.Adham napolsi", "pls help","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("Dr.khalil el mohammady", "get him decapitated","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("jannet el maghraby", "so badd","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("Mr.Adham napolsi", "pls help","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("Dr.khalil el mohammady", "get him decapitated","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
        negativeBehaviorNotes.add(new BehaviorNote("jannet el maghraby", "so badd","Thanks alot for being were there when nobody gave me a prayer of succuss no one to support, build and craft. You were here almost for 2 years day and nights working really hard, catching the moon without losing the ground. I’m grateful for your appreciation of situation difficulty. \n" +
                "This achievement will never happen without your efforts."));
    }


    private void openNotificationsActivity() {
        Intent myIntent = new Intent(StudentDetailActivity.this, NotificationsActivity.class);
        startActivity(myIntent);
    }

}
