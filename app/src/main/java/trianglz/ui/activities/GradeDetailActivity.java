package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.skolera.skolera_android.R;

import trianglz.models.CourseGroup;
import trianglz.ui.adapters.GradeDetailAdapter;
import trianglz.utils.Constants;

public class GradeDetailActivity extends AppCompatActivity {
    CourseGroup courseGroup;
    private RecyclerView recyclerView;
    private GradeDetailAdapter gradeDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);
        getValueFromIntent();
        bindViews();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void getValueFromIntent() {
        courseGroup = (CourseGroup) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_COURSE_GROUPS);
    }
}
