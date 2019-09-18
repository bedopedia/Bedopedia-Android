package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import trianglz.core.presenters.TeacherAttendancePresenter;
import trianglz.managers.api.ArrayResponseListener;
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

    public void getFullDayTeacherAttendance(String url) {
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

    public void createBatchAttendance(String url, String date, String comment, String status, int studentId, int timetableSlotId) {
        UserManager.createBatchAttendance(url, date, comment, status, studentId, timetableSlotId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                teacherAttendancePresenter.onBatchAttendanceCreatedSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onBatchAttendanceCreatedFailure(message, errorCode);
            }
        });
    }

    public void createBatchAttendance(String url, String date, String comment, String status, int studentId) {
        UserManager.createBatchAttendance(url, date, comment, status, studentId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                teacherAttendancePresenter.onBatchAttendanceCreatedSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onBatchAttendanceCreatedFailure(message, errorCode);
            }
        });
    }

    public void updateAttendance(String url, String comment, String status, int attendanceID, int timetableSlotId) {
        UserManager.updateAttendance(url, comment, status, timetableSlotId, attendanceID, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                teacherAttendancePresenter.onUpdateAttendanceSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onUpdateAttendanceFailure(message, errorCode);
            }
        });

    }

    public void updateAttendance(String url, String comment, String status, int attendanceID) {
        UserManager.updateAttendance(url, comment, status, attendanceID, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                teacherAttendancePresenter.onUpdateAttendanceSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onUpdateAttendanceFailure(message, errorCode);
            }
        });

    }
}
