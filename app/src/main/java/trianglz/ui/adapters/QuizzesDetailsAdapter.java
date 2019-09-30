package trianglz.ui.adapters;
 // Created by gemy

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.managers.SessionManager;
import trianglz.models.Quizzes;
import trianglz.utils.Util;

public class QuizzesDetailsAdapter extends RecyclerView.Adapter<QuizzesDetailsAdapter.Holder>{
    public Context context;
    public List<Quizzes> mDataList;
    private QuizzesDetailsInterface anInterface;
    private String courseName = "";


    public QuizzesDetailsAdapter(Context context, QuizzesDetailsInterface assignmentDetailInterface,String courseName) {
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
//        Boolean teacher = !SessionManager.getInstance().getUserType();
        if(position == mDataList.size() - 1 ){
            if(!SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())){
                anInterface.onReachPosition();
            }
        }
        final Quizzes quizzes = mDataList.get(position);
        DateTime dateTime = new DateTime(quizzes.getStartDate());
        if (quizzes.getName() != null) {
            holder.subjectNameTextView.setText(courseName);
        }
        if (quizzes.getEndDate() != null) {
            holder.dateTextView.setText(Util.getPostDateAmPm(quizzes.getEndDate(),context));
        }

        if (quizzes.getEndDate() != null) {
            holder.dayTextView.setText(Util.getAssigmentDetailEndDateDay(quizzes.getEndDate()));
            holder.monthTextView.setText(Util.getAssigmentDetailEndDateMonth(quizzes.getEndDate(),context));
        }
        if (quizzes.getName() != null) {
            holder.assignmentNameTextView.setText(quizzes.getName());
        }
        if(quizzes.getState() != null){
            if (quizzes.getState().equals("running")) {
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage));
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.pine,null));
                holder.clockImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.green_clock_icon,null));
            } else {
                holder.dateLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.curved_red));
                holder.dateTextView.setTextColor(context.getResources().getColor(R.color.dirt_brown,null));
                holder.clockImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.red_clock_icon,null));
            }
        }else {
            holder.dateTextView.setVisibility(View.INVISIBLE);
        }

        String published = context.getString(R.string.published) + " " + Util.getPostDate(dateTime.toString(), context);
        holder.publishedTextView.setText(published);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anInterface.onItemClicked(quizzes);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<Quizzes> quizzes) {
        this.mDataList.clear();
        if(quizzes != null) this.mDataList.addAll(quizzes);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView, dayTextView, monthTextView, publishedTextView;
        public IImageLoader imageLoader;
        private AvatarView courseAvatarView;
        public LinearLayout dateLinearLayout;
        public CardView cardView;
        private ImageView clockImageView;

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
        }
    }


    public interface QuizzesDetailsInterface {
        void onItemClicked(Quizzes quizzes);
        void onReachPosition();
    }
}
