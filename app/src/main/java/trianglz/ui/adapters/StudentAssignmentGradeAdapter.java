package trianglz.ui.adapters;

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

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;

/**
 * Created by Farah A. Moniem on 04/08/2019.
 */
public class StudentAssignmentGradeAdapter extends RecyclerView.Adapter<StudentAssignmentGradeAdapter.ViewHolder> {

    Context context;
    StudentGradeInterface studentGradeInterface;

    public StudentAssignmentGradeAdapter(Context context, StudentGradeInterface studentGradeInterface) {
        this.context = context;
        this.studentGradeInterface = studentGradeInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_assignment_grade, parent, false);
        return new StudentAssignmentGradeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(holder.studentAvatar, new AvatarPlaceholderModified("Farah Ahmed"), "Path of Image");
//        if (studentName == null || studentName.equals("") || studentName.isEmpty()) {
//            studentAvatar.setVisibility(View.INVISIBLE);
//        }
        holder.studentName.setText("Courtney fisher");
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
        return 10;
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
