package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import agency.tango.android.avatarview.views.AvatarView;
import trianglz.core.views.PostDetailsView;
import trianglz.models.PostDetails;
import trianglz.models.PostsResponse;

public class PostDetailsAdapter extends RecyclerView.Adapter {
    ArrayList<PostDetails> postDetails;
    private Context context;
    public PostDetailsAdapter(Context context) {
        this.context = context;
        postDetails = new ArrayList<>(postDetails);

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
    public void addData(ArrayList<PostDetails> postDetails) {
        postDetails.clear();
        postDetails.addAll(postDetails);
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
        public PostDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_owner_image);
            ownerTextview = itemView.findViewById(R.id.tv_owner_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            bodyTextView = itemView.findViewById(R.id.tv_body);
            firstButton = itemView.findViewById(R.id.btn_first_attachment);
            secondButton = itemView.findViewById(R.id.btn_second_attachment);
            thirdButton = itemView.findViewById(R.id.btn_third_attachment);

        }
    }

}
