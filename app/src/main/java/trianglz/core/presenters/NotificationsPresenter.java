package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Notification;

/**
 * This file is spawned by Gemy on 11/1/2018.
 */
public interface NotificationsPresenter {
    void onGetNotificationSuccess(ArrayList<Notification> notifications);
    void onGetNotificationFailure();
}
