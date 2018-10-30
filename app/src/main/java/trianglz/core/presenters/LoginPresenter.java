package trianglz.core.presenters;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public interface LoginPresenter {
    void onLoginSuccess();
    void onLoginFailure(String message,int code);
    void onTokenUpdatedSuccess();
    void onTokenUpdatedFailure();
}
