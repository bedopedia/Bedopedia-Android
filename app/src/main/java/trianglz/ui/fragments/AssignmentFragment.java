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
import trianglz.core.presenters.SingleAssignmentPresenter;
import trianglz.core.views.SingleAssignmentView;
import trianglz.models.AssignmentsDetail;
import trianglz.models.SingleAssignment;
import trianglz.models.UploadedObject;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class AssignmentFragment extends Fragment implements AttachmentAdapter.AttachmentAdapterInterface, SingleAssignmentPresenter, View.OnClickListener {

    private StudentMainActivity activity;
    private View rootView;
    private AssignmentsDetail assignmentsDetail;
    private TextView courseNameTextView;
    private HtmlTextView courseDescriptionTextView;
    private RecyclerView recyclerView;
    private AttachmentAdapter adapter;
    private SingleAssignmentView singleAssignmentView;
    private int courseId;
    private String studentName;
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
        ReadIntent();
        bindViews();
        setListeners();
        activity.showLoadingDialog();
        singleAssignmentView.showAssignment(courseId, assignmentsDetail.getId());
    }


    private void ReadIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            assignmentsDetail = AssignmentsDetail.create(bundle.getString(Constants.KEY_ASSIGNMENT_DETAIL));
            courseId = bundle.getInt(Constants.KEY_COURSE_ID, 0);
            studentName = bundle.getString(Constants.KEY_STUDENT_NAME);
        }
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        courseNameTextView = rootView.findViewById(R.id.tv_course_name);
        courseDescriptionTextView = rootView.findViewById(R.id.tv_course_description);
        cardView = rootView.findViewById(R.id.card_view);
        if (assignmentsDetail.getName() == null || assignmentsDetail.getName().isEmpty()) {
            courseNameTextView.setText(getResources().getString(R.string.assignments));
        } else {
            courseNameTextView.setText(assignmentsDetail.getName());
        }
        if (assignmentsDetail.getDescription() == null || assignmentsDetail.getDescription().isEmpty()) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            courseDescriptionTextView.setHtml(assignmentsDetail.getDescription(),
                    new HtmlHttpImageGetter(courseDescriptionTextView));
        }
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16, activity), false));
        singleAssignmentView = new SingleAssignmentView(activity, this);
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
    public void onShowAssignmentSuccess(SingleAssignment singleAssignment) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        ArrayList<UploadedObject> uploadedObjects = new ArrayList<>(Arrays.asList(singleAssignment.getUploadedFiles()));
        adapter.addData(uploadedObjects);
    }

    @Override
    public void onShowAssignmentFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");


    }

    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }
}
