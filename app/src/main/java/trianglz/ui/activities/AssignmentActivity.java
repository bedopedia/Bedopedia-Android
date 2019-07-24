package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import trianglz.models.AssignmentsDetail;
import trianglz.utils.Constants;

public class AssignmentActivity extends SuperActivity {
    private AssignmentsDetail assignmentsDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ReadIntent();

    }

    private void ReadIntent() {
        assignmentsDetail = AssignmentsDetail.create(getIntent().getStringExtra(Constants.KEY_ASSIGNMENT_DETAIL));
    }
}
