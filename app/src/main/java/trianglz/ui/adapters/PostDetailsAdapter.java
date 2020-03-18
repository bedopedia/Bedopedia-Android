package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Arrays;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.utils.Util;

public class PostDetailsAdapter extends RecyclerView.Adapter {
    ArrayList<PostDetails> postDetails;
    private Context context;
    IImageLoader imageLoader;
    private PostDetailsInterface postDetailsInterface;
    private int currentLimit = 0;

    public PostDetailsAdapter(Context context, PostDetailsInterface postDetailsInterface) {
        this.context = context;
        postDetails = new ArrayList<>();
        this.postDetailsInterface = postDetailsInterface;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_detail, parent, false);
        return new PostDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PostDetailsViewHolder viewHolder = (PostDetailsViewHolder) holder;
        final PostDetails postDetail = postDetails.get(position);
        String imageUrl = postDetail.getOwner().avatarUrl;
        imageLoader = new PicassoLoader();
        setAvatarView(viewHolder.avatarView, postDetail.getOwner().nameWithTitle, imageUrl);
        viewHolder.ownerTextView.setText(postDetail.getOwner().nameWithTitle);
        String date = Util.getTimeAndDate(postDetail.getCreatedAt(), context);
        viewHolder.dateTextView.setText(date);
        if (postDetail.getContent().isEmpty() && postDetail.getUploadedFiles().length == 0) {
            viewHolder.bodyTextView.setTextColor(context.getResources().getColor(R.color.greyish_brown, null));
            viewHolder.bodyTextView.setText(context.getResources().getString(R.string.no_content));
        } else {
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
        }
        // setting the attachments buttons
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
        final ArrayList<UploadedObject> uploadedObjects = new ArrayList<>();
        uploadedObjects.addAll(Arrays.asList(postDetail.getUploadedFiles()));
        if (viewHolder.firstTextView.getVisibility() != View.GONE)
            viewHolder.firstTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!uploadedObjects.isEmpty())
                        postDetailsInterface.onAttachmentClicked(uploadedObjects);
                }
            });
        if (viewHolder.secondTextView.getVisibility() != View.GONE)
            viewHolder.secondTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!uploadedObjects.isEmpty())
                        postDetailsInterface.onAttachmentClicked(uploadedObjects);
                }
            });
        if (viewHolder.thirdTextView.getVisibility() != View.GONE)
            viewHolder.thirdTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!uploadedObjects.isEmpty())
                        postDetailsInterface.onAttachmentClicked(uploadedObjects);
                }
            });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDetailsInterface.onCardClicked(postDetail);
            }
        });
        viewHolder.bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDetailsInterface.onCardClicked(postDetail);
            }
        });
        if (currentLimit != position) {
            if (position == postDetails.size() - 4) {
                currentLimit = position;
                float size = postDetails.size();
                postDetailsInterface.loadNextPage(Math.round(size / 10) + 1);
            }
        }
    }

    private void setAvatarView(final AvatarView avatarView, final String name, String imageUrl) {
        imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(name), imageUrl);
    }

    public void addData(ArrayList<PostDetails> mPostDetails, int page) {
        if (page == 1) postDetails.clear();
        if (mPostDetails != null) postDetails.addAll(mPostDetails);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postDetails.size();
    }

    public class PostDetailsViewHolder extends RecyclerView.ViewHolder {

        AvatarView avatarView;
        TextView ownerTextView, dateTextView;
        HtmlTextView bodyTextView;
        TextView firstTextView, secondTextView, thirdTextView, firstSizeTextView, secondSizeTextView, thirdSizeTextView;
        LinearLayout buttonsLayout, firstLinearLayout, secondLinearLayout, thirdLinearLayout;
        ImageView imageView1, imageView2, imageView3;
        View view;

        PostDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_owner_image);
            ownerTextView = itemView.findViewById(R.id.tv_owner_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            firstTextView = itemView.findViewById(R.id.btn_first_attachment);
            secondTextView = itemView.findViewById(R.id.btn_second_attachment);
            thirdTextView = itemView.findViewById(R.id.btn_third_attachment);

            firstLinearLayout = itemView.findViewById(R.id.first_attachment_layout);
            secondLinearLayout = itemView.findViewById(R.id.second_attachment_layout);
            thirdLinearLayout = itemView.findViewById(R.id.third_attachment_layout);

            firstSizeTextView = itemView.findViewById(R.id.file_size_tv_1);
            secondSizeTextView = itemView.findViewById(R.id.file_size_tv_2);
            thirdSizeTextView = itemView.findViewById(R.id.file_size_tv_3);
            imageView1 = itemView.findViewById(R.id.image_1);
            imageView2 = itemView.findViewById(R.id.image_2);
            imageView3 = itemView.findViewById(R.id.image_3);


            buttonsLayout = itemView.findViewById(R.id.ll_three_buttons);

            view = itemView.findViewById(R.id.view);

        }
    }

    public interface PostDetailsInterface {
        void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects);

        void loadNextPage(int page);

        void onCardClicked(PostDetails postDetails);
    }
}
