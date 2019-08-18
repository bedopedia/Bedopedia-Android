package trianglz.core.presenters;

import trianglz.models.PostDetails;

/**
 * Created by Farah A. Moniem on 06/08/2019.
 */
public interface CreateTeacherPostPresenter {
    void onPostCreatedSuccess(PostDetails post);
    void onPostCreatedFailure(String message, int errorCode);
}
