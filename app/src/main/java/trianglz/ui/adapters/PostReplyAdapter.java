package trianglz.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.PostDetails;
import trianglz.models.Reply;
import trianglz.models.UploadedObject;

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
        String imageUrl = postDetail.getOwner().getAvatarUrl();
        imageLoader = new PicassoLoader();
        if (position == 0) {
            final PostReplyViewHolder viewHolder = (PostReplyViewHolder) holder;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)viewHolder.cardView.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            viewHolder.cardView.setLayoutParams(params);
            setAvatarView(viewHolder.avatarView, postDetail.getOwner().getName(), imageUrl);
            viewHolder.ownerTextview.setText(postDetail.getOwner().getName());
            DateTime dateTime = new DateTime(postDetail.getCreatedAt());
            String date = dateTime.toLocalDate().toString();
            viewHolder.dateTextView.setText(date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewHolder.bodyTextView.setText(Html.fromHtml(postDetail.getContent(), Html.FROM_HTML_MODE_COMPACT));
                viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                viewHolder.bodyTextView.setText(Html.fromHtml(postDetail.getContent()));
            }
            viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            // setting the attachments buttons
            switch (postDetail.getUploadedFiles().length) {
                case 0:
                    viewHolder.buttonsLayout.setVisibility(View.GONE);
                    viewHolder.firstButton.setVisibility(View.GONE);
                    viewHolder.secondButton.setVisibility(View.GONE);
                    viewHolder.thirdButton.setVisibility(View.GONE);
                    break;
                case 1:
                    viewHolder.firstButton.setText(postDetail.getUploadedFiles()[0].getName());
                    viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                    viewHolder.firstButton.setVisibility(View.VISIBLE);
                    viewHolder.secondButton.setVisibility(View.GONE);
                    viewHolder.thirdButton.setVisibility(View.GONE);
                    break;
                case 2:
                    viewHolder.firstButton.setText(postDetail.getUploadedFiles()[0].getName());
                    viewHolder.secondButton.setText(postDetail.getUploadedFiles()[1].getName());
                    viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                    viewHolder.firstButton.setVisibility(View.VISIBLE);
                    viewHolder.secondButton.setVisibility(View.VISIBLE);
                    viewHolder.thirdButton.setVisibility(View.GONE);
                    break;
                default:
                    viewHolder.firstButton.setText(postDetail.getUploadedFiles()[0].getName());
                    viewHolder.secondButton.setText(postDetail.getUploadedFiles()[1].getName());
                    viewHolder.thirdButton.setText("+" + (postDetail.getUploadedFiles().length - 2));
                    viewHolder.buttonsLayout.setVisibility(View.VISIBLE);
                    viewHolder.firstButton.setVisibility(View.VISIBLE);
                    viewHolder.secondButton.setVisibility(View.VISIBLE);
                    viewHolder.thirdButton.setVisibility(View.VISIBLE);
            }
            final ArrayList<UploadedObject> uploadedObjects = new ArrayList<>();
            uploadedObjects.addAll(Arrays.asList(postDetail.getUploadedFiles()));
            if (viewHolder.firstButton.getVisibility() != View.GONE)
                viewHolder.firstButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!uploadedObjects.isEmpty())
                            postReplyInterface.onAttachmentClicked(uploadedObjects);
                    }
                });
            if (viewHolder.secondButton.getVisibility() != View.GONE)
                viewHolder.secondButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!uploadedObjects.isEmpty())
                            postReplyInterface.onAttachmentClicked(uploadedObjects);
                    }
                });
            if (viewHolder.thirdButton.getVisibility() != View.GONE)
                viewHolder.thirdButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!uploadedObjects.isEmpty())
                            postReplyInterface.onAttachmentClicked(uploadedObjects);
                    }
                });
        } else if (position == 1) {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            PostReplyViewHolder viewHolder = (PostReplyViewHolder) holder;
            Reply reply = postDetail.getComments()[position - 2];
            imageUrl = reply.getOwner().getAvatarUrl();
            setAvatarView(viewHolder.avatarView, reply.getOwner().getName(), imageUrl);
            viewHolder.ownerTextview.setText(reply.getOwner().getName());
            DateTime dateTime = new DateTime(reply.getCreatedAt());
            String date = dateTime.toLocalDate().toString();
            viewHolder.dateTextView.setText(date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewHolder.bodyTextView.setText(Html.fromHtml(reply.getContent(), Html.FROM_HTML_MODE_COMPACT));
                viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                viewHolder.bodyTextView.setText(Html.fromHtml(reply.getContent()));
            }
            viewHolder.bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            // setting the attachments buttons
            viewHolder.buttonsLayout.setVisibility(View.GONE);
        }
    }

    private void setAvatarView(final AvatarView avatarView,final String name, String imageUrl) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(context)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(avatarView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    public void addData(PostDetails postDetail) {
        this.postDetail = postDetail;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return TYPE_REPLY;
        } else {
            return TYPE_POST;
        }

    }

    @Override
    public int getItemCount() {
        return postDetail.getComments().length + 2;
    }

    public class PostReplyViewHolder extends RecyclerView.ViewHolder {

        public AvatarView avatarView;
        public TextView ownerTextview, dateTextView, bodyTextView;
        public Button firstButton, secondButton, thirdButton;
        public LinearLayout buttonsLayout;
        public CardView cardView;
        public PostReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_owner_image);
            ownerTextview = itemView.findViewById(R.id.tv_owner_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            firstButton = itemView.findViewById(R.id.btn_first_attachment);
            secondButton = itemView.findViewById(R.id.btn_second_attachment);
            thirdButton = itemView.findViewById(R.id.btn_third_attachment);
            buttonsLayout = itemView.findViewById(R.id.ll_three_buttons);
            cardView = itemView.findViewById(R.id.root);

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
    }

}
