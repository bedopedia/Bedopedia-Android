package trianglz.core.views;

import android.content.Context;

import org.json.JSONException;
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

    public void getSchoolUrl(String url,String code){
        UserManager.getSchoolUrl(url,code, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String url = response.optString(Constants.KEY_URL);
                presenter.onGetSchoolUrlSuccess(url);
                
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSchoolUrlFailure();
            }
        });
    }

    public void getSchoolData(String url,String code) {
        UserManager.getSchoolUrl(url, code, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                int id = Integer.parseInt(response.optString(Constants.KEY_ID));
                String name = response.optString(Constants.KEY_NAME);
                String schoolDescription = response.optString(Constants.KEY_SCHOOL_DESCRIPTION);
                String avatarUrl = response.optString(Constants.KEY_AVATER_URL);
                String gaTrackingId = response.optString(Constants.KEY_GA_TRACKING_ID);

                try {
                    JSONObject baseResponse = new JSONObject(String.valueOf(response));
                    JSONObject configObject = baseResponse.optJSONObject(Constants.KEY_CONFIG);
                    String attendanceAllowSlot = configObject.getString("attendance_allow_teacher_record_slot");
                    String attendanceAllowFullDay = configObject.optString("attendance_allow_teacher_record_full_day");
                    // TODO: 10/29/2018 parse json array
                    String defaultConfigSlot = "attendance_allow_teacher_record_slot";
                    String defaultConfigFullDay = "attendance_allow_teacher_record_full_day";

                    School school = new School(id, name, schoolDescription, avatarUrl, gaTrackingId, attendanceAllowSlot,
                            attendanceAllowFullDay, defaultConfigSlot, defaultConfigFullDay);
                    presenter.onGetSchoolDataSuccess(school);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });
    }
}
