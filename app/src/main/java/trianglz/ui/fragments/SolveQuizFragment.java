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
import trianglz.core.presenters.SolveQuizPresenter;
import trianglz.core.views.SolveQuizView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.QuizQuestion;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.models.Student;
import trianglz.models.StudentSubmission;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.SingleMultiSelectAnswerAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class SolveQuizFragment extends Fragment implements View.OnClickListener, SolveQuizPresenter {
    private StudentMainActivity activity;
    private View rootView;

    private TextView subjectNameTextView;
    private TextView timerTextView, questionCounterTextView;
    private ImageButton backButton;
    private Button previousButton, nextButton, submitButton, questionCounterButton;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private SingleMultiSelectAnswerAdapter singleMultiSelectAnswerAdapter;
    private Quizzes quizzes;
    private CountDownTimer countDownTimer;
    private QuizQuestion quizQuestion;
    private int index = 0;
    private Fragment fragment;
    private SolveQuizView solveQuizView;
    private Student student;
    private QuizzCourse course;
    private int mode; //0 solve, 1 view questions, 2 view answers

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
            }

            public void onFinish() {
                QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment);
                quizSubmittedDialog.show();
                countDownTimer.cancel();
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
        questionCounterButton = rootView.findViewById(R.id.bottom_question_count);
        questionCounterTextView = rootView.findViewById(R.id.question_counter_tv);
        subjectNameTextView.setText(quizzes.getName());
        solveQuizView = new SolveQuizView(activity, this);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(activity);
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, mode);
        recyclerView.setAdapter(singleMultiSelectAnswerAdapter);
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
            mode = bundle.getInt(Constants.MODE);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            course = QuizzCourse.create(bundle.getString(Constants.KEY_COURSE_QUIZZES));

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
        if (quizzes.getStudentSubmissions() == null) {
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createQuizSubmission();
            solveQuizView.createQuizSubmission(url, quizzes.getId(), student.getId(), course.getId(), 0);
            activity.showLoadingDialog();
        } else {

        }

        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizQuestions(quizzes.getId());
        //   showQuizView.getQuizQuestions(url);
        JSONObject response;
        try {
            response = new JSONObject("{\n" +
                    "  \"id\": 111,\n" +
                    "  \"name\": \"aaaa\",\n" +
                    "  \"start_date\": \"2019-07-31T00:00:00.000Z\",\n" +
                    "  \"end_date\": \"2019-08-01T15:59:59.000Z\",\n" +
                    "  \"description\": \"\",\n" +
                    "  \"course_groups\": [\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18A\",\n" +
                    "      \"id\": 516,\n" +
                    "      \"students\": 20\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"18B\",\n" +
                    "      \"id\": 532,\n" +
                    "      \"students\": 18\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"category\": {\n" +
                    "    \"id\": 5044,\n" +
                    "    \"name\": \"Classwork\",\n" +
                    "    \"weight\": 20.0,\n" +
                    "    \"course_id\": null,\n" +
                    "    \"created_at\": \"2019-09-25T11:56:15.000Z\",\n" +
                    "    \"updated_at\": \"2019-09-25T11:56:15.000Z\",\n" +
                    "    \"grading_category_id\": null,\n" +
                    "    \"deleted_at\": null,\n" +
                    "    \"parent_id\": 5043,\n" +
                    "    \"numeric\": false\n" +
                    "  },\n" +
                    "  \"lesson\": {\n" +
                    "    \"id\": 2988,\n" +
                    "    \"name\": \"حي الأشجار\",\n" +
                    "    \"unit_id\": 2557,\n" +
                    "    \"description\": null,\n" +
                    "    \"date\": \"2018-09-16\",\n" +
                    "    \"order\": 0,\n" +
                    "    \"created_at\": \"2018-10-12T19:35:46.000Z\",\n" +
                    "    \"updated_at\": \"2018-10-12T19:35:46.000Z\",\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"unit\": {\n" +
                    "    \"id\": 2557,\n" +
                    "    \"name\": \"الدرس الاول\",\n" +
                    "    \"chapter_id\": 1410,\n" +
                    "    \"description\": null,\n" +
                    "    \"order\": 0,\n" +
                    "    \"created_at\": \"2018-10-12T19:35:22.000Z\",\n" +
                    "    \"updated_at\": \"2018-10-12T19:35:22.000Z\",\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"chapter\": {\n" +
                    "    \"id\": 1410,\n" +
                    "    \"name\": \"الوحدة الأولى\",\n" +
                    "    \"course_id\": 247,\n" +
                    "    \"description\": null,\n" +
                    "    \"order\": 1,\n" +
                    "    \"created_at\": \"2018-10-12T19:35:22.000Z\",\n" +
                    "    \"updated_at\": \"2018-10-12T19:35:22.000Z\",\n" +
                    "    \"lock\": false,\n" +
                    "    \"deleted_at\": null\n" +
                    "  },\n" +
                    "  \"duration\": 5,\n" +
                    "  \"is_questions_randomized\": true,\n" +
                    "  \"num_of_questions_per_page\": 5,\n" +
                    "  \"state\": \"finished\",\n" +
                    "  \"total_score\": 15.0,\n" +
                    "  \"lesson_id\": 2988,\n" +
                    "  \"student_solved\": true,\n" +
                    "  \"blooms\": [\n" +
                    "    \"Comprehension\",\n" +
                    "    \"Application\"\n" +
                    "  ],\n" +
                    "  \"grading_period_lock\": false,\n" +
                    "  \"grading_period\": null,\n" +
                    "  \"questions\": [\n" +
                    "    {\n" +
                    "      \"id\": 256,\n" +
                    "      \"body\": \"<p>Reorder</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 6.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 611,\n" +
                    "          \"body\": \"step 2\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T08:36:30.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"2\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 613,\n" +
                    "          \"body\": \"Step 4\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T09:18:36.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"4\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 614,\n" +
                    "          \"body\": \"Step 5\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T09:18:36.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"5\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 612,\n" +
                    "          \"body\": \"Step 3\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T09:18:36.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"3\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 610,\n" +
                    "          \"body\": \"step 1\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T09:18:36.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"1\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 615,\n" +
                    "          \"body\": \"Step 6\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-08-06T09:18:36.000Z\",\n" +
                    "          \"question_id\": 256,\n" +
                    "          \"match\": \"6\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"Reorder\",\n" +
                    "      \"bloom\": [\n" +
                    "        \"Comprehension\"\n" +
                    "      ],\n" +
                    "      \"files\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4241/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=cf4fc3a729045accefabd5b7fd5afc5bfa4543bf005e95f18c38c943a2656411\",\n" +
                    "      \"uploaded_file\": {\n" +
                    "        \"id\": 4241,\n" +
                    "        \"file\": {\n" +
                    "          \"url\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4241/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=cf4fc3a729045accefabd5b7fd5afc5bfa4543bf005e95f18c38c943a2656411\"\n" +
                    "        },\n" +
                    "        \"content_type\": \"image/jpeg\",\n" +
                    "        \"name\": \"Sports\",\n" +
                    "        \"description\": null,\n" +
                    "        \"file_size\": 4526,\n" +
                    "        \"downloads_number\": 0,\n" +
                    "        \"fileable_id\": 256,\n" +
                    "        \"fileable_type\": \"Question\",\n" +
                    "        \"created_at\": \"2019-07-30T21:38:13.000Z\",\n" +
                    "        \"updated_at\": \"2019-07-30T21:38:13.000Z\",\n" +
                    "        \"deleted_at\": null,\n" +
                    "        \"upload_type\": \"device\",\n" +
                    "        \"creator_id\": null\n" +
                    "      },\n" +
                    "      \"correct_answers_count\": 0\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 257,\n" +
                    "      \"body\": \"<p>Multiple Choice</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 1.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 616,\n" +
                    "          \"body\": \"Choice 1\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"question_id\": 257,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 620,\n" +
                    "          \"body\": \"Choice 5\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"question_id\": 257,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 619,\n" +
                    "          \"body\": \"Choice 4\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"question_id\": 257,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 618,\n" +
                    "          \"body\": \"Choice 3 - Correct\",\n" +
                    "          \"is_correct\": true,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"question_id\": 257,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 617,\n" +
                    "          \"body\": \"Choice 2\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "          \"question_id\": 257,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"MultipleChoice\",\n" +
                    "      \"bloom\": [\n" +
                    "        \"Comprehension\"\n" +
                    "      ],\n" +
                    "      \"files\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4242/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=7c12b9aba1f5e4103b5c13b4228a9b7c883e3dcf9092fcb4ef375b40a52297f6\",\n" +
                    "      \"uploaded_file\": {\n" +
                    "        \"id\": 4242,\n" +
                    "        \"file\": {\n" +
                    "          \"url\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4242/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=7c12b9aba1f5e4103b5c13b4228a9b7c883e3dcf9092fcb4ef375b40a52297f6\"\n" +
                    "        },\n" +
                    "        \"content_type\": \"image/jpeg\",\n" +
                    "        \"name\": \"Sports\",\n" +
                    "        \"description\": null,\n" +
                    "        \"file_size\": 4526,\n" +
                    "        \"downloads_number\": 0,\n" +
                    "        \"fileable_id\": 257,\n" +
                    "        \"fileable_type\": \"Question\",\n" +
                    "        \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "        \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "        \"deleted_at\": null,\n" +
                    "        \"upload_type\": \"device\",\n" +
                    "        \"creator_id\": null\n" +
                    "      },\n" +
                    "      \"correct_answers_count\": 1\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 258,\n" +
                    "      \"body\": \"<p>Match</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 5.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 623,\n" +
                    "          \"body\": \"M3\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 258,\n" +
                    "          \"match\": \"M3\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 622,\n" +
                    "          \"body\": \"M2\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 258,\n" +
                    "          \"match\": \"M2\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 624,\n" +
                    "          \"body\": \"M4\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 258,\n" +
                    "          \"match\": \"M4\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 625,\n" +
                    "          \"body\": \"M5\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 258,\n" +
                    "          \"match\": \"M5\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 621,\n" +
                    "          \"body\": \"M1\",\n" +
                    "          \"is_correct\": null,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 258,\n" +
                    "          \"match\": \"M1\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"Match\",\n" +
                    "      \"bloom\": [\n" +
                    "        \"Comprehension\"\n" +
                    "      ],\n" +
                    "      \"files\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4243/pta.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=c1feb11d88317cc42f50a2cc6ac964528ac7a8cdf22f25cb6b46fce81612f5a9\",\n" +
                    "      \"uploaded_file\": {\n" +
                    "        \"id\": 4243,\n" +
                    "        \"file\": {\n" +
                    "          \"url\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4243/pta.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=c1feb11d88317cc42f50a2cc6ac964528ac7a8cdf22f25cb6b46fce81612f5a9\"\n" +
                    "        },\n" +
                    "        \"content_type\": \"image/jpeg\",\n" +
                    "        \"name\": \"PTA\",\n" +
                    "        \"description\": null,\n" +
                    "        \"file_size\": 7274,\n" +
                    "        \"downloads_number\": 0,\n" +
                    "        \"fileable_id\": 258,\n" +
                    "        \"fileable_type\": \"Question\",\n" +
                    "        \"created_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "        \"updated_at\": \"2019-07-30T21:38:14.000Z\",\n" +
                    "        \"deleted_at\": null,\n" +
                    "        \"upload_type\": \"device\",\n" +
                    "        \"creator_id\": null\n" +
                    "      },\n" +
                    "      \"correct_answers_count\": 0\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 259,\n" +
                    "      \"body\": \"<p>Multi Select</p>\",\n" +
                    "      \"difficulty\": \"Medium\",\n" +
                    "      \"score\": 2.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 629,\n" +
                    "          \"body\": \"S4 - Correct\",\n" +
                    "          \"is_correct\": true,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 259,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 630,\n" +
                    "          \"body\": \"S5\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 259,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 627,\n" +
                    "          \"body\": \"S2 - Correct\",\n" +
                    "          \"is_correct\": true,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 259,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 628,\n" +
                    "          \"body\": \"S3\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 259,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"id\": 626,\n" +
                    "          \"body\": \"S1\",\n" +
                    "          \"is_correct\": false,\n" +
                    "          \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "          \"question_id\": 259,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": \"AcceptPartialAnswers\",\n" +
                    "      \"type\": \"MultipleSelect\",\n" +
                    "      \"bloom\": [\n" +
                    "        \"Application\"\n" +
                    "      ],\n" +
                    "      \"files\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4244/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=1a18fa3b07b30405cfa36956fd887ac4208fd046d7be29ace2417965ff5da224\",\n" +
                    "      \"uploaded_file\": {\n" +
                    "        \"id\": 4244,\n" +
                    "        \"file\": {\n" +
                    "          \"url\": \"https://skolera-edu-training.s3-eu-west-1.amazonaws.com/uploads/uploaded_file/file/4244/sports.jpg?X-Amz-Expires=86400&X-Amz-Date=20191002T085245Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIXFDQFQEE5B4DNQ/20191002/eu-west-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=1a18fa3b07b30405cfa36956fd887ac4208fd046d7be29ace2417965ff5da224\"\n" +
                    "        },\n" +
                    "        \"content_type\": \"image/jpeg\",\n" +
                    "        \"name\": \"Sports\",\n" +
                    "        \"description\": null,\n" +
                    "        \"file_size\": 4526,\n" +
                    "        \"downloads_number\": 0,\n" +
                    "        \"fileable_id\": 259,\n" +
                    "        \"fileable_type\": \"Question\",\n" +
                    "        \"created_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "        \"updated_at\": \"2019-07-30T21:38:15.000Z\",\n" +
                    "        \"deleted_at\": null,\n" +
                    "        \"upload_type\": \"device\",\n" +
                    "        \"creator_id\": null\n" +
                    "      },\n" +
                    "      \"correct_answers_count\": 2\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 260,\n" +
                    "      \"body\": \"<p>aaaa</p>\",\n" +
                    "      \"difficulty\": \"Easy\",\n" +
                    "      \"score\": 1.0,\n" +
                    "      \"answers_attributes\": [\n" +
                    "        {\n" +
                    "          \"id\": 631,\n" +
                    "          \"body\": \"\",\n" +
                    "          \"is_correct\": true,\n" +
                    "          \"created_at\": \"2019-07-30T22:00:56.000Z\",\n" +
                    "          \"updated_at\": \"2019-07-30T22:00:56.000Z\",\n" +
                    "          \"question_id\": 260,\n" +
                    "          \"match\": \"\",\n" +
                    "          \"deleted_at\": null\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"correction_style\": null,\n" +
                    "      \"type\": \"TrueOrFalse\",\n" +
                    "      \"bloom\": [\n" +
                    "        \"Application\"\n" +
                    "      ],\n" +
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
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 186,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 193,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 195,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 196,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 199,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 200,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 201,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 202,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 207,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 208,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 209,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 210,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 211,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 212,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 213,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 516,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 214,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 187,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 194,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 197,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 198,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 203,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 204,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 205,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 206,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 215,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 216,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 217,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 218,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 219,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 220,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 221,\n" +
                    "      \"select_all\": true\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"course_group_id\": 532,\n" +
                    "      \"quiz_id\": 111,\n" +
                    "      \"deleted_at\": null,\n" +
                    "      \"hide_grade\": false,\n" +
                    "      \"id\": 222,\n" +
                    "      \"select_all\": true\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            quizQuestion = QuizQuestion.create(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //    singleMultiSelectAnswerAdapter.addData(getFakeData());
        if (mode == Constants.SOLVE_QUIZ) {
            startCountDown(quizzes.getDuration() * 1000);
        } else {
            timerTextView.setVisibility(View.GONE);
        }
        displayQuestionsAndAnswers(index);
        // activity.showLoadingDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mode == Constants.SOLVE_QUIZ)
            countDownTimer.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (mode == Constants.SOLVE_QUIZ)
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
                countDownTimer.cancel();
                QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment);
                quizSubmittedDialog.show();
                break;
        }
    }

    void updateQuestionCounterTextViewAndNextBtn(int index) {
        String questionCountText = index + 1 + " " + activity.getResources().getString(R.string.out_of) + " " + quizQuestion.getQuestions().size();
        if (index == quizQuestion.getQuestions().size() - 1) {
            if (mode != Constants.SOLVE_QUIZ) {
                nextButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                questionCounterButton.setVisibility(View.VISIBLE);
                questionCounterTextView.setVisibility(View.GONE);
                questionCounterButton.setText(questionCountText);
            } else {
                nextButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                questionCounterTextView.setVisibility(View.VISIBLE);
                questionCounterTextView.setText(questionCountText);
                questionCounterButton.setVisibility(View.GONE);
            }
        } else if (index == 0) {
            questionCounterTextView.setVisibility(View.GONE);
            previousButton.setText(questionCountText);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
            questionCounterButton.setVisibility(View.GONE);
            questionCounterTextView.setVisibility(View.VISIBLE);
            questionCounterTextView.setText(questionCountText);
            previousButton.setText(activity.getResources().getString(R.string.previous));
        }

    }

    void displayQuestionsAndAnswers(int index) {
        if (!quizQuestion.getQuestions().isEmpty()) {
            updateQuestionCounterTextViewAndNextBtn(index);
            switch (quizQuestion.getQuestions().get(index).getType()) {
                case Constants.TYPE_MULTIPLE_SELECT:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION;
                    break;
                case Constants.TYPE_MULTIPLE_CHOICE:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.SINGLE_SELECTION;
                    break;
                case Constants.TYPE_TRUE_OR_FALSE:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.TRUE_OR_FALSE;
                    break;
                case Constants.TYPE_MATCH:
                    detachItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.MATCH_ANSWERS;
                    break;
                case Constants.TYPE_REORDER:
                    setItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.REORDER_ANSWERS;
                    break;

            }
            recyclerView.scrollToPosition(0);
            singleMultiSelectAnswerAdapter.addData(quizQuestion.getQuestions().get(index));
        }
    }

    @Override
    public void onCreateSubmissionSuccess(StudentSubmission studentSubmission) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
    }

    @Override
    public void onCreateSubmissionFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }
}
