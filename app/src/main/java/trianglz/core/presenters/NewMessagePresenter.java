package trianglz.core.presenters;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public interface NewMessagePresenter {
    void onGetCourseGroupsSuccess();
    void onGetCourseGroupsFailure(String message,int errorCode);
}
