package trianglz.core.presenters;

import trianglz.models.Attendance;


/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
public interface TeacherAttendancePresenter {
    void onGetTeacherAttendanceSuccess(Attendance attendance);
    void onGetTeacherAttendanceFailure(String message,int code);
    void onBatchAttendanceCreatedSuccess();
    void onBatchAttendanceCreatedFailure(String message,int code);
    void onUpdateAttendanceSuccess();
    void onUpdateAttendanceFailure(String message,int code);
}
