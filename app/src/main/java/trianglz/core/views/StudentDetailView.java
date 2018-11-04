package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import Tools.CalendarUtils;
import timetable.TimetableSlot;
import trianglz.core.presenters.StudentDetailPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.CourseGroup;
import trianglz.utils.Constants;

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


    public void getStudentCourses(String url) {
        UserManager.getStudentCourse(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                studentDetailPresenter.onGetStudentCourseGroupSuccess(parseStudentCourseResponse(responseArray));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                studentDetailPresenter.onGetStudentCourseGroupFailure(message, errorCode);

            }
        });
    }


    public void getStudentGrades(String url, final ArrayList<CourseGroup> courseGroups) {
        UserManager.getStudentGrades(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                parseStudentGrades(response, courseGroups);
                String totalGrade = response.optJSONObject(Constants.KEY_GRADE).optString(Constants.KEY_LETTER);
                studentDetailPresenter.onGetStudentGradesSuccess(parseStudentGrades(response, courseGroups), totalGrade);
            }

            @Override
            public void onFailure(String message, int errorCode) {

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


    private ArrayList<trianglz.models.CourseGroup> parseStudentCourseResponse(JSONArray responseArray) {
        ArrayList<trianglz.models.CourseGroup> courseGroups = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject courseGroupData = responseArray.optJSONObject(i);
            JSONObject course = courseGroupData.optJSONObject(Constants.COURSE);
            int id = courseGroupData.optInt(Constants.KEY_ID);
            int courseId = course.optInt(Constants.KEY_ID);
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
                        if (courseData.optString(Constants.KEY_ICON).equals("null"))
                            // TODO: 11/4/2018 change dragon icon in grades
                            courseGroups.get(j).setIcon(Constants.KEY_DRAGON);
                        else
                            courseGroups.get(j).setIcon(courseData.optString(Constants.KEY_ICON));
                    } else {
                        courseGroups.get(j).setIcon("non");
                    }
                }

        }

        return courseGroups;

    }

    private ArrayList<Object> parseStudentTimeTable(JSONArray jsonArray) {
        ArrayList<Object> timeTableData = new ArrayList<>();
        String nextSlot = "";
        List<TimetableSlot> todaySlots = new ArrayList<TimetableSlot>();
        List<TimetableSlot> tomorrowSlots = new ArrayList<>();
        Calendar calendar = CalendarUtils.getCalendarWithoutDate();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        String tomorrow = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        today = today.toLowerCase();
        tomorrow = tomorrow.toLowerCase();
        if (today.equals(Constants.THURSDAY)) {
            tomorrow = "sunday";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("Egypt"));
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject slot = jsonArray.optJSONObject(i);
            String from = slot.optString(Constants.KEY_FROM);
            String to = slot.optString(Constants.KEY_TO);
            String day = slot.optString(Constants.KEY_DAY);
            String courseName = slot.optString(Constants.KEY_COURSE_NAME);
            String classRoom = slot.optString(Constants.KEY_SCHOOL_UNIT);

            Date fromDate = null;
            Date toDate = null;
            try {
                fromDate = formatter.parse(from);
                toDate = formatter.parse(to);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (day.equals(today)) {
                todaySlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
            } else if (day.equals(tomorrow)) {
                tomorrowSlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
            }

        }
        Date current = new Date();
        boolean nextSlotFound = false;
        Collections.sort(todaySlots);
        Collections.sort(tomorrowSlots);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        for (TimetableSlot timeSlotIterator : todaySlots) {
            if ((timeSlotIterator.getFrom().getHours() == current.getHours() && timeSlotIterator.getFrom().getMinutes() >= current.getMinutes()) ||
                    timeSlotIterator.getFrom().getHours() > current.getHours()) {
                nextSlotFound = true;
                nextSlot = ("Next: " + timeSlotIterator.getCourseName() + ", " + timeSlotIterator.getDay() + " " + dateFormat.format(timeSlotIterator.getFrom()));
                break;
            }
        }
        if (!nextSlotFound && tomorrowSlots.size() > 0) {
            TimetableSlot timeSlot = tomorrowSlots.get(0);
            nextSlot = ("Next: " + timeSlot.getCourseName() + ", " + timeSlot.getDay() + " " + dateFormat.format(timeSlot.getFrom()));
        }
        timeTableData.add(todaySlots);
        timeTableData.add(tomorrowSlots);
        timeTableData.add(nextSlot);
        return timeTableData;
    }

}
