package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.PostsResponse;

public class PostsAdapter extends RecyclerView.Adapter {
    private Context context;
    ArrayList<PostsResponse> postsResponses;
    private PostsInterface postsInterface;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_posts, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostsViewHolder viewHolder = (PostsViewHolder) holder;
        final PostsResponse postsResponse = postsResponses.get(position);
        viewHolder.subjectTextView.setText(postsResponse.getCourseName());
        viewHolder.nameTextview.setText(postsResponse.getPosts().getOwner().getName());
        viewHolder.counterTextView.setText(String.valueOf(postsResponse.getPostsCount()));
        IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(viewHolder.subjectImageView, new AvatarPlaceholderModified(postsResponse.getCourseName()), "Path of Image");
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postsInterface.onCourseClicked(postsResponse.getCourseId(), postsResponse.getCourseName());
                }
            });

    }

    public PostsAdapter(Context context, PostsInterface postsInterface) {
        postsResponses = new ArrayList<>();
        this.context = context;
        this.postsInterface = postsInterface;
    }
    @Override
    public int getItemCount() {
        return postsResponses.size();
    }
    public void addData(ArrayList<PostsResponse> postsResponses) {
        postsResponses.clear();
        postsResponses.addAll(postsResponses);
        notifyDataSetChanged();
    }


    public class PostsViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rootView;
        public AvatarView subjectImageView;
        public TextView subjectTextView;
        public TextView counterTextView;
        public ImageButton openBtn;
        public TextView nameTextview;
        public TextView descriptionTextView;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImageView = itemView.findViewById(R.id.iv_post_img);
            subjectTextView = itemView.findViewById(R.id.tv_subject);
            counterTextView = itemView.findViewById(R.id.tv_number);
            openBtn = itemView.findViewById(R.id.btn_open);
            nameTextview = itemView.findViewById(R.id.tv_name);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            rootView = itemView.findViewById(R.id.ll_root);
        }
    }

    public interface PostsInterface {
        void onCourseClicked(int courseId, String courseName);
    }
}
