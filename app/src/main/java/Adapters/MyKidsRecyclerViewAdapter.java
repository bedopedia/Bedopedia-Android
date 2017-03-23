package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.MyKidsActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.Student;
import Services.ApiClient;

/**
 * Created by mo2men on 13/03/17.
 */

public class MyKidsRecyclerViewAdapter extends RecyclerView.Adapter < MyKidsRecyclerViewAdapter.ViewHolderStudent> {

    private LayoutInflater layoutInflater;
    public static MyKidsActivity mContext;
    private ArrayList<Student> studentList = new ArrayList<>();
    ArrayList<JsonArray> kidsAttendances;


    public MyKidsRecyclerViewAdapter(Context context, ArrayList<Student> list , ArrayList<JsonArray> attendances) {
        mContext = (MyKidsActivity) context;
        layoutInflater = LayoutInflater.from(context);
        this.studentList = list;
        kidsAttendances = attendances;
        notifyItemChanged(0, studentList.size());
    }

    public void setStudentList(ArrayList<Student> list) {
        this.studentList = list;
        notifyItemChanged(0, studentList.size());
    }

    @Override
    public ViewHolderStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = layoutInflater.inflate(R.layout.single_student,parent,false);
        ViewHolderStudent viewHolder = new ViewHolderStudent(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderStudent holder, final int position) {
        final Student curStudent = studentList.get(position);

        holder.level.setText(curStudent.getLevel());
        holder.name.setText(curStudent.getFirstName() + " " + curStudent.getLastName());
        Picasso.with(mContext).load(ApiClient.BASE_URL+curStudent.getAvatar()).into(holder.avatar, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.avatar.setVisibility(View.GONE);
                holder.textName.setVisibility(View.VISIBLE);
                holder.textName.setText(curStudent.getFirstName().charAt(0) + "" + curStudent.getLastName().charAt(0));

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, StudentActivity.class);
//                intent.putExtra("student_id", String.valueOf(studentList.get(position).getId()));
//                intent.putExtra("student_name", studentList.get(position).getFirstName() + " " + studentList.get(position).getLastName());
//                intent.putExtra("student_avatar", studentList.get(position).getAvatar());
//                intent.putExtra("student_level", studentList.get(position).getLevel());
//                intent.putExtra("attendances",kidsAttendances.get(position).toString());
                mContext.itemClicked(position);
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

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class ViewHolderStudent extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView level;
        TextView todaySummary;
        TextView dueTasks;
        TextView textName;
        View cardView;

        public ViewHolderStudent(View itemView) {
            super(itemView);
            cardView = itemView;
            avatar = (ImageView) itemView.findViewById(R.id.student_avatar);
            name = (TextView) itemView.findViewById(R.id.student_name);
            level = (TextView) itemView.findViewById(R.id.student_level);
            todaySummary = (TextView) itemView.findViewById(R.id.st_today_summary);
            dueTasks = (TextView) itemView.findViewById(R.id.st_due_tasks);
            textName = (TextView) itemView.findViewById(R.id.st_text_name);
            setTextType();
        }
        private void setTextType() {
            Typeface robotoMedian = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Medium.ttf");
            Typeface robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Regular.ttf");
            Typeface robotoBold = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Bold.ttf");
            name.setTypeface(robotoMedian);
            todaySummary.setTypeface(robotoMedian);
            dueTasks.setTypeface(robotoMedian);
            level.setTypeface(robotoRegular);
            textName.setTypeface(robotoBold);
        }
    }
}
