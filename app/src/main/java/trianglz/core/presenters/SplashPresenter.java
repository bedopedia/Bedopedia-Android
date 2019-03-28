package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Actor;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public interface SplashPresenter {
    void onLoginSuccess();
    void onLoginSuccess(Actor actor);
    void onLoginFailure(String message,int code);
    void onGetStudentsHomeSuccess(ArrayList<Object> objectArrayList);
    void onGetStudentsHomeFailure(String message,int errorCode);
}
