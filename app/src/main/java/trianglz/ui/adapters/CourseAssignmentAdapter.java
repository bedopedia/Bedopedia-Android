package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import trianglz.models.CourseAssignment;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 4/22/2019.
 */
public class CourseAssignmentAdapter extends RecyclerView.Adapter<CourseAssignmentAdapter.Holder> {
    public Context context;
    public List<CourseAssignment> mDataList;
    private CourseAssignmentAdapterInterface anInterface;


    public CourseAssignmentAdapter(Context context, CourseAssignmentAdapterInterface courseAssignmentAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.anInterface =  courseAssignmentAdapterInterface;
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
            holder.dateLinearLayout.setVisibility(View.VISIBLE);
            if (courseAssignment.getAssignmentState().equals("running")) {
//                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
                holder.clockImageView.setImageResource(R.drawable.ic_clock_green);
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
            } else {
//                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.red, null));
                holder.clockImageView.setImageResource(R.drawable.ic_clock_red);
            }
        }else {
            holder.dateLinearLayout.setVisibility(View.GONE);
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
        if(courseAssignments != null) this.mDataList.addAll(courseAssignments);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView, dayTextView, monthTextView, assignmentCountsTextView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;
        private LinearLayout dateLinearLayout;
        private ImageView clockImageView;



        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_course_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            clockImageView = itemView.findViewById(R.id.date_icon);
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


    public interface CourseAssignmentAdapterInterface{
        void onItemClicked(CourseAssignment courseAssignment);
    }

}