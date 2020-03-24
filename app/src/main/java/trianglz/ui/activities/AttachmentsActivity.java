package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ImageButton backImageButton;
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
            backImageButton = findViewById(R.id.btn_back);
            courseNameTextView = findViewById(R.id.tv_course_name);
            courseNameTextView.setText(subjectName);
            adapter = new AttachmentAdapter(this, this);
            adapter.type = Constants.TYPE_ATTACHMENT;
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, this), false));
            adapter.addData(uploadedObjects);
            backImageButton.setOnClickListener(v -> onBackPressed());

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
