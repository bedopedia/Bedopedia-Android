package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.OnlineQuizzesPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.QuizzCourse;

/**
 * Created by gemy on 7/24/19.
 */
public class OnlineQuizzesView {
    private Context context;
    private Gson gson;
    private OnlineQuizzesPresenter onlineQuizzesPresenter;

    public OnlineQuizzesView(Context context, OnlineQuizzesPresenter onlineQuizzesPresenter) {
        this.context = context;
        this.onlineQuizzesPresenter = onlineQuizzesPresenter;
        gson = new Gson();
    }

    public void getQuizzesCourses(int studentId) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizzesCourses(studentId);
        UserManager.getQuizzesCourses(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                ArrayList<QuizzCourse> quizzCourses = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    QuizzCourse quizzCourse = gson.fromJson(response.optJSONObject(i).toString(), QuizzCourse.class);
                    quizzCourses.add(quizzCourse);
                }
                onlineQuizzesPresenter.onGetQuizzesCoursesSuccess(quizzCourses);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                onlineQuizzesPresenter.onGetQuizzesCoursesFailure();
            }
        });
    }
}
