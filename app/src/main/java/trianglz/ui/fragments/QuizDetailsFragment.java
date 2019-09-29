package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.ui.activities.StudentMainActivity;

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
    }

    private void bindViews() {
        quizDescriptionTextView = rootView.findViewById(R.id.quiz_desc_tv);
        quizNameTextView = rootView.findViewById(R.id.quiz_name_tv);
        publishDateTextView = rootView.findViewById(R.id.publish_date_tv);
        dueDateTextView = rootView.findViewById(R.id.duration_tv);
        totalScoreTextView = rootView.findViewById(R.id.total_score_tv);
        bloomsTextView = rootView.findViewById(R.id.blooms_tv);
        topicTextView = rootView.findViewById(R.id.topic_tv);
        subtopicTextView = rootView.findViewById(R.id.subtopic_tv);
        lessonTextView = rootView.findViewById(R.id.lesson_tv);
        objectivesTextView = rootView.findViewById(R.id.learning_obj_tv);
        courseGroupsTextView = rootView.findViewById(R.id.course_group_tv);
        backButton = rootView.findViewById(R.id.btn_back);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            getParentFragment().getChildFragmentManager().popBackStack();
        }
    }
}
