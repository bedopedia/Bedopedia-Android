package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.models.CourseAssignment;

/**
 * Created by ${Aly} on 4/22/2019.
 */
public class CourseAssignmentAdapter extends RecyclerView.Adapter<CourseAssignmentAdapter.Holder> {
    public Context context;
    public List<CourseAssignment> mDataList;
    private AdapterPaginationInterface paginationInterface;




    public CourseAssignmentAdapter(Context context, AdapterPaginationInterface adapterPaginationInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.paginationInterface = adapterPaginationInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_assigment_detail, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CourseAssignment courseAssignment = mDataList.get(position);
        holder.subjectNameTextView.setText(courseAssignment.getCourseName());
        holder.assignmentNameTextView.setText(courseAssignment.getAssignmentName());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<CourseAssignment> courseAssignments) {
        this.mDataList.clear();
        this.mDataList.addAll(courseAssignments);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView,dayTextView,monthTextView;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            assignmentNameTextView = itemView.findViewById(R.id.img_subject);
            assignmentNameTextView = itemView.findViewById(R.id.tv_assignment_name);
            dayTextView = itemView.findViewById(R.id.tv_day_number);
            monthTextView = itemView.findViewById(R.id.tv_month);
        }
    }

}