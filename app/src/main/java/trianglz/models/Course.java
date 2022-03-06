package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class Course implements Serializable {
    public String checkListString;
    public String code;
    public String createdAt;
    public String deletedAt;
    public String descriptionField;
    public int hodId;
    public String iconName;
    public int id;
    public int levelId;
    public String name;
    public String ownerId;
    public int passLimit;
    public String questionPoolId;
    public String semesterId;
    public boolean showFinalGrade;
    public int totalGrade;
    public String updatedAt;

    public Course(String checkListString, String code, String createdAt, String deletedAt,
                  String descriptionField, int hodId, String iconName, int id, int levelId,
                  String name, String ownerId, int passLimit, String questionPoolId,
                  String semesterId, boolean showFinalGrade, int totalGrade, String updatedAt) {
        this.checkListString = checkListString;
        this.code = code;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.descriptionField = descriptionField;
        this.hodId = hodId;
        this.iconName = iconName;
        this.id = id;
        this.levelId = levelId;
        this.name = name;
        this.ownerId = ownerId;
        this.passLimit = passLimit;
        this.questionPoolId = questionPoolId;
        this.semesterId = semesterId;
        this.showFinalGrade = showFinalGrade;
        this.totalGrade = totalGrade;
        this.updatedAt = updatedAt;
    }
}
