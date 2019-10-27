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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import trianglz.components.CustomeLayoutManager;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.components.QuizSubmittedDialog;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.SolveQuizPresenter;
import trianglz.core.views.SolveQuizView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.AnswerSubmission;
import trianglz.models.Answers;
import trianglz.models.Questions;
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
    private Questions question;
    private int quizSubmissionId;
    private int mode; //0 solve, 1 view questions, 2 view answers
    private boolean isSubmit = false;

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
        if (mode == Constants.SOLVE_QUIZ)
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
                    Collections.swap(singleMultiSelectAnswerAdapter.question.getAnswers(), draggedPosition - 2, targetPosition - 2);
                    setMatchReorder(singleMultiSelectAnswerAdapter.question.getAnswers());
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
            createSubmission();
        } else {
            getQuizSolveDetails();
            quizSubmissionId = quizzes.getStudentSubmissions().getId();
        }
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
                if (index > 0) {
                    index--;
                    question = quizQuestion.getQuestions().get(index);
                }
                displayQuestionsAndAnswers(index);
                break;
            case R.id.btn_next:
                isSubmit = false;
                submitAnswer();
                break;
            case R.id.btn_submit:
                isSubmit = true;
                submitAnswer();
                break;
        }
    }

    void updateQuestionCounterTextViewAndNextBtn(int index) {
        String questionCountText = index + 1 + " " + activity.getResources().getString(R.string.out_of) + " " + quizQuestion.getQuestions().size();
        if (index == quizQuestion.getQuestions().size() - 1) {
            previousButton.setText(activity.getResources().getString(R.string.previous));
            nextButton.setVisibility(View.GONE);
            if (mode != Constants.SOLVE_QUIZ) {
                submitButton.setVisibility(View.GONE);
                questionCounterButton.setVisibility(View.VISIBLE);
                questionCounterTextView.setVisibility(View.GONE);
                questionCounterButton.setText(questionCountText);
            } else {
                submitButton.setVisibility(View.VISIBLE);
                questionCounterTextView.setVisibility(View.VISIBLE);
                questionCounterTextView.setText(questionCountText);
                questionCounterButton.setVisibility(View.GONE);
            }
        } else if (index == 0) {
            questionCounterTextView.setVisibility(View.GONE);
            previousButton.setText(questionCountText);
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
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
            detachItemTouchHelper();
            switch (question.getType()) {
                case Constants.TYPE_MULTIPLE_SELECT:
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION;
                    break;
                case Constants.TYPE_MULTIPLE_CHOICE:
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.SINGLE_SELECTION;
                    break;
                case Constants.TYPE_TRUE_OR_FALSE:
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.TRUE_OR_FALSE;
                    break;
                case Constants.TYPE_MATCH:
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.MATCH_ANSWERS;
                    break;
                case Constants.TYPE_REORDER:
                    setItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.REORDER_ANSWERS;
                    break;

            }
            recyclerView.scrollToPosition(0);
            singleMultiSelectAnswerAdapter.addData(question);
        }
    }

    @Override
    public void onCreateSubmissionSuccess(StudentSubmission studentSubmission) {
        quizSubmissionId = studentSubmission.getId();
        getQuizSolveDetails();
    }

    @Override
    public void onCreateSubmissionFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    @Override
    public void onGetQuizSolveDetailsSuccess(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
        getAnswerSubmission();
    }

    @Override
    public void onGetQuizSolveDetailsFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    @Override
    public void onPostAnswerSubmissionSuccess() {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if (isSubmit) {
            countDownTimer.cancel();
            QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment);
            quizSubmittedDialog.show();
        } else {
            if (index < quizQuestion.getQuestions().size() - 1) {
                index++;
                question = quizQuestion.getQuestions().get(index);
                displayQuestionsAndAnswers(index);
            }
        }
    }

    @Override
    public void onPostAnswerSubmissionFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    @Override
    public void onGetAnswerSubmissionSuccess(JSONObject jsonObject) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        checkQuestionHasAnswer(jsonObject);
        if (mode == Constants.SOLVE_QUIZ) {
            startCountDown(quizQuestion.getDuration() * 60000);
        } else {
            timerTextView.setVisibility(View.GONE);
        }
        question = quizQuestion.getQuestions().get(index);
        displayQuestionsAndAnswers(index);
    }

    @Override
    public void onGetAnswerSubmissionFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    private void submitAnswer() {
        AnswerSubmission answerSubmission = new AnswerSubmission();
        answerSubmission.answers = new ArrayList<>();
        if (question.getType().equals(Constants.TYPE_REORDER)) {
            answerSubmission.answers.addAll(singleMultiSelectAnswerAdapter.question.getAnswers());
            answerSubmission.setQuestionId(question.getId());
            submitSingleAnswer(answerSubmission);
        } else {
            if (singleMultiSelectAnswerAdapter.questionsAnswerHashMap.containsKey(question.getId())) {
                ArrayList<Answers> answers = singleMultiSelectAnswerAdapter.questionsAnswerHashMap.get(question.getId());
                answerSubmission.answers.addAll(answers);
                answerSubmission.setQuestionId(question.getId());
                submitSingleAnswer(answerSubmission);
            } else {
                if (index < quizQuestion.getQuestions().size() - 1) {
                    index++;
                    question = quizQuestion.getQuestions().get(index);
                    displayQuestionsAndAnswers(index);
                }
            }
        }
    }


    private void createSubmission() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createQuizSubmission();
        solveQuizView.createQuizSubmission(url, quizzes.getId(), student.getId(), course.getId(), 0);
        activity.showLoadingDialog();
    }

    private void getQuizSolveDetails() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizSolveDetails(quizzes.getId());
        solveQuizView.getQuizSolveDetails(url);
        activity.showLoadingDialog();
    }

    private void getAnswerSubmission() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnswerSubmission(quizSubmissionId);
        solveQuizView.getAnswerSubmission(url);
        activity.showLoadingDialog();
    }

    private void submitSingleAnswer(AnswerSubmission answerSubmission) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postAnswerSubmission();
        solveQuizView.postAnswerSubmission(url, quizSubmissionId, answerSubmission);
        activity.showLoadingDialog();
    }

    private void setMatchReorder(List<Answers> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setMatch(Integer.toString(i + 1));
        }
    }

    private void checkQuestionHasAnswer(JSONObject jsonObject) {
        ArrayList<Answers> answers;
        for (Questions question : quizQuestion.getQuestions()) {
            if (question.getType().equals(Constants.TYPE_REORDER)) {
                setMatchReorder(question.getAnswers());
            }
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(Integer.toString(question.getId()));
                answers = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject answerObject = jsonArray.optJSONObject(i);
                    Answers answer = Answers.create(answerObject.toString());
                    answer.setId(answer.getAnswerId());
                    answers.add(answer);
                }
                if (question.getType().equals(Constants.TYPE_REORDER)) {
                    for (int i = 0; i < question.getAnswers().size(); i++) {
                        for (int k = 0; k < answers.size(); k++) {
                            if (question.getAnswers().get(i).getId() == answers.get(k).getAnswerId()) {
                                question.getAnswers().get(i).setMatch(answers.get(k).getMatch());
                            }
                        }
                    }
                } else {
                    singleMultiSelectAnswerAdapter.questionsAnswerHashMap.put(question.getId(), answers);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
