package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Tools.CalendarUtils;
import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.MessageThread;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class ContactTeacherAdapter extends RecyclerView.Adapter<ContactTeacherAdapter.Holder> {
    public Context context;
    public ArrayList<MessageThread> mDataList;
    ContactTeacherAdapterInterface contactTeacherAdapterInterface;
    private IImageLoader imageLoader;


    public ContactTeacherAdapter(Context context, ContactTeacherAdapterInterface contactTeacherAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.contactTeacherAdapterInterface = contactTeacherAdapterInterface;
        imageLoader = new PicassoLoader();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_teacher, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        MessageThread messageThread = mDataList.get(position);
        holder.teacherName.setText(messageThread.otherNames);
        holder.subjectTextView.setText(messageThread.courseName);
        holder.dateBtn.setText(getDate(messageThread.lastAddedDate));
        setTeacherImage(messageThread.otherAvatars,messageThread.otherNames,holder);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactTeacherAdapterInterface.onThreadClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData( ArrayList<MessageThread> messageThreadArrayList) {
        this.mDataList.clear();
        this.mDataList.addAll(messageThreadArrayList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView teacherName, subjectTextView;
        public AvatarView teacherImageView;
        public LinearLayout itemLayout;
        public Button dateBtn;

        public Holder(View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.tv_teacher_name);
            subjectTextView = itemView.findViewById(R.id.tv_subject_name);
            teacherImageView = itemView.findViewById(R.id.img_teacher);
            itemLayout = itemView.findViewById(R.id.item_layout);
            dateBtn = itemView.findViewById(R.id.btn_date);
        }
    }

    public interface ContactTeacherAdapterInterface{
        void onThreadClicked(int position);
    }


    private String getDate(String messageTime){
        String finalData = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date date = null;
        try {
            date = fmt.parse(messageTime);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            String [] dates = fmtOut.format(date).split(" ");
            Calendar cal = CalendarUtils.getCalendar(Calendar.getInstance().getTime());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            String notificationDate = fmtOut.format(date);
            if (dates[0].equals(String.valueOf(day))) {
                notificationDate = "Today" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            } else if (dates[0].equals(String.valueOf(day  - 1))) {
                notificationDate = "Yesterday" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            }
           finalData = notificationDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalData;
    }

    private void setTeacherImage(String imageUrl, final String name, final Holder holder) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(holder.teacherImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(context)
                    .load(imageUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.teacherImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(holder.teacherImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

}