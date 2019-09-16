package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.TeacherAttendancePresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Attendance;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
public class TeacherAttendanceView {
    Context context;
    TeacherAttendancePresenter teacherAttendancePresenter;

    public TeacherAttendanceView(Context context, TeacherAttendancePresenter teacherAttendancePresenter) {
        this.context = context;
        this.teacherAttendancePresenter = teacherAttendancePresenter;
    }

    public void getTeacherAttendance(String url) {
        UserManager.getTeacherAttendance(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Attendance attendance = Attendance.create(response.toString());
                teacherAttendancePresenter.onGetTeacherAttendanceSuccess(attendance);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onGetTeacherAttendanceFailure(message, errorCode);
            }
        });
    }
}
