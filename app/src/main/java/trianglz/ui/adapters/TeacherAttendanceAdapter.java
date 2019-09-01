package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
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


/**
 * Created by Farah A. Moniem on 01/09/2019.
 */
public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder>{
    public Context context;
    List<String> items;
    public STATE state;


    public TeacherAttendanceAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.state = state;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setStudentImage("",holder,"Amanda Paul");
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public void addData(ArrayList<String> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }
    public enum STATE {
        PRESENT ,LATE, EXCUSED, ABSENT
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView studentName;
        IImageLoader imageLoader;
        AvatarView studentImageView;
        RadioButton presentRadioButton, lateRadioButton, excusedRadioButton, absentRadioButton;
        public ViewHolder(View itemView) {
            super(itemView);
            imageLoader = new PicassoLoader();
            studentImageView = itemView.findViewById(R.id.student_avatar);
            studentName = itemView.findViewById(R.id.student_name_tv);
            presentRadioButton = itemView.findViewById(R.id.present_btn);
            lateRadioButton = itemView.findViewById(R.id.late_btn);
            excusedRadioButton = itemView.findViewById(R.id.excused_btn);
            absentRadioButton = itemView.findViewById(R.id.absent_btn);
        }
    }

    private void setStudentImage(String imageUrl, final ViewHolder holder, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(context)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(holder.studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            holder.imageLoader = new PicassoLoader();
                            holder.imageLoader.loadImage(holder.studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }


}
