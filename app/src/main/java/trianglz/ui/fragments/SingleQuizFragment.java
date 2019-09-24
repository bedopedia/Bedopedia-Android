package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class SingleQuizFragment extends Fragment implements View.OnClickListener {

    private StudentMainActivity activity;
    private View rootView;
    private TextView subjectNameTextView, dateTextView,
            assignmentNameTextView, dayTextView, monthTextView, publishedTextView;
    private IImageLoader imageLoader;
    private Button solveQuizBtn, detailsBtn, questionsBtn, answersBtn;
    private LinearLayout dateLinearLayout, buttonsLayout;
    private LinearLayout cardView;
    private TextView headTextView;
    private QuizzCourse course;
    private Quizzes quizzes;
    private AvatarView avatarView;
    private ImageButton backButton;
    private Student student;
    private ImageView clockImageView;

    // grades variables
    private View quizGradeView, quizNotStartedView;
    private TextView gradeTextView, outOfTextView, noteTextview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_single_quiz, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        solveQuizBtn.setOnClickListener(this);
        detailsBtn.setOnClickListener(this);
        questionsBtn.setOnClickListener(this);
        answersBtn.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            quizzes = Quizzes.create(bundle.getString(Constants.KEY_QUIZZES));
            course = QuizzCourse.create(bundle.getString(Constants.KEY_COURSE_QUIZZES));
            student = Student.create(bundle.getString(Constants.STUDENT));

        }
    }

    private void bindViews() {
        headTextView = rootView.findViewById(R.id.tv_quiz_name);
        headTextView.setText(course.getCourseName());
        subjectNameTextView = rootView.findViewById(R.id.tv_subject_name);
        dateTextView = rootView.findViewById(R.id.tv_date);
        assignmentNameTextView = rootView.findViewById(R.id.tv_assignment_name);
        imageLoader = new PicassoLoader();
        dayTextView = rootView.findViewById(R.id.tv_day_number);
        monthTextView = rootView.findViewById(R.id.tv_month);
        dateLinearLayout = rootView.findViewById(R.id.ll_date);
        publishedTextView = rootView.findViewById(R.id.tv_published);
        cardView = rootView.findViewById(R.id.card_view);
        avatarView = rootView.findViewById(R.id.img_student);
        solveQuizBtn = rootView.findViewById(R.id.solve_quiz_btn);
        detailsBtn = rootView.findViewById(R.id.details_btn);
        questionsBtn = rootView.findViewById(R.id.questions_btn);
        answersBtn = rootView.findViewById(R.id.answers_btn);
        buttonsLayout = rootView.findViewById(R.id.buttons_layout);
        clockImageView = rootView.findViewById(R.id.date_icon);
        // views
        quizGradeView = rootView.findViewById(R.id.v_quiz_grade);
        quizNotStartedView = rootView.findViewById(R.id.v_not_started_quiz);

        gradeTextView = rootView.findViewById(R.id.tv_score);
        outOfTextView = rootView.findViewById(R.id.tv_out_of);
        noteTextview = rootView.findViewById(R.id.tv_note);
        backButton = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        DateTime dateTime = new DateTime(quizzes.getStartDate());

        if (quizzes.getName() != null) {
            subjectNameTextView.setText(course.getCourseName());
        }
        if (quizzes.getEndDate() != null) {
            dateTextView.setText(Util.getPostDateAmPm(quizzes.getEndDate(), activity));
        }

        if (quizzes.getEndDate() != null) {
            dayTextView.setText(Util.getAssigmentDetailEndDateDay(quizzes.getEndDate()));
            monthTextView.setText(Util.getAssigmentDetailEndDateMonth(quizzes.getEndDate(), activity));
        }
        if (quizzes.getName() != null) {
            assignmentNameTextView.setText(quizzes.getName());
        }

        String published = this.getString(R.string.published) + " " + Util.getPostDate(dateTime.toString(), activity);
        publishedTextView.setText(published);
        if (quizzes.getState() != null) {

            if (quizzes.getState().equals("running")) {
                if (quizzes.getStudentSubmissions() != null) {
                    quizGradeView.setVisibility(View.VISIBLE);
                    dateLinearLayout.setBackground(this.getResources().getDrawable(R.drawable.curved_red, null));
                    dateTextView.setTextColor(this.getResources().getColor(R.color.dirt_brown,null));
                    clockImageView.setBackground(this.getResources().getDrawable(R.drawable.red_clock_icon,null));
                    dateTextView.setText(this.getResources().getString(R.string.solved));
                    buttonsLayout.setVisibility(View.VISIBLE);
                    gradeTextView.setText(quizzes.getStudentSubmissions().getScore() + "");
                    outOfTextView.setText(getResources().getString(R.string.out_of) + " " + quizzes.getTotalScore() + "");
                    String feedBack = quizzes.getStudentSubmissions().getFeedback();
                    if (feedBack != null) {
                        noteTextview.setText(quizzes.getStudentSubmissions().getFeedback());
                    } else {
                        noteTextview.setVisibility(View.INVISIBLE);
                    }
                } else {
                    quizGradeView.setVisibility(View.GONE);
                    buttonsLayout.setVisibility(View.GONE);
                    solveQuizBtn.setVisibility(View.VISIBLE);
                    dateLinearLayout.setBackground(this.getResources().getDrawable(R.drawable.curved_light_sage, null));
                    dateTextView.setTextColor(this.getResources().getColor(R.color.pine,null));
                    clockImageView.setBackground(this.getResources().getDrawable(R.drawable.green_clock_icon,null));
                }
            } else {
                dateLinearLayout.setBackground(this.getResources().getDrawable(R.drawable.curved_red, null));
                dateTextView.setTextColor(this.getResources().getColor(R.color.dirt_brown,null));
                clockImageView.setBackground(this.getResources().getDrawable(R.drawable.red_clock_icon,null));
                if (quizzes.getStudentSubmissions() != null) {
                    quizGradeView.setVisibility(View.VISIBLE);
                    gradeTextView.setText(quizzes.getStudentSubmissions().getScore() + "");
                    outOfTextView.setText(getResources().getString(R.string.out_of) + " " + quizzes.getTotalScore() + "");
                    String feedBack = quizzes.getStudentSubmissions().getFeedback();
                    if (feedBack != null) {
                        noteTextview.setText(quizzes.getStudentSubmissions().getFeedback());
                    } else {
                        noteTextview.setVisibility(View.INVISIBLE);
                    }
                } else {
                    quizGradeView.setVisibility(View.GONE);
//                gradeTextView.setText("--");
//                outOfTextView.setText(getResources().getString(R.string.out_of) + " " + quizzes.getTotalScore() + "");
//                noteTextview.setText(getResources().getString(R.string.no_submission));
                }
            }
        } else {
            dateTextView.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.solve_quiz_btn:
                openSolveQuizActivity();
                break;
            case R.id.details_btn:
                break;
            case R.id.questions_btn:
                break;
            case R.id.answers_btn:
                break;
        }
    }

    private void openSolveQuizActivity() {
        //  Intent intent = new Intent(this, SolveQuizActivity.class);
        // startActivity(intent);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(avatarView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

}
