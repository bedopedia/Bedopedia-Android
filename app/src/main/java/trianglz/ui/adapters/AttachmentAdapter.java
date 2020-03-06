package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import trianglz.models.UploadedObject;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AttachmentAdapter extends RecyclerView.Adapter {

    private ArrayList<UploadedObject> uploadedObjects;
    private Context context;
    private AttachmentAdapterInterface attachmentAdapterInterface;
    public int type;
    public String assignmentDescription;


    public AttachmentAdapter(Context context, AttachmentAdapterInterface attachmentAdapterInterface) {
        this.context = context;
        uploadedObjects = new ArrayList<>();
        this.attachmentAdapterInterface = attachmentAdapterInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Constants.TYPE_ASSIGNMENT) {
            view = LayoutInflater.from(context).inflate(R.layout.assignment_description_item, parent, false);
            return new AssignmentDescriptionViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
            return new AttachmentViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("count", "onBindViewHolder: " + getItemCount());
        if (type == Constants.TYPE_ASSIGNMENT) {
            if (position == 0) {
                final AssignmentDescriptionViewHolder viewHolder = (AssignmentDescriptionViewHolder) holder;
                if (assignmentDescription.trim().isEmpty()) {
                    viewHolder.cardView.setVisibility(View.GONE);
                } else {
                    viewHolder.cardView.setVisibility(View.VISIBLE);
                    viewHolder.assignmentHtmlTextView.setHtml(assignmentDescription,
                            new HtmlHttpImageGetter(viewHolder.assignmentHtmlTextView));
                }
            } else {
                final AttachmentViewHolder viewHolder = (AttachmentViewHolder) holder;
                final UploadedObject uploadedObject = uploadedObjects.get(position - 1);
                viewHolder.fileNameTextView.setText(uploadedObject.getName());
                DateTime dateTime = new DateTime(uploadedObject.getCreatedAt());
                String date = dateTime.toLocalDate().toString();
                viewHolder.dateAddedTextView.setText(date);
                setAttachmentImage(viewHolder.fileType, uploadedObject.getExtension());
                viewHolder.rootView.setOnClickListener(view -> attachmentAdapterInterface.onAttachmentClicked(uploadedObject.getUrl()));

            }
        } else {
            final AttachmentViewHolder viewHolder = (AttachmentViewHolder) holder;
            final UploadedObject uploadedObject = uploadedObjects.get(position);
            viewHolder.fileNameTextView.setText(uploadedObject.getName());
            DateTime dateTime = new DateTime(uploadedObject.getCreatedAt());
            String date = dateTime.toLocalDate().toString();
            viewHolder.dateAddedTextView.setText(Util.humanReadableByteCountBin(uploadedObject.getFileSize()));
            setAttachmentImage(viewHolder.fileType, uploadedObject.getExtension());
            viewHolder.rootView.setOnClickListener(view -> attachmentAdapterInterface.onAttachmentClicked(uploadedObject.getUrl()));

        }
    }

    public void addData(ArrayList<UploadedObject> uploadedObjects) {
        this.uploadedObjects.clear();
        this.uploadedObjects.addAll(uploadedObjects);
        notifyDataSetChanged();
    }

    private void setAttachmentImage(ImageView attachmentImageView, String ext) {
        if (ext.contains("pdf")) {
            attachmentImageView.setImageResource((R.drawable.pdf_icon));
        } else if (ext.contains("doc") || ext.contains("rtf")) {
            attachmentImageView.setImageResource((R.drawable.doc_icon));
        } else if (ext.contains("pp")) {
            attachmentImageView.setImageResource((R.drawable.ppt_icon));
        } else if (ext.contains("xl")) {
            attachmentImageView.setImageResource((R.drawable.xlsx_icon));
        } else if (ext.contains("rar") || ext.contains("zip")) {
            attachmentImageView.setImageResource((R.drawable.zip_icon));
        } else if (ext.contains("mp3") || ext.contains("wav")) {
            attachmentImageView.setImageResource((R.drawable.audio_icon));
        } else if (ext.contains("mp4") || ext.contains("3gp")) {
            attachmentImageView.setImageResource((R.drawable.video_icon));
        } else {
            attachmentImageView.setImageResource((R.drawable.file_icon));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (type == Constants.TYPE_ASSIGNMENT) {
            if (position == 0) {
                return Constants.TYPE_ASSIGNMENT;
            } else {
                return Constants.TYPE_ATTACHMENT;
            }
        } else {
            return Constants.TYPE_ATTACHMENT;
        }
    }

    @Override
    public int getItemCount() {
        if (type == Constants.TYPE_ASSIGNMENT) {
            return uploadedObjects.size() + 1;
        } else {
            return uploadedObjects.size();
        }
    }

    private class AttachmentViewHolder extends RecyclerView.ViewHolder {

        private ImageView fileType;
        private TextView fileNameTextView, dateAddedTextView;
        private LinearLayout rootView;

        private AttachmentViewHolder(View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.iv_file_type);
            fileNameTextView = itemView.findViewById(R.id.tv_name);
            dateAddedTextView = itemView.findViewById(R.id.tv_date);
            rootView = itemView.findViewById(R.id.ll_root);
        }
    }

    private class AssignmentDescriptionViewHolder extends RecyclerView.ViewHolder {

        private HtmlTextView assignmentHtmlTextView;
        private CardView cardView;

        private AssignmentDescriptionViewHolder(View itemView) {
            super(itemView);
            assignmentHtmlTextView = itemView.findViewById(R.id.tv_course_description);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public interface AttachmentAdapterInterface {
        void onAttachmentClicked(String fileUrl);
    }

}
