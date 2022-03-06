package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;

import trianglz.core.presenters.GradesPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.PostsResponse;

public class GradesView {
    private Context context;
    private GradesPresenter gradesPresenter;

    public GradesView(Context context, GradesPresenter gradesPresenter) {
        this.context = context;
        this.gradesPresenter = gradesPresenter;
    }

    public void getGradesCourses(int studentId) {
        UserManager.getGradesCourses(studentId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                gradesPresenter.onGetGradesCoursesSuccess(PostsResponse.getArrayList(responseArray.toString()));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradesPresenter.onGetGradesCoursesFailure(message, errorCode);
            }
        });
    }

}
