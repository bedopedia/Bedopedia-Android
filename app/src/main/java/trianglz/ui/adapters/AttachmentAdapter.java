package trianglz.ui.adapters;

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

import org.joda.time.DateTime;

import java.util.ArrayList;

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
        setAttachmentImage(viewHolder.fileType, uploadedObject.getExtension());
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentAdapterInterface.onAttachmentClicked(uploadedObject.getUrl());
            }
        });
    }

    public void addData(ArrayList<UploadedObject> uploadedObjects) {
        this.uploadedObjects.clear();
        this.uploadedObjects.addAll(uploadedObjects);
        notifyDataSetChanged();
    }
    private void setAttachmentImage(ImageView attachmentImageView, String ext){
        if(ext.contains("pdf")){
            attachmentImageView.setImageResource((R.drawable.pdf_icon));
        }else if(ext.contains("doc")||ext.contains("rtf")){
            attachmentImageView.setImageResource((R.drawable.doc_icon));
        }else if(ext.contains("pp")){
            attachmentImageView.setImageResource((R.drawable.ppt_icon));
        }else if(ext.contains("xl")){
            attachmentImageView.setImageResource((R.drawable.xlsx_icon));
        }else if(ext.contains("rar") || ext.contains("zip")){
            attachmentImageView.setImageResource((R.drawable.zip_icon));
        }else if(ext.contains("mp3") || ext.contains("wav")){
            attachmentImageView.setImageResource((R.drawable.audio_icon));
        }else if(ext.contains("mp4") || ext.contains("3gp")){
            attachmentImageView.setImageResource((R.drawable.video_icon));
        }else {
            attachmentImageView.setImageResource((R.drawable.file_icon));
        }

    }

    @Override
    public int getItemCount() {
        return uploadedObjects.size();
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileType;
        public TextView fileNameTextView, dateAddedTextView;
        public LinearLayout rootView;

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.iv_file_type);
            fileNameTextView = itemView.findViewById(R.id.tv_name);
            dateAddedTextView = itemView.findViewById(R.id.tv_date);
            rootView = itemView.findViewById(R.id.ll_root);
        }
    }

    public interface AttachmentAdapterInterface {
        void onAttachmentClicked(String fileUrl);
    }

}
