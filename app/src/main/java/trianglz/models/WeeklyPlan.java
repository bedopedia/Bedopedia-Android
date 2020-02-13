package trianglz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class WeeklyPlan implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("general_note")
    private GeneralNote generalNote;
    private DailyNotes dailyNotes;



}