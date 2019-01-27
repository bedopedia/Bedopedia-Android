package trianglz.core.presenters;

import trianglz.models.Actor;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public interface SplashPresenter {
    void onLoginSuccess();
    void onLoginSuccess(Actor actor);
    void onLoginFailure(String message,int code);

}
