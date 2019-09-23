package trianglz.ui.fragments;

import android.graphics.Color;
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
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.QuizzesDetailsPresenter;
import trianglz.core.views.QuizzesDetailsView;
import trianglz.managers.SessionManager;
import trianglz.models.CourseGroups;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.QuizzesDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class QuizzesDetailsFragment extends Fragment implements View.OnClickListener, QuizzesDetailsPresenter, QuizzesDetailsAdapter.QuizzesDetailsInterface {

    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private QuizzesDetailsAdapter adapter;
    private String courseName = "";
    private String courseGroupName = "";
    private TextView headerTextView;
    private RadioButton openButton, closedButton;
    private SegmentedGroup segmentedGroup;
    private QuizzCourse quizzCourse;
    private ArrayList<Quizzes> quizzes;
    private QuizzesDetailsView quizzesDetailsView;
    private CourseGroups courseGroup;
    private boolean teacherMode = false;
    private int lastPage = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_quizzes_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (!teacherMode) {
            activity.showLoadingDialog();
            quizzesDetailsView.getQuizzesDetails(student.getId(), quizzCourse.getId(),1);
        }
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            if (bundle.getBoolean(Constants.KEY_TEACHERS, false)) {
                quizzes = bundle.getParcelableArrayList(Constants.KEY_QUIZZES);
                teacherMode = true;
                courseName = bundle.getString(Constants.KEY_COURSE_NAME);
                courseGroupName = bundle.getString(Constants.KEY_COURSE_GROUP_NAME);
                courseGroup = CourseGroups.create(bundle.getString(Constants.KEY_COURSE_GROUPS));
                return;
            }
            student = Student.create(bundle.getString(Constants.STUDENT));
            quizzCourse = QuizzCourse.create(bundle.getString(Constants.KEY_COURSE_QUIZZES));
//        assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ASSIGNMENTS);
            courseName = quizzCourse.getCourseName();
        }
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        if (!teacherMode) setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if (teacherMode) {
            adapter = new QuizzesDetailsAdapter(activity, this, courseGroupName);
        } else {
            adapter = new QuizzesDetailsAdapter(activity, this, courseName);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10,activity),false));
        headerTextView = rootView.findViewById(R.id.tv_header);
        headerTextView.setText(courseName);
        if (quizzes != null) adapter.addData(getArrayList(true));

        // radio button for segment control
        segmentedGroup = rootView.findViewById(R.id.segmented);
        openButton = rootView.findViewById(R.id.btn_open);
        closedButton = rootView.findViewById(R.id.btn_closed);
        segmentedGroup.check(openButton.getId());
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        if (!teacherMode) {
            quizzesDetailsView = new QuizzesDetailsView(activity, this);
            quizzes = new ArrayList<>();
        }

    }


    private void setListeners() {
        backBtn.setOnClickListener(this);
        openButton.setOnClickListener(this);
        closedButton.setOnClickListener(this);
    }
    private ArrayList<Quizzes> getArrayList(boolean isOpen) {
        if (quizzes.isEmpty()) return null ;
        ArrayList<Quizzes> filteredQuizzes = new ArrayList<>();
        for (Quizzes quiz : quizzes) {
            if (quiz.getState().equals("running")) {
                if (isOpen) filteredQuizzes.add(quiz);
            } else {
                if (!isOpen) filteredQuizzes.add(quiz);
            }
        }
        return filteredQuizzes;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.btn_open:
                adapter.addData(getArrayList(true));
                break;
            case R.id.btn_closed:
                adapter.addData(getArrayList(false));
                break;
        }
    }

    @Override
    public void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        this.quizzes = quizzes;
        adapter.addData(getArrayList(true));
    }

    @Override
    public void onGetQuizzesDetailsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void onItemClicked(Quizzes quizzes) {
        if (!teacherMode) {
            SingleQuizFragment singleQuizFragment = new SingleQuizFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_QUIZZES, quizzes.toString());
            bundle.putString(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
            singleQuizFragment.setArguments(bundle);
            getParentFragment().getChildFragmentManager().
                    beginTransaction().add(R.id.menu_fragment_root, singleQuizFragment,"MenuFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        } else {
            GradingFragment gradingFragment = new GradingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_QUIZ_ID, quizzes.getId());
            if (courseGroup != null) {
                bundle.putInt(Constants.KEY_COURSE_ID, courseGroup.getCourseId());
                bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroup.getId());
            }
            bundle.putBoolean(Constants.KEY_ASSIGNMENTS_GRADING, false);
            bundle.putString(Constants.KEY_QUIZ_NAME, quizzes.getName());
            gradingFragment.setArguments(bundle);
            getParentFragment().getChildFragmentManager().
                    beginTransaction().add(R.id.course_root, gradingFragment,"CoursesFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();        }
    }

    @Override
    public void loadNextPage(int page) {
        if (lastPage != page) {
            quizzesDetailsView.getQuizzesDetails(student.getId(), quizzCourse.getId(),page);
            lastPage = page;
        }
    }
}
