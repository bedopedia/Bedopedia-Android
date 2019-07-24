package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.QuizzCourse;

/**
 * Created by gemy on 7/24/19.
 */
public interface OnlineQuizzesPresenter {
    void onGetQuizzesCoursesSuccess(ArrayList<QuizzCourse> quizzCourses);
    void onGetQuizzesCoursesFailure();
}
