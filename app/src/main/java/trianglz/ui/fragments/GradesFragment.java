package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.GradesPresenter;
import trianglz.core.views.GradesView;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradesAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradesFragment extends Fragment implements GradesAdapter.GradesAdapterInterface, View.OnClickListener, GradesPresenter {

    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private RecyclerView recyclerView;
    private GradesAdapter gradesAdapter;
    private Student student;
    private IImageLoader imageLoader;
    private GradesView gradesView;
    private StudentMainActivity activity;
    private FrameLayout listFrameLayout, placeholderFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_grades, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        gradesView = new GradesView(getActivity(), this);
        if (!activity.progress.isShowing()) activity.progress.show();
        gradesView.getGradesCourses(student.userId);
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        backBtn = rootView.findViewById(R.id.btn_back);
        studentImageView = rootView.findViewById(R.id.img_student);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);
        gradesAdapter = new GradesAdapter(activity, this);
        imageLoader = new PicassoLoader();
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
        recyclerView.setAdapter(gradesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onSubjectSelected(PostsResponse postsResponse) {
        openGradeDetailFragment(postsResponse);
    }

    private void openGradeDetailFragment(PostsResponse postsResponse) {

        GradeDetailFragment gradeDetailFragment = new GradeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, postsResponse);
        bundle.putSerializable(Constants.STUDENT, student);
        gradeDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, gradeDetailFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void onGetGradesCoursesSuccess(ArrayList<PostsResponse> arrayList) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        if (arrayList.isEmpty()) {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else {
            listFrameLayout.setVisibility(View.VISIBLE);
            placeholderFrameLayout.setVisibility(View.GONE);
            gradesAdapter.addData(arrayList);

        }
    }

    @Override
    public void onGetGradesCoursesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode,"");
        listFrameLayout.setVisibility(View.GONE);
        placeholderFrameLayout.setVisibility(View.VISIBLE);
    }
}
