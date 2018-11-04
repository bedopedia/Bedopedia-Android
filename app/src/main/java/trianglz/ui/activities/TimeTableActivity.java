package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import java.util.List;

import trianglz.models.Student;
import trianglz.models.TimeTableSlot;
import trianglz.utils.Constants;

public class TimeTableActivity extends SuperActivity {
    private Student student;
    private List<TimeTableSlot> tomorrowSlots;
    private List<TimeTableSlot> todaySlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        getValueFromIntent();
    }

    private void getValueFromIntent() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_BUNDLE);
         student = (Student) bundle.getSerializable(Constants.STUDENT);
         todaySlots = (List<TimeTableSlot>) bundle.getSerializable(Constants.KEY_TODAY);
        tomorrowSlots = (List<TimeTableSlot>) bundle.getSerializable(Constants.KEY_TOMORROW);


    }
}
