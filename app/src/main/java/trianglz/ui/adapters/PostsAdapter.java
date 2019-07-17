package trianglz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

public class PostsAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {

        public ImageView subjectImageView;
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
        }
    }
}
