package trianglz.core.presenters;

import org.json.JSONArray;

import trianglz.models.Actor;
import trianglz.models.Student;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public interface LoginPresenter {
    void onLoginSuccess(Actor actor);
    void onLoginSuccess();
    void onLoginFailure(String message,int code);
    void onGetStudentsHomeSuccess(Student student, JSONArray attendanceJsonArray);
    void onGetStudentsHomeFailure(String message,int errorCode);
    void onPasswordChangedSuccess(String newPassword);
    void onPasswordChangedFailure(String message, int errorCode);
}
