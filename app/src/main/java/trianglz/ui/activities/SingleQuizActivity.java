package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SingleQuizActivity extends AppCompatActivity {
    private TextView subjectNameTextView, dateTextView,
            assignmentNameTextView, dayTextView, monthTextView, publishedTextView;
    private IImageLoader imageLoader;
    private LinearLayout dateLinearLayout;
    private CardView cardView;
    private TextView headTextView;
    private QuizzCourse course;
    private Quizzes quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quiz);
        getValueFromIntent();
        bindViews();
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
    }
}
