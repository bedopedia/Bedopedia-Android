package trianglz.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skolera.skolera_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import trianglz.models.QuizQuestion;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 29/09/2019.
 */
public class QuizDetailsFragment extends Fragment implements View.OnClickListener {
    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backButton;
    private TextView quizDescriptionTextView, quizNameTextView, publishDateTextView, dueDateTextView, durationTextView,
            totalScoreTextView, bloomsTextView, topicTextView, subtopicTextView, lessonTextView, objectivesTextView,
            courseGroupsTextView, toolbarQuizNameTextView;
    private QuizQuestion quizQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_quiz_details, container, false);
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
            quizQuestion = QuizQuestion.create(bundle.getString(Constants.KEY_QUIZ_QUESTION));
        }
    }

    private void bindViews() {
        toolbarQuizNameTextView = rootView.findViewById(R.id.tv_quiz_name);
        quizDescriptionTextView = rootView.findViewById(R.id.quiz_desc_tv);
        quizNameTextView = rootView.findViewById(R.id.quiz_name_tv);
        publishDateTextView = rootView.findViewById(R.id.publish_date_tv);
        dueDateTextView = rootView.findViewById(R.id.due_date_tv);
        durationTextView = rootView.findViewById(R.id.duration_tv);
        totalScoreTextView = rootView.findViewById(R.id.total_score_tv);
        bloomsTextView = rootView.findViewById(R.id.blooms_tv);
        topicTextView = rootView.findViewById(R.id.topic_tv);
        subtopicTextView = rootView.findViewById(R.id.subtopic_tv);
        lessonTextView = rootView.findViewById(R.id.lesson_tv);
        objectivesTextView = rootView.findViewById(R.id.learning_obj_tv);
        courseGroupsTextView = rootView.findViewById(R.id.course_group_tv);
        backButton = rootView.findViewById(R.id.btn_back);

        toolbarQuizNameTextView.setText(quizQuestion.getName());
        quizNameTextView.setText(quizQuestion.getName());
        quizDescriptionTextView.setText(quizQuestion.getDescription());
        publishDateTextView.setText(dateConverter(quizQuestion.getStartDate()));
        dueDateTextView.setText(dateConverter(quizQuestion.getEndDate()));
        durationTextView.setText(String.format(getString(R.string.minutes),String.valueOf(quizQuestion.getDuration())));
        totalScoreTextView.setText(String.valueOf(quizQuestion.getTotalScore()));
        bloomsTextView.setText(Arrays.toString(quizQuestion.getBlooms()).replaceAll("\\[|\\]", ""));
        topicTextView.setText(quizQuestion.getUnit().getName());
        subtopicTextView.setText(quizQuestion.getChapter().getName());
        lessonTextView.setText(quizQuestion.getLesson().getName());

        if(quizQuestion.getDescription().trim().isEmpty()){
            quizDescriptionTextView.setVisibility(View.GONE);
        }

        if (!quizQuestion.getObjectives().isEmpty()) {
            objectivesTextView.setText("");
            for (int i = 0; i < quizQuestion.getObjectives().size(); i++) {
                if (i == quizQuestion.getObjectives().size() - 1) {
                    objectivesTextView.append(quizQuestion.getObjectives().get(i).getName());
                } else {
                    objectivesTextView.append(quizQuestion.getObjectives().get(i).getName() + ", ");
                }
            }
        }
        if (!quizQuestion.getCourseGroups().isEmpty()) {
            for (int i = 0; i < quizQuestion.getCourseGroups().size(); i++) {
                if (i == quizQuestion.getCourseGroups().size() - 1) {
                    courseGroupsTextView.append(quizQuestion.getCourseGroups().get(i).getName());
                } else {
                    courseGroupsTextView.append(quizQuestion.getCourseGroups().get(i).getName() + ", ");
                }
            }
        }
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
    }

    private String dateConverter(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("d MMMM, yyyy - hh:mm a");
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        return formattedDate;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            getParentFragment().getChildFragmentManager().popBackStack();
        }
    }
}
