package trianglz.core.presenters;

import trianglz.models.Reply;

public interface PostReplyPresenter {
    void onPostReplySuccess(Reply reply);

    void onPostReplyFailure(String message, int errorCode);
}
