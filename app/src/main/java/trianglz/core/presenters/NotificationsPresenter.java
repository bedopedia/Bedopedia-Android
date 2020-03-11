package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Meta;
import trianglz.models.Notification;

/**
 * This file is spawned by Gemy on 11/1/2018.
 */
public interface NotificationsPresenter {
    void onGetNotificationSuccess(ArrayList<Notification> notifications, Meta meta);
    void onGetNotificationFailure(String message, int errorCode);
}
