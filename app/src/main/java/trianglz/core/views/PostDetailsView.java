package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.PostDetails;

public class PostDetailsView {
    private Gson gson;
    private Context context;
    private PostDetailsPresenter postDetailsPresenter;

    public PostDetailsView(Context context, PostDetailsPresenter postDetailsPresenter) {
        this.context = context;
        this.postDetailsPresenter = postDetailsPresenter;
        gson = new Gson();
    }

    public void getPostDetails(int courseId, int page) {
        UserManager.getPostDetails(courseId, page, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("posts");
                ArrayList<PostDetails> postDetails = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    postDetails.add(PostDetails.create(jsonArray.opt(i).toString()));
                }
                postDetailsPresenter.ongetPostDetailsSuccess(postDetails);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                postDetailsPresenter.ongetPostDetailsFailure();
            }
        });
    }
}
