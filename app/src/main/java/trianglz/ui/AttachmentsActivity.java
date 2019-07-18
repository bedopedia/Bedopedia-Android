package trianglz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import trianglz.models.UploadedObject;
import trianglz.utils.Constants;

public class AttachmentsActivity extends AppCompatActivity {
    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments);
        ArrayList<UploadedObject> uploadedObjects = (ArrayList<UploadedObject>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_UPLOADED_OBJECTS);
        subjectName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);

    }
}
