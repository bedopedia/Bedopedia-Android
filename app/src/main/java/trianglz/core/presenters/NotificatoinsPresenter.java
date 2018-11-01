package trianglz.core.presenters;

import org.json.JSONObject;

/**
 * This file is spawned by Gemy on 11/1/2018.
 */
public interface NotificatoinsPresenter {
    void onGetNotificationSuccess(JSONObject response);
    void onGetNotificationFailure();
}
