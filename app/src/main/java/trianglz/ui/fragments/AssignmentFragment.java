package trianglz.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

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
    private HtmlTextView courseDescriptionTextView;
    private RecyclerView recyclerView;
    private AttachmentAdapter adapter;
    private SingleAssignment singleAssignment;
    private CardView cardView;
    private ImageButton backBtn;

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
        if (singleAssignment.getName() == null || singleAssignment.getName().isEmpty()) {
            courseNameTextView.setText(getResources().getString(R.string.assignments));
        } else {
            courseNameTextView.setText(singleAssignment.getName());
        }
        cardView = rootView.findViewById(R.id.card_view);
        courseDescriptionTextView = rootView.findViewById(R.id.tv_course_description);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if (singleAssignment.getContent() == null || singleAssignment.getContent().isEmpty()) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            courseDescriptionTextView.setHtml(singleAssignment.getContent(), new HtmlHttpImageGetter(courseDescriptionTextView
            ));
        }
        adapter = new AttachmentAdapter(activity, this);
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