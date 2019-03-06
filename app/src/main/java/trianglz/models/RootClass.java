package trianglz.models;

import org.json.*;

import java.io.Serializable;
import java.util.*;


public class RootClass implements Serializable {

    private Meta meta;
    private ArrayList<WeeklyPlan> weeklyPlans;

    public void setMeta(Meta meta){
        this.meta = meta;
    }
    public Meta getMeta(){
        return this.meta;
    }
    public void setWeeklyPlans( ArrayList<WeeklyPlan> weeklyPlans){
        this.weeklyPlans = weeklyPlans;
    }
    public  ArrayList<WeeklyPlan> getWeeklyPlans(){
        return this.weeklyPlans;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public RootClass(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        meta = new Meta(jsonObject.optJSONObject("meta"));
        JSONArray weeklyPlansJsonArray = jsonObject.optJSONArray("weekly_plans");
        if(weeklyPlansJsonArray != null){
            ArrayList<WeeklyPlan> weeklyPlansArrayList = new ArrayList<>();
            for (int i = 0; i < weeklyPlansJsonArray.length(); i++) {
                JSONObject weeklyPlansObject = weeklyPlansJsonArray.optJSONObject(i);
                weeklyPlansArrayList.add(new WeeklyPlan(weeklyPlansObject));
            }
            weeklyPlans =  weeklyPlansArrayList;
        }	}

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
//    public JSONObject toJsonObject()
//    {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("meta", meta.toJsonObject());
//            if(weeklyPlans != null && weeklyPlans.length > 0){
//                JSONArray weeklyPlansJsonArray = new JSONArray();
//                for(WeeklyPlan weeklyPlansElement : weeklyPlans){
//                    weeklyPlansJsonArray.put(weeklyPlansElement.toJsonObject());
//                }
//                jsonObject.put("weekly_plans", weeklyPlansJsonArray);
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }

}