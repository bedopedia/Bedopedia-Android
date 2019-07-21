package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.presenters.PostReplyPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.ui.adapters.PostReplyAdapter;

public class PostReplyView {

    private Gson gson;
    private Context context;
    private PostReplyPresenter postReplyPresenter;

    public PostReplyView(Context context, PostReplyPresenter postReplyPresenter) {
        this.context = context;
        this.postReplyPresenter = postReplyPresenter;
        gson = new Gson();
    }

    public void postReply (String message, int ownerId, int postId) {
        UserManager.postReply(message, ownerId, postId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                postReplyPresenter.onPostReplySuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                postReplyPresenter.onPostReplyFailure(message, errorCode);
            }
        });
    }

}
