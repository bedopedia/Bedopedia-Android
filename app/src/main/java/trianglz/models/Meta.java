package trianglz.models;

import org.json.*;

import java.io.Serializable;
import java.util.*;


public class Meta implements Serializable {

    private int currentPage;
    private int nextPage;
    private int prevPage;
    private int totalCount;
    private int totalPages;

    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }
    public int getCurrentPage(){
        return this.currentPage;
    }
    public void setNextPage(int nextPage){
        this.nextPage = nextPage;
    }
    public int getNextPage(){
        return this.nextPage;
    }
    public void setPrevPage(int prevPage){
        this.prevPage = prevPage;
    }
    public int getPrevPage(){
        return this.prevPage;
    }
    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }
    public int getTotalCount(){
        return this.totalCount;
    }
    public void setTotalPages(int totalPages){
        this.totalPages = totalPages;
    }
    public int getTotalPages(){
        return this.totalPages;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Meta(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        currentPage = jsonObject.optInt("current_page");
        nextPage = jsonObject.optInt("next_page");
        prevPage = jsonObject.optInt("prev_page");
        totalCount = jsonObject.optInt("total_count");
        totalPages = jsonObject.optInt("total_pages");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("current_page", currentPage);
            jsonObject.put("next_page", nextPage);
            jsonObject.put("prev_page", prevPage);
            jsonObject.put("total_count", totalCount);
            jsonObject.put("total_pages", totalPages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}