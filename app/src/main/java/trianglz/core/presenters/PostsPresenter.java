package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.PostsResponse;

public interface PostsPresenter {
    void onGetPostsSuccess(ArrayList<PostsResponse> parsePostsResponse);

    void onGetPostsFailure(String message, int errorCode);
}
