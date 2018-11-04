package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import attendance.Attendance;
import timetable.Fragments.TodayFragment;
import timetable.Fragments.TomorrowFragment;
import timetable.TimetableActivity;
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

public class StudentDetailActivity extends SuperActivity implements StudentDetailPresenter,View.OnClickListener {

    private List<TimeTableSlot> todaySlots;
    private List<TimeTableSlot> tomorrowSlots;
    private List<BehaviorNote> positiveNotesList;
    private static List<BehaviorNote> negativeNotesList;
    private ArrayList<trianglz.models.CourseGroup> courseGroups;
    private Student student;
    private AvatarView studentImage;
    private ArrayList<Student> studentArrayList;
    private ArrayList<Attendance> attendanceArrayList;
    private TextView nameTextView, levelTextView, nextSlotTextView,studentGradeTextView,
    positiveCounterTextView,negativeCounterTextView;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private String studentName = "";
    private StudentDetailView studentDetailView;
    private LinearLayout attendanceLayout,timeTableLayout,gradesLayout,behaviourNotesLayout;
    private ImageButton backBtn;


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
        positiveNotesList = new ArrayList<>();
        negativeNotesList = new ArrayList<>();
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
        setAttendance();
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
        String courseUrl = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/course_groups";
        if (Util.isNetworkAvailable(this)) {
            studentDetailView.getStudentCourses(courseUrl);
            showLoadingDialog();
        } else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    private void setListeners() {
        attendanceLayout.setOnClickListener(this);
        timeTableLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        behaviourNotesLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    private void setStudentData() {

    }


    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.STUDENT).getSerializable(Constants.STUDENT);
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
        // TODO: 11/4/2018 aly set time attendance
    }

    @Override
    public void onGetStudentCourseGroupSuccess(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
        String url = SessionManager.getInstance().getBaseUrl() + "/api/students/" + student.getId() + "/grade_certificate";
        studentDetailView.getStudentGrades(url,courseGroups);
    }

    @Override
    public void onGetStudentCourseGroupFailure(String message, int code) {
        progress.dismiss();
    }

    @Override
    public void onGetStudentGradesSuccess(ArrayList<trianglz.models.CourseGroup> courseGroups,String totalGrade) {
        totalGrade = getResources().getString(R.string.average_grade) + " " +totalGrade;
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
        nextSlotTextView.setText(nextSlot);
        String url = SessionManager.getInstance().getBaseUrl() + "/api/behavior_notes";
        studentDetailView.getStudentBehavioursNotes(url,student.getId()+"");
    }

    @Override
    public void onGetTimeTableFailure(String message, int code) {
        progress.dismiss();
    }

    @Override
    public void onGetBehaviorNotesSuccess( HashMap<String,List<BehaviorNote>> behaviorNoteHashMap) {
        List<BehaviorNote> positiveBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_POSITIVE);
        List<BehaviorNote> negativeBehaviorNotes = behaviorNoteHashMap.get(Constants.KEY_NEGATIVE);
        String positiveCounter = positiveBehaviorNotes.size()+"";
        String negativeCounter = negativeBehaviorNotes.size()+"";
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
        switch (view.getId()){
            case R.id.layout_timetable:
                openTimeTableActivity();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void openTimeTableActivity(){
        Intent timeTableIntent = new Intent(this, TimeTableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_TOMORROW ,(Serializable)tomorrowSlots);
        bundle.putSerializable(Constants.KEY_TODAY, (Serializable) todaySlots);
        bundle.putSerializable(Constants.STUDENT,student);
        timeTableIntent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(timeTableIntent);

    }
}
