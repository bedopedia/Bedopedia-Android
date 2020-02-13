package trianglz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Day implements Serializable {
    public String day;
    public ArrayList<PlannerSubject> plannerSubjectArrayList;

    public Day(String day, ArrayList<PlannerSubject> plannerSubjectArrayList) {
        this.day = day;
        this.plannerSubjectArrayList = plannerSubjectArrayList;
    }
}
