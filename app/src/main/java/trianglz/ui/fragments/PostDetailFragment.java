package trianglz.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.FragmentCommunicationInterface;
import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.views.PostDetailsView;
import trianglz.managers.SessionManager;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.ui.activities.PostReplyActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.PostDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 08/09/2019.
 */
public class PostDetailFragment extends Fragment implements FragmentCommunicationInterface,PostDetailsPresenter, View.OnClickListener, PostDetailsAdapter.PostDetailsInterface {

    private StudentMainActivity activity;
    private View rootView;
    private int courseGroupId;
    private String courseName;
    private RecyclerView recyclerView;
    private PostDetailsView postDetailsView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private PostDetailsAdapter adapter;
    private String subjectName;
    private int courseId;
    private int lastPage = 0;

    private FloatingActionButton addPostFab;
    private boolean isStudent, isParent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_post_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValuesFromIntent();
        bindViews();
        setListeners();
    }


    private void getValuesFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseName = bundle.getString(Constants.KEY_COURSE_NAME);
            courseGroupId = bundle.getInt(Constants.KEY_COURSE_GROUP_ID, 0);
            courseId = bundle.getInt(Constants.KEY_COURSE_ID, 0);

        }
    }

    private void bindViews() {
        isStudent = SessionManager.getInstance().getStudentAccount();
        isParent = SessionManager.getInstance().getUserType();
        addPostFab = rootView.findViewById(R.id.add_post_btn);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        postDetailsView = new PostDetailsView(activity, this);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
        if (!isStudent && !isParent) {
            addPostFab.setVisibility(View.VISIBLE);
        }
        courseNameTextView = rootView.findViewById(R.id.tv_course_name);
        courseNameTextView.setText(courseName);
        adapter = new PostDetailsAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16, activity), false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(16, activity), false));
    }

    private void setListeners() {
        addPostFab.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.showLoadingDialog();
        postDetailsView.getPostDetails(courseId, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_post_btn:
                  openCreatePostActivity();
                break;
        }
    }

    @Override
    public void onGetPostDetailsSuccess(ArrayList<PostDetails> postDetails, int page) {
        adapter.addData(postDetails, page);
        if (activity.progress.isShowing()) activity.progress.dismiss();
    }

    @Override
    public void onGetPostDetailsFailure() {
        if (activity.progress.isShowing()) activity.progress.dismiss();

    }

    @Override
    public void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects) {
        AttachmentsFragment attachmentsFragment = new AttachmentsFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> uploadedObjectStrings = new ArrayList<>();
        for (UploadedObject uploadedObject : uploadedObjects) {
            uploadedObjectStrings.add(uploadedObject.toString());
        }
        if (!uploadedObjectStrings.isEmpty())
            bundle.putStringArrayList(Constants.KEY_UPLOADED_OBJECTS, uploadedObjectStrings);
        bundle.putString(Constants.KEY_COURSE_NAME, courseName);
        attachmentsFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, attachmentsFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void loadNextPage(int page) {
        if (lastPage != page) {
            postDetailsView.getPostDetails(courseId, page);
            lastPage = page;
        }
    }

    @Override
    public void onCardClicked(PostDetails postDetails) {
        Intent intent = new Intent(activity, PostReplyActivity.class);
        intent.putExtra(Constants.KEY_COURSE_NAME, courseName);
        intent.putExtra(Constants.POST_DETAILS, postDetails.toString());
        startActivity(intent);
    }

    private void openCreatePostActivity() {
        CreateTeacherPostFragment createTeacherPostFragment = CreateTeacherPostFragment.newInstance(this);
        Bundle bundle= new Bundle();
        bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroupId);
        createTeacherPostFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.course_root, createTeacherPostFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();

    }

    @Override
    public void reloadEvents() {
        activity.showLoadingDialog();
        postDetailsView.getPostDetails(courseId, 1);
    }
}
