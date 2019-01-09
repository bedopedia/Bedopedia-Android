package trianglz.core.presenters;

import trianglz.models.Actor;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public interface LoginPresenter {
    void onLoginSuccess(Actor actor);
    void onLoginSuccess();
    void onLoginFailure(String message,int code);

}
