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
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.UploadedObject;

public class AttachmentAdapter extends RecyclerView.Adapter {

    ArrayList<UploadedObject> uploadedObjects;
    private Context context;
    private AttachmentAdapterInterface attachmentAdapterInterface;

    public AttachmentAdapter(Context context, AttachmentAdapterInterface attachmentAdapterInterface) {
        this.context = context;
        uploadedObjects = new ArrayList<>();
        this.attachmentAdapterInterface = attachmentAdapterInterface;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
        return new AttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AttachmentViewHolder viewHolder = (AttachmentViewHolder) holder;
        final UploadedObject uploadedObject = uploadedObjects.get(position);
        viewHolder.fileNameTextView.setText(uploadedObject.getName());
        DateTime dateTime = new DateTime(uploadedObject.getCreatedAt());
        String date = dateTime.toLocalDate().toString();
        viewHolder.dateAddedTextView.setText(date);
    }

    public void addData(ArrayList<UploadedObject> uploadedObjects) {
        this.uploadedObjects.clear();
        this.uploadedObjects.addAll(uploadedObjects);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return uploadedObjects.size();
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileType;
        public TextView fileNameTextView, dateAddedTextView;

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.iv_file_type);
            fileNameTextView = itemView.findViewById(R.id.tv_name);
            dateAddedTextView = itemView.findViewById(R.id.tv_date);
        }
    }

    public interface AttachmentAdapterInterface {
        void onAttachmentClicked();
    }

}
