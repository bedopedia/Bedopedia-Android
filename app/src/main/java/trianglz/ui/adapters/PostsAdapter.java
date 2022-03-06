package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_assignment, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostsViewHolder viewHolder = (PostsViewHolder) holder;
        final PostsResponse postsResponse = postsResponses.get(position);
        viewHolder.subjectTextView.setText(postsResponse.getCourseName());
        if (postsResponse.getPosts() != null) {
            if (postsResponse.getPosts().getOwner() != null) {
                viewHolder.nameTextview.setVisibility(View.VISIBLE);
                viewHolder.nameTextview.setText(postsResponse.getPosts().getOwner().nameWithTitle);
            } else {
                viewHolder.nameTextview.setVisibility(View.GONE);
            }
            viewHolder.counterTextView.setVisibility(View.VISIBLE);
            viewHolder.counterTextView.setText(String.valueOf(postsResponse.getPostsCount()));

            // sets the text view is both cases whether the back end response contains html or not
            if (postsResponse.getPosts().getContent() != null && !postsResponse.getPosts().getContent().isEmpty())
                viewHolder.descriptionTextView.setVisibility(View.VISIBLE);
            viewHolder.descriptionTextView.setHtml(postsResponse.getPosts().getContent(),
                    new HtmlHttpImageGetter(viewHolder.descriptionTextView));
        } else {
            viewHolder.nameTextview.setText(context.getResources().getString(R.string.no_posts));
            viewHolder.counterTextView.setVisibility(View.GONE);
            viewHolder.descriptionTextView.setVisibility(View.GONE);
        }
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(viewHolder.subjectImageView, new AvatarPlaceholderModified(postsResponse.getCourseName()), "Path of Image");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsInterface.onCourseClicked(postsResponse.getId(), postsResponse.getCourseName());
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

    public void addData(ArrayList<PostsResponse> mpostsResponses) {
        postsResponses.clear();
        if (mpostsResponses != null) postsResponses.addAll(mpostsResponses);
        notifyDataSetChanged();
    }


    public class PostsViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rootView;
        private AvatarView subjectImageView;
        private TextView subjectTextView;
        private TextView counterTextView;
        private TextView nameTextview;
        private HtmlTextView descriptionTextView;
        private ImageView clockImageView;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImageView = itemView.findViewById(R.id.img_course);
            subjectTextView = itemView.findViewById(R.id.tv_course_name);
            counterTextView = itemView.findViewById(R.id.tv_assignment_count);
            nameTextview = itemView.findViewById(R.id.tv_assignment_name);
            descriptionTextView = itemView.findViewById(R.id.tv_date);
            rootView = itemView.findViewById(R.id.ll_root);
            clockImageView = itemView.findViewById(R.id.date_icon);
            clockImageView.setVisibility(View.GONE);
        }
    }

    public interface PostsInterface {
        void onCourseClicked(int courseId, String courseName);
    }
}
