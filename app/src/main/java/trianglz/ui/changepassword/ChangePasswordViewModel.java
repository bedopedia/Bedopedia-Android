package trianglz.ui.changepassword;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

public class ChangePasswordViewModel extends ViewModel {
    public MutableLiveData<String> oldPassword;
    public MutableLiveData<String> newPassword;
    public MutableLiveData<Boolean> hideDialogEvent, finishEvent;
    public MutableLiveData<Pair<String,Integer>> showErrorDialog;


    public ChangePasswordViewModel() {
        super();
        oldPassword = new MutableLiveData<>();
        newPassword = new MutableLiveData<>();
        hideDialogEvent = new MutableLiveData<>();
        finishEvent = new MutableLiveData<>();
        showErrorDialog = new MutableLiveData<>();
    }
    void changePassword(String currentPassword, String newPassword) {
        int userId = Integer.parseInt(SessionManager.getInstance().getUserId());
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.changePassword(userId);
        UserManager.changePassword(url, currentPassword, userId, newPassword, SessionManager.getInstance().getHeaderHashMap(), new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                hideDialogEvent.setValue(true);
                finishEvent.setValue(true);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                hideDialogEvent.setValue(true);
                showErrorDialog.setValue(Pair.create(message, errorCode));
            }
        });

    }

    void hideDialogHandled() {
        hideDialogEvent.setValue(false);
    }
    void finishEventHandled () {
        finishEvent.setValue(false);
    }
    void showErrorDialogHandled() {
        showErrorDialog.setValue(null);
    }
}
