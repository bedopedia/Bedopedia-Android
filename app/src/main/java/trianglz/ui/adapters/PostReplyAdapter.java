package trianglz.ui.adapters;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Arrays;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.managers.SessionManager;
import trianglz.models.PostDetails;
import trianglz.models.Reply;
import trianglz.models.UploadedObject;
import trianglz.utils.Util;

public class PostReplyAdapter extends RecyclerView.Adapter {

    PostDetails postDetail;
    private Context context;
    IImageLoader imageLoader;
    private PostReplyInterface postReplyInterface;
    private final int TYPE_POST = 0;
    private final int TYPE_REPLY = 1;

    public PostReplyAdapter(Context context, PostReplyInterface postReplyInterface) {
        this.context = context;
        postDetail = new PostDetails();
        this.postReplyInterface = postReplyInterface;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_POST) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_detail, parent, false);
            return new PostReplyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post_reply, parent, false);
            return new ReplyViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String imageUrl = postDetail.getOwner().avatarUrl;
        imageLoader = new PicassoLoader();
        if (position == 0) {
            final PostReplyViewHolder viewHolder = (PostReplyViewHolder) holder;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.cardView.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            viewHolder.cardView.setLayoutParams(params);
            String nameWithTitle = postDetail.getOwner().nameWithTitle;
            if (nameWithTitle != null && !nameWithTitle.isEmpty()) {
                viewHolder.ownerTextview.setText(nameWithTitle);
            }
            if (imageUrl != null && !imageUrl.isEmpty()) {
                setAvatarView(viewHolder.avatarView, nameWithTitle, imageUrl);
            }
            String date = Util.getTimeAndDate(postDetail.getCreatedAt(), context);
            viewHolder.dateTextView.setText(date);

            if (!postDetail.getContent().isEmpty()) {
                viewHolder.bodyTextView.setVisibility(View.VISIBLE);
                viewHolder.bodyTextView.setTextColor(context.getResources().getColor(R.color.black, null));
                viewHolder.bodyTextView.setHtml(postDetail.getContent(),
                        new HtmlHttpImageGetter(viewHolder.bodyTextView));
                Linkify.addLinks(viewHolder.bodyTextView, Linkify.WEB_URLS);
                viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                viewHolder.bodyTextView.setVisibility(View.GONE);
            }
//            viewHolder.bodyTextView.setHtml(postDetail.getContent(),
//                    new HtmlHttpImageGetter(viewHolder.bodyTextView));
//            Linkify.addLinks(viewHolder.bodyTextView, Linkify.WEB_URLS);
//            viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            // setting the attachments buttons
            final ArrayList<UploadedObject> uploadedObjects = new ArrayList<>();
            uploadedObjects.addAll(Arrays.asList(postDetail.getUploadedFiles() != null ? postDetail.getUploadedFiles() : new UploadedObject[0]));
            if (postDetail.getUploadedFiles() != null) {
                switch (postDetail.getUploadedFiles().length) {
                    case 0:
                        viewHolder.view.setVisibility(View.GONE);
                        viewHolder.buttonsLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewHolder.view.setVisibility(View.VISIBLE);
                        viewHolder.firstTextView.setText(postDetail.getUploadedFiles()[0].getName());
                        viewHolder.firstSizeTextView.setText(Util.humanReadableByteCountBin(postDetail.getUploadedFiles()[0].getFileSize()));
                        viewHolder.view.setVisibility(View.VISIBLE);
                        viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                        viewHolder.firstLinearLayout.setVisibility(View.VISIBLE);

                        viewHolder.secondLinearLayout.setVisibility(View.GONE);
                        viewHolder.thirdLinearLayout.setVisibility(View.GONE);

                        viewHolder.imageView2.setVisibility(View.GONE);
                        viewHolder.imageView3.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewHolder.view.setVisibility(View.VISIBLE);

                        viewHolder.firstTextView.setText(postDetail.getUploadedFiles()[0].getName());
                        viewHolder.firstSizeTextView.setText(Util.humanReadableByteCountBin(postDetail.getUploadedFiles()[0].getFileSize()));
                        viewHolder.secondTextView.setText(postDetail.getUploadedFiles()[1].getName());
                        viewHolder.secondSizeTextView.setText(Util.humanReadableByteCountBin(postDetail.getUploadedFiles()[1].getFileSize()));

                        viewHolder.buttonsLayout.setVisibility(View.VISIBLE);

                        viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                        viewHolder.firstLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.secondLinearLayout.setVisibility(View.VISIBLE);

                        viewHolder.thirdLinearLayout.setVisibility(View.GONE);
                        viewHolder.imageView2.setVisibility(View.VISIBLE);
                        viewHolder.imageView3.setVisibility(View.GONE);
                        break;
                    default:
                        viewHolder.view.setVisibility(View.VISIBLE);
                        viewHolder.firstTextView.setText(postDetail.getUploadedFiles()[0].getName());
                        viewHolder.secondTextView.setText(postDetail.getUploadedFiles()[1].getName());
                        viewHolder.thirdTextView.setText("+" + (postDetail.getUploadedFiles().length - 2));

                        viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                        viewHolder.firstLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.secondLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.thirdLinearLayout.setVisibility(View.VISIBLE);
                        viewHolder.imageView3.setVisibility(View.VISIBLE);
                        viewHolder.thirdSizeTextView.setVisibility(View.VISIBLE);
                }
            } else  {
                viewHolder.buttonsLayout.setVisibility(View.GONE);
                viewHolder.view.setVisibility(View.GONE);
            }
            if (viewHolder.buttonsLayout.getVisibility() != View.GONE)
                viewHolder.buttonsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!uploadedObjects.isEmpty())
                            postReplyInterface.onAttachmentClicked(uploadedObjects);
                    }
                });
        } else if (position == 1) {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
                replyViewHolder.itemView.setVisibility(View.GONE);
            }
            if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
                replyViewHolder.itemView.setVisibility(View.VISIBLE);
            }
            replyViewHolder.replyBtn.setTextColor(context.getResources().getColor(Util.checkUserColor(),null));
            replyViewHolder.replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postReplyInterface.onReplyClicked();
                }
            });
        } else {
            PostReplyViewHolder viewHolder = (PostReplyViewHolder) holder;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.cardView.getLayoutParams();
            params.setMargins(0, 0, 0, (int) Util.convertDpToPixel(16, context)); //substitute parameters for left, top, right, bottom
            Reply reply = postDetail.getComments()[position - 2];
            imageUrl = reply.getOwner().avatarUrl;
            setAvatarView(viewHolder.avatarView, reply.getOwner().nameWithTitle, imageUrl);
            viewHolder.ownerTextview.setText(reply.getOwner().nameWithTitle);
            String date = Util.getTimeAndDate(reply.getCreatedAt(), context);
            viewHolder.dateTextView.setText(date);
            viewHolder.view.setVisibility(View.GONE);
            viewHolder.buttonsLayout.setVisibility(View.GONE);

            if (reply.getContent().isEmpty()) {
                viewHolder.bodyTextView.setTextColor(context.getResources().getColor(R.color.greyish_brown, null));
                viewHolder.bodyTextView.setText(context.getResources().getString(R.string.no_content));
            } else {
                if (!reply.getContent().isEmpty()) {
                    viewHolder.bodyTextView.setVisibility(View.VISIBLE);
                    viewHolder.bodyTextView.setTextColor(context.getResources().getColor(R.color.black, null));
                    viewHolder.bodyTextView.setHtml(reply.getContent(),
                            new HtmlHttpImageGetter(viewHolder.bodyTextView));
                    Linkify.addLinks(viewHolder.bodyTextView, Linkify.WEB_URLS);
                    viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    viewHolder.bodyTextView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setAvatarView(final AvatarView avatarView, final String name, String imageUrl) {
        imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), imageUrl);
    }

    public void addData(PostDetails postDetail) {
        this.postDetail = postDetail;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1 && postDetail.wasAnnouncement) {
            return TYPE_REPLY;
        } else {
            return TYPE_POST;
        }

    }

    @Override
    public int getItemCount() {
        return postDetail.wasAnnouncement ? postDetail.getComments().length + 1 : postDetail.getComments().length + 2;
    }

    public class PostReplyViewHolder extends RecyclerView.ViewHolder {

        public AvatarView avatarView;
        public TextView ownerTextview, dateTextView;
        public HtmlTextView bodyTextView;
        public TextView firstTextView, secondTextView, thirdTextView;
        public LinearLayout buttonsLayout;
        public CardView cardView;
        TextView firstSizeTextView, secondSizeTextView, thirdSizeTextView;
        LinearLayout firstLinearLayout, secondLinearLayout, thirdLinearLayout;
        ImageView imageView1, imageView2, imageView3;
        View view;

        public PostReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_owner_image);
            ownerTextview = itemView.findViewById(R.id.tv_owner_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            firstTextView = itemView.findViewById(R.id.btn_first_attachment);
            secondTextView = itemView.findViewById(R.id.btn_second_attachment);
            thirdTextView = itemView.findViewById(R.id.btn_third_attachment);
            buttonsLayout = itemView.findViewById(R.id.ll_three_buttons);
            cardView = itemView.findViewById(R.id.root);


            firstLinearLayout = itemView.findViewById(R.id.first_attachment_layout);
            secondLinearLayout = itemView.findViewById(R.id.second_attachment_layout);
            thirdLinearLayout = itemView.findViewById(R.id.third_attachment_layout);

            firstSizeTextView = itemView.findViewById(R.id.file_size_tv_1);
            secondSizeTextView = itemView.findViewById(R.id.file_size_tv_2);
            thirdSizeTextView = itemView.findViewById(R.id.file_size_tv_3);
            imageView1 = itemView.findViewById(R.id.image_1);
            imageView2 = itemView.findViewById(R.id.image_2);
            imageView3 = itemView.findViewById(R.id.image_3);
            view = itemView.findViewById(R.id.view);

        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        public Button replyBtn;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            replyBtn = itemView.findViewById(R.id.btn_reply);
        }
    }

    public interface PostReplyInterface {
        void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects);

        void onReplyClicked();
    }

}
