package trianglz.ui.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Locale;

import trianglz.components.CustomeLayoutManager;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.components.QuizSubmittedDialog;
import trianglz.components.TopItemDecoration;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.QuizQuestion;
import trianglz.models.Quizzes;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.SingleMultiSelectAnswerAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class SolveQuizFragment extends Fragment implements View.OnClickListener {
    private StudentMainActivity activity;
    private View rootView;

    private TextView subjectNameTextView;
    private TextView timerTextView, questionCounterTextView;
    private ImageButton backButton;
    private Button previousButton, nextButton, submitButton;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private SingleMultiSelectAnswerAdapter singleMultiSelectAnswerAdapter;
    private Quizzes quizzes;
    public long millisUntilFinish;
    private CountDownTimer countDownTimer;
    private QuizQuestion quizQuestion;
    private int index = 0;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_solve_quiz, container, false);
        fragment = this;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        getQuizQuestions();
    }

    private void startCountDown(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(secondsConverter(millisUntilFinished / 1000));
                millisUntilFinish = millisUntilFinished;
            }

            public void onFinish() {
                QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment, countDownTimer);
                quizSubmittedDialog.show();
            }

        }.start();
    }

    private void bindViews() {
        subjectNameTextView = rootView.findViewById(R.id.tv_subject_name);
        timerTextView = rootView.findViewById(R.id.tv_counter);
        recyclerView = rootView.findViewById(R.id.rv_answers);
        backButton = rootView.findViewById(R.id.back_btn);
        previousButton = rootView.findViewById(R.id.btn_previous);
        nextButton = rootView.findViewById(R.id.btn_next);
        submitButton = rootView.findViewById(R.id.btn_submit);
        questionCounterTextView = rootView.findViewById(R.id.question_counter_tv);
        subjectNameTextView.setText(quizzes.getName());
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(activity);
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));

    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        recyclerView.setOnTouchListener(new HideKeyboardOnTouch(activity));
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            quizzes = Quizzes.create(bundle.getString(Constants.KEY_QUIZZES));
        }
    }

    private void setItemTouchHelper() {

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return current.getItemViewType() == target.getItemViewType();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {
                if (dragged.getItemViewType() == SingleMultiSelectAnswerAdapter.TYPE_QUESTION || dragged.getItemViewType() == SingleMultiSelectAnswerAdapter.TYPE_ANSWER_TEXT) {
                    return false;
                } else {
                    int draggedPosition = dragged.getAdapterPosition();
                    int targetPosition = target.getAdapterPosition();
                    Collections.swap(singleMultiSelectAnswerAdapter.question.getAnswersAttributes(), draggedPosition - 2, targetPosition - 2);
                    singleMultiSelectAnswerAdapter.notifyItemMoved(draggedPosition, targetPosition);
                    return false;
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void detachItemTouchHelper() {
        if (itemTouchHelper != null) {
            itemTouchHelper.attachToRecyclerView(null);
        }
    }

    private String secondsConverter(long time) {
        long seconds = time % 60;
        long hours = time / 60;
        long minutes = hours % 60;
        hours = hours / 60;
        if (hours == 0) {
            return String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
        } else {
            return String.format(Locale.ENGLISH, "%02d", hours) + ":" + String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
        }
    }

    void getQuizQuestions() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizQuestions(quizzes.getId());
        //   showQuizView.getQuizQuestions(url);
        JSONObject response;
        try {
            response = new JSONObject("{\n" +
                    "  \"id\": 94,\n" +
                    "  \"name\": \"uuu\",\n" +
                    "  \"start_date\": \"2019-07-11T00:00:00.000Z\",\n" +
                    "  \"end_date\": \"2019-07-18T00:00:00.000Z\",\n" +
                    "  \"description\": null,\n" +
                    "  \"course_groups\": [\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"category\": null,\n" +
                    "  \"lesson\": {\n" +
                    "    \"id\": 247,\n" +
                    "    \"name\": \"General\",\n" +
                    "    \"unit_id\": 247,\n" +
                    "    \"description\": null,\n" +
                    "    \"date\": null,\n" +
                    "    \"order\": 0,\n" +
                    "    \"created_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"updated_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"unit\": {\n" +
                    "    \"id\": 247,\n" +
                    "    \"name\": \"General\",\n" +
                    "    \"chapter_id\": 247,\n" +
                    "    \"description\": null,\n" +
                    "    \"order\": 0,\n" +
                    "    \"created_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"updated_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"chapter\": {\n" +
                    "    \"id\": 247,\n" +
                    "    \"name\": \"General\",\n" +
                    "    \"course_id\": 247,\n" +
                    "    \"description\": null,\n" +
                    "    \"order\": 0,\n" +
                    "    \"created_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"updated_at\": \"2018-09-01T18:03:56.000Z\",\n" +
                    "    \"lock\": true,\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"duration\": 5,\n" +
                    "  \"is_questions_randomized\": false,\n" +
                    "  \"num_of_questions_per_page\": 5,\n" +
                    "  \"state\": \"running\",\n" +
                    "  \"total_score\": 26.0,\n" +
                    "  \"lesson_id\": 247,\n" +
                    "  \"student_solved\": true,\n" +
                    "  \"blooms\": [],\n" +
                    "  \"grading_period_lock\": false,\n" +
                    "  \"grading_period\": null,\n" +
                    "  \"questions\": [\n" +
                    "    {\n" +
                    "      \"id\": 231,\n" +
                    "      \"body\": \"<p>m,mjkj</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 5.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 579,\n" +
                    "          \"body\": \"234\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 231,\n" +
                    "          \"match\": \"234\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 578,\n" +
                    "          \"body\": \"123\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 231,\n" +
                    "          \"match\": \"123\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"Match\",\n" +
                    "      \"bloom\": [],\n" +
                    "      \"files\": null,\n" +
                    "      \"uploaded_file\": null,\n" +
                    "      \"correct_answers_count\": 0\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 232,\n" +
                    "      \"body\": \"<p>hguyu</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 5.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 582,\n" +
                    "          \"body\": \"5565\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 232,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 580,\n" +
                    "          \"body\": \"12345\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 232,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 581,\n" +
                    "          \"body\": \"98766\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 232,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"MultipleSelect\",\n" +
                    "      \"bloom\": [],\n" +
                    "      \"files\": null,\n" +
                    "      \"uploaded_file\": null,\n" +
                    "      \"correct_answers_count\": 1\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 233,\n" +
                    "      \"body\": \"<p>choose</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 6.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 583,\n" +
                    "          \"body\": \"jhk\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 233,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 584,\n" +
                    "          \"body\": \"jk\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 233,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"MultipleChoice\",\n" +
                    "      \"bloom\": [],\n" +
                    "      \"files\": null,\n" +
                    "      \"uploaded_file\": null,\n" +
                    "      \"correct_answers_count\": 1\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 234,\n" +
                    "      \"body\": \"<p>jhkh</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 5.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 585,\n" +
                    "          \"body\": \"5555\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 234,\n" +
                    "          \"match\": \"0\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 586,\n" +
                    "          \"body\": \"6666\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 234,\n" +
                    "          \"match\": \"1\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 587,\n" +
                    "          \"body\": \"7777\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 234,\n" +
                    "          \"match\": \"2\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"Reorder\",\n" +
                    "      \"bloom\": [],\n" +
                    "      \"files\": null,\n" +
                    "      \"uploaded_file\": null,\n" +
                    "      \"correct_answers_count\": 0\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 235,\n" +
                    "      \"body\": \"<p>khkhk</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 5.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 588,\n" +
                    "          \"body\": \"\",\n" +
                    "          \"created_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"updated_at\": \"2019-05-15T11:10:35.000Z\",\n" +
                    "          \"question_id\": 235,\n" +
                    "          \"match\": null,\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"TrueOrFalse\",\n" +
                    "      \"bloom\": [],\n" +
                    "      \"files\": null,\n" +
                    "      \"uploaded_file\": null,\n" +
                    "      \"correct_answers_count\": 1\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"objectives\": [],\n" +
                    "  \"grouping_students\": [],\n" +
                    "  \"course_groups_quiz\": [\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 94,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 158,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 94,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 159,\n" +
                    "      \"select_all\": true\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            quizQuestion = QuizQuestion.create(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //    singleMultiSelectAnswerAdapter.addData(getFakeData());
        startCountDown(quizzes.getDuration() * 1000);
        displayQuestionsAndAnswers(index);
        // activity.showLoadingDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        startCountDown(millisUntilFinish);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                countDownTimer.cancel();
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.btn_previous:
                if (index > 0)
                    index--;
                displayQuestionsAndAnswers(index);
                break;
            case R.id.btn_next:
                if (index < quizQuestion.getQuestions().size() - 1)
                    index++;
                displayQuestionsAndAnswers(index);
                break;
            case R.id.btn_submit:
                QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment, countDownTimer);
                quizSubmittedDialog.show();
                break;
        }
    }

    void updateQuestionCounterTextViewAndNextBtn(int index) {
        questionCounterTextView.setText(index + 1 + " " + activity.getResources().getString(R.string.out_of) + " " + quizQuestion.getQuestions().size());
        if (index == quizQuestion.getQuestions().size() - 1) {
            nextButton.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }
    }

    void displayQuestionsAndAnswers(int index) {
        if (!quizQuestion.getQuestions().isEmpty()) {
            updateQuestionCounterTextViewAndNextBtn(index);
            switch (quizQuestion.getQuestions().get(index).getType()) {
                case Constants.TYPE_MULTIPLE_SELECT:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION);
                    break;
                case Constants.TYPE_MULTIPLE_CHOICE:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.SINGLE_SELECTION);
                    break;
                case Constants.TYPE_TRUE_OR_FALSE:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.TRUE_OR_FALSE);
                    break;
                case Constants.TYPE_MATCH:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.MATCH_ANSWERS);
                    break;
                case Constants.TYPE_REORDER:
                    setItemTouchHelper();
                    singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.REORDER_ANSWERS);
                    break;

            }
            recyclerView.setAdapter(singleMultiSelectAnswerAdapter);
            singleMultiSelectAnswerAdapter.addData(quizQuestion.getQuestions().get(index));
        }
    }
}
