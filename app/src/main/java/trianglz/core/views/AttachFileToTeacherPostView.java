package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;

import trianglz.core.presenters.AttachFileToTeacherPostPresenter;
import trianglz.managers.api.MultiPartResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by Farah A. Moniem on 08/08/2019.
 */
public class AttachFileToTeacherPostView {
    private  Context context;
    private  AttachFileToTeacherPostPresenter attachFileToTeacherPostPresenter;

    public AttachFileToTeacherPostView(Context context, AttachFileToTeacherPostPresenter attachFileToTeacherPostPresenter) {
        this.context = context;
        this.attachFileToTeacherPostPresenter = attachFileToTeacherPostPresenter;
    }
    public  void attachFileToTeacherPost(String url, int postId, File file){
        UserManager.attachFileToTeacherPost(url, postId, file, new MultiPartResponseListener() {
            @Override
            public void onProgress(long uploaded, long total) {

            }

            @Override
            public void onSuccess(JSONObject response) {
                attachFileToTeacherPostPresenter.onAttachmentUploadedSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                attachFileToTeacherPostPresenter.onAttachmentUploadedFailure(message,errorCode);

            }
        });
    }
}
