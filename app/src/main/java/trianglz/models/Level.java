package trianglz.models;

import org.json.*;

import java.io.Serializable;
import java.util.*;


public class Level implements Serializable {

    private int id;
    private String name;
    private String sectionName;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSectionName(String sectionName){
        this.sectionName = sectionName;
    }
    public String getSectionName(){
        return this.sectionName;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Level(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        id = jsonObject.optInt("id");
        name = jsonObject.optString("name");
        sectionName = jsonObject.optString("section_name");
    }

//    /**
//     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
//     */
//    public JSONObject toJsonObject()
//    {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("id", id);
//            jsonObject.put("name", name);
//            jsonObject.put("section_name", sectionName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }

}
