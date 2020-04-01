package trianglz.core.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import Tools.CalendarUtils;
import trianglz.core.presenters.StudentDetailPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Announcement;
import trianglz.models.AnnouncementReceiver;
import trianglz.models.BehaviorNote;
import trianglz.models.CourseGroup;
import trianglz.models.DailyNotes;
import trianglz.models.Day;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Notification;
import trianglz.models.Participant;
import trianglz.models.PlannerSubject;
import trianglz.models.TimeTableSlot;
import trianglz.models.User;
import trianglz.models.WeeklyPlan;
import trianglz.models.WeeklyPlannerResponse;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 11/4/2018.
 */
public class StudentDetailView {
    private Context context;
    private StudentDetailPresenter studentDetailPresenter;

    public StudentDetailView(Context context, StudentDetailPresenter studentDetailPresenter) {
        this.context = context;
        this.studentDetailPresenter = studentDetailPresenter;
    }



    public void getStudentGrades(String url, final ArrayList<CourseGroup> courseGroups) {
        UserManager.getStudentGrades(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String totalGrade = response.optJSONObject(Constants.KEY_GRADE).optString(Constants.KEY_LETTER);
                studentDetailPresenter.onGetStudentGradesSuccess(parseStudentGrades(response, courseGroups), totalGrade);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetStudentGradesFailure(message,errorCode);
            }
        });
    }


    public void getStudentTimeTable(String url) {
        UserManager.getStudentTimeTable(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                studentDetailPresenter.oneGetTimeTableSuccess(parseStudentTimeTable(responseArray));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetTimeTableFailure(message, errorCode);
            }
        });

    }


    public void getStudentBehavioursNotes(String url,String id){
        UserManager.getStudentBehaviourNotes(url, id, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                studentDetailPresenter.onGetBehaviorNotesSuccess(parseBehaviourNotes(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetBehaviorNotesFailure(message,errorCode);
            }
        });

    }


    public void getWeeklyPlanner(){
        UserManager.getWeeklyPlanner(new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                studentDetailPresenter.onGetWeeklyPlannerSuccess(parseWeeklyPlannerResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetWeeklyPlannerFailure(message, errorCode);
            }
        });

    }



    private ArrayList<trianglz.models.CourseGroup> parseStudentCourseResponse(JSONArray responseArray) {
        ArrayList<trianglz.models.CourseGroup> courseGroups = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject courseGroupData = responseArray.optJSONObject(i);
            int id = courseGroupData.optInt(Constants.KEY_ID);
            int courseId = courseGroupData.optInt(Constants.KEY_COURSE_ID);
            String name = courseGroupData.optString(Constants.KEY_NAME);
            String courseName = courseGroupData.optString(Constants.KEY_COURSE_NAME);
            courseGroups.add(new trianglz.models.CourseGroup(id, courseId, name, courseName));
        }
        return courseGroups;
    }


    private ArrayList<CourseGroup> parseStudentGrades(JSONObject response, ArrayList<CourseGroup> courseGroups) {
        JSONArray gradesJsonArray = response.optJSONArray(Constants.KEY_COURSES_GRADES);
        for (int i = 0; i < gradesJsonArray.length(); i++) {
            JSONObject courseData = gradesJsonArray.optJSONObject(i);
            if (courseData.has(Constants.KEY_COURSE_ID))
                for (int j = 0; j < courseGroups.size(); j++) {
                    if (courseGroups.get(j).getCourseId() == courseData.optInt(Constants.KEY_COURSE_ID)
                            && !(courseData.opt(Constants.KEY_GRADE) instanceof JSONArray)) {
                        courseGroups.get(j).setGrade(courseData.optJSONObject(Constants.KEY_GRADE).optString(Constants.KEY_NUMERIC));
                        courseGroups.get(j).setLetter(courseData.optJSONObject(Constants.KEY_GRADE).optString(Constants.KEY_LETTER));
                        if (courseData.optString(Constants.KEY_ICON).equals("null"))
                            courseGroups.get(j).setIcon(Constants.KEY_DRAGON);
                        else
                            courseGroups.get(j).setIcon(courseData.optString(Constants.KEY_ICON));
                    } else {
                        courseGroups.get(j).setIcon("non");
                    }
                    JSONArray gradingPeriodsGradesJsonArray = courseData.optJSONArray(Constants.KEY_GRADING_PERIODS_GRADES);
                    for(int gradingPeriodPos =0; gradingPeriodPos<gradingPeriodsGradesJsonArray.length();gradingPeriodPos++){
                        JSONObject gradingPeriodGradeJsonObject = gradingPeriodsGradesJsonArray.optJSONObject(gradingPeriodPos);
                        boolean publish = gradingPeriodGradeJsonObject.optBoolean(Constants.KEY_PUBLISH);
                        if(!publish){
                            courseGroups.get(j).publish = false;
                        }
                        if(!publish){
                            break;
                        }
                    }
                }

        }
       ArrayList<CourseGroup> courseGroupWithnoEmptyGrade = new ArrayList<>();
        for(int i = 0; i <courseGroups.size(); i++){
            if(courseGroups.get(i).getGrade() != null){
                courseGroupWithnoEmptyGrade.add(courseGroups.get(i));
            }
        }
        return courseGroupWithnoEmptyGrade;

    }

    private ArrayList<Object> parseStudentTimeTable(JSONArray jsonArray) {
        ArrayList<Object> timeTableData = new ArrayList<>();
        String nextSlot = "";
        List<TimeTableSlot> todaySlots = new ArrayList<TimeTableSlot>();
        List<TimeTableSlot> tomorrowSlots = new ArrayList<>();
        Calendar calendar = CalendarUtils.getCalendarWithoutDate();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        String tomorrow = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        today = today.toLowerCase();
        tomorrow = tomorrow.toLowerCase();
//        if (today.equals(Constants.THURSDAY)) {
//            tomorrow = "sunday";
//        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject slot = jsonArray.optJSONObject(i);
            String from = slot.optString(Constants.KEY_FROM);
            String to = slot.optString(Constants.KEY_TO);
            String day = slot.optString(Constants.KEY_DAY);
            String courseName = slot.optString(Constants.KEY_COURSE_NAME);
            String classRoom = slot.optString(Constants.KEY_SCHOOL_UNIT);
            String sectionName = slot.optString("section_name");
            String courseGroupName = slot.optString("course_group_name");

            Date fromDate = null;
            Date toDate = null;
            try {
                fromDate = formatter.parse(from);
                toDate = formatter.parse(to);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (day.equals(today)) {
                todaySlots.add(new TimeTableSlot(fromDate, toDate, day, courseName, classRoom,courseGroupName,sectionName));
            } else if (day.equals(tomorrow)) {
                tomorrowSlots.add(new TimeTableSlot(fromDate, toDate, day, courseName, classRoom,courseGroupName,sectionName));
            }

        }
        Date current = new Date();
        boolean nextSlotFound = false;
        Collections.sort(todaySlots);
        Collections.sort(tomorrowSlots);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        for (TimeTableSlot timeSlotIterator : todaySlots) {
                nextSlotFound = true;
                nextSlot = ("Next: " + timeSlotIterator.getCourseName() + ", " + timeSlotIterator.getDay() + " " + dateFormat.format(timeSlotIterator.getFrom()));
                break;
        }
        if (!nextSlotFound && tomorrowSlots.size() > 0) {
            TimeTableSlot timeSlot = tomorrowSlots.get(0);
            nextSlot = ("Next: " + timeSlot.getCourseName() + ", " + timeSlot.getDay() + " " + dateFormat.format(timeSlot.getFrom()));
        }
        timeTableData.add(todaySlots);
        timeTableData.add(tomorrowSlots);
        timeTableData.add(nextSlot);
        return timeTableData;
    }

    private   HashMap<String,List<BehaviorNote>> parseBehaviourNotes(JSONObject response){
        HashMap<String,List<BehaviorNote>> behaviorNoteHashMap = new HashMap<>();
        List<BehaviorNote> positiveBehaviorNotesList = new ArrayList<>();
        List<BehaviorNote> negativeBehaviorNotesList = new ArrayList<>();
        List<BehaviorNote> otherBehaviorNotesList = new ArrayList<>();
        JSONArray behaviourNotes = response.optJSONArray(Constants.KEY_BEHAVIOUR_NOTES);
        for(int i = 0 ; i<behaviourNotes.length(); i++){
            BehaviorNote behaviorNote = BehaviorNote.create(behaviourNotes.opt(i).toString());
            switch (behaviorNote.type) {
                case Constants.GOOD:
                    positiveBehaviorNotesList.add(behaviorNote);
                    break;
                case Constants.BAD:
                    negativeBehaviorNotesList.add(behaviorNote);
                    break;
                case Constants.OTHER:
                    otherBehaviorNotesList.add(behaviorNote);
                    break;
            }
        }
        behaviorNoteHashMap.put(Constants.KEY_POSITIVE,positiveBehaviorNotesList);
        behaviorNoteHashMap.put(Constants.KEY_NEGATIVE,negativeBehaviorNotesList);
        behaviorNoteHashMap.put(Constants.OTHER,otherBehaviorNotesList);
        return behaviorNoteHashMap;

    }


    private ArrayList<Notification> parseNotificationResponse(JSONObject response) {
        ArrayList<Notification> notifications = new ArrayList<>();
        JSONArray notificationArray = new JSONArray();
        try {
            notificationArray = response.getJSONArray(Constants.KEY_NOTIFICATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < notificationArray.length(); i++) {

            JSONObject notificationObj = notificationArray.optJSONObject(i);
            JSONObject to = notificationObj.optJSONObject("to");
            String time = notificationObj.optString("created_at");
            notifications.add(new Notification(notificationObj.optString("text"),
                    formatDate(time),
                    notificationObj.optString("logo"),
                    to.optString("firstname"),
                    notificationObj.optString("message")));

        }
        return notifications;
    }

    private String formatDate(String time) {
        ISO8601DateFormat iso = new ISO8601DateFormat();
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd,' 'hh:mm a");
        Date date = null;
        try {
            date = iso.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate = dateFormat.format(date);
        if (DateUtils.isToday(Objects.requireNonNull(date).getTime())) {
            return "Today";
        }
        return finalDate;
    }


    private ArrayList<Announcement> parseAnnouncementResponse(JSONObject response){
        JSONArray announcementJsonArray  = response.optJSONArray(Constants.KEY_ANNOUNCEMENTS);
        ArrayList<Announcement> announcementArrayList = new ArrayList<>();
        for(int i = 0; i< announcementJsonArray.length(); i++){
            JSONObject announcementJsonObject = announcementJsonArray.optJSONObject(i);
            ArrayList<AnnouncementReceiver> announcementReceiverArrayList = new ArrayList<>();
            int id = announcementJsonObject.optInt(Constants.KEY_ID);
            String title = announcementJsonObject.optString(Constants.KEY_TITLE);
            String body = announcementJsonObject.optString(Constants.KEY_BODY);
            String endAt = announcementJsonObject.optString(Constants.KEY_END_AT);
            int adminId = announcementJsonObject.optInt(Constants.KEY_ADMIN_ID);
            String createdAt = announcementJsonObject.optString(Constants.KEY_CREATED_AT);
            String imageUrl =  announcementJsonObject.optString(Constants.KEY_IMAGE_URL);
            JSONArray announcementReceiversJsonArray = announcementJsonObject.optJSONArray(Constants.KEY_ANNOUNCEMENT_RECEIVERS);
            for(int j= 0; j< announcementReceiversJsonArray.length(); j++){
                JSONObject announcementReceiverObject = announcementReceiversJsonArray.optJSONObject(j);
                int annoucementReceiverId = announcementReceiverObject.optInt(Constants.KEY_ID);
                int annoucenmentID = announcementReceiverObject.optInt(Constants.KEY_ANNOUCEMENT_ID);
                String userType =  announcementReceiverObject.optString(Constants.KEY_USER_TYPE);
                AnnouncementReceiver announcementReceiver = new AnnouncementReceiver(annoucementReceiverId,annoucenmentID,userType);
                announcementReceiverArrayList.add(announcementReceiver);
            }
            announcementArrayList.add( new Announcement(id,title,body,formatDate(endAt),formatDate(createdAt),adminId,imageUrl, announcementReceiverArrayList));
        }
        return announcementArrayList;
    }

    private ArrayList<MessageThread> parseGetMessagesResponse(JSONObject jsonObject) {

        ArrayList<MessageThread> messageThreadArrayList = new ArrayList<>();
        JSONArray threads = jsonObject.optJSONArray(Constants.KEY_MESSAGE_THREADS);
        for (int i = 0; i < threads.length(); i++) {
            JSONObject messageThread = threads.optJSONObject(i);
            int courseId = messageThread.optInt(Constants.KEY_COURSE_ID);
            String courseName = messageThread.optString(Constants.KEY_COURSE_NAME);
            int id = messageThread.optInt(Constants.KEY_ID);
            boolean isRead = messageThread.optBoolean(Constants.KEY_IS_READ);
            JSONArray avatarJsonArray =  messageThread.optJSONArray(Constants.KEY_OTHER_AVATARS);
            String otherAvatar;
            if(avatarJsonArray.length()>0){
                otherAvatar = avatarJsonArray.optString(avatarJsonArray.length()-1);
            }else {
                otherAvatar = avatarJsonArray.toString();
            }
            String otherNames = messageThread.optString(Constants.KEY_OTHER_NAMES);
            String tag = messageThread.optString(Constants.KEY_TAG);
            String lastAddedDate = messageThread.optString(Constants.KEY_LAST_ADDED_DATE);
            String name = messageThread.optString(Constants.KEY_NAME);
            JSONArray participantsJsonArray = messageThread.optJSONArray(Constants.KEY_PARTICIPANTS);
            ArrayList<Participant> participantArrayList = new ArrayList<>();
            if(participantsJsonArray != null) {
                for (int p = 0; p < participantsJsonArray.length(); p++) {
                    JSONObject participantJsonObject = participantsJsonArray.optJSONObject(p);
                    String participantName = participantJsonObject.optString(Constants.KEY_NAME);
                    int threadId = participantJsonObject.optInt(Constants.KEY_THREAD_ID);
                    int userId = participantJsonObject.optInt(Constants.KEY_USER_ID);
                    String userAvatarUrl = participantJsonObject.optString(Constants.KEY_USER_AVATAR_URL);
                    participantArrayList.add(new Participant(participantName, threadId, userId, userAvatarUrl));
                }
            }
            JSONArray messages = messageThread.optJSONArray(Constants.KEY_MESSAGES);
            ArrayList<Message> messageArrayList = new ArrayList<>();
            for (int j = 0; j < messages.length(); j++) {
                JSONObject messageObj = messages.optJSONObject(j);
                JSONObject user = messageObj.optJSONObject(Constants.KEY_USER);
                int userId = user.optInt(Constants.KEY_ID);
                String firstName = user.optString(Constants.KEY_FIRST_NAME);
                String lastName = user.optString(Constants.KEY_LAST_NAME);
                String gender = user.optString(Constants.KEY_GENDER);
                String avatarUrl = user.optString(Constants.KEY_AVATER_URL);
                String userType = user.optString(Constants.KEY_USER_TYPE);
                User sender = new User(userId, firstName, lastName, gender, "",
                        avatarUrl, userType);
                String attachmentUrl = messageObj.opt(Constants.KEY_ATTACHMENT_URL).toString();
                String fileName = messageObj.opt(Constants.KEY_FILE_NAME).toString();
                String ext = messageObj.opt(Constants.KEY_EXT).toString();
                String body = messageObj.optString(Constants.KEY_BODY);
                String createdAt = messageObj.optString(Constants.KEY_CREATED_AT);
                String updatedAt = messageObj.optString(Constants.KEY_UPADTED_AT);
                int messageThreadId = messageObj.optInt(Constants.KEY_ID);
                Message message = new Message(attachmentUrl,body, createdAt, ext,fileName,userId,messageThreadId,updatedAt, sender);
                messageArrayList.add(message);
            }

            messageThreadArrayList.add(new MessageThread(courseId,courseName,id,isRead,lastAddedDate
                    ,messageArrayList,name,otherAvatar,otherNames,participantArrayList,tag));
        }

        return messageThreadArrayList;
    }
        private WeeklyPlannerResponse parseWeeklyPlannerResponse(JSONObject jsonObject){
            WeeklyPlannerResponse weeklyPlannerResponse = WeeklyPlannerResponse.create(jsonObject.toString());
            JSONArray weeklyPlans = jsonObject.optJSONArray(Constants.KEY_WEEKLY_PLAN);
            for (int i = 0; i < weeklyPlans.length(); i++) {
                JSONObject weeklyPlan = weeklyPlans.optJSONObject(i);
                JSONObject dailyNotes = weeklyPlan.optJSONObject(Constants.KEY_DAILY_NOTEs);
                ArrayList<Day> days = new ArrayList<>();
                // calculates number of days between start and end
                DateTime startDate = new DateTime(weeklyPlannerResponse.weeklyPlans.get(0).startDate);
                int numDays = Days.daysBetween(startDate, new DateTime(weeklyPlannerResponse.weeklyPlans.get(0).endDate)).getDays();
                for (DayWithDate dayWithDate: getCurrentWeekArray(startDate, numDays + 1)) {
                    JSONArray day = dailyNotes.optJSONArray(dayWithDate.date);
                    ArrayList<PlannerSubject> plannerSubjects = new ArrayList<>();
                    if (day != null) {
                        for (int k = 0; k < day.length(); k++) {
                            plannerSubjects.add(PlannerSubject.create(day.opt(k).toString()));
                        }
                    }
                    if (!plannerSubjects.isEmpty()) {
                        Day dayToAdd = new Day();
                        dayToAdd.day = dayWithDate.name;
                        dayToAdd.plannerSubjectArrayList = new ArrayList<>(plannerSubjects);
                        days.add(dayToAdd);
                    }
                }
                if (!days.isEmpty()) {
                    WeeklyPlan weeklyPlan1 = weeklyPlannerResponse.weeklyPlans.get(i);
                    weeklyPlan1.dailyNotes = new DailyNotes();
                    weeklyPlan1.dailyNotes.days = new ArrayList<>(days);
                    weeklyPlannerResponse.weeklyPlans.remove(i);
                    weeklyPlannerResponse.weeklyPlans.add(weeklyPlan1);
                }
            }
            return weeklyPlannerResponse;
        }

    private ArrayList<DayWithDate> getCurrentWeekArray(DateTime dateTime, int numberOfDays) {
        ArrayList<DayWithDate> dayWithDates = new ArrayList<>();
        for (int i = 0; i <= numberOfDays; i++) {
            DayWithDate dayWithDate = new DayWithDate();
            dayWithDate.date = (Util.getWeeklyPlannerDate(dateTime.toDate(), true));
            dayWithDate.name = Util.getDay(dateTime.toDate()) + " " + Util.getWeeklyPlannerDate(dateTime.toDate(), false);
            dayWithDates.add(dayWithDate);
            dateTime = dateTime.plusDays(1);
        }
        return dayWithDates;
    }

    private static class DayWithDate {
        public String name;
        public String date;
    }

    public void getAttendanceCount(int studentId) {
        UserManager.getAttendanceCount(studentId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                double total, presentCount, percentage;
                total = response.optDouble(Constants.KEY_TOTAL);
                presentCount = response.optDouble(Constants.PRESENT_COUNT);
                percentage = response.optDouble(Constants.PERCENTAGE);
                studentDetailPresenter.onGetAttendanceCountSuccess(total, presentCount, percentage);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetAttendanceCountFailure(message, errorCode);
            }
        });
    }
}
