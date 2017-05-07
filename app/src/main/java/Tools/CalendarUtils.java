package Tools;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ali on 07/05/17.
 */

public class CalendarUtils {

    public static Calendar getCalendar( Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar getCalendarWithoutDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static Calendar getGregorianCalendar(Date date){
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        return calendar;
    }



}
