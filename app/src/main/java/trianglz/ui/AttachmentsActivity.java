package trianglz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import trianglz.models.UploadedObject;
import trianglz.utils.Constants;

public class AttachmentsActivity extends AppCompatActivity {
    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments);
        ArrayList<UploadedObject> uploadedObjects = new ArrayList<>();
        for (String string : Objects.requireNonNull(getIntent().getBundleExtra(Constants.KEY_BUNDLE).getStringArrayList(Constants.KEY_UPLOADED_OBJECTS))) {
            uploadedObjects.add(UploadedObject.create(string));
        }
        subjectName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);

    }
}
