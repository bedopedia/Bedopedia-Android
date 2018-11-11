package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Subject;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public interface NewMessagePresenter {
    void onGetCourseGroupsSuccess(ArrayList<Subject> subjectArrayList);
    void onGetCourseGroupsFailure(String message,int errorCode);
}
