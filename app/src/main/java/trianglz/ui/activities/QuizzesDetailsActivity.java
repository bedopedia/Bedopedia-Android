package trianglz.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import trianglz.models.AssignmentsDetail;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.models.Student;
import trianglz.ui.adapters.QuizzesDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class QuizzesDetailsActivity extends SuperActivity implements View.OnClickListener, QuizzesDetailsPresenter, QuizzesDetailsAdapter.QuizzesDetailsInterface {

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
    private boolean teacherMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_details);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (!teacherMode) {
            showLoadingDialog();
            quizzesDetailsView.getQuizzesDetails(student.getId(), quizzCourse.getId());
        }
    }

    private void getValueFromIntent() {
        if (getIntent().getBooleanExtra(Constants.KEY_TEACHERS, false)) {
            quizzes = getIntent().getParcelableArrayListExtra(Constants.KEY_QUIZZES);
            teacherMode = true;
            courseName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
            courseGroupName = getIntent().getStringExtra(Constants.KEY_COURSE_GROUP_NAME);
            return;
        }
        student = Student.create(getIntent().getStringExtra(Constants.STUDENT));
        quizzCourse = QuizzCourse.create(getIntent().getStringExtra(Constants.KEY_COURSE_QUIZZES));
//        assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ASSIGNMENTS);
        courseName = quizzCourse.getCourseName();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        if (!teacherMode) setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = findViewById(R.id.recycler_view);
        if (teacherMode) {
            adapter = new QuizzesDetailsAdapter(this, this, courseGroupName);
        } else {
            adapter = new QuizzesDetailsAdapter(this, this, courseName);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10,this),false));
        headerTextView = findViewById(R.id.tv_header);
        headerTextView.setText(courseName);
        if (quizzes != null) adapter.addData(getArrayList(true));

        // radio button for segment control
        segmentedGroup = findViewById(R.id.segmented);
        openButton = findViewById(R.id.btn_open);
        closedButton = findViewById(R.id.btn_closed);
        segmentedGroup.check(openButton.getId());
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        if (!teacherMode) {
            quizzesDetailsView = new QuizzesDetailsView(this, this);
            quizzes = new ArrayList<>();
        }

    }


    private void setListeners() {
        backBtn.setOnClickListener(this);
        openButton.setOnClickListener(this);
        closedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_open:
                adapter.addData(getArrayList(true));
                break;
            case R.id.btn_closed:
                adapter.addData(getArrayList(false));
                break;
        }
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
            Picasso.with(this)
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
    public void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        this.quizzes = quizzes;
        adapter.addData(getArrayList(true));
    }

    @Override
    public void onGetQuizzesDetailsFailure() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClicked(Quizzes quizzes) {
        if (!teacherMode) {
            Intent intent = new Intent(this, SingleQuizActivity.class);
            intent.putExtra(Constants.KEY_QUIZZES, quizzes.toString());
            intent.putExtra(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
            startActivity(intent);
        }
    }
}
