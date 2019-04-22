package trianglz.models;
/**
 * Created by ${Aly} on 4/22/2019.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class AssignmentsDetail {

    @SerializedName("assignments")
    private ArrayList<PendingAssignment> assignments;
    @SerializedName("meta")
    private Meta meta;

    public void setAssignments(PendingAssignment[] assignments) {

        this.assignments = new ArrayList<>();
        for (int i = 0; i < assignments.length; i++) {
            this.assignments.add(assignments[i]);
        }
    }

    public ArrayList<PendingAssignment> getAssignments() {
        return this.assignments;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }


    public static AssignmentsDetail create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, AssignmentsDetail.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}