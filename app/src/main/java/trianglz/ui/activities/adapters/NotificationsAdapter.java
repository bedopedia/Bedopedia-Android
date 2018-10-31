package trianglz.ui.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

/**
 * This file is spawned by Gemy on 10/31/2018.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    public NotificationsAdapter(Context context) {
        this.context = context;
        this.stringArrayList = new ArrayList<>();
    }

    private ArrayList<String> stringArrayList;


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationsViewHolder(view);
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        TextView nameTv,dateTv, descriptionTv;
        ImageView imageView;
        public NotificationsViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout);
            nameTv = itemView.findViewById(R.id.tv_student_name);
            dateTv = itemView.findViewById(R.id.tv_date);
            descriptionTv = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.image);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }
    public void addData(ArrayList<String> dataList) {
        stringArrayList.clear();
        stringArrayList.addAll(dataList);
        notifyDataSetChanged();

    }
}
