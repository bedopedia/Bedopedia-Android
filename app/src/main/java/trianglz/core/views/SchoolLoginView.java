package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.School;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class SchoolLoginView {
    private Context context;
    private SchoolLoginPresenter presenter;

    public SchoolLoginView(Context context, SchoolLoginPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getSchoolUrl(String url, String code) {
        UserManager.getSchoolUrl(url, code, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String url = response.optString(Constants.KEY_URL);
                presenter.onGetSchoolUrlSuccess(url);

            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSchoolUrlFailure(message, errorCode);
            }
        });
    }

    public void getSchoolData(String url, String code) {
        UserManager.getSchoolUrl(url, code, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                int id = Integer.parseInt(response.optString(Constants.KEY_ID));
                String name = response.optString(Constants.KEY_NAME);
                String avatarUrl = response.optString(Constants.KEY_AVATER_URL);
                String code = response.optString(Constants.KEY_CODE);
                String url = response.optString(Constants.KEY_URL);
                School school = new School(id, name, code, url,avatarUrl);
                presenter.onGetSchoolDataSuccess(school);

            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSchoolDataFailure(message, errorCode);
            }
        });
    }
}
