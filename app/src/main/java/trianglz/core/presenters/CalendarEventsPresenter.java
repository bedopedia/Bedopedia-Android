package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Event;

/**
 * Created by Farah A. Moniem on 28/07/2019.
 */
public interface CalendarEventsPresenter {
    void onGetEventsSuccess(ArrayList<Event> events);
    void onGetEventsFailure(String message,int code);

}
