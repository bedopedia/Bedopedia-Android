package trianglz.core.presenters;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public interface SplashPresenter {
    void onLoginSuccess();
    void onLoginFailure(String message,int code);

}
