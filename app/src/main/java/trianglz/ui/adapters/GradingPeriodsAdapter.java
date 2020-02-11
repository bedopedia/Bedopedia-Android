package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.GradingPeriod;
import trianglz.models.SubGradingPeriod;

public class GradingPeriodsAdapter extends RecyclerView.Adapter<GradingPeriodsAdapter.Holder> {
    public Context context;
    public ArrayList<Object> mDataList;
    GradingPeriodsInterface gradingPeriodsInterface;
    public Period type;
    public String subjectName = "";

    public GradingPeriodsAdapter(Context context,
                                 GradingPeriodsInterface gradingPeriodsInterface
            , Period type) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.gradingPeriodsInterface = gradingPeriodsInterface;
        this.type = type;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewPeriod) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (type == Period.SEMESTER) {
            GradingPeriod gradingPeriod = (GradingPeriod) mDataList.get(position);
            String teacherCount = "";
            if (gradingPeriod.subGradingPeriods.size() > 1) {
                teacherCount = (gradingPeriod .subGradingPeriods.size() + " " +
                        context.getResources().getString(R.string.quarters));
            } else {
                teacherCount = (gradingPeriod.subGradingPeriods.size() + " " +
                        context.getResources().getString(R.string.quarters));
            }
            holder.teacherCountTextView.setVisibility(View.VISIBLE);
            holder.teacherCountTextView.setText(teacherCount);
            holder.subjectNameTextView.setText(gradingPeriod.name.trim());
            holder.itemLayout.setOnClickListener(view -> {
                if (type.equals(Period.QUARTER)) {
                    gradingPeriodsInterface.onQuarterSelected(holder.getAdapterPosition());
                } else {
                    gradingPeriodsInterface.onSemesterSelected(holder.getAdapterPosition());
                }

            });
        } else {
            SubGradingPeriod subGradingPeriod = (SubGradingPeriod) mDataList.get(position);
            holder.subjectNameTextView.setText(subGradingPeriod.name);
            holder.teacherCountTextView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<Object> courseGroupList, Period type) {
        this.type = type;
        this.mDataList.clear();
        this.mDataList.addAll(courseGroupList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, teacherCountTextView;
        public LinearLayout itemLayout;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            teacherCountTextView = itemView.findViewById(R.id.tv_teacher_count);
            itemLayout = itemView.findViewById(R.id.item_layout);

        }
    }

    public interface GradingPeriodsInterface {
        void onSemesterSelected(int position);

        void onQuarterSelected(int position);
    }

    public enum Period {
        SEMESTER, QUARTER
    }

}