package attendance;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    final private String lateKey = "late";
    final private String statusKey = "status";
    final private String nullKey = "null";
    final private String commentKey = "comment";
    final private String noCommentKey = "No Comment";
    final private String absentKey = "absent";
    final private String excusedKey = "excused";
    final private String dateKey = "date";



    // TODO: Rename and change types of parameters


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
            date.setTime(day.get(dateKey).getAsLong());

            if(day.get(statusKey).getAsString().equals(lateKey)){
                if(!day.get(commentKey).toString().equals(nullKey))
                    lateDates.add(new Attendance(date, day.get(commentKey).getAsString()));
                else
                    lateDates.add(new Attendance(date, noCommentKey));
            } else if(day.get(statusKey).getAsString().equals(absentKey)){
                if(!day.get(commentKey).toString().equals(nullKey))
                    absentDates.add(new Attendance(date, day.get(commentKey).getAsString()));
                else
                    absentDates.add(new Attendance(date, noCommentKey));
            } else if (day.get(statusKey).getAsString().equals(excusedKey)){
                if ( !day.get(commentKey).toString().equals(nullKey))
                    excusedDates.add(new Attendance(date, day.get(commentKey).getAsString()));
                else
                    excusedDates.add(new Attendance(date, noCommentKey));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lateCounter = (TextView) view.findViewById(R.id.late_count);
        lateLabel = (TextView) view.findViewById(R.id.late_label);
        excusedCounter = (TextView) view.findViewById(R.id.excused_count);
        excusedLabel = (TextView) view.findViewById(R.id.excused_label);
        absentCounter = (TextView) view.findViewById(R.id.absent_count);
        absentLabel = (TextView) view.findViewById(R.id.absent_label);

        excusedListCounter = (TextView) view.findViewById(R.id.excused_list_counter);
        excusedListLabel = (TextView) view.findViewById(R.id.excused_list_label);

        selectedMonth = (TextView) view.findViewById(R.id.selected_month);

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        final String[] months = dateFormatSymbols.getMonths();

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
        RecyclerView absentAttendaceList = (RecyclerView) view.findViewById(R.id.excused_dates);
        ExcusedAdapter adapter = new ExcusedAdapter(getActivity(), R.layout.single_excused_attendance, dates);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        absentAttendaceList.setLayoutManager(manager);
        absentAttendaceList.setAdapter(adapter);
        excusedListCounter.setText(dates.size()+"");

        CompactCalendarView compactCalendar = (CompactCalendarView) view.findViewById(R.id.compact_calendar_view);
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
                RecyclerView absentAttendaceList = (RecyclerView) view.findViewById(R.id.excused_dates);
                ExcusedAdapter adapter = new ExcusedAdapter(getActivity(), R.layout.single_excused_attendance, dates);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                absentAttendaceList.setLayoutManager(manager);
                absentAttendaceList.setAdapter(adapter);
                excusedListCounter.setText(dates.size()+"");
            }
        });



        for(int i = 0 ; i < lateDates.size() ; i++){
            Event event = new Event(getResources().getColor(R.color.electric_violet), lateDates.get(i).getDate().getTime(),getString(R.string.AttendanceLateLable));
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < excusedDates.size() ; i++){
            Event event = new Event(getResources().getColor(R.color.dark_tangerine), excusedDates.get(i).getDate().getTime(),getString(R.string.AttendanceExcusedLable));
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < absentDates.size() ; i++){
            Event event = new Event(getResources().getColor(R.color.coral_red), absentDates.get(i).getDate().getTime(),getString(R.string.AttendanceAbsentLable));
            compactCalendar.addEvent(event);
        }

        lateCounter.setText(lateDates.size()+"");
        excusedCounter.setText(excusedDates.size()+"");
        absentCounter.setText(absentDates.size()+"");
    }
}
