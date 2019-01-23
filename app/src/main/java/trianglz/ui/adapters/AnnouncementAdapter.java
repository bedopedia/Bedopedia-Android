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
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import trianglz.components.RoundCornersTransformation;
import trianglz.models.Announcement;
import trianglz.utils.Util;

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
        holder.dateBtn.setText(announcement.createdAt);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcementAdapterInterface.onAnnouncementSelected(mDataList.get(holder.getAdapterPosition()));
            }
        });
        if(announcement.imageUrl !=null && !announcement.imageUrl.isEmpty() && !announcement.imageUrl.equals("null")){
           holder.announcementImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(announcement.imageUrl)
                    .transform(new RoundCornersTransformation((int) Util.convertDpToPixel(5,context),0))
                    .fit()
                    .into(holder.announcementImage);
        }else {
            holder.announcementImage.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<Announcement> announcementList,boolean newIncomingNotificationData) {
        newData = newIncomingNotificationData;
        this.mDataList.clear();
        this.mDataList.addAll(announcementList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public ImageView announcementImage;
        public TextView announcementHeaderTextView,contentTextView;
        public Button dateBtn;
        public LinearLayout itemLayout;

        public Holder(View itemView) {
            super(itemView);
            announcementImage = itemView.findViewById(R.id.img_annoucement);
            announcementImage.setVisibility(View.GONE);
            announcementHeaderTextView = itemView.findViewById(R.id.tv_header);
            dateBtn = itemView.findViewById(R.id.btn_date);
            contentTextView = itemView.findViewById(R.id.tv_content);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public interface AnnouncementAdapterInterface{
        void onAnnouncementSelected(Announcement announcement);
        void onReachPosition();
    }


}