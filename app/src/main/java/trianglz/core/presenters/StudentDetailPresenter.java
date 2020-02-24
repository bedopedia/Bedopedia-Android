package trianglz.core.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trianglz.models.Announcement;
import trianglz.models.BehaviorNote;
import trianglz.models.CourseGroup;
import trianglz.models.MessageThread;
import trianglz.models.Notification;
import trianglz.models.WeeklyPlannerResponse;

/**
 * Created by ${Aly} on 11/4/2018.
 */
public interface StudentDetailPresenter {
    void onGetStudentCourseGroupSuccess(ArrayList<CourseGroup> courseGroups);
    void onGetStudentCourseGroupFailure(String message,int code);

    void onGetStudentGradesSuccess(ArrayList<CourseGroup> courseGroups,String totalGrade);
    void onGetStudentGradesFailure(String message,int code);

    void oneGetTimeTableSuccess(ArrayList<Object> timeTableData);
    void onGetTimeTableFailure(String message,int code);


    void onGetBehaviorNotesSuccess( HashMap<String,List<BehaviorNote>> behaviorNoteHashMap);
    void onGetBehaviorNotesFailure(String message,int code);

    void onGetWeeklyPlannerSuccess(WeeklyPlannerResponse weeklyPlannerResponse);
    void onGetWeeklyPlannerFailure(String message, int code);

    void onGetNotificationSuccess(ArrayList<Notification> notification);
    void onGetNotificationFailure(String message, int code);


    void onGetAnnouncementsSuccess(ArrayList<Announcement> announcementArrayList);
    void onGetAnnouncementsFailure(String message, int code);


    void onGetMessagesSuccess(ArrayList<MessageThread> messageArrayList,int unreadMessageCount);
    void onGetMessagesFailure(String message, int code);

}
