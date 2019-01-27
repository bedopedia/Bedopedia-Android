package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import trianglz.models.Message;
import trianglz.utils.Util;


/**
 * Created by ${Aly} on 11/12/2018.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ME = 0;
    private static final int TYPE_OTHER = 1;
    private static final int TYPE_DATE = 2;
    private static final int TYPE_ME_IMAGE = 3;
    private static final int TYPE_OTHER_IMAGE = 4;

    public List<Object> mDataList;

    private Context context;
    private String userId;


    public ChatAdapter(Context context, String userId) {

        this.context = context;
        mDataList = new ArrayList<>();
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (viewType == TYPE_ME) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_me, parent, false);
            return new MeViewHolder(view);
        } else if (viewType == TYPE_OTHER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_other, parent, false);
            return new OtherViewHolder(view);
        }else if (viewType == TYPE_ME_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_me_image, parent, false);
            return new ImageMeViewHolder(view);
        }else if (viewType == TYPE_OTHER_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_other_image, parent, false);
            return new ImageOtherViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_time, parent, false);
            return new TimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(mDataList.get(position) instanceof String){
            String date = ((String )mDataList.get(position));
            TimeViewHolder timeViewHolder = ((TimeViewHolder)holder);
            timeViewHolder.timeTextView.setText(date);
        }else {
            final Message message = (Message) mDataList.get(position);
            String messageTime = setMessageTime(Util.convertUtcToLocal(message.createdAt));
            if (!message.attachmentUrl.equals("") && !message.attachmentUrl.equals("null")) {
                if (userId.equals(String.valueOf(message.user.getId()))) {
                    ImageMeViewHolder imageMeViewHolder = ((ImageMeViewHolder) holder);
                    imageMeViewHolder.messageTimeTextView.setText(messageTime);
                    Picasso.with(context)
                            .load(message.attachmentUrl)
                            .fit()
                            .centerCrop()
                            .into(imageMeViewHolder.imageView);
                    imageMeViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> images = new ArrayList<>();
                            images.add(message.attachmentUrl);
                            new ImageViewer.Builder<>(context, images)
                                    .setStartPosition(0)
                                    .hideStatusBar(false)
                                    .allowZooming(true)
                                    .allowSwipeToDismiss(true)
                                    .setBackgroundColorRes((R.color.munsell))
                                    .show();
                        }
                    });
                } else {
                    ImageOtherViewHolder imageOtherViewHolder = ((ImageOtherViewHolder) holder);
                    imageOtherViewHolder.messageTimeTextView.setText(messageTime);
                    Picasso.with(context)
                            .load(message.attachmentUrl)
                            .fit()
                            .centerCrop()
                            .into(imageOtherViewHolder.imageView);
                    imageOtherViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> images = new ArrayList<>();
                            images.add(message.attachmentUrl);
                            new ImageViewer.Builder<>(context, images)
                                    .setStartPosition(0)
                                    .hideStatusBar(false)
                                    .allowZooming(true)
                                    .allowSwipeToDismiss(true)
                                    .setBackgroundColorRes((R.color.munsell))
                                    .show();
                        }
                    });
                }
            } else {
                String body = android.text.Html.fromHtml(message.body).toString();
                body = StringEscapeUtils.unescapeJava(body);
                if (userId.equals(String.valueOf(message.user.getId()))) {
                    MeViewHolder meViewHolder = ((MeViewHolder) holder);
                    meViewHolder.bodyTextView.setText(body);
                    meViewHolder.messageTimeTextView.setText(messageTime);
                } else {
                    OtherViewHolder otherViewHolder = ((OtherViewHolder) holder);
                    otherViewHolder.bodyTextView.setText(body);
                    otherViewHolder.messageTimeTextView.setText(messageTime);
                }
            }
        }

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position) instanceof String) {
            return TYPE_DATE;
        } else {
            Message message = (Message) mDataList.get(position);
            if (userId.equals(String.valueOf(message.user.getId()))) {
                if(!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")){
                    return TYPE_ME_IMAGE;
                }else {
                    return TYPE_ME;
                }
            } else {
                if(!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")){
                    return TYPE_OTHER_IMAGE;
                }else {
                    return TYPE_OTHER;
                }
            }
        }

    }

    public void addData(ArrayList<Object> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }


    private class OtherViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyTextView;
        public TextView messageTimeTextView;
        public OtherViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            messageTimeTextView = itemView.findViewById(R.id.tv_date);
        }
    }

    private class MeViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyTextView;
        public TextView messageTimeTextView;
        public MeViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            messageTimeTextView = itemView.findViewById(R.id.tv_date);
        }
    }

    private class ImageMeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView messageTimeTextView;
        public ImageMeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
        }
    }


    private class ImageOtherViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView messageTimeTextView;

        public ImageOtherViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
        }
    }





    private class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView;

        public TimeViewHolder(View itemView) {
            super(itemView);
           timeTextView = itemView.findViewById(R.id.tv_date_header);
        }
    }


    public static String setMessageTime(Date date) {
        String dateString = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        dateString = simpleDateFormat.format(date);
        return dateString;
    }



}