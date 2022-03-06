package trianglz.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.ErrorDialog;
import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.core.views.GradeDetailView;
import trianglz.models.GradeBook;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradeDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradeDetailFragment extends Fragment implements View.OnClickListener, GradeDetailPresenter, ErrorDialog.DialogConfirmationInterface, GradeDetailAdapter.GradeDetailsAdapterInterface {

    private View rootView;
    private StudentMainActivity activity;
    private PostsResponse courseGroup;
    private RecyclerView recyclerView;
    private GradeDetailAdapter gradeDetailAdapter;
    private Student student;
    private int gradingPeriodId;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private ImageButton backBtn;
    private GradeDetailView gradeDetailView;
    private TextView subjectHeaderTextView;
    private FrameLayout listFrameLayout, placeholderFrameLayout;
    private SkeletonScreen skeletonScreen;
    // add segmented group


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_grade_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (Util.isNetworkAvailable(activity)) {
            gradeDetailView.getGradesDetails(student.actableId, courseGroup.getCourseId(), courseGroup.getId(), gradingPeriodId);
            showShimmer();
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }

    private void showShimmer() {
        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(gradeDetailAdapter)
                .load(R.layout.skeleton_grades_details_layout)
                .count(16)
                .angle(45)
                .color(R.color.white_70)
                .show();
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroup = (PostsResponse) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            gradingPeriodId = (int) bundle.getSerializable(Constants.KEY_GRADING_PERIOD_ID);
        }
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        gradeDetailAdapter = new GradeDetailAdapter(activity,this);
        recyclerView.setAdapter(gradeDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        imageLoader = new PicassoLoader();
        setStudentImage();
        backBtn = rootView.findViewById(R.id.btn_back);
        gradeDetailView = new GradeDetailView(activity, this);
        subjectHeaderTextView = rootView.findViewById(R.id.tv_subject_header);
        subjectHeaderTextView.setText(courseGroup.getCourseName());
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);

    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void setStudentImage() {
        final String imageUrl = student.avatar;
        final String name = student.firstName + " " + student.lastName;
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .noPlaceholder()
                    .fit()
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
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }




    @Override
    public void onConfirm() {
        Log.d("", "onConfirm: ");
        getParentFragment().getChildFragmentManager().popBackStack();

    }

    @Override
    public void onCancel() {
        Log.d("", "onConfirm: ");

    }

    @Override
    public void onGetGradesDetailsSuccess(GradeBook gradeBook) {
        gradeDetailAdapter.addData(gradeBook);
        skeletonScreen.hide();
    }

    @Override
    public void onGetGradesDetailsFailure(String message, int errorCode) {
        placeholderFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        skeletonScreen.hide();
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void arrayStatus(boolean isEmpty) {
        if (isEmpty) {
            placeholderFrameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            placeholderFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
