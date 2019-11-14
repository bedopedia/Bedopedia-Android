package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.Objects;

import trianglz.components.TopItemDecoration;
import trianglz.models.UploadedObject;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AttachmentsActivity extends SuperActivity implements AttachmentAdapter.AttachmentAdapterInterface {
    private String subjectName;
    ArrayList<UploadedObject> uploadedObjects;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private AttachmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments);
        getDataFromIntent();
        bindViews();
    }

        private void bindViews() {
            recyclerView = findViewById(R.id.recycler_view);
            toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            courseNameTextView = findViewById(R.id.tv_course_name);
            courseNameTextView.setText(subjectName);
            adapter = new AttachmentAdapter(this, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, this), false));
            adapter.addData(uploadedObjects);
    }

    private void getDataFromIntent() {
        uploadedObjects = new ArrayList<>();
        for (String string : Objects.requireNonNull(getIntent().getBundleExtra(Constants.KEY_BUNDLE).getStringArrayList(Constants.KEY_UPLOADED_OBJECTS))) {
            uploadedObjects.add(UploadedObject.create(string));
        }
        subjectName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
    }

    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }
}
