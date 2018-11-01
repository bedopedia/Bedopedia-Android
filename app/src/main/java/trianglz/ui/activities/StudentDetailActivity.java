package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.views.AvatarView;
import grades.CourseGroup;
import timetable.TimetableSlot;
import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.models.BehaviorNote;
import trianglz.utils.Constants;

public class StudentDetailActivity extends SuperActivity {

    private List<TimeTableSlot> todaySlots;
    private   List<TimeTableSlot> tomorrowSlots;
    private   List<BehaviorNote> positiveNotesList;
    private static List<BehaviorNote> negativeNotesList;
    private ArrayList<CourseGroup> courseGroups;
    private Student student;
    private AvatarView studentImage;


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

    }

    private void setListeners() {

    }

    private void setStudentData() {

    }


    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.STUDENT).getSerializable(Constants.STUDENT);
    }
}
