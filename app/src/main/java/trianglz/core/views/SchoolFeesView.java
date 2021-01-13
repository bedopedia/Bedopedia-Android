package trianglz.core.views;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import trianglz.core.presenters.SchoolFeePresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Meta;
import trianglz.models.SchoolFee;
import trianglz.utils.Constants;

public class SchoolFeesView {
    private Context context;
    private SchoolFeePresenter presenter;

    public SchoolFeesView(Context context, SchoolFeePresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getSchoolFees(String url, int pageNumber, int numberPerPage) {
        UserManager.getSchoolFees(url, pageNumber, numberPerPage + "", new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Meta meta = new Meta(response.optJSONObject(Constants.KEY_META));
                Log.d("TAG", "onSuccess: " + response);
                presenter.onGetSchoolFeesSuccess(parseNotificationResponse(response), meta);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                Log.d("TAG", "onFailure: " + message.toString());
                presenter.onGetSchoolFeesFailure(message, errorCode);
            }
        });
    }

    private ArrayList<SchoolFee> parseNotificationResponse(JSONObject response) {
        ArrayList<SchoolFee> schoolFees = new ArrayList<>();
        JSONArray notificationArray = new JSONArray();
        try {
            notificationArray = response.getJSONArray(Constants.KEY_SCHOOL_FEES);
            Log.d("TAG", "parseNotificationResponse: " + notificationArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < notificationArray.length(); i++) {

            JSONObject notificationObj = notificationArray.optJSONObject(i);

            String time = notificationObj.optString("due_date");
            schoolFees.add(new SchoolFee(notificationObj.optInt("id"),
                    notificationObj.optString("name"),
                    notificationObj.optString("amount"),
                    formatDate(time),
                    notificationObj.optString("student_name")));
        }
        return schoolFees;
    }

    private String formatDate(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      //  DateFormat finalFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));

//        if (DateUtils.isToday(Objects.requireNonNull(date).getTime())) {
//            return context.getResources().getString(R.string.today);
//        }
        return dateFormat.format(date);
    }


}