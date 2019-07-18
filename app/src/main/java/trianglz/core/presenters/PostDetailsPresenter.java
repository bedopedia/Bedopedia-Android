package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.PostDetails;

public interface PostDetailsPresenter {
    void ongetPostDetailsSuccess(ArrayList<PostDetails> postDetails);
    void ongetPostDetailsFailure();
}
