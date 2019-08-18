package trianglz.core.presenters;

import org.json.JSONObject;

/**
 * Created by Farah A. Moniem on 08/08/2019.
 */
public interface AttachFileToTeacherPostPresenter {
    void onAttachmentUploadedSuccess(JSONObject response);
    void onAttachmentUploadedFailure(String message, int errorCode);
}
