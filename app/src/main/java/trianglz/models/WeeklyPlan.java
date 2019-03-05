package trianglz.models;

import org.json.*;
import java.util.*;


public class WeeklyPlan{

    private int coursesCount;
    private ArrayList <DailyNote> dailyNotes;
    private String endDate;
    private int id;
    private Level level;
    private int levelId;
    private Object[] pendingCourses;
    private String startDate;
    private String state;
    private ArrayList<WeeklyNote> weeklyNotes;

    public void setCoursesCount(int coursesCount){
        this.coursesCount = coursesCount;
    }
    public int getCoursesCount(){
        return this.coursesCount;
    }
    public void setDailyNotes(ArrayList<DailyNote> dailyNotes){
        this.dailyNotes = dailyNotes;
    }
    public ArrayList<DailyNote> getDailyNotes(){
        return this.dailyNotes;
    }
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    public String getEndDate(){
        return this.endDate;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setLevel(Level level){
        this.level = level;
    }
    public Level getLevel(){
        return this.level;
    }
    public void setLevelId(int levelId){
        this.levelId = levelId;
    }
    public int getLevelId(){
        return this.levelId;
    }
    public void setPendingCourses(Object[] pendingCourses){
        this.pendingCourses = pendingCourses;
    }
    public Object[] getPendingCourses(){
        return this.pendingCourses;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    public String getStartDate(){
        return this.startDate;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setWeeklyNotes(ArrayList <WeeklyNote> weeklyNotes){
        this.weeklyNotes = weeklyNotes;
    }
    public ArrayList<WeeklyNote> getWeeklyNotes(){
        return this.weeklyNotes;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public WeeklyPlan(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        coursesCount = jsonObject.optInt("courses_count");
        JSONArray dailyNotesJsonArray = jsonObject.optJSONArray("daily_notes");
        if(dailyNotesJsonArray != null){
            ArrayList<DailyNote> dailyNotesArrayList = new ArrayList<>();
            for (int i = 0; i < dailyNotesJsonArray.length(); i++) {
                JSONObject dailyNotesObject = dailyNotesJsonArray.optJSONObject(i);
                dailyNotesArrayList.add(new DailyNote(dailyNotesObject));
            }
            dailyNotes =  dailyNotesArrayList;
        }		endDate = jsonObject.optString("end_date");
        id = jsonObject.optInt("id");
        level = new Level(jsonObject.optJSONObject("level"));
        levelId = jsonObject.optInt("level_id");
        JSONArray pendingCoursesTmp = jsonObject.optJSONArray("pending_courses");
        if(pendingCoursesTmp != null){
            pendingCourses = new Object[pendingCoursesTmp.length()];
            for(int i = 0; i < pendingCoursesTmp.length(); i++){
                try {
                    pendingCourses[i] = pendingCoursesTmp.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        startDate = jsonObject.optString("start_date");
        state = jsonObject.optString("state");
        JSONArray weeklyNotesJsonArray = jsonObject.optJSONArray("weekly_notes");
        if(weeklyNotesJsonArray != null){
            ArrayList<WeeklyNote> weeklyNotesArrayList = new ArrayList<>();
            for (int i = 0; i < weeklyNotesJsonArray.length(); i++) {
                JSONObject weeklyNotesObject = weeklyNotesJsonArray.optJSONObject(i);
                weeklyNotesArrayList.add(new WeeklyNote(weeklyNotesObject));
            }
            weeklyNotes = weeklyNotesArrayList;
        }	}

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
//    public JSONObject toJsonObject()
//    {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("courses_count", coursesCount);
//            if(dailyNotes != null && dailyNotes.length > 0){
//                JSONArray dailyNotesJsonArray = new JSONArray();
//                for(Object dailyNotesElement : dailyNotes){
//                    dailyNotesJsonArray.put(dailyNotesElement.toJsonObject());
//                }
//                jsonObject.put("daily_notes", dailyNotesJsonArray);
//            }
//            jsonObject.put("end_date", endDate);
//            jsonObject.put("id", id);
//            jsonObject.put("level", level.toJsonObject());
//            jsonObject.put("level_id", levelId);
//            jsonObject.put("pending_courses", pendingCourses);
//            jsonObject.put("start_date", startDate);
//            jsonObject.put("state", state);
//            if(weeklyNotes != null && weeklyNotes.length > 0){
//                JSONArray weeklyNotesJsonArray = new JSONArray();
//                for(WeeklyNote weeklyNotesElement : weeklyNotes){
//                    weeklyNotesJsonArray.put(weeklyNotesElement.toJsonObject());
//                }
//                jsonObject.put("weekly_notes", weeklyNotesJsonArray);
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }

}