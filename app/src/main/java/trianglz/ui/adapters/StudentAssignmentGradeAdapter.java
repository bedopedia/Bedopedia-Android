package trianglz.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.StudentAssignmentSubmission;

/**
 * Created by Farah A. Moniem on 04/08/2019.
 */
public class StudentAssignmentGradeAdapter extends RecyclerView.Adapter<StudentAssignmentGradeAdapter.ViewHolder> {

    Context context;
    StudentGradeInterface studentGradeInterface;
    ArrayList<StudentAssignmentSubmission> mDataList;

    public StudentAssignmentGradeAdapter(Context context, StudentGradeInterface studentGradeInterface) {
        this.context = context;
        this.studentGradeInterface = studentGradeInterface;
        mDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_assignment_grade, parent, false);
        return new StudentAssignmentGradeAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final StudentAssignmentSubmission submission = mDataList.get(position);
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(holder.studentAvatar, new AvatarPlaceholderModified(submission.getStudentName()), "Path of Image");
//        if (studentName == null || studentName.equals("") || studentName.isEmpty()) {
//            studentAvatar.setVisibility(View.INVISIBLE);
//        }

        if (submission.getFeedback() != null) {
            holder.studentGrade.setText(Double.toString(submission.getGrade()));
            holder.studentFeedback.setText(submission.getFeedback().getContent());
        }
        holder.studentName.setText(submission.getStudentName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentGradeInterface.onGradeButtonClick(holder.studentGrade.getText().toString(), holder.studentFeedback.getText().toString());
            }
        });
        holder.downloadAssignmnentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentGradeInterface.onDownloadButtonClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public void addData(ArrayList<StudentAssignmentSubmission> studentAssignmentSubmissions) {
        this.mDataList.clear();
        if(studentAssignmentSubmissions != null) this.mDataList.addAll(studentAssignmentSubmissions);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AvatarView studentAvatar;
        ImageButton downloadAssignmnentBtn;
        TextView studentName, studentGrade, studentFeedback;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            studentAvatar = itemView.findViewById(R.id.student_avatar);
            studentName = itemView.findViewById(R.id.tv_student_name);
            downloadAssignmnentBtn = itemView.findViewById(R.id.download_btn);
            studentGrade = itemView.findViewById(R.id.student_grade_btn);
            studentFeedback = itemView.findViewById(R.id.student_feedback_btn);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public interface StudentGradeInterface {
        void onGradeButtonClick(String grade, String feedback);

        void onDownloadButtonClick();
    }
}