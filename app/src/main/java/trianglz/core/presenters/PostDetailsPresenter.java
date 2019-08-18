package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.PostDetails;

public interface PostDetailsPresenter {
    void onGetPostDetailsSuccess(ArrayList<PostDetails> postDetails);
    void onGetPostDetailsFailure();
}
