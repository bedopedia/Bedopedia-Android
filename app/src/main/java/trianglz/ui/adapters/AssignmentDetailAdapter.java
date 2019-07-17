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

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.models.AssignmentsDetail;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 6/23/2019.
 */
public class AssignmentDetailAdapter extends RecyclerView.Adapter<AssignmentDetailAdapter.Holder> {
    public Context context;
    public List<AssignmentsDetail> mDataList;
    private AssignmentDetailInterface anInterface;
    private String courseName = "";


    public AssignmentDetailAdapter(Context context, AssignmentDetailInterface assignmentDetailInterface,String courseName) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.anInterface = assignmentDetailInterface;
        this.courseName = courseName;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_assigment_detail, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        AssignmentsDetail assignmentsDetail = mDataList.get(position);
        if (assignmentsDetail.getName() != null) {
            holder.subjectNameTextView.setText(courseName);
        }
        if (assignmentsDetail.getStartAt() != null) {
            holder.dateTextView.setText(context.getResources().getString(R.string.publish) +" "+ Util.getAssigmentDetailStartDate(assignmentsDetail.getStartAt()));
        }

        if (assignmentsDetail.getEndAt() != null) {
            holder.dayTextView.setText(Util.getAssigmentDetailEndDateDay(assignmentsDetail.getEndAt()));
            holder.monthTextView.setText(Util.getAssigmentDetailEndDateMonth(assignmentsDetail.getEndAt(),context));
        }
        if (assignmentsDetail.getName() != null) {
            holder.assignmentNameTextView.setText(assignmentsDetail.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<AssignmentsDetail> assignmentsDetailArrayList) {
        this.mDataList.clear();
        this.mDataList.addAll(assignmentsDetailArrayList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView, dayTextView, monthTextView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;


        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            assignmentNameTextView = itemView.findViewById(R.id.tv_assignment_name);
            imageLoader = new PicassoLoader();
            courseAvatarView = itemView.findViewById(R.id.img_course);
            dayTextView = itemView.findViewById(R.id.tv_day_number);
            monthTextView = itemView.findViewById(R.id.tv_month);
        }
    }


    public interface AssignmentDetailInterface {
        void onItemClicked(AssignmentsDetail assignmentsDetail);
    }
}