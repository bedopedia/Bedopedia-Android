package trianglz.models;

import java.util.Date;

/**
 * This file is spawned by Gemy on 1/20/2019.
 */
public class DailyNote {
    private String subjectName;
    private String classWork;

    public DailyNote(String subjectName,
                     String classWork,
                     String homeWork,
                     String activities,
                     Date date,
                     String weeklyPlanId) {
        this.subjectName = subjectName;
        this.classWork = classWork;
        this.homeWork = homeWork;
        this.activities = activities;
        this.date = date;
        this.weeklyPlanId = weeklyPlanId;
    }

    private String homeWork;
    private String activities;
    private Date date;
    private String weeklyPlanId;


    DailyNote() {
        subjectName = "";
        classWork = "";
        homeWork = "";
        activities = "";
        weeklyPlanId = "";
        date = new Date(date.getTime());
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassWork() {
        return classWork;
    }

    public void setClassWork(String classWork) {
        this.classWork = classWork;
    }

    public String getHomeWork() {
        return homeWork;
    }

    public void setHomeWork(String homeWork) {
        this.homeWork = homeWork;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeeklyPlanId() {
        return weeklyPlanId;
    }

    public void setWeeklyPlanId(String weeklyPlanId) {
        this.weeklyPlanId = weeklyPlanId;
    }
}
