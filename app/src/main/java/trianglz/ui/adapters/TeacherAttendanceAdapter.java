package trianglz.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import trianglz.models.AttendanceTimetableSlot;
import trianglz.models.Attendances;
import trianglz.utils.Constants;


/**
 * Created by Farah A. Moniem on 01/09/2019.
 */
public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder> {
    public Context context;
    public AttendanceTimetableSlot attendanceTimetableSlot = null;
    public TeacherAttendanceAdapterInterface teacherAttendanceAdapterInterface;
    public HashMap<Integer, Attendances> positionStatusHashMap;
    public HashMap<Integer, AttendanceStudent> positionCheckStatusHashMap;
    private ArrayList<AttendanceStudent> mDataList;
    private ArrayList<Attendances> mDataAttendancesList;


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
        if (positionStatusHashMap.containsKey(mDataList.get(position).getChildId())) {
            Attendances attendances = positionStatusHashMap.get(mDataList.get(position).getChildId());
            switch (attendances.getStatus()) {
                case Constants.TYPE_PRESENT:
                    setButtonDrawble(holder.presentButton, R.drawable.curved_light_sage, R.drawable.present_icon, R.color.boring_green);
                    break;
                case Constants.TYPE_LATE:
                    setButtonDrawble(holder.lateButton, R.drawable.curved_light_tan_two, R.drawable.late_icon, R.color.electric_violet);
                    break;
                case Constants.TYPE_ABSENT:
                    setButtonDrawble(holder.absentButton, R.drawable.curved_very_light_pink, R.drawable.absent_icon, R.color.salmon);
                    break;
                case Constants.TYPE_EXCUSED:
                    setButtonDrawble(holder.excusedButton, R.drawable.curved_powder_blue, R.drawable.excused_icon, R.color.cerulean_blue);
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
        positionStatusHashMap.clear();
        for (int i = 0; i < data.size(); i++) {
            if (!attendances.isEmpty()) {
                for (int k = 0; k < attendances.size(); k++) {
                    if (attendanceTimetableSlot == null) {
                        if (data.get(i).getChildId() == attendances.get(k).getStudentId()) {
                            positionStatusHashMap.put(data.get(i).getChildId(), attendances.get(k));
                        }
                    } else {
                        if (attendanceTimetableSlot.getId() == attendances.get(k).getTimetableSlotId()) {
                            if (data.get(i).getChildId() == attendances.get(k).getStudentId()) {
                                positionStatusHashMap.put(data.get(i).getChildId(), attendances.get(k));
                            }
                        }
                    }
                }
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
                    teacherAttendanceAdapterInterface.onStatusClicked(mDataList.get(getAdapterPosition()).getChildId(), Constants.TYPE_PRESENT, "");
                    break;
                case R.id.late_btn:
                    teacherAttendanceAdapterInterface.onStatusClicked(mDataList.get(getAdapterPosition()).getChildId(), Constants.TYPE_LATE, "");
                    break;
                case R.id.excused_btn:
                    Attendances attendances = positionStatusHashMap.get(mDataList.get(getAdapterPosition()).getChildId());
                    String excusedString = "";
                    if (attendances != null)
                    excusedString = attendances.getComment();
                    teacherAttendanceAdapterInterface.onStatusClicked(mDataList.get(getAdapterPosition()).getChildId(), Constants.TYPE_EXCUSED,
                            (excusedString != null) ? excusedString : "");
                    break;
                case R.id.absent_btn:
                    teacherAttendanceAdapterInterface.onStatusClicked(mDataList.get(getAdapterPosition()).getChildId(), Constants.TYPE_ABSENT, "");
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

            setButtonDrawble(presentButton, R.drawable.curved_white_four, R.drawable.not_present, R.color.warm_grey2);
            setButtonDrawble(lateButton, R.drawable.curved_white_four, R.drawable.not_late, R.color.warm_grey2);
            setButtonDrawble(excusedButton, R.drawable.curved_white_four, R.drawable.not_excused, R.color.warm_grey2);
            setButtonDrawble(absentButton, R.drawable.curved_white_four, R.drawable.not_absent, R.color.warm_grey2);
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

    private void setButtonDrawble(Button button, int background, int compoundDrawable, int color) {
        button.setBackground(context.getResources().getDrawable(background, null));
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(compoundDrawable, null), null, null, null);
        button.setTextColor(context.getResources().getColor(color, null));
    }

    public interface TeacherAttendanceAdapterInterface {

        void onStatusClicked(int studentId, String status, String excusedString);

        void onCheckClicked(Boolean isSelected);
    }
}
