package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.QuizzCourse;
import trianglz.utils.Util;

public class OnlineQuizzesAdapter extends RecyclerView.Adapter<OnlineQuizzesAdapter.Holder> {
    public Context context;
    public List<QuizzCourse> mDataList;
    private OnlineQuizzesInterface anInterface;


    public OnlineQuizzesAdapter(Context context, OnlineQuizzesInterface onlineQuizzesInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.anInterface = onlineQuizzesInterface;
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
        if (quizzCourse.getQuizName() != null && !quizzCourse.getQuizName().isEmpty()) {
            holder.assignmentNameTextView.setText(quizzCourse.getQuizName());
        } else {
            holder.assignmentNameTextView.setText(context.getResources().getString(R.string.no_quiz));
        }
        if (quizzCourse.getRunningQuizzesCount() > 0) {
            holder.assignmentCountsTextView.setVisibility(View.VISIBLE);
            holder.assignmentCountsTextView.setText(quizzCourse.getRunningQuizzesCount() + "");
        } else {
            holder.assignmentCountsTextView.setVisibility(View.GONE);
        }
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(holder.courseAvatarView, new AvatarPlaceholderModified(quizzCourse.getCourseName()), "path of image");
        if (quizzCourse.getNextQuizDate() != null && !quizzCourse.getNextQuizDate().isEmpty()) {
            holder.dateLinearLayout.setVisibility(View.VISIBLE);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime dateTime =fmt.parseDateTime(quizzCourse.getNextQuizDate());

            if (dateTime.isBefore(DateTime.now())) {
//                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.red, null));
                holder.clockImageView.setImageResource(R.drawable.ic_clock_red);

            } else {
//                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
                holder.clockImageView.setImageResource(R.drawable.ic_clock_green);
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
            }
            holder.dateTextView.setText(Util.getPostDateAmPm(quizzCourse.getNextQuizDate(), context));
        } else {
            holder.dateLinearLayout.setVisibility(View.GONE);
        }

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
        private ImageView clockImageView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;
        private LinearLayout dateLinearLayout;


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

    public interface OnlineQuizzesInterface {
        void onItemClicked(QuizzCourse quizzCourse);
    }

}