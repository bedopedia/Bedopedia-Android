package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.models.Notification;

/**
 * This file is spawned by Gemy on 10/31/2018.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    private Context context;
    private ArrayList<Notification> notificationArrayList;
    private AdapterPaginationInterface paginationInterface;
    private boolean newData;


    public NotificationsAdapter(Context context, AdapterPaginationInterface adapterPaginationInterface) {
        this.context = context;
        this.notificationArrayList = new ArrayList<>();
        paginationInterface = adapterPaginationInterface;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        if (position == notificationArrayList.size() - 2 && newData) {
            paginationInterface.onReachPosition();
        }
        Notification notification = notificationArrayList.get(position);
        holder.dateTv.setText(notification.getDate());
        holder.descriptionTv.setText(notification.getMessage());
        holder.descriptionTv.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.steel, null));
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public void addData(ArrayList<Notification> notifications, boolean newIncomingNotificationData) {
        newData = newIncomingNotificationData;
        if (newData) {
            this.notificationArrayList.addAll(notifications);
            notifyDataSetChanged();
        }
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        TextView dateTv, descriptionTv;
        ImageView imageView;

        public NotificationsViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout);
            dateTv = itemView.findViewById(R.id.tv_date);
            descriptionTv = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}