package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.CreateTeacherPostPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.PostDetails;

/**
 * Created by Farah A. Moniem on 06/08/2019.
 */
public class CreateTeacherPostView {
    Context context;
    CreateTeacherPostPresenter createTeacherPostPresenter;

    public CreateTeacherPostView(Context context, CreateTeacherPostPresenter createTeacherPostPresenter) {
        this.context = context;
        this.createTeacherPostPresenter = createTeacherPostPresenter;
    }

    public void createPost(String url, String post, int ownerId, int courseGroupId, String postableType){
        UserManager.createTeacherPost(url, post, ownerId, courseGroupId, postableType, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                PostDetails postDetails=new PostDetails();
                //todo parse response
                createTeacherPostPresenter.onPostCreatedSuccess(postDetails);
            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });
    }
}