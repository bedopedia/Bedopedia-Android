package trianglz.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.AttendanceStudent;
import trianglz.models.Attendances;
import trianglz.utils.Constants;


/**
 * Created by Farah A. Moniem on 01/09/2019.
 */
public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder> {
    public Context context;
    TeacherAttendanceAdapterInterface teacherAttendanceAdapterInterface;
    HashMap<Integer, Status> positionStatusHashMap;
    HashMap<Integer, AttendanceStudent> positionCheckStatusHashMap;
    ArrayList<AttendanceStudent> mDataList;
    ArrayList<Attendances> mDataAttendancesList;


    public TeacherAttendanceAdapter(Context context, TeacherAttendanceAdapterInterface teacherAttendanceAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.mDataAttendancesList = new ArrayList<>();
        positionStatusHashMap = new HashMap<>();
        positionCheckStatusHashMap = new HashMap<>();
        this.teacherAttendanceAdapterInterface = teacherAttendanceAdapterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setStudentImage(mDataList.get(position).getAvatarUrl(), holder, mDataList.get(position).getName());
        holder.clearAllStatus();
        if (positionStatusHashMap.containsKey(position)) {
            Status status = positionStatusHashMap.get(position);
            switch (status) {
                case PRESENT:
                    holder.presentButton.setBackground(context.getResources().getDrawable(R.drawable.curved_light_sage, null));
                    holder.presentButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.present_icon, null), null, null, null);
                    holder.presentButton.setTextColor(context.getResources().getColor(R.color.boring_green, null));
                    break;
                case LATE:
                    holder.lateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_light_tan_two, null));
                    holder.lateButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.late_icon, null), null, null, null);
                    holder.lateButton.setTextColor(context.getResources().getColor(R.color.electric_violet, null));
                    break;
                case ABSENT:
                    holder.absentButton.setBackground(context.getResources().getDrawable(R.drawable.curved_very_light_pink, null));
                    holder.absentButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.absent_icon, null), null, null, null);
                    holder.absentButton.setTextColor(context.getResources().getColor(R.color.salmon, null));
                    break;
                case EXCUSED:
                    holder.excusedButton.setBackground(context.getResources().getDrawable(R.drawable.curved_powder_blue, null));
                    holder.excusedButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.excused_icon, null), null, null, null);
                    holder.excusedButton.setTextColor(context.getResources().getColor(R.color.cerulean_blue, null));
                    break;

            }
        }
        if (positionCheckStatusHashMap.containsKey(position)) {
            holder.checkAttendanceImageButton.setBackground(context.getResources().getDrawable(R.drawable.curved_solid_cerulean_blue, null));
            holder.checkAttendanceImageButton.setImageResource(R.drawable.attendance_check);
        } else {
            holder.checkAttendanceImageButton.setBackground(context.getResources().getDrawable(R.drawable.curved_cool_grey, null));
            holder.checkAttendanceImageButton.setImageResource(R.color.transparent);
        }
        holder.studentName.setText(mDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<AttendanceStudent> data, ArrayList<Attendances> attendances) {
        mDataAttendancesList.clear();
        mDataAttendancesList.addAll(attendances);
        mDataList.clear();
        mDataList.addAll(data);
        for (int i = 0; i < data.size(); i++) {
            if (!attendances.isEmpty()) {
                for (int k = 0; k < attendances.size(); k++) {
                    if (data.get(i).getChildId() == attendances.get(k).getStudentId()) {
                        switch (attendances.get(k).getStatus()) {
                            case Constants.TYPE_ABSENT:
                                positionStatusHashMap.put(i, Status.ABSENT);
                                break;
                            case Constants.TYPE_LATE:
                                positionStatusHashMap.put(i, Status.LATE);
                                break;
                            case Constants.TYPE_PRESENT:
                                positionStatusHashMap.put(i, Status.PRESENT);
                                break;
                            case Constants.TYPE_EXCUSED:
                                positionStatusHashMap.put(i, Status.EXCUSED);
                                break;
                        }
                    }
                }
                if (!positionStatusHashMap.containsKey(i)) {
                    positionStatusHashMap.put(i, Status.NOATTENDANCE);
                }
            } else {
                positionStatusHashMap.put(i, Status.NOATTENDANCE);
            }
        }


        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentName;
        IImageLoader imageLoader;
        ImageButton checkAttendanceImageButton;
        AvatarView studentImageView;
        Button presentButton, lateButton, excusedButton, absentButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageLoader = new PicassoLoader();
            studentImageView = itemView.findViewById(R.id.student_avatar);
            studentName = itemView.findViewById(R.id.student_name_tv);
            checkAttendanceImageButton = itemView.findViewById(R.id.check_attendance_btn);
            presentButton = itemView.findViewById(R.id.present_btn);
            lateButton = itemView.findViewById(R.id.late_btn);
            excusedButton = itemView.findViewById(R.id.excused_btn);
            absentButton = itemView.findViewById(R.id.absent_btn);

            checkAttendanceImageButton.setOnClickListener(this);
            presentButton.setOnClickListener(this);
            lateButton.setOnClickListener(this);
            excusedButton.setOnClickListener(this);
            absentButton.setOnClickListener(this);

            increaseButtonsHitArea();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.present_btn:
                    teacherAttendanceAdapterInterface.onPresentClicked(mDataList.get(getAdapterPosition()), positionStatusHashMap.get(getAdapterPosition()));
                    break;
                case R.id.late_btn:
                    teacherAttendanceAdapterInterface.onLateClicked(mDataList.get(getAdapterPosition()), positionStatusHashMap.get(getAdapterPosition()));
                    break;
                case R.id.excused_btn:
                    teacherAttendanceAdapterInterface.onExcusedClicked(mDataList.get(getAdapterPosition()), positionStatusHashMap.get(getAdapterPosition()));
                    break;
                case R.id.absent_btn:
                    teacherAttendanceAdapterInterface.onAbsentClicked(mDataList.get(getAdapterPosition()), positionStatusHashMap.get(getAdapterPosition()));
                    break;
                case R.id.check_attendance_btn:
                    if (positionCheckStatusHashMap.containsKey(getAdapterPosition())) {
                        positionCheckStatusHashMap.remove(getAdapterPosition());
                        if (positionCheckStatusHashMap.size() == 0) {
                            teacherAttendanceAdapterInterface.onCheckClicked(false);
                        }
                    } else {
                        positionCheckStatusHashMap.put(getAdapterPosition(), mDataList.get(getAdapterPosition()));
                        teacherAttendanceAdapterInterface.onCheckClicked(true);
                    }
                    notifyDataSetChanged();
                    break;
            }
        }

        private void clearAllStatus() {

            checkAttendanceImageButton.setBackground(context.getResources().getDrawable(R.drawable.curved_cool_grey, null));
            checkAttendanceImageButton.setImageResource(R.color.transparent);

            presentButton.setBackground(context.getResources().getDrawable(R.drawable.curved_white_four, null));
            presentButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.not_present, null), null, null, null);
            presentButton.setTextColor(context.getResources().getColor(R.color.warm_grey2, null));

            lateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_white_four, null));
            lateButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.not_late, null), null, null, null);
            lateButton.setTextColor(context.getResources().getColor(R.color.warm_grey2, null));

            excusedButton.setBackground(context.getResources().getDrawable(R.drawable.curved_white_four, null));
            excusedButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.not_excused, null), null, null, null);
            excusedButton.setTextColor(context.getResources().getColor(R.color.warm_grey2, null));

            absentButton.setBackground(context.getResources().getDrawable(R.drawable.curved_white_four, null));
            absentButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.not_absent, null), null, null, null);
            absentButton.setTextColor(context.getResources().getColor(R.color.warm_grey2, null));
        }

        private void increaseButtonsHitArea() {
            final View parent = (View) checkAttendanceImageButton.getParent();
            parent.post(new Runnable() {
                public void run() {
                    final Rect rect = new Rect();
                    checkAttendanceImageButton.getHitRect(rect);
                    rect.top -= 100;    // increase top hit area
                    rect.left -= 100;   // increase left hit area
                    rect.bottom += 100; // increase bottom hit area
                    rect.right += 100;  // increase right hit area
                    parent.setTouchDelegate(new TouchDelegate(rect, checkAttendanceImageButton));
                }
            });
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

    public enum Status {
        PRESENT, LATE, ABSENT, EXCUSED, NOATTENDANCE
    }


    public interface TeacherAttendanceAdapterInterface {

        void onAbsentClicked(AttendanceStudent attendanceStudent, Status status);

        void onPresentClicked(AttendanceStudent attendanceStudent, Status status);

        void onExcusedClicked(AttendanceStudent attendanceStudent, Status status);

        void onLateClicked(AttendanceStudent attendanceStudent, Status status);

        void onCheckClicked(Boolean isSelected);
    }
}
