package trianglz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Day implements Serializable {
    @SerializedName("day")
    public String day;
    @SerializedName("plannerSubjectArray")
    public ArrayList<PlannerSubject> plannerSubjectArrayList;
}
