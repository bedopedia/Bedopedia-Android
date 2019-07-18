package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

public class PostDetailsView {
    private Gson gson;
    private Context context;
    private PostDetailsPresenter postDetailsPresenter;

    public PostDetailsView(Context context, PostDetailsPresenter postDetailsPresenter) {
        this.context = context;
        this.postDetailsPresenter = postDetailsPresenter;
        gson = new Gson();
    }

    public void getPostDetails(int courseId) {
        UserManager.getPostDetails(courseId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                postDetailsPresenter.ongetPostDetailsSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                postDetailsPresenter.ongetPostDetailsFailure();
            }
        });
    }
}
