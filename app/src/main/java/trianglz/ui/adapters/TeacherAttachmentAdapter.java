package trianglz.ui.adapters;

/**
 * Created by Farah A. Moniem on 07/08/2019.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.io.File;
import java.util.ArrayList;

public class TeacherAttachmentAdapter extends RecyclerView.Adapter {

    private ArrayList<Uri> filesUri;
    private Context context;
    private TeacherAttachmentInterface teacherAttachmentInterface;

    public TeacherAttachmentAdapter(Context context, TeacherAttachmentInterface teacherAttachmentInterface) {
        this.context = context;
        filesUri = new ArrayList<>();
        this.teacherAttachmentInterface = teacherAttachmentInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
        return new AttachmentViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AttachmentViewHolder viewHolder = (AttachmentViewHolder) holder;
        final File file = new File(filesUri.get(position).toString());
        viewHolder.fileNameTextView.setText(getAttachmentName(filesUri.get(position)));
        viewHolder.dateAddedTextView.setVisibility(View.GONE);
        viewHolder.fileType.setImageResource(R.drawable.file_icon);
        viewHolder.deleteAttachmentBtn.setVisibility(View.VISIBLE);
        viewHolder.deleteAttachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherAttachmentInterface.onDeleteClicked(position);
            }
        });


    }

    public void addData(ArrayList<Uri> filesUri) {
        this.filesUri.clear();
        this.filesUri.addAll(filesUri);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filesUri.size();
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

    private String getAttachmentName(Uri uri) {
        File myFile = new File(uri.toString());
        String displayName = null;
        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = myFile.getName();
        }
        return displayName;
    }

    public interface TeacherAttachmentInterface {
        void onDeleteClicked(int position);
    }
}

