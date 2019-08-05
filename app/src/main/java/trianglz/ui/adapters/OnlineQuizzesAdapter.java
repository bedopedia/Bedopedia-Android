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

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.models.QuizzCourse;
import trianglz.utils.Util;

public class OnlineQuizzesAdapter extends RecyclerView.Adapter<OnlineQuizzesAdapter.Holder> {
    public Context context;
    public List<QuizzCourse> mDataList;
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
        QuizzCourse quizzCourse = mDataList.get(position);
        holder.subjectNameTextView.setText(quizzCourse.getCourseName());
        if(quizzCourse.getQuizName() != null && !quizzCourse.getQuizName().isEmpty()){
            holder.assignmentNameTextView.setText(quizzCourse.getQuizName());
        }else {
            holder.assignmentNameTextView.setText(context.getResources().getString(R.string.no_quiz));
        }
        holder.assignmentCountsTextView.setText(quizzCourse.getQuizzesCount() + "");
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(holder.courseAvatarView,new AvatarPlaceholderModified(quizzCourse.getCourseName()),"path of image");
        if(quizzCourse.getQuizState()!= null){
            if (quizzCourse.getQuizState().equals("running")) {
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
            } else {
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
            }
        }else {
            holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
        }
        if (quizzCourse.getNextQuizDate() == null || quizzCourse.getNextQuizDate().equals("")) {
            holder.dateLinearLayout.setVisibility(View.INVISIBLE);
        }
        holder.dateTextView.setText(Util.getCourseDate(quizzCourse.getNextQuizDate()));
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

    public void addData(ArrayList<QuizzCourse> quizzCourses) {
        this.mDataList.clear();
        if (quizzCourses != null) this.mDataList.addAll(quizzCourses);
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

    public interface OnlineQuizzesInterface{
        void onItemClicked(QuizzCourse quizzCourse);
    }

}