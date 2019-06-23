package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 6/23/2019.
 */
public class AssignmentDetailAdapter extends RecyclerView.Adapter<AssignmentDetailAdapter.Holder> {
    public Context context;
    public List<AssignmentsDetail> mDataList;
    private AssignmentDetailInterface anInterface;


    public AssignmentDetailAdapter(Context context, AssignmentDetailInterface assignmentDetailInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.anInterface = assignmentDetailInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_assigment_detail, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        AssignmentsDetail assignmentsDetail = mDataList.get(position);
        holder.subjectNameTextView.setText(assignmentsDetail.getName());
//        holder.assignmentNameTextView.setText(assignmentsDetail.getAssignmentName());
//        holder.assignmentCountsTextView.setText(assignmentsDetail.getAssignmentsCount() + "");
//        setCourseImage("", assignmentsDetail.getCourseName(), holder);
//        if (assignmentsDetail.getAssignmentState() != null) {
//            if (assignmentsDetail.getAssignmentState().equals("running")) {
//                holder.dateTextView.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
//            } else {
//                holder.dateTextView.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
//            }
//        } else {
//            holder.dateLinearLayout.setVisibility(View.INVISIBLE);
//        }
//        holder.dateTextView.setText(Util.getCourseDate(assignmentsDetail.getNextAssignmentDate()));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                anInterface.onItemClicked(mDataList.get(position));
//            }
//        });
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
                assignmentNameTextView, dayTextView, monthTextView, assignmentCountsTextView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;
        private LinearLayout dateLinearLayout;


        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_course_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            assignmentNameTextView = itemView.findViewById(R.id.tv_assignment_name);
            imageLoader = new PicassoLoader();
            courseAvatarView = itemView.findViewById(R.id.img_course);
            assignmentCountsTextView = itemView.findViewById(R.id.tv_assignment_count);
            dateLinearLayout = itemView.findViewById(R.id.ll_date);
        }
    }

    private void setCourseImage(String imageUrl, final String name, final Holder holder) {
        if (imageUrl == null || imageUrl.equals("")) {
            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.courseAvatarView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(context)
                    .load(imageUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.courseAvatarView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            holder.imageLoader = new PicassoLoader();
                            holder.imageLoader.loadImage(holder.courseAvatarView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }


    public interface AssignmentDetailInterface {
        void onItemClicked(AssignmentsDetail assignmentsDetail);
    }
}