package trianglz.core.presenters;

import org.json.JSONArray;

import trianglz.models.Actor;
import trianglz.models.Student;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public interface LoginPresenter {
    void onLoginSuccess(Actor actor);
    void onParentLoginSuccess(JSONArray students);
    void onLoginFailure(String message,int code);
    void onStudentLoginSuccess(Student student, JSONArray attendanceJsonArray);
    void onGetStudentsHomeFailure(String message,int errorCode);
    void onPasswordChangedSuccess(String newPassword);
    void onPasswordChangedFailure(String message, int errorCode);
}
