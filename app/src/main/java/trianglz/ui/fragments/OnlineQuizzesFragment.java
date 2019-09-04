package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.OnlineQuizzesPresenter;
import trianglz.core.views.OnlineQuizzesView;
import trianglz.models.AssignmentsDetail;
import trianglz.models.QuizzCourse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.OnlineQuizzesAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class OnlineQuizzesFragment extends Fragment implements View.OnClickListener, OnlineQuizzesAdapter.OnlineQuizzesInterface, OnlineQuizzesPresenter {
    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private OnlineQuizzesAdapter adapter;
    private ArrayList<AssignmentsDetail> assignmentsDetailArrayList;
    private String courseName = "";
    private TextView headerTextView;
    private RadioButton openButton, closedButton;
    private SegmentedGroup segmentedGroup;
    // networking view
    private OnlineQuizzesView onlineQuizzesView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_online_quizzes, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        activity.showLoadingDialog();
        onBackPress();
        onlineQuizzesView.getQuizzesCourses(student.getId());
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = Student.create(bundle.getString(Constants.STUDENT));
        }
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new OnlineQuizzesAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        onlineQuizzesView = new OnlineQuizzesView(activity, this);
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

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onGetQuizzesCoursesSuccess(ArrayList<QuizzCourse> quizzCourses) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        adapter.addData(quizzCourses);
    }

    @Override
    public void onGetQuizzesCoursesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
    }


    @Override
    public void onItemClicked(QuizzCourse quizzCourse) {
       // openQuizzesDetailsActivity(quizzCourse);
    }
    private void openQuizzesDetailsActivity(QuizzCourse quizzCourse) {
//        Intent intent = new Intent(this, QuizzesDetailsActivity.class);
//        intent.putExtra(Constants.STUDENT, student.toString());
//        intent.putExtra(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
//        startActivity(intent);
    }
}
