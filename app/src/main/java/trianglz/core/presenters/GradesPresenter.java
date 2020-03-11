package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.PostsResponse;

public interface GradesPresenter {
    void onGetGradesCoursesSuccess(ArrayList<PostsResponse> arrayList);
    void onGetGradesCoursesFailure (String message, int errorCode);
}
