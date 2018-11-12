package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.Subject;
import trianglz.models.Teacher;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class NewMessageAdapter extends RecyclerView.Adapter<NewMessageAdapter.Holder> {
    public Context context;
    public ArrayList<Object> mDataList;
    NewMessageAdapterInterface newMessageAdapterInterface;
    public Type type;
    public String subjectName = "";

    public NewMessageAdapter(Context context,
                             NewMessageAdapterInterface newMessageAdapterInterface
                              , Type type) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.newMessageAdapterInterface = newMessageAdapterInterface;
        this.type = type;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if(type == Type.SUBJECT){
            Subject subject = (Subject) mDataList.get(position);
            String teacherCount = "";
            if (subject.teacherArrayList.size() > 1) {
                teacherCount = (subject.teacherArrayList.size() + " " +
                        context.getResources().getString(R.string.teacher));
            } else {
                teacherCount = (subject.teacherArrayList.size() + " " +
                        context.getResources().getString(R.string.teacher));
            }
            holder.teacherCountTextView.setText(teacherCount);
            holder.subjectNameTextView.setText(subject.courseName);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type.equals(Type.TEACHER)){
                        newMessageAdapterInterface.onTeacherSelected(holder.getAdapterPosition());
                    }else {
                        newMessageAdapterInterface.onSubjectSelected(holder.getAdapterPosition());
                    }

                }
            });
        }else {
            Teacher teacher =(Teacher) mDataList.get(position);
            holder.subjectNameTextView.setText(teacher.name);
            holder.teacherCountTextView.setText(subjectName);
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<Object> courseGroupList,Type type) {
        this.type = type;
        this.mDataList.clear();
        this.mDataList.addAll(courseGroupList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, teacherCountTextView;
        public LinearLayout itemLayout;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            teacherCountTextView = itemView.findViewById(R.id.tv_teacher_count);
            itemLayout = itemView.findViewById(R.id.item_layout);

        }
    }

    public interface NewMessageAdapterInterface {
        void onSubjectSelected(int position);
        void onTeacherSelected(int position);
    }

    public enum Type {
        TEACHER, SUBJECT
    }

}