package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;

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


    public AssignmentDetailAdapter(Context context, AssignmentDetailInterface assignmentDetailInterface, String courseName) {
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
        final AssignmentsDetail assignmentsDetail = mDataList.get(position);
        DateTime dateTime = new DateTime(assignmentsDetail.getStartAt());
        if (assignmentsDetail.getName() != null) {
            holder.subjectNameTextView.setText(courseName);
        }
        if (assignmentsDetail.getEndAt() != null) {
            holder.dateTextView.setText(Util.getPostDateAmPm(assignmentsDetail.getEndAt(), context));
            holder.dueTimeTextView.setText(Util.getTimeAm(assignmentsDetail.getEndAt(), context,false));
        }

        if (assignmentsDetail.getEndAt() != null) {
            holder.dayTextView.setText(Util.getEndDateDay(assignmentsDetail.getEndAt(), false));
            holder.monthTextView.setText(Util.getEndDateMonth(assignmentsDetail.getEndAt(), context, false));
        }
        if (assignmentsDetail.getName() != null) {
            holder.assignmentNameTextView.setText(assignmentsDetail.getName());
        }
        if (assignmentsDetail.getState() != null) {
            holder.dateTextView.setVisibility(View.VISIBLE);
            if (assignmentsDetail.getState().equals("running")) {
//                holder.clockImageView.setImageResource(R.drawable.ic_clock_green);
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.color.transparent_light_sage,null));

                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
                holder.dayTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
                holder.monthTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
                holder.dueTimeTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
                holder.dueTimeTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_clock_green, null), null, null, null);
                holder.cardView.setOnClickListener(view -> anInterface.onRunningAssignmentClicked(assignmentsDetail));

            } else {
//                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.red, null));
//                holder.clockImageView.setImageResource(R.drawable.ic_clock_red);
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.color.very_light_pink,null));
                holder.dayTextView.setTextColor(context.getResources().getColor(R.color.transparent_red, null));
                holder.monthTextView.setTextColor(context.getResources().getColor(R.color.transparent_red, null));
                holder.dueTimeTextView.setTextColor(context.getResources().getColor(R.color.red, null));
                holder.dueTimeTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_clock_red, null), null, null, null);
                holder.cardView.setOnClickListener(view -> anInterface.onItemClicked(assignmentsDetail));

            }
        } else {
            holder.dateTextView.setVisibility(View.INVISIBLE);
        }

        String published = context.getString(R.string.published) + " " + Util.getPostDate(dateTime.toString(), context);
        holder.publishedTextView.setText(published);
        //  if (assignmentsDetail.getDescription() != null || assignmentsDetail.getUploadedFilesCount() != 0) {
        //  }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<AssignmentsDetail> assignmentsDetailArrayList) {
        this.mDataList.clear();
        if (assignmentsDetailArrayList != null) this.mDataList.addAll(assignmentsDetailArrayList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView, dayTextView, monthTextView, publishedTextView,dueTimeTextView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;
        public FrameLayout dateLinearLayout;
        public CardView cardView;
        public ImageView clockImageView;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            assignmentNameTextView = itemView.findViewById(R.id.tv_assignment_name);
            imageLoader = new PicassoLoader();
            courseAvatarView = itemView.findViewById(R.id.img_course);
            dayTextView = itemView.findViewById(R.id.tv_day_number);
            monthTextView = itemView.findViewById(R.id.tv_month);
            dateLinearLayout = itemView.findViewById(R.id.ll_date);
            publishedTextView = itemView.findViewById(R.id.tv_published);
            cardView = itemView.findViewById(R.id.card_view);
            clockImageView = itemView.findViewById(R.id.date_icon);
            dueTimeTextView = itemView.findViewById(R.id.due_time_tv);
        }
    }


    public interface AssignmentDetailInterface {
        void onItemClicked(AssignmentsDetail assignmentsDetail);
        void onRunningAssignmentClicked(AssignmentsDetail assignmentsDetail);
    }
}