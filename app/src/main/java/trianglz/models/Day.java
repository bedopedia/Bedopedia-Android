package trianglz.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Day implements Serializable {
    public String day;
    public ArrayList<DailyNote> dailyNoteArrayList;

    public Day(String day, ArrayList<DailyNote> dailyNoteArrayList) {
        this.day = day;
        this.dailyNoteArrayList = dailyNoteArrayList;
    }
}
