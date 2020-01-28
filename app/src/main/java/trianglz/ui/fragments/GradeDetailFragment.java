package trianglz.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.ErrorDialog;
import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.core.views.GradeDetailView;
import trianglz.managers.SessionManager;
import trianglz.models.GradesDetailsResponse;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradeDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradeDetailFragment extends Fragment implements View.OnClickListener, GradeDetailPresenter, ErrorDialog.DialogConfirmationInterface {

    private View rootView;
    private StudentMainActivity activity;
    private PostsResponse courseGroup;
    private RecyclerView recyclerView;
    private GradeDetailAdapter gradeDetailAdapter;
    private Student student;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private ImageButton backBtn;
    private GradeDetailView gradeDetailView;
    private TextView subjectHeaderTextView;
    private FrameLayout listFrameLayout, placeholderFrameLayout;
    // add segmented group
    private SegmentedGroup segmentedGroup;
    private RadioButton allBtn, currentBtn;

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
            activity.showLoadingDialog();
            gradeDetailView.getGradesDetails(student.userId, courseGroup.getCourseId(), courseGroup.getId());
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroup = (PostsResponse) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        gradeDetailAdapter = new GradeDetailAdapter(activity);
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

        segmentedGroup = rootView.findViewById(R.id.segmented);
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        // radio buttons
        allBtn = rootView.findViewById(R.id.btn_all);
        currentBtn = rootView.findViewById(R.id.btn_current);
        segmentedGroup.check(allBtn.getId());
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        currentBtn.setOnClickListener(this);
    }

    private void setStudentImage() {
        final String imageUrl = student.getAvatar();
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
            case R.id.btn_all:
//                gradeDetailAdapter.addData(allSemestersList);
                break;
            case R.id.btn_current:
//                gradeDetailAdapter.addData(currentSemester);
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
    public void onGetGradesDetailsSuccess(GradesDetailsResponse gradesDetailsResponse) {
        gradeDetailAdapter.addData(gradesDetailsResponse);
        activity.progress.dismiss();
    }

    @Override
    public void onGetGradesDetailsFailure(String message, int errorCode) {
        activity.progress.dismiss();
    }
}
