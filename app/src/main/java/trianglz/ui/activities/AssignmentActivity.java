package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import org.w3c.dom.Text;

import trianglz.models.AssignmentsDetail;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;

public class AssignmentActivity extends SuperActivity implements AttachmentAdapter.AttachmentAdapterInterface {
    private AssignmentsDetail assignmentsDetail;
    private TextView courseNameTextView, courseDescriptionTextView;
    private RecyclerView recyclerView;
    private AttachmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ReadIntent();
        bindViews();
        setListeners();
    }

    private void ReadIntent() {
        assignmentsDetail = AssignmentsDetail.create(getIntent().getStringExtra(Constants.KEY_ASSIGNMENT_DETAIL));
    }

    private void bindViews() {
        courseNameTextView = findViewById(R.id.tv_course_name);
        courseDescriptionTextView = findViewById (R.id.tv_course_description);
        courseNameTextView.setText(assignmentsDetail.getName());
        courseDescriptionTextView.setText(assignmentsDetail.getDescription());
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(this, this);

    }
    private void setListeners() {

    }

    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }
}
