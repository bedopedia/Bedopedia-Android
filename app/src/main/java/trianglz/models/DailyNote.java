package trianglz.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * This file is spawned by Gemy on 1/20/2019.
 */

public class DailyNote implements Serializable {

    private String activities;
    private String classWork;
    private int courseId;
    private String date;
    private String homework;
    private int id;
    private String title;
    private int weeklyPlanId;

    public void setActivities(String activities){
        this.activities = activities;
    }
    public String getActivities(){
        return this.activities;
    }
    public void setClassWork(String classWork){
        this.classWork = classWork;
    }
    public String getClassWork(){
        return this.classWork;
    }
    public void setCourseId(int courseId){
        this.courseId = courseId;
    }
    public int getCourseId(){
        return this.courseId;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setHomework(String homework){
        this.homework = homework;
    }
    public String getHomework(){
        return this.homework;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setWeeklyPlanId(int weeklyPlanId){
        this.weeklyPlanId = weeklyPlanId;
    }
    public int getWeeklyPlanId(){
        return this.weeklyPlanId;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public DailyNote(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        activities = jsonObject.optString("activities");
        classWork = jsonObject.optString("class_work");
        courseId = jsonObject.optInt("course_id");
        date = jsonObject.optString("date");
        homework = jsonObject.optString("homework");
        id = jsonObject.optInt("id");
        title = jsonObject.optString("title");
        weeklyPlanId = jsonObject.optInt("weekly_plan_id");
    }
//
//    /**
//     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
//     */
//    public JSONObject toJsonObject()
//    {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("activities", activities);
//            jsonObject.put("class_work", classWork);
//            jsonObject.put("course_id", courseId);
//            jsonObject.put("date", date);
//            jsonObject.put("homework", homework);
//            jsonObject.put("id", id);
//            jsonObject.put("title", title);
//            jsonObject.put("weekly_plan_id", weeklyPlanId);
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }

}


