package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.SingleCourseGroupPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Quizzes;

public class SingleCourseGroupView {
    private Context context;
    private SingleCourseGroupPresenter singleCourseGroupPresenter;

    public SingleCourseGroupView(Context context, SingleCourseGroupPresenter singleCourseGroupPresenter) {
        this.context = context;
        this.singleCourseGroupPresenter = singleCourseGroupPresenter;
    }

    public void getTeacherQuizzes (String courseGroupId) {
        UserManager.getTeacherQuizzes(courseGroupId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                ArrayList<Quizzes> quizzes = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    quizzes.add(Quizzes.create(responseArray.opt(i).toString()));
                }
                singleCourseGroupPresenter.onGetTeacherQuizzesSuccess(quizzes);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                singleCourseGroupPresenter.onGetTeacherQuizzesFailure(message, errorCode);
            }
        });
    }

}
