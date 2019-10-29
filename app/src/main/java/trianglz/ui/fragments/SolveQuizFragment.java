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

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import trianglz.models.StudentSubmissions;
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
    private StudentSubmissions studentSubmission;
    private int mode; //0 solve, 1 view questions, 2 view correct answers, 3 view student answers
    private boolean isSubmit = false;
    private HashMap<String, Answers> previousMatchAnswersHashMap;
    private HashMap<Integer, ArrayList<Answers>> previousAnswersHashMap;

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
        if (mode == Constants.SOLVE_QUIZ) {
            getQuizQuestions();
        } else {
            question = quizQuestion.getQuestions().get(index);
            getAnswerSubmission();
            displayQuestionsAndAnswers(index);
        }
    }

    private void startCountDown(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(secondsConverter(millisUntilFinished / 1000));
            }

            public void onFinish() {
                // submitQuiz();
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
        previousMatchAnswersHashMap = new HashMap<>();
        previousAnswersHashMap = new HashMap<>();
        if (mode != Constants.SOLVE_QUIZ) {
            timerTextView.setVisibility(View.GONE);
        }

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
            if (mode != Constants.SOLVE_QUIZ) {
                quizQuestion = QuizQuestion.create(bundle.getString(Constants.KEY_QUIZ_QUESTION));
            }
            if (quizzes.getStudentSubmissions() != null) {
                studentSubmission = quizzes.getStudentSubmissions();
            }
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
                if (mode == Constants.SOLVE_QUIZ) {
                    submitAnswer();
                } else {
                    nextPage();
                }
                break;
            case R.id.btn_submit:
                isSubmit = true;
                submitAnswer();
                break;
        }
    }

    void updateQuestionCounterTextViewAndNextBtn(int index) {
        String questionCountText = index + 1 + " " + activity.getResources().getString(R.string.out_of) + " " + quizQuestion.getQuestions().size();
        if (quizQuestion.getQuestions().size() == 1) {
            previousButton.setVisibility(View.INVISIBLE);
        }
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
            questionCounterButton.setVisibility(View.GONE);
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
                    if (mode == Constants.SOLVE_QUIZ)
                        setItemTouchHelper();
                    singleMultiSelectAnswerAdapter.type = SingleMultiSelectAnswerAdapter.TYPE.REORDER_ANSWERS;
                    break;

            }
            recyclerView.scrollToPosition(0);
            singleMultiSelectAnswerAdapter.addData(question);
        }
    }

    @Override
    public void onCreateSubmissionSuccess(StudentSubmissions studentSubmission) {
        this.studentSubmission = studentSubmission;
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
    public void onPostAnswerSubmissionSuccess(ArrayList<Answers> answers) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if (question.getType().equals(Constants.TYPE_MATCH)) {
            copyMatchMaps(previousMatchAnswersHashMap, singleMultiSelectAnswerAdapter.matchAnswersHashMap);
        } else {
            previousAnswersHashMap.put(question.getId(), answers);
        }
        nextPage();
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
        if (mode == Constants.SOLVE_QUIZ) {
            checkQuestionHasAnswer(jsonObject);
            startCountDown(calculateTimerDuration(quizQuestion.getDuration()));
        } else if (mode == Constants.VIEW_STUDENT_ANSWERS) {
            checkQuestionHasAnswer(jsonObject);
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

    @Override
    public void onDeleteAnswerSubmissionSuccess() {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if (question.getType().equals(Constants.TYPE_MATCH)) {
            for (int i = 0; i < question.getAnswers().get(0).getMatches().size(); i++) {
                previousMatchAnswersHashMap.remove(Jsoup.parse(question.getAnswers().get(0).getMatches().get(i)).text());
            }
        }
        nextPage();
    }

    @Override
    public void onDeleteAnswerSubmissionFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    @Override
    public void onSubmitQuizSuccess() {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        showSubmissionDialog();
    }

    @Override
    public void onSubmitQuizFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, message);
    }

    private void submitAnswer() {
        AnswerSubmission answerSubmission = new AnswerSubmission();
        answerSubmission.answers = new ArrayList<>();
        if (question.getType().equals(Constants.TYPE_REORDER)) {
            if (singleMultiSelectAnswerAdapter.reorderAnswers.equals(singleMultiSelectAnswerAdapter.question.getAnswers())) {
                nextPage();
            } else {
                answerSubmission.answers.addAll(singleMultiSelectAnswerAdapter.question.getAnswers());
                answerSubmission.setQuestionId(question.getId());
                submitSingleAnswer(answerSubmission);
            }
        } else if (question.getType().equals(Constants.TYPE_MATCH)) {
            if (!previousMatchAnswersHashMap.isEmpty() && singleMultiSelectAnswerAdapter.matchAnswersHashMap.isEmpty()) {
                deleteSingleAnswer(question.getId(), studentSubmission.getId());
            } else if (previousMatchAnswersHashMap.equals(singleMultiSelectAnswerAdapter.matchAnswersHashMap)) {
                nextPage();
            } else {
                for (Map.Entry mapElement : singleMultiSelectAnswerAdapter.matchAnswersHashMap.entrySet()) {
                    String match = (String) mapElement.getKey();
                    Answers value = ((Answers) mapElement.getValue());
                    value.setMatch(match);
                    answerSubmission.answers.add(value);
                }
                answerSubmission.setQuestionId(question.getId());
                submitSingleAnswer(answerSubmission);
            }
        } else {
            if (compareHashMaps(previousAnswersHashMap, singleMultiSelectAnswerAdapter.questionsAnswerHashMap, question)) {
                ArrayList<Answers> currentAnswers = new ArrayList<>();
                currentAnswers.addAll(singleMultiSelectAnswerAdapter.questionsAnswerHashMap.get(question.getId()));
                answerSubmission.answers.addAll(currentAnswers);
                fillAnswerSubmission(currentAnswers, answerSubmission);
            } else {
                nextPage();
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
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnswerSubmission(studentSubmission.getId());
        solveQuizView.getAnswerSubmission(url);
        activity.showLoadingDialog();
    }

    private void submitSingleAnswer(AnswerSubmission answerSubmission) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postAnswerSubmission();
        solveQuizView.postAnswerSubmission(url, studentSubmission.getId(), answerSubmission);
        activity.showLoadingDialog();
    }

    private void deleteSingleAnswer(int questionId, int quizSubmissionId) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.deleteAnswerSubmission();
        solveQuizView.deleteAnswerSubmission(url, questionId, quizSubmissionId);
        activity.showLoadingDialog();
    }

    private void submitQuiz() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.submitQuiz();
        solveQuizView.submitQuiz(url, studentSubmission.getId());
        activity.showLoadingDialog();
    }

    private void showSubmissionDialog() {
        QuizSubmittedDialog quizSubmittedDialog = new QuizSubmittedDialog(activity, fragment);
        quizSubmittedDialog.show();
        countDownTimer.cancel();
    }

    private void setMatchReorder(List<Answers> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setMatch(Integer.toString(i + 1));
        }
    }

    private void nextPage() {
        if (isSubmit) {
            //todo validate empty answers
            //submitQuiz();
        } else {
            if (index < quizQuestion.getQuestions().size() - 1) {
                index++;
                question = quizQuestion.getQuestions().get(index);
                displayQuestionsAndAnswers(index);
            }
        }
    }

    private void checkQuestionHasAnswer(JSONObject jsonObject) {
        ArrayList<Answers> answers;
        for (Questions question : quizQuestion.getQuestions()) {
            if (question.getType().equals(Constants.TYPE_REORDER)) {
                setMatchReorder(question.getAnswers());
                singleMultiSelectAnswerAdapter.reorderAnswers.clear();
                singleMultiSelectAnswerAdapter.reorderAnswers.addAll(question.getAnswers());
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
                    singleMultiSelectAnswerAdapter.reorderAnswers.clear();
                    singleMultiSelectAnswerAdapter.reorderAnswers.addAll(question.getAnswers());
                } else if (question.getType().equals(Constants.TYPE_MULTIPLE_SELECT) || question.getType().equals(Constants.TYPE_MULTIPLE_CHOICE)) {
                    ArrayList<Answers> correctAnswers = new ArrayList<>();
                    for (int i = 0; i < answers.size(); i++) {
                        if (answers.get(i).isCorrect()) {
                            correctAnswers.add(answers.get(i));
                        }
                    }
                    singleMultiSelectAnswerAdapter.questionsAnswerHashMap.put(question.getId(), correctAnswers);
                    previousAnswersHashMap.put(question.getId(), answers);
                } else if (question.getType().equals(Constants.TYPE_MATCH)) {
                    if (mode == Constants.SOLVE_QUIZ) {
                        setMatchIndex(question.getAnswers().get(0).getOptions());
                        fillMatchHashmap(question.getAnswers().get(0).getOptions(), answers);
                    } else {
                        setMatchIndex((ArrayList<Answers>) question.getAnswers());
                        fillMatchHashmap((ArrayList<Answers>) question.getAnswers(), answers);
                    }

                } else {
                    singleMultiSelectAnswerAdapter.questionsAnswerHashMap.put(question.getId(), answers);
                    previousAnswersHashMap.put(question.getId(), answers);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillAnswerSubmission(ArrayList<Answers> currentAnswers, AnswerSubmission answerSubmission) {
        if (question.getType().equals(Constants.TYPE_MULTIPLE_SELECT) || question.getType().equals(Constants.TYPE_MULTIPLE_CHOICE)) {
            ArrayList<Answers> correctAnswers = new ArrayList<>();
            for (int i = 0; i < question.getAnswers().size(); i++) {
                question.getAnswers().get(i).setCorrect(false);
            }
            correctAnswers.addAll(question.getAnswers());
            for (int i = 0; i < currentAnswers.size(); i++) {
                for (int j = 0; j < correctAnswers.size(); j++) {
                    if (currentAnswers.get(i).getId() == correctAnswers.get(j).getId()) {
                        correctAnswers.get(j).setCorrect(true);
                    }
                }
            }
            answerSubmission.answers.clear();
            answerSubmission.answers.addAll(correctAnswers);
        }
        answerSubmission.setQuestionId(question.getId());
        submitSingleAnswer(answerSubmission);
    }

    private void fillMatchHashmap(ArrayList<Answers> options, ArrayList<Answers> answers) {
        for (int i = 0; i < answers.size(); i++) {
            for (int j = 0; j < options.size(); j++) {
                if (answers.get(i).getAnswerId() == options.get(j).getId()) {
                    singleMultiSelectAnswerAdapter.matchAnswersHashMap.put(answers.get(i).getMatch(), options.get(j));
                    previousMatchAnswersHashMap.put(answers.get(i).getMatch(), options.get(j));
                }
            }
        }
    }

    private void setMatchIndex(ArrayList<Answers> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setMatchIndex(i + 1);
        }
    }

    private void copyMatchMaps(HashMap<String, Answers> previousMap, HashMap<String, Answers> currentMap) {
        for (Map.Entry mapElement : currentMap.entrySet()) {
            Answers previousAnswer;
            String match = (String) mapElement.getKey();
            Answers answers = (Answers) mapElement.getValue();
            previousAnswer = answers;
            previousMap.put(match, previousAnswer);
        }
    }

    private Boolean compareHashMaps(HashMap<Integer, ArrayList<Answers>> previousAnswersHashMap, HashMap<Integer, ArrayList<Answers>> currentAnswersHashMap, Questions question) {
        Boolean isToCreateAnswer = false;
        if (currentAnswersHashMap.containsKey(question.getId())) {
            if (previousAnswersHashMap.containsKey(question.getId())) {
                ArrayList<Answers> currentAnswersArrayList = new ArrayList<>();
                ArrayList<Answers> previousAnswersArrayList = new ArrayList<>();
                currentAnswersArrayList.addAll(currentAnswersHashMap.get(question.getId()));
                previousAnswersArrayList.addAll(previousAnswersHashMap.get(question.getId()));
                if (question.getType().equals(Constants.TYPE_MULTIPLE_SELECT) && currentAnswersArrayList.isEmpty()) {
                    deleteSingleAnswer(question.getId(), studentSubmission.getId());
                } else {
                    if (currentAnswersArrayList.size() == previousAnswersArrayList.size() || question.getType().equals(Constants.TYPE_MULTIPLE_CHOICE)) {
                        for (int i = 0; i < currentAnswersArrayList.size(); i++) {
                            for (int j = 0; j < previousAnswersArrayList.size(); j++) {
                                if (currentAnswersArrayList.get(i).getId() == previousAnswersArrayList.get(j).getAnswerId()) {
                                    if (question.getType().equals(Constants.TYPE_MULTIPLE_CHOICE) || (question.getType().equals(Constants.TYPE_MULTIPLE_SELECT)) || (question.getType().equals(Constants.TYPE_TRUE_OR_FALSE))) {
                                        if (currentAnswersArrayList.get(i).isCorrect() != previousAnswersArrayList.get(j).isCorrect()) {
                                            isToCreateAnswer = true;
                                        }
                                    }
                                }
                            }
                        }
                        return isToCreateAnswer;
                    } else {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private long calculateTimerDuration(long quizDuration) {
        long durationLeft=0, timeElapsed;
        quizDuration = quizDuration * 60000; //minutes convert
        DateTime dateTime = new DateTime(studentSubmission.getCreatedAt());
        Calendar c = Calendar.getInstance();
        timeElapsed = c.getTimeInMillis() - dateTime.getMillis();
        durationLeft = quizDuration - timeElapsed;
        return durationLeft;
    }
}
