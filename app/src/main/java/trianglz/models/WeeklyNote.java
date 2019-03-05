package trianglz.models;

import org.json.*;
import java.util.*;


public class WeeklyNote{

    private String description;
    private int id;
    private Object imageUrl;
    private String title;
    private int weeklyPlanId;

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setImageUrl(Object imageUrl){
        this.imageUrl = imageUrl;
    }
    public Object getImageUrl(){
        return this.imageUrl;
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
    public WeeklyNote(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        description = jsonObject.optString("description");
        id = jsonObject.optInt("id");
        imageUrl = jsonObject.opt("image_url");
        title = jsonObject.optString("title");
        weeklyPlanId = jsonObject.optInt("weekly_plan_id");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("description", description);
            jsonObject.put("id", id);
            jsonObject.put("image_url", imageUrl);
            jsonObject.put("title", title);
            jsonObject.put("weekly_plan_id", weeklyPlanId);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

}
