package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

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

public class OnlineQuizzesAdapter extends RecyclerView.Adapter<OnlineQuizzesAdapter.Holder> {
    public Context context;
    public List<CourseAssignment> mDataList;
    private OnlineQuizzesInterface anInterface;


    public OnlineQuizzesAdapter(Context context, OnlineQuizzesInterface onlineQuizzesInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.anInterface =  onlineQuizzesInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_course_assignment, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CourseAssignment courseAssignment = mDataList.get(position);
        holder.subjectNameTextView.setText(courseAssignment.getCourseName());
        if(courseAssignment.getAssignmentName() != null && !courseAssignment.getAssignmentName().isEmpty()){
            holder.assignmentNameTextView.setText(courseAssignment.getAssignmentName());
        }else {
            holder.assignmentNameTextView.setText(context.getResources().getString(R.string.no_assignment));
        }
        holder.assignmentCountsTextView.setText(courseAssignment.getAssignmentsCount() + "");
        setCourseImage("", courseAssignment.getCourseName(), holder);
        if(courseAssignment.getAssignmentState() != null){
            if (courseAssignment.getAssignmentState().equals("running")) {
                holder.dateTextView.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
            } else {
                holder.dateTextView.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
            }
        }else {
            holder.dateLinearLayout.setVisibility(View.INVISIBLE);
        }
        holder.dateTextView.setText(Util.getCourseDate(courseAssignment.getNextAssignmentDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.onItemClicked(mDataList.get(position));
            }
        });
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


    public interface OnlineQuizzesInterface{
        void onItemClicked(CourseAssignment courseAssignment);
    }

}