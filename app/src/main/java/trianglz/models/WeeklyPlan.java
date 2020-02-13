package trianglz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class WeeklyPlan implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("start_date")
    public String startDate;
    @SerializedName("end_date")
    public String endDate;
    @SerializedName("general_note")
    public GeneralNote generalNote;
    public DailyNotes dailyNotes;



}