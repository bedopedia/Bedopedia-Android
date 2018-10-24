package trianglz.managers.api;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface ResponseListener {
    void onSuccess(Object response);
    void onFailure(String message,int errorCode);
}
