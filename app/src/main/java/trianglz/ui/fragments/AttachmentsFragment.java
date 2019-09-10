package trianglz.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.Objects;

import trianglz.models.UploadedObject;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 08/09/2019.
 */
public class AttachmentsFragment extends Fragment implements AttachmentAdapter.AttachmentAdapterInterface {
    private StudentMainActivity activity;
    private View rootView;
    private String subjectName;
    ArrayList<UploadedObject> uploadedObjects;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private AttachmentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_attachments, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataFromIntent();
        bindViews();
    //    onBackPress();
    }

    private void getDataFromIntent() {
        uploadedObjects = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            for (String string : Objects.requireNonNull(bundle.getStringArrayList(Constants.KEY_UPLOADED_OBJECTS))) {
                uploadedObjects.add(UploadedObject.create(string));
            }
            subjectName = bundle.getString(Constants.KEY_COURSE_NAME);

        }
    }
    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });
        courseNameTextView = rootView.findViewById(R.id.tv_course_name);
        courseNameTextView.setText(subjectName);
        adapter = new AttachmentAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter.addData(uploadedObjects);
    }
    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    activity.getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }
}
