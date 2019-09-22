package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public void createBatchAttendance(String url, String date, String comment, String status, ArrayList<Integer> studentIds, int timetableSlotId) {
        UserManager.createBatchAttendance(url, date, comment, status,  studentIds, timetableSlotId, new ArrayResponseListener() {
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

    public void createBatchAttendance(String url, String date, String comment, String status, ArrayList<Integer> studentIds) {
        UserManager.createBatchAttendance(url, date, comment, status,  studentIds, new ArrayResponseListener() {
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

    public void deleteBatchAttendance(String url,ArrayList<Integer> studentIds){
        UserManager.deleteBatchAttendance(url, studentIds, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                teacherAttendancePresenter.onBatchAttendanceDeletedSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherAttendancePresenter.onBatchAttendanceCreatedFailure(message, errorCode);
            }
        });
    }
}
