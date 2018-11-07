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

import java.util.ArrayList;
import java.util.List;

import trianglz.models.CourseGroup;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.Holder> {
    public Context context;
    List<CourseGroup> mDataList;
    GradesAdapterInterface gradesAdapterInterface;


    public GradesAdapter(Context context, GradesAdapterInterface gradesAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.gradesAdapterInterface = gradesAdapterInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CourseGroup courseGroup = mDataList.get(position);
        holder.gradeTextView.setText(courseGroup.getGrade());
        holder.subjectNameTextView.setText(courseGroup.getCourseName());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradesAdapterInterface.onSubjectSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<CourseGroup> courseGroupList) {
        this.mDataList.clear();
        this.mDataList.addAll(courseGroupList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, gradeTextView;
        public ImageView subjectImageView;
        public LinearLayout itemLayout;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            gradeTextView = itemView.findViewById(R.id.tv_grade);
            subjectImageView = itemView.findViewById(R.id.img_subject);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public interface GradesAdapterInterface{
        void onSubjectSelected(int position);
    }

}