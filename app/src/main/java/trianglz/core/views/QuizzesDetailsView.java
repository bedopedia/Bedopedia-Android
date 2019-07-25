package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.QuizzesDetailsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
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
    public void getQuizzesDetails(int studentId, int courseId) {
        UserManager.getQuizzesDetails(studentId, courseId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray(Constants.KEY_QUIZZES);
                ArrayList<Quizzes> quizzes = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    quizzes.add(Quizzes.create(jsonArray.optString(i)));
                }
                quizzesDetailsPresenter.onGetQuizzesDetailsSuccess(quizzes);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                quizzesDetailsPresenter.onGetQuizzesDetailsFailure();
            }
        });
    }
}