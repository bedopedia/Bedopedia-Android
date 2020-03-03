package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import login.Services.ApiClient;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Student;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.KidsViewHolder> {


    private ArrayList<Student> mDataList;
    private Context context;
    final String urlUploadsKey = "/uploads";

    private HomeAdapterInterface homeAdapterInterface;

    public HomeAdapter(Context context, HomeAdapterInterface homeAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.homeAdapterInterface = homeAdapterInterface;
    }


    @Override
    public KidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_kid_home, parent, false);
        return new KidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KidsViewHolder holder, final int position) {
        final Student student = mDataList.get(position);
        if (position == mDataList.size() - 1) {
            //  holder.lineView.setVisibility(View.GONE);
        }
        boolean expanded = student.isExpanded();
        // Set the visibility based on state

//        if(position % 4 == 0){
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.color1));
//        }else if(position % 4 == 1){
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.color2));
//        }else if(position % 4 == 2){
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.color3));
//        }else {
//            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.color4));
//        }
        String name = student.firstName + " " + student.lastName;
        holder.studentName.setText(name);
        String imageUrl = student.getAvatar();
        if (imageUrl.substring(0, 8).equals(urlUploadsKey)) {
            imageUrl = ApiClient.BASE_URL + imageUrl;
        }
        setStudentImage(imageUrl, holder, name);
        setAttendanceCircle(student.getTodayAttendance(), holder);
        holder.gradeTextView.setText(student.level);
        String quizzes = String.valueOf(student.getTodayQuizzesCount());
        holder.quizzesTextView.setText(quizzes);
        String assignments = String.valueOf(student.getTodayAssignmentsCount());
        holder.assignmentsTextView.setText(assignments);
        String events = String.valueOf(student.getTodayEventsCount());
        holder.eventsTextView.setText(events);

        if (student.isExpanded()) {
            holder.expandableLayout.expand();
            holder.expandImageButton.setImageResource(R.drawable.ic_keyboard_arrow_up);
        } else {
            holder.expandableLayout.collapse();
            holder.expandImageButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeAdapterInterface.onOpenStudentClicked(mDataList.get(position), position);
            }
        });
        holder.assignmentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeAdapterInterface.onOpenStudentClicked(mDataList.get(position), position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<Student> mDataList) {
        this.mDataList.clear();
        this.mDataList.addAll(mDataList);
        notifyDataSetChanged();
    }

    public class KidsViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName, gradeTextView, stateTextView, quizzesTextView,
                assignmentsTextView, eventsTextView;
        public AvatarView studentImageView;
        // public View lineView;
        public IImageLoader imageLoader;
        public LinearLayout itemLayout;
        public ImageButton expandImageButton;
        public LinearLayout expandedLinearLayout;
        public ExpandableLayout expandableLayout;

        public KidsViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.tv_student_name);
            gradeTextView = itemView.findViewById(R.id.tv_grade);
            stateTextView = itemView.findViewById(R.id.tv_state_student);
            studentImageView = itemView.findViewById(R.id.img_student);
            //lineView = itemView.findViewById(R.id.view_line);
            imageLoader = new PicassoLoader();
            quizzesTextView = itemView.findViewById(R.id.tv_quizzes);
            assignmentsTextView = itemView.findViewById(R.id.tv_assignment);
            eventsTextView = itemView.findViewById(R.id.tv_events);
            itemLayout = itemView.findViewById(R.id.layout_item);
            expandImageButton = itemView.findViewById(R.id.expand_btn);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            Util.increaseButtonsHitArea(expandImageButton);
            expandImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded = mDataList.get(getAdapterPosition()).isExpanded();
                    // Change the state
                    mDataList.get(getAdapterPosition()).setExpanded(!expanded);
                    // Notify the adapter that item has changed
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }

    }

    public void setAttendanceCircle(String attendanceStatus, KidsViewHolder holder) {
        attendanceStatus = attendanceStatus.substring(0, 1).toUpperCase() + attendanceStatus.substring(1).toLowerCase();
        holder.stateTextView.setText(attendanceStatus);
//        if (attendanceStatus.equals("Present")) {
//            holder.stateTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.present_icon, null), null, null, null);
//        } else if (attendanceStatus.equals("Not taken")) {
//            holder.stateTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.attendance_circle_grey, null), null, null, null);
//        } else if (attendanceStatus.equals("Late")) {
//            holder.stateTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.late_icon, null), null, null, null);
//        } else if (attendanceStatus.equals("Excused")) {
//            holder.stateTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.excused_icon, null), null, null, null);
//        } else if (attendanceStatus.equals("Absent")) {
//            holder.stateTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.absent_icon, null), null, null, null);
//        }
    }

    private void setStudentImage(String imageUrl, final KidsViewHolder holder, final String name) {
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

    public interface HomeAdapterInterface {
        void onOpenStudentClicked(Student student, int position);

        void onAssignmentClicked(Student student);
    }


}