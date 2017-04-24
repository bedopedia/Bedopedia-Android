package attendance;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class AttendanceFragment extends Fragment {
    private static List<Attendance> absentDates;
    private static List<Attendance> lateDates;
    private static List<Attendance> excusedDates;
    private String attendance;

    Typeface robotoMedium;
    Typeface robotoRegular;
    Typeface robotoBold;

    TextView selectedMonth;
    TextView lateCounter;
    TextView lateLabel;
    TextView excusedCounter;
    TextView excusedLabel;
    TextView absentCounter;
    TextView absentLabel;
    TextView excusedListCounter;
    TextView excusedListLabel;


    // TODO: Rename and change types of parameters
    private List<Attendance> absentDatesParam;
    private List<Attendance> lateDatesParam;
    private List<Attendance> excusedDatesParam;


    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String attendance) {
        Fragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString("attendances", attendance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attendance = getArguments().getString("attendances");
        }

        absentDates = new ArrayList<Attendance>();
        lateDates = new ArrayList<Attendance>();
        excusedDates = new ArrayList<Attendance>();

        robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Bold.ttf");

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(attendance);
        final JsonArray attenobdances = tradeElement.getAsJsonArray();

        for(JsonElement element: attenobdances){
            JsonObject day = element.getAsJsonObject();
            Date date = new Date();
            date.setTime(day.get("date").getAsLong());

            if(day.get("status").getAsString().equals("late")){
                if(!day.get("comment").toString().equals("null"))
                    lateDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    lateDates.add(new Attendance(date, "No Comment"));
            } else if(day.get("status").getAsString().equals("absent")){
                if(!day.get("comment").toString().equals("null"))
                    absentDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    absentDates.add(new Attendance(date, "No Comment"));
            } else if (day.get("status").getAsString().equals("excused")){
                if ( !day.get("comment").toString().equals("null"))
                    excusedDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    excusedDates.add(new Attendance(date, "No Comment"));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        lateCounter = (TextView) rootView.findViewById(R.id.late_count);
        lateLabel = (TextView) rootView.findViewById(R.id.late_label);
        excusedCounter = (TextView) rootView.findViewById(R.id.excused_count);
        excusedLabel = (TextView) rootView.findViewById(R.id.excused_label);
        absentCounter = (TextView) rootView.findViewById(R.id.absent_count);
        absentLabel = (TextView) rootView.findViewById(R.id.absent_label);

        excusedListCounter = (TextView) rootView.findViewById(R.id.excused_list_counter);
        excusedListLabel = (TextView) rootView.findViewById(R.id.excused_list_label);

        selectedMonth = (TextView) rootView.findViewById(R.id.selected_month);

        DateFormatSymbols dfs = new DateFormatSymbols();
        final String[] months = dfs.getMonths();

        selectedMonth.setText(months[new Date().getMonth()]);

        selectedMonth.setTypeface(robotoMedium);
        lateCounter.setTypeface(robotoMedium);
        lateLabel.setTypeface(robotoRegular);
        excusedCounter.setTypeface(robotoMedium);
        excusedLabel.setTypeface(robotoRegular);
        absentCounter.setTypeface(robotoMedium);
        absentLabel.setTypeface(robotoRegular);

        excusedListCounter.setTypeface(robotoBold);
        excusedListLabel.setTypeface(robotoMedium);

        List<Attendance> dates= new ArrayList<Attendance>();
        for (int i = 0 ; i < excusedDates.size() ; i++){
            if(new Date().getMonth() == excusedDates.get(i).getDate().getMonth() &&
                    new Date().getYear() == excusedDates.get(i).getDate().getYear()){
                dates.add(excusedDates.get(i));
            }
        }
        ListView absentAttendaceList = (ListView) rootView.findViewById(R.id.excused_dates);
        ExcusedAdapter adapter = new ExcusedAdapter(getActivity(), R.layout.single_excused_attendance, dates);
        absentAttendaceList.setAdapter(adapter);
        excusedListCounter.setText(dates.size()+"");

        CompactCalendarView compactCalendar = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setSelected(false);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                selectedMonth.setText(months[firstDayOfNewMonth.getMonth()]);
                List<Attendance> dates= new ArrayList<Attendance>();
                for (int i = 0 ; i < excusedDates.size() ; i++){
                    if(firstDayOfNewMonth.getMonth() == excusedDates.get(i).getDate().getMonth() &&
                            firstDayOfNewMonth.getYear() == excusedDates.get(i).getDate().getYear()){
                        dates.add(excusedDates.get(i));
                    }
                }
                ListView absentAttendaceList = (ListView) rootView.findViewById(R.id.excused_dates);
                ExcusedAdapter adapter = new ExcusedAdapter(getActivity(), R.layout.single_excused_attendance, dates);
                absentAttendaceList.setAdapter(adapter);
                excusedListCounter.setText(dates.size()+"");
            }
        });



        for(int i = 0 ; i < lateDates.size() ; i++){
            Event event = new Event(Color.parseColor("#ff9013fe"), lateDates.get(i).getDate().getTime(), "Late");
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < excusedDates.size() ; i++){
            Event event = new Event(Color.parseColor("#fff5a623"), excusedDates.get(i).getDate().getTime(), "Excused");
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < absentDates.size() ; i++){
            Event event = new Event(Color.parseColor("#ffff3b30"), absentDates.get(i).getDate().getTime(), "Absent");
            compactCalendar.addEvent(event);
        }

        lateCounter.setText(lateDates.size()+"");
        excusedCounter.setText(excusedDates.size()+"");
        absentCounter.setText(absentDates.size()+"");

        return rootView;
    }


}