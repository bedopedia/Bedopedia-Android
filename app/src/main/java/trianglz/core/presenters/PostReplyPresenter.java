package trianglz.core.presenters;

public interface PostReplyPresenter {
    void onPostReplySuccess();

    void onPostReplyFailure(String message, int errorCode);
}
