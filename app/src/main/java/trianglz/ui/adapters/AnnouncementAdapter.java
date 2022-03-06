package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.Announcement;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.Holder> {
    public Context context;
    public List<Announcement> mDataList;
    public AnnouncementAdapterInterface announcementAdapterInterface;
    private boolean newData;


    public AnnouncementAdapter(Context context, AnnouncementAdapterInterface anInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.announcementAdapterInterface = anInterface;
    }

    @Override
    public AnnouncementAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_annoucement, parent, false);
        return new AnnouncementAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final AnnouncementAdapter.Holder holder, final int position) {
        if (position == mDataList.size() - 2 && newData) {
            announcementAdapterInterface.onReachPosition();
        }
        Announcement announcement = mDataList.get(holder.getAdapterPosition());
        String body = android.text.Html.fromHtml(announcement.body).toString();
        body = StringEscapeUtils.unescapeJava(body);
        holder.contentTextView.setText(body);
        holder.announcementHeaderTextView.setText(announcement.title);
        holder.dateTextView.setText(announcement.endAt);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementAdapterInterface.onAnnouncementSelected(mDataList.get(holder.getAdapterPosition()));
            }
        });
        holder.imageLoader.loadImage(holder.announcementImage, new AvatarPlaceholderModified(announcement.title), "Path of Image");
    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<Announcement> announcementList,boolean newIncomingNotificationData) {
        newData = newIncomingNotificationData;
        if(newData){
            this.mDataList.addAll(announcementList);
            notifyDataSetChanged();
        }

    }

    public static class Holder extends RecyclerView.ViewHolder {

        public AvatarView announcementImage;
        public TextView announcementHeaderTextView,contentTextView;
        public TextView dateTextView ;
        public LinearLayout itemLayout;
        public IImageLoader imageLoader;

        public Holder(View itemView) {
            super(itemView);
            imageLoader =  new PicassoLoader();
            announcementImage = itemView.findViewById(R.id.img_annoucement);
            announcementHeaderTextView = itemView.findViewById(R.id.tv_header);
            dateTextView = itemView.findViewById(R.id.tv_date);
            contentTextView = itemView.findViewById(R.id.tv_content);
            contentTextView.setVisibility(View.GONE);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public interface AnnouncementAdapterInterface{
        void onAnnouncementSelected(Announcement announcement);
        void onReachPosition();
    }


}