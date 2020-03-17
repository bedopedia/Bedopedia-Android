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
import trianglz.models.StudentSubmission;

/**
 * Created by Farah A. Moniem on 04/08/2019.
 */
public class StudentGradeAdapter extends RecyclerView.Adapter<StudentGradeAdapter.ViewHolder> {

    Context context;
    StudentGradeInterface studentGradeInterface;
    ArrayList<StudentSubmission> mDataList;
    private boolean isQuizzes = false;

    public StudentGradeAdapter(Context context, StudentGradeInterface studentGradeInterface) {
        this.context = context;
        this.studentGradeInterface = studentGradeInterface;
        mDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_assignment_grade, parent, false);
        return new StudentGradeAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final StudentSubmission submission = mDataList.get(position);
        IImageLoader imageLoader = new PicassoLoader();
        String grade;
        // for displaying the previous grade while editing
        if (!isQuizzes) {
            if (submission.getGrade() == null) {
                grade = "";
            } else if (submission.getGrade() == 0) {
                grade = "0";
            } else {
                grade = Double.toString(submission.getGrade());
            }
        }else {
            if (submission.getScore() == null) {
                grade = "";
            } else if (submission.getScore() == 0) {
                grade = "0";
            } else {
                grade = Double.toString(submission.getScore());
            }
        }
        if (!isQuizzes) {
            imageLoader.loadImage(holder.studentAvatar, new AvatarPlaceholderModified(submission.getStudentName()), submission.getAvatarUrl());


            if (submission.getFeedback() != null) {
                holder.studentFeedback.setText(submission.getFeedback().getContent());
            } else {
                holder.studentFeedback.setText("");
            }
            if (submission.getGrade() != null) {
                holder.studentGrade.setText(Double.toString(submission.getGrade()));
            } else {
                holder.studentGrade.setText("");
            }
            holder.studentName.setText(submission.getStudentName());
            holder.itemView.setOnClickListener(view -> {
                studentGradeInterface.onItemCLicked(grade
                        , holder.studentFeedback.getText().toString(), submission.getStudentId());

            });
            holder.downloadAssignmentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studentGradeInterface.onDownloadButtonClick();
                }
            });
        } else {
            imageLoader.loadImage(holder.studentAvatar, new AvatarPlaceholderModified(submission.getStudentName()), submission.getAvatarUrl());


            if (submission.getFeedback() != null) {
                holder.studentFeedback.setText(submission.getFeedback().getContent());
            } else {
                holder.studentFeedback.setText("");
            }

            if (submission.getScore() != null) {
                holder.studentGrade.setText(Double.toString(submission.getScore()));
            } else {
                holder.studentGrade.setText("");
            }
            holder.studentName.setText(submission.getStudentName());
                holder.itemView.setOnClickListener(view -> {

                    studentGradeInterface.onItemCLicked(grade
                            , holder.studentFeedback.getText().toString(), submission.getStudentId());

                });
            holder.downloadAssignmentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studentGradeInterface.onDownloadButtonClick();
                }
            });
        }
        holder.downloadAssignmentBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<StudentSubmission> studentSubmissions) {
        this.mDataList.clear();
        if (studentSubmissions != null) this.mDataList.addAll(studentSubmissions);
        notifyDataSetChanged();
    }

    public void setIsQuizzes() {
        this.isQuizzes = true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AvatarView studentAvatar;
        ImageButton downloadAssignmentBtn;
        TextView studentName, studentGrade, studentFeedback;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            studentAvatar = itemView.findViewById(R.id.student_avatar);
            studentName = itemView.findViewById(R.id.tv_student_name);
            downloadAssignmentBtn = itemView.findViewById(R.id.download_btn);
            studentGrade = itemView.findViewById(R.id.student_grade_btn);
            studentFeedback = itemView.findViewById(R.id.student_feedback_btn);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public interface StudentGradeInterface {
        void onItemCLicked(String grade, String feedback, int studentId);

        void onDownloadButtonClick();
    }
}
