package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.QuizzesDetailsPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Meta;
import trianglz.models.QuizQuestion;
import trianglz.models.Quizzes;
import trianglz.utils.Constants;

// Created by gemy
public class QuizzesDetailsView {
    private Context context;
    private Gson gson;
    private QuizzesDetailsPresenter quizzesDetailsPresenter;

    public QuizzesDetailsView(Context context, QuizzesDetailsPresenter quizzesDetailsPresenter) {
        this.context = context;
        this.quizzesDetailsPresenter = quizzesDetailsPresenter;
    }

    public void getQuizzesDetails( int courseId) {
        UserManager.getQuizzesDetails(courseId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray(Constants.KEY_QUIZZES);
                JSONObject metaJsonObject = response.optJSONObject(Constants.KEY_META);
                Meta meta = new Meta(metaJsonObject);
                ArrayList<Quizzes> quizzes = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    quizzes.add(Quizzes.create(jsonArray.optString(i)));
                }
                quizzesDetailsPresenter.onGetQuizzesDetailsSuccess(quizzes, meta);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                quizzesDetailsPresenter.onGetQuizzesDetailsFailure(message, errorCode);
            }
        });
    }

    public void submitQuiz(String url, int submissionId) {
        UserManager.submitQuiz(url, submissionId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                quizzesDetailsPresenter.onSubmitQuizSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                quizzesDetailsPresenter.onSubmitQuizFailure(message, errorCode);
            }
        });
    }

    public void getTeacherQuizzes(String courseGroupId) {
        UserManager.getTeacherQuizzes(courseGroupId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                ArrayList<Quizzes> quizzes = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    quizzes.add(Quizzes.create(responseArray.opt(i).toString()));
                }
                quizzesDetailsPresenter.onGetTeacherQuizzesSuccess(quizzes);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                quizzesDetailsPresenter.onGetTeacherQuizzesFailure(message, errorCode);
            }
        });
    }
    public void getQuizQuestions(String url){
        UserManager.getQuizQuestions(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                QuizQuestion quizQuestion = QuizQuestion.create(response.toString());
                quizzesDetailsPresenter.onGetQuizQuestionsSuccess(quizQuestion);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                quizzesDetailsPresenter.onGetQuizQuestionsFailure(message, errorCode);
            }
        });
    }
}
