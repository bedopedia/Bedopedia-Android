package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.PostsPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.PostsResponse;

public class PostsView {
    private Gson gson;
    private Context context;
    private PostsPresenter postsPresenter;

    public PostsView(Context context, PostsPresenter postsPresenter) {
        this.context = context;
        this.postsPresenter = postsPresenter;
        gson = new Gson();
    }
    public void getRecentPosts(int id) {
        UserManager.getRecentPosts(id, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                postsPresenter.onGetPostsSuccess(parsePostsResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                postsPresenter.onGetPostsFailure(message, errorCode);
            }
        });
    }

    private ArrayList<PostsResponse> parsePostsResponse(JSONArray response) {
        try {
            ArrayList<PostsResponse> postsResponses = new ArrayList<>();
            for (int i = 0 ; i < response.length(); i++) {
                postsResponses.add(PostsResponse.create(response.getJSONObject(i).toString()));
            }
            return postsResponses;
        } catch (JSONException e) {
            return null;
        }
    }

}
