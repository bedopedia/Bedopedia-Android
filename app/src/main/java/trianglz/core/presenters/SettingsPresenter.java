package trianglz.core.presenters;

/**
 * Created by Farah A. Moniem on 15/12/2019.
 */
public interface SettingsPresenter {
    void onPasswordChangedSuccess(String newPassword);
    void onPasswordChangedFailure(String message, int errorCode);

    void onLogoutSuccess();
    void onLogoutFailure(String message, int errorCode);
}
