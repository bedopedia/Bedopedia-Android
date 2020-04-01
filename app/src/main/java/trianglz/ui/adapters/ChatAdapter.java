package trianglz.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import trianglz.components.MimeTypeInterface;
import trianglz.models.Message;
import trianglz.ui.activities.ChatActivity;
import trianglz.utils.Util;


/**
 * Created by ${Aly} on 11/12/2018.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MimeTypeInterface {

    private static final int TYPE_ME = 0;
    private static final int TYPE_OTHER = 1;
    private static final int TYPE_DATE = 2;
    private static final int TYPE_ME_IMAGE = 3;
    private static final int TYPE_OTHER_IMAGE = 4;
    private static final int TYPE_ME_ATTACHMENT = 5;
    private static final int TYPE_OTHER_ATTACHMENT = 6;

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
        } else if (viewType == TYPE_ME_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_me_image, parent, false);
            return new ImageMeViewHolder(view);
        } else if (viewType == TYPE_OTHER_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_other_image, parent, false);
            return new ImageOtherViewHolder(view);
        } else if (viewType == TYPE_ME_ATTACHMENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_me_attachment, parent, false);
            return new AttachmentMeViewHolder(view);
        } else if (viewType == TYPE_OTHER_ATTACHMENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_other_attachment, parent, false);
            return new AttachmentOtherViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_time, parent, false);
            return new TimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (mDataList.get(position) instanceof String) {
            String date = ((String) mDataList.get(position));
            TimeViewHolder timeViewHolder = ((TimeViewHolder) holder);
            timeViewHolder.timeTextView.setText(date);
        } else {
            final Message message = (Message) mDataList.get(position);
            String messageTime = setMessageTime(Util.convertUtcToLocal(message.createdAt));
            switch (getItemViewType(position)) {
                case TYPE_ME:
                    String body = android.text.Html.fromHtml(message.body).toString();
                    body = StringEscapeUtils.unescapeJava(body);
                    MeViewHolder meViewHolder = ((MeViewHolder) holder);
                    meViewHolder.bodyTextView.setText(body);
                    meViewHolder.messageTimeTextView.setText(messageTime);
                    break;
                case TYPE_OTHER:
                    String body1 = android.text.Html.fromHtml(message.body).toString();
                    body1 = StringEscapeUtils.unescapeJava(body1);
                    OtherViewHolder otherViewHolder = ((OtherViewHolder) holder);
                    otherViewHolder.bodyTextView.setText(body1);
                    otherViewHolder.messageTimeTextView.setText(messageTime);
                    break;
                case TYPE_ME_IMAGE:
                    ImageMeViewHolder imageMeViewHolder = ((ImageMeViewHolder) holder);
                    imageMeViewHolder.messageTimeTextView.setText(messageTime);
                    Transformation transformation = new RoundedTransformationBuilder()
                            .cornerRadiusDp((int) Util.convertDpToPixel(5, context))
                            .oval(false)
                            .build();
                    Picasso.with(context)
                            .load(message.attachmentUrl)
                            .fit()
                            .transform(transformation)
                            .centerCrop()
                            .into(imageMeViewHolder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Util.isImageUrl(message, holder.getAdapterPosition(), ChatAdapter.this);
                                }
                            });
                    imageMeViewHolder.imageView.setOnClickListener(view -> {
                        ArrayList<String> images = new ArrayList<>();
                        images.add(message.attachmentUrl);
                        new ImageViewer.Builder<>(context, images)
                                .setStartPosition(0)
                                .hideStatusBar(false)
                                .allowZooming(true)
                                .allowSwipeToDismiss(true)
                                .setBackgroundColorRes((R.color.munsell))
                                .show();
                    });
                    break;
                case TYPE_OTHER_IMAGE:
                    ImageOtherViewHolder imageOtherViewHolder = ((ImageOtherViewHolder) holder);
                    imageOtherViewHolder.messageTimeTextView.setText(messageTime);
                    Transformation transformations = new RoundedTransformationBuilder()
                            .cornerRadiusDp((int) Util.convertDpToPixel(5, context))
                            .oval(false)
                            .build();
                    Picasso.with(context)
                            .load(message.attachmentUrl)
                            .transform(transformations)
                            .fit()
                            .centerCrop()
                            .into(imageOtherViewHolder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Util.isImageUrl(message, holder.getAdapterPosition(), ChatAdapter.this);
                                }
                            });
                    imageOtherViewHolder.imageView.setOnClickListener(view -> {
                        ArrayList<String> images = new ArrayList<>();
                        images.add(message.attachmentUrl);
                        new ImageViewer.Builder<>(context, images)
                                .setStartPosition(0)
                                .hideStatusBar(false)
                                .allowZooming(true)
                                .allowSwipeToDismiss(true)
                                .setBackgroundColorRes((R.color.munsell))
                                .show();
                    });
                    break;
                case TYPE_ME_ATTACHMENT:
                    AttachmentMeViewHolder attachmentMeViewHolder = (AttachmentMeViewHolder) (holder);
                    attachmentMeViewHolder.messageTimeTextView.setText(messageTime);
                    attachmentMeViewHolder.progressBar.setVisibility(View.GONE);
                    setAttachmentImage(attachmentMeViewHolder.imageView, message.ext);
                    attachmentMeViewHolder.imageView.setOnClickListener(view -> openAttachment(message.attachmentUrl));
                    break;
                case TYPE_OTHER_ATTACHMENT:
                    AttachmentOtherViewHolder attachmentOtherViewHolder = (AttachmentOtherViewHolder) (holder);
                    attachmentOtherViewHolder.messageTimeTextView.setText(messageTime);
                    setAttachmentImage(attachmentOtherViewHolder.imageView, message.ext);
                    attachmentOtherViewHolder.progressBar.setVisibility(View.GONE);
                    attachmentOtherViewHolder.imageView.setOnClickListener(view -> openAttachment(message.attachmentUrl));
                    break;

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
            if (userId.equals(String.valueOf(message.user.id))) {
                if (!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")) {
                    if (message.isImage) {
                        return TYPE_ME_IMAGE;
                    } else {
                        return TYPE_ME_ATTACHMENT;
                    }
                } else {
                    return TYPE_ME;
                }
            } else {
                if (!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")) {
                    if (message.isImage) {
                        return TYPE_OTHER_IMAGE;
                    } else {
                        return TYPE_OTHER_ATTACHMENT;
                    }
                } else {
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

    @Override
    public void onCheckType(final Message message, final int position) {
        ((ChatActivity) context).runOnUiThread(() -> {
            if (position < mDataList.size() && !message.isImage) {
                mDataList.set(position, message);
                notifyItemChanged(position);
            }
        });
    }


    private class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView bodyTextView;
        TextView messageTimeTextView;

        OtherViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            messageTimeTextView = itemView.findViewById(R.id.tv_date);
        }
    }

    private class MeViewHolder extends RecyclerView.ViewHolder {
        TextView bodyTextView;
        TextView messageTimeTextView;

        MeViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            messageTimeTextView = itemView.findViewById(R.id.tv_date);
        }
    }

    private class ImageMeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView messageTimeTextView;

        ImageMeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
        }
    }


    private class ImageOtherViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView messageTimeTextView;

        ImageOtherViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
        }
    }


    private class AttachmentMeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView messageTimeTextView;
        ProgressBar progressBar;

        AttachmentMeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
            progressBar = itemView.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }


    private class AttachmentOtherViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView messageTimeTextView;
        ProgressBar progressBar;

        AttachmentOtherViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            messageTimeTextView = itemView.findViewById(R.id.textview_time);
            progressBar = itemView.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }


    private class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        TimeViewHolder(View itemView) {
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

    private void setAttachmentImage(ImageView attachmentImageView, String ext) {
        if (ext.contains("pdf")) {
            attachmentImageView.setImageResource((R.drawable.pdf_icon));
        } else if (ext.contains("doc") || ext.contains("rtf")) {
            attachmentImageView.setImageResource((R.drawable.doc_icon));
        } else if (ext.contains("pp")) {
            attachmentImageView.setImageResource((R.drawable.ppt_icon));
        } else if (ext.contains("xl")) {
            attachmentImageView.setImageResource((R.drawable.xlsx_icon));
        } else if (ext.contains("rar") || ext.contains("zip")) {
            attachmentImageView.setImageResource((R.drawable.zip_icon));
        } else if (ext.contains("mp3") || ext.contains("wav")) {
            attachmentImageView.setImageResource((R.drawable.audio_icon));
        } else if (ext.contains("mp4") || ext.contains("3gp")) {
            attachmentImageView.setImageResource((R.drawable.video_icon));
        } else {
            attachmentImageView.setImageResource((R.drawable.file_icon));
        }

    }


    private void openAttachment(String attachmentUrl) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(attachmentUrl));
        context.startActivity(i);
    }


}