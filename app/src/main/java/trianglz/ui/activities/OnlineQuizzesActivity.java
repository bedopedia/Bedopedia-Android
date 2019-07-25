package trianglz.ui.activities;

import android.content.Intent;
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
import trianglz.core.presenters.OnlineQuizzesPresenter;
import trianglz.core.views.OnlineQuizzesView;
import trianglz.models.AssignmentsDetail;
import trianglz.models.QuizzCourse;
import trianglz.models.Student;
import trianglz.ui.adapters.OnlineQuizzesAdapter;
import trianglz.utils.Constants;

public class OnlineQuizzesActivity extends SuperActivity implements View.OnClickListener, OnlineQuizzesAdapter.OnlineQuizzesInterface, OnlineQuizzesPresenter {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_quizzes);
        getValueFromIntent();
        bindViews();
        setListeners();
        showLoadingDialog();
        onlineQuizzesView.getQuizzesCourses(student.getId());
    }

    private void getValueFromIntent() {
        student = Student.create(getIntent().getStringExtra(Constants.STUDENT));
    }
    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new OnlineQuizzesAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        onlineQuizzesView = new OnlineQuizzesView(this, this);
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

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
    private void openQuizzesDetailsActivity(QuizzCourse quizzCourse) {
        Intent intent = new Intent(this,QuizzesDetailsActivity.class);
        intent.putExtra(Constants.STUDENT, student.toString());
        intent.putExtra(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
        startActivity(intent);
    }

    @Override
    public void onGetQuizzesCoursesSuccess(ArrayList<QuizzCourse> quizzCourses) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        adapter.addData(quizzCourses);
    }

    @Override
    public void onGetQuizzesCoursesFailure() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onItemClicked(QuizzCourse quizzCourse) {
        openQuizzesDetailsActivity(quizzCourse);

    }
}