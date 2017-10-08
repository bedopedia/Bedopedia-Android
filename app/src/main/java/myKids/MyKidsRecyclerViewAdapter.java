package myKids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import login.Services.ApiClient;
import student.StudentActivity;

/**
 * Created by mo2men on 13/03/17.
 */

public class MyKidsRecyclerViewAdapter extends RecyclerView.Adapter < MyKidsRecyclerViewAdapter.StudentViewHolder> {

    private LayoutInflater layoutInflater;
    public static MyKidsActivity mContext;
    StudentViewHolder viewHolder;
    private ArrayList<JsonArray> attendanceList ;
    private ArrayList<Student> studentList = new ArrayList<>();
    final String urlUploadsKey = "/uploads";
    final String studentIdKey = "student_id";
    final String studentNameKey = "student_name";
    final String studentAvatarKey = "student_avatar";
    final String studentLevelKey = "student_level";
    final String attendancesKey = "attendances";


    public MyKidsRecyclerViewAdapter(Context context, ArrayList<Student> list , ArrayList<JsonArray> attendanceList) {
        mContext = (MyKidsActivity) context;
        layoutInflater = LayoutInflater.from(context);
        this.studentList = list;
        notifyItemChanged(0, studentList.size());
        this.studentList = list ;
        this.attendanceList = attendanceList ;
    }


    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = layoutInflater.inflate(R.layout.single_student,parent,false);
        viewHolder = new StudentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudentViewHolder holder, final int position) {
        final Student curStudent = studentList.get(position);

        holder.studentLevel.setText(curStudent.getLevel());
        holder.studentName.setText(curStudent.getFirstName() + " " + curStudent.getLastName());
        String imageUrl = curStudent.getAvatar();

        SetAttendanceCircle(curStudent.getTodayAttendance(), holder);


        holder.studentTodayQuizzes.setText(R.string.quizzes);
        holder.studentTodayAssignments.setText(R.string.assignments);
        holder.studentTodayEvents.setText(R.string.events);

        holder.studentTodayQuizzesCnt.setText(String.valueOf(curStudent.getTodayQuizzesCount()));
        holder.studentTodayAssignmentsCnt.setText(String.valueOf(curStudent.getTodayAssignmentsCount()));
        holder.studentTodayEventsCnt.setText(String.valueOf(curStudent.getTodayEventsCount()));



        if(imageUrl.substring(0,8).equals(urlUploadsKey)) {
            imageUrl = ApiClient.BASE_URL + imageUrl;
        }
        Picasso.with(mContext).load(imageUrl).into(holder.studentAvatar, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.studentAvatar.setVisibility(View.GONE);
                holder.studentTextName.setVisibility(View.VISIBLE);
                holder.studentTextName.setText(curStudent.getFirstName().charAt(0) + "" + curStudent.getLastName().charAt(0));

            }
        });

        holder.studentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent studentActivityIntent = new Intent(mContext, StudentActivity.class);
                studentActivityIntent.putExtra(studentIdKey, String.valueOf(studentList.get(position).getId()));
                studentActivityIntent.putExtra(studentNameKey, studentList.get(position).getFirstName() + " " + studentList.get(position).getLastName());
                studentActivityIntent.putExtra(studentAvatarKey, studentList.get(position).getAvatar());
                studentActivityIntent.putExtra(studentLevelKey, studentList.get(position).getLevel());
                studentActivityIntent.putExtra(attendancesKey,attendanceList.get(position).toString());
                mContext.startActivity(studentActivityIntent);
            }
        });


//        holder.todaySummary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MyKidsActivity.class);
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.dueTasks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MyKidsActivity.class);
//                mContext.startActivity(intent);
//            }
//        });
    }

    private void SetAttendanceCircle(String attendanceStatus, final StudentViewHolder holder) {
        attendanceStatus = attendanceStatus.substring(0,1).toUpperCase() + attendanceStatus.substring(1).toLowerCase();

        holder.studentTodayAttendance.setText(attendanceStatus);
        if(attendanceStatus.equals("Present")) {
            holder.studentTodayAttendanceCircle.setBackgroundResource(R.drawable.attendance_circle_green);
        } else if (attendanceStatus.equals("Not taken")) {
            holder.studentTodayAttendanceCircle.setBackgroundResource(R.drawable.attendance_circle_grey);
        } else if (attendanceStatus.equals("Late")) {
            holder.studentTodayAttendanceCircle.setBackgroundResource(R.drawable.attendance_circle_yellow);
        }else if (attendanceStatus.equals("Excused")) {
            holder.studentTodayAttendanceCircle.setBackgroundResource(R.drawable.attendance_circle_blue);
        } else if (attendanceStatus.equals("Absent")) {
            holder.studentTodayAttendanceCircle.setBackgroundResource(R.drawable.attendance_circle_red);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView studentAvatar;
        TextView studentName;
        TextView studentLevel;
        TextView studentTodayQuizzes;
        TextView studentTodayAssignments;
        TextView studentTodayEvents;
        TextView studentTodayQuizzesCnt;
        TextView studentTodayAssignmentsCnt;
        TextView studentTodayEventsCnt;
        TextView studentTextName;
        TextView studentTodayAttendance;
        TextView studentTodayAttendanceCircle;
        View studentCardView;

        public StudentViewHolder(View itemView) {
            super(itemView);
            studentCardView = itemView;
            studentAvatar = (ImageView) itemView.findViewById(R.id.student_avatar);
            studentName = (TextView) itemView.findViewById(R.id.student_name);
            studentLevel = (TextView) itemView.findViewById(R.id.student_level);
            studentTodayQuizzes = (TextView) itemView.findViewById(R.id.st_due_quizzes);
            studentTodayAssignments = (TextView) itemView.findViewById(R.id.st_due_assignments);
            studentTodayEvents = (TextView) itemView.findViewById(R.id.st_due_events);
            studentTodayQuizzesCnt = (TextView) itemView.findViewById(R.id.st_due_quizzes_cnt);
            studentTodayAssignmentsCnt = (TextView) itemView.findViewById(R.id.st_due_assignments_cnt);
            studentTodayEventsCnt = (TextView) itemView.findViewById(R.id.st_due_events_cnt);
            studentTodayAttendance = (TextView) itemView.findViewById(R.id.st_today_attendance);
            studentTodayAttendanceCircle = (TextView) itemView.findViewById(R.id.st_today_attendance_circle);
            studentTextName = (TextView) itemView.findViewById(R.id.st_text_name);
            setTextType();
        }
        private void setTextType() {
            Typeface robotoMedian = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Medium.ttf");
            Typeface robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Regular.ttf");
            Typeface robotoBold = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Bold.ttf");
            studentName.setTypeface(robotoMedian);
            studentTodayQuizzes.setTypeface(robotoMedian);
            studentTodayAssignments.setTypeface(robotoMedian);
            studentTodayEvents.setTypeface(robotoMedian);
            studentTodayQuizzesCnt.setTypeface(robotoMedian);
            studentTodayAssignmentsCnt.setTypeface(robotoMedian);
            studentTodayEventsCnt.setTypeface(robotoMedian);
            studentLevel.setTypeface(robotoRegular);
            studentTodayAttendance.setTypeface(robotoRegular);
            studentTextName.setTypeface(robotoBold);
        }
    }
}
