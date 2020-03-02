package trianglz.core.presenters;

import org.json.JSONArray;

import trianglz.models.Actor;
import trianglz.models.Student;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public interface SplashPresenter {
    void onLoginSuccess();
    void onLoginSuccess(Actor actor);
    void onLoginFailure(String message,int code);
    void onGetStudentsHomeSuccess(Student student, JSONArray attendanceArray);
    void onGetStudentsHomeFailure(String message,int errorCode);
    void updateTokenSuccess();
    void updateTokenFailure();
}
