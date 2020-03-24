package trianglz.ui.adapters;

/**
 * Created by Farah A. Moniem on 07/08/2019.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.io.File;
import java.util.ArrayList;

public class TeacherAttachmentAdapter extends RecyclerView.Adapter {

    public ArrayList<File> filesList;
    private Context context;
    private TeacherAttachmentInterface teacherAttachmentInterface;

    public TeacherAttachmentAdapter(Context context, TeacherAttachmentInterface teacherAttachmentInterface) {
        this.context = context;
        filesList = new ArrayList<>();
        this.teacherAttachmentInterface = teacherAttachmentInterface;
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
        final File file = new File(filesList.get(position).toString());
        viewHolder.fileNameTextView.setText(file.getName());
        viewHolder.dateAddedTextView.setVisibility(View.GONE);
        setAttachmentImage(viewHolder.fileType, getFileExtension(file.getName()));
        viewHolder.deleteAttachmentBtn.setVisibility(View.VISIBLE);
        viewHolder.deleteAttachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherAttachmentInterface.onDeleteClicked(position);
            }
        });


    }

    public void addData(ArrayList<File> filesList) {
        this.filesList.clear();
        this.filesList.addAll(filesList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileType;
        public TextView fileNameTextView, dateAddedTextView;
        public LinearLayout rootView;
        public ImageButton deleteAttachmentBtn;

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.iv_file_type);
            fileNameTextView = itemView.findViewById(R.id.tv_name);
            dateAddedTextView = itemView.findViewById(R.id.tv_date);
            rootView = itemView.findViewById(R.id.ll_root);
            deleteAttachmentBtn = itemView.findViewById(R.id.delet_attachement_btn);
        }
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

    private String getFileExtension(String path) {
        String extension = path.substring(path.lastIndexOf("."));
        return extension;
    }

    public interface TeacherAttachmentInterface {
        void onDeleteClicked(int position);
    }
}

