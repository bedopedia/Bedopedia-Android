package trianglz.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
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
import trianglz.core.views.PostDetailsView;
import trianglz.models.PostDetails;
import trianglz.models.PostsResponse;
import trianglz.models.UploadedObject;

public class PostDetailsAdapter extends RecyclerView.Adapter {
    ArrayList<PostDetails> postDetails;
    private Context context;
    IImageLoader imageLoader;
    private PostDetailsInterface postDetailsInterface;

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
        String imageUrl = postDetail.getOwner().getAvatarUrl();
        imageLoader = new PicassoLoader();
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
        if (viewHolder.firstButton.getVisibility() != View.GONE)  viewHolder.firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!uploadedObjects.isEmpty()) postDetailsInterface.onAttachmentClicked(uploadedObjects);
            }
        });
        if (viewHolder.secondButton.getVisibility() != View.GONE)  viewHolder.secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!uploadedObjects.isEmpty()) postDetailsInterface.onAttachmentClicked(uploadedObjects);
            }
        });
        if (viewHolder.thirdButton.getVisibility() != View.GONE) viewHolder.thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!uploadedObjects.isEmpty()) postDetailsInterface.onAttachmentClicked(uploadedObjects);
            }
        });
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

    public void addData(ArrayList<PostDetails> mPostDetails) {
        postDetails.clear();
        postDetails.addAll(mPostDetails);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postDetails.size();
    }

    public class PostDetailsViewHolder extends RecyclerView.ViewHolder {

        public AvatarView avatarView;
        public TextView ownerTextview, dateTextView, bodyTextView;
        public Button firstButton, secondButton, thirdButton;
        public LinearLayout buttonsLayout;
        public PostDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_owner_image);
            ownerTextview = itemView.findViewById(R.id.tv_owner_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            firstButton = itemView.findViewById(R.id.btn_first_attachment);
            secondButton = itemView.findViewById(R.id.btn_second_attachment);
            thirdButton = itemView.findViewById(R.id.btn_third_attachment);
            buttonsLayout = itemView.findViewById(R.id.ll_three_buttons);

        }
    }

    public interface PostDetailsInterface {
        void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects);
    }
}
