package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.CalendarEventsPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Event;

/**
 * Created by Farah A. Moniem on 28/07/2019.
 */
public class CalendarEventsView {
    Context context;
    CalendarEventsPresenter calendarEventsPresenter;

    public CalendarEventsView(Context context, CalendarEventsPresenter calendarEventsPresenter) {
        this.context = context;
        this.calendarEventsPresenter = calendarEventsPresenter;
    }

    public void getEvents(String url) {
        UserManager.getEvents(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                Gson gson = new Gson();
                ArrayList<Event> events = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    Event event = gson.fromJson(responseArray.optJSONObject(i).toString(), Event.class);
                    events.add(event);
                }
                calendarEventsPresenter.onGetEventsSuccess(events);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                calendarEventsPresenter.onGetEventsFailure(message, errorCode);
            }
        });
    }
}
