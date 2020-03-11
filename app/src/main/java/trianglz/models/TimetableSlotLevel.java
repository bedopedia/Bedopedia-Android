package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
public class TimetableSlotLevel implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("stage_id")
    private int stageId;
    @SerializedName("level_type")
    private Object levelType;
    @SerializedName("slots_count")
    private int slotsCount;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getStageId() {
        return this.stageId;
    }

    public void setLevelType(Object levelType) {
        this.levelType = levelType;
    }

    public Object getLevelType() {
        return this.levelType;
    }

    public void setSlotsCount(int slotsCount) {
        this.slotsCount = slotsCount;
    }

    public int getSlotsCount() {
        return this.slotsCount;
    }


    public static TimetableSlotLevel create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, TimetableSlotLevel.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}