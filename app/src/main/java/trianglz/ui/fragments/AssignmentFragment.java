package trianglz.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.Arrays;

import trianglz.components.TopItemDecoration;
import trianglz.models.SingleAssignment;
import trianglz.models.UploadedObject;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class AssignmentFragment extends Fragment implements AttachmentAdapter.AttachmentAdapterInterface, View.OnClickListener {

    private StudentMainActivity activity;
    private View rootView;
    private TextView courseNameTextView;
    private RecyclerView recyclerView;
    private AttachmentAdapter adapter;
    private SingleAssignment singleAssignment;
    private ImageButton backBtn;
    private CardView cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_assignment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readIntent();
        bindViews();
        setListeners();
    }


    private void readIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            singleAssignment = SingleAssignment.create(bundle.getString(Constants.KEY_ASSIGNMENT_DETAIL));
        }
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        courseNameTextView = rootView.findViewById(R.id.tv_course_name);
        cardView = rootView.findViewById(R.id.card_view);
        if (singleAssignment.getName() == null || singleAssignment.getName().isEmpty()) {
            courseNameTextView.setText(getResources().getString(R.string.assignments));
        } else {
            courseNameTextView.setText(singleAssignment.getName());
        }
        cardView.setVisibility(View.GONE);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(activity, this);
        adapter.type = Constants.TYPE_ASSIGNMENT;
        if (singleAssignment.getContent() == null || singleAssignment.getContent().isEmpty()) {
            adapter.assignmentDescription = "";
        } else {
            adapter.assignmentDescription = singleAssignment.getContent();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16, activity), false));
        ArrayList<UploadedObject> uploadedObjects = new ArrayList<>(Arrays.asList(singleAssignment.getUploadedFiles()));
        adapter.addData(uploadedObjects);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }
}
