package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import java.util.Date;

import trianglz.core.presenters.CreatePersonalEventPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by Farah A. Moniem on 30/07/2019.
 */
public class CreatePersonalEventView {
    Context context;
    CreatePersonalEventPresenter createPersonalEventPresenter;

    public CreatePersonalEventView(Context context, CreatePersonalEventPresenter createPersonalEventPresenter) {
        this.context = context;
        this.createPersonalEventPresenter = createPersonalEventPresenter;
    }

    public void createEvent(String url, Date startDate, Date endDate, String type, String allDay, String title, String listenerType, String listenerId, String description, String cancel ){
        UserManager.createEvent(url,startDate,endDate,type,allDay,title,listenerType,listenerId,description,cancel, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                createPersonalEventPresenter.onCreateEventSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                createPersonalEventPresenter.onCreateEventFailure(message,errorCode);
            }
        });
    }
}
