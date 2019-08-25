package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SingleQuizActivity extends SuperActivity implements View.OnClickListener {
    private TextView subjectNameTextView, dateTextView,
            assignmentNameTextView, dayTextView, monthTextView, publishedTextView;
    private IImageLoader imageLoader;
    private Button solveQuizBtn;
    private LinearLayout dateLinearLayout;
    private LinearLayout cardView;
    private TextView headTextView;
    private QuizzCourse course;
    private Quizzes quizzes;
    private AvatarView avatarView;
    private ImageButton backButton;

    // grades variables
    private View quizGradeView, quizNotStartedView;
    private TextView gradeTextView, outOfTextView, noteTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quiz);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void setListeners() {
        solveQuizBtn.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        quizzes = Quizzes.create(getIntent().getStringExtra(Constants.KEY_QUIZZES));
        course = QuizzCourse.create(getIntent().getStringExtra(Constants.KEY_COURSE_QUIZZES));

    }

    private void bindViews() {
        headTextView = findViewById(R.id.tv_quiz_name);
        headTextView.setText(course.getCourseName());
        subjectNameTextView = findViewById(R.id.tv_subject_name);
        dateTextView = findViewById(R.id.tv_date);
        assignmentNameTextView = findViewById(R.id.tv_assignment_name);
        imageLoader = new PicassoLoader();
        dayTextView = findViewById(R.id.tv_day_number);
        monthTextView = findViewById(R.id.tv_month);
        dateLinearLayout = findViewById(R.id.ll_date);
        publishedTextView = findViewById(R.id.tv_published);
        cardView = findViewById(R.id.card_view);
        avatarView = findViewById(R.id.img_student);
        solveQuizBtn = findViewById(R.id.solve_quiz_btn);
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(course.getCourseName()), "Path url");
        // views
        quizGradeView = findViewById(R.id.v_quiz_grade);
        quizNotStartedView = findViewById(R.id.v_not_started_quiz);

        gradeTextView = findViewById(R.id.tv_score);
        outOfTextView = findViewById(R.id.tv_out_of);
        noteTextview = findViewById(R.id.tv_note);
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(this);
        DateTime dateTime = new DateTime(quizzes.getStartDate());
        if (quizzes.getName() != null) {
            subjectNameTextView.setText(course.getCourseName());
        }
        if (quizzes.getEndDate() != null) {
            dateTextView.setText(Util.getPostDateAmPm(quizzes.getEndDate(), this));
        }

        if (quizzes.getEndDate() != null) {
            dayTextView.setText(Util.getAssigmentDetailEndDateDay(quizzes.getEndDate()));
            monthTextView.setText(Util.getAssigmentDetailEndDateMonth(quizzes.getEndDate(), this));
        }
        if (quizzes.getName() != null) {
            assignmentNameTextView.setText(quizzes.getName());
        }
        if (quizzes.getState() != null) {
            if (quizzes.getState().equals("running")) {
                dateLinearLayout.setBackground(this.getResources().getDrawable(R.drawable.curved_light_sage));
            } else {
                dateLinearLayout.setBackground(this.getResources().getDrawable(R.drawable.curved_red));
            }
        } else {
            dateTextView.setVisibility(View.INVISIBLE);
        }

        String published = this.getString(R.string.published) + " " + Util.getPostDate(dateTime.toString(), this);
        publishedTextView.setText(published);

        if (quizzes.getState().equals("running")) {
            quizNotStartedView.setVisibility(View.VISIBLE);
            quizGradeView.setVisibility(View.GONE);
        } else {
            quizNotStartedView.setVisibility(View.GONE);
            quizGradeView.setVisibility(View.VISIBLE);
            if (quizzes.getStudentSubmissions() != null) {
                gradeTextView.setText(quizzes.getStudentSubmissions().getScore() + "");
                outOfTextView.setText(getResources().getString(R.string.out_of) + " " + quizzes.getTotalScore() + "");
                String feedBack = quizzes.getStudentSubmissions().getFeedback();
                if (feedBack != null) {
                    noteTextview.setText(quizzes.getStudentSubmissions().getFeedback());
                } else {
                    noteTextview.setVisibility(View.INVISIBLE);
                }
            } else {
                gradeTextView.setText("--");
                outOfTextView.setText(getResources().getString(R.string.out_of) + " " + quizzes.getTotalScore() + "");
                noteTextview.setText(getResources().getString(R.string.no_submission));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (view.getId() == R.id.solve_quiz_btn) {
            openSolveQuizActivity();
        }
    }

    private void openSolveQuizActivity() {
        Intent intent = new Intent(this, SolveQuizActivity.class);
        startActivity(intent);
    }
}
