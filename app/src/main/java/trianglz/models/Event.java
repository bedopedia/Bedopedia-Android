package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by Farah A. Moniem on 28/07/2019.
 */

public class Event {

    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private String type;
    @SerializedName("end_date")
    public String endDate;
    @SerializedName("start_date")
    public String startDate;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;
    @SerializedName("all_day")
    private boolean allDay;
    @SerializedName("owner_id")
    private int ownerId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //    public Date getEndDate() {
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(endDate * 1000L);
//        String dateString = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
//        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        Date date=new Date();
//        try {
//            date=formatter.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;    }
    public String getEndDate() {
        return this.endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    //    public Date getStartDate() {
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(startDate * 1000L);
//        String dateString = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
//        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        Date date=new Date();
//        try {
//            date=formatter.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
    public String getStartDate() {
        return this.startDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public boolean getAllDay() {
        return this.allDay;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }


    public static RootClass create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, RootClass.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }


    public static class SortByDate implements Comparator<Event> {
        @Override
        public int compare(Event a, Event b) {
            return 1;
            //     return a.getStartDate().compareTo(b.getStartDate());
        }
    }

}