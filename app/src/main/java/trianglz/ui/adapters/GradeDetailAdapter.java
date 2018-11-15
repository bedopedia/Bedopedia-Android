package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import gradeBook.Course;
import trianglz.models.Assignment;
import trianglz.models.CourseGradingPeriods;
import trianglz.models.GradeHeader;
import trianglz.models.GradeItem;
import trianglz.models.Quiz;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_QUARTER = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_DETAIL = 3;


    public List<Object> mDataList;
    private Context context;


    public GradeDetailAdapter(Context context) {
        this.context = context;
        this.mDataList = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_QUARTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_quarter, parent, false);
            return new QuarterViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
            return new DetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(mDataList.get(position) instanceof CourseGradingPeriods){
            CourseGradingPeriods courseGradingPeriods = (CourseGradingPeriods) mDataList.get(position);
            QuarterViewHolder quarterViewHolder = ((QuarterViewHolder)holder);
            quarterViewHolder.quarterTextView.setText(courseGradingPeriods.name);
        }else if(mDataList.get(position) instanceof Assignment){
            DetailViewHolder detailViewHolder = ((DetailViewHolder)holder);
            Assignment assignment = (Assignment) mDataList.get(position);
            detailViewHolder.classWorkTextView.setText(assignment.name);
            detailViewHolder.markTextView.setText(assignment.grade+"/"+assignment.total);
            detailViewHolder.averageGradeTextView.setText(assignment.averageGrade+"");
        }else if(mDataList.get(position) instanceof Quiz){
            DetailViewHolder detailViewHolder = ((DetailViewHolder)holder);
            Quiz quiz = (Quiz) mDataList.get(position);
            detailViewHolder.classWorkTextView.setText(quiz.name);
            detailViewHolder.markTextView.setText(quiz.grade+"/"+quiz.total);
            detailViewHolder.averageGradeTextView.setText(quiz.averageGrade+"");

        }else if(mDataList.get(position) instanceof GradeItem){
            DetailViewHolder detailViewHolder = ((DetailViewHolder)holder);
            GradeItem gradeItem = (GradeItem) mDataList.get(position);
            detailViewHolder.classWorkTextView.setText(gradeItem.name);
            detailViewHolder.markTextView.setText(gradeItem.grade+"/"+gradeItem.total);
            detailViewHolder.averageGradeTextView.setText(gradeItem.averageGrade+"");
        }else {
            HeaderViewHolder headerViewHolder = ((HeaderViewHolder)holder);
            GradeHeader gradeHeader = ((GradeHeader)mDataList.get(position));
            headerViewHolder.headerTextView.setText(gradeHeader.header);
            String average = gradeHeader.sumOfStudentMarks +"/" + gradeHeader.totalSummtion;
            headerViewHolder.headerGradeTextView.setText(average);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position)instanceof CourseGradingPeriods) {
            return TYPE_QUARTER;
        } else if (mDataList.get(position) instanceof Assignment ||
                mDataList.get(position) instanceof Quiz ||
                mDataList.get(position) instanceof GradeItem) {
            return TYPE_DETAIL;
        } else {
            return TYPE_HEADER;
        }
    }

    public void addData(ArrayList<Object> objectArrayList) {
        mDataList.clear();
        mDataList.addAll(objectArrayList);
        notifyDataSetChanged();
    }

    public class QuarterViewHolder extends RecyclerView.ViewHolder {
        public TextView quarterTextView;
        public QuarterViewHolder(View itemView) {
            super(itemView);
            quarterTextView = itemView.findViewById(R.id.tv_quarter);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView, headerGradeTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.tv_header);
            headerGradeTextView = itemView.findViewById(R.id.tv_total_header_grade);
        }
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView classWorkTextView,markTextView,averageGradeTextView,stateTextView;
        public DetailViewHolder(View itemView) {
            super(itemView);
        classWorkTextView = itemView.findViewById(R.id.tv_class_work);
        markTextView = itemView.findViewById(R.id.tv_mark);
        averageGradeTextView = itemView.findViewById(R.id.tv_avg_grade);
        stateTextView = itemView.findViewById(R.id.tv_state);
        }
    }


}