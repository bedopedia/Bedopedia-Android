package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.SettingsPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by Farah A. Moniem on 15/12/2019.
 */
public class SettingsView {
    private Context context;
    private SettingsPresenter settingsPresenter;

    public SettingsView(Context context, SettingsPresenter settingsPresenter) {
        this.context = context;
        this.settingsPresenter = settingsPresenter;
    }

    public void changePassword(String url, String currentPassword, int userId, String newPassword) {
        UserManager.changePassword(url, currentPassword, userId, newPassword, SessionManager.getInstance().getHeaderHashMap(), new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                settingsPresenter.onPasswordChangedSuccess(newPassword);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                settingsPresenter.onPasswordChangedFailure(message, errorCode);
            }
        });

    }

    public void logout() {
        UserManager.logout(new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                settingsPresenter.onLogoutSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                settingsPresenter.onLogoutFailure(message, errorCode);
            }
        });
    }
}
