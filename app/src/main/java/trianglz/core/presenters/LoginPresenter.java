package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Actor;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public interface LoginPresenter {
    void onLoginSuccess(Actor actor);
    void onLoginSuccess();
    void onLoginFailure(String message,int code);
    void onGetStudentsHomeSuccess(ArrayList<Object> objectArrayList);
    void onGetStudentsHomeFailure(String message,int errorCode);
}
