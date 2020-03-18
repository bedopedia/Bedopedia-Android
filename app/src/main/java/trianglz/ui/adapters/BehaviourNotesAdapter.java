package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import trianglz.models.BehaviorNote;
import trianglz.utils.Util;

/**
 * This file is spawned by Gemy on 11/6/2018.
 */
public class BehaviourNotesAdapter extends RecyclerView.Adapter<BehaviourNotesAdapter.BehaviourNotesViewHolder> {
    private Context context;
    private ArrayList<BehaviorNote> behaviorNotes;
    private String type ="";

    public BehaviourNotesAdapter(Context context,String type) {
        this.context = context;
        this.behaviorNotes = new ArrayList<>();
        this.type = type;

    }

    @NonNull
    @Override

    public BehaviourNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_behaviour_note, parent, false);
        return new BehaviourNotesViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull BehaviourNotesViewHolder holder, int position) {
        BehaviorNote behaviorNote = behaviorNotes.get(position);
        holder.titleTv.setHtml(behaviorNote.category);
        Context context = holder.itemView.getContext();
        String behaviourNoteString = "";
        if (behaviorNote.location != null && !behaviorNote.location.isEmpty()) {
            behaviourNoteString += String.format(context.getString(R.string.location), behaviorNote.location);
        }
        if (behaviorNote.consequence != null && !behaviorNote.consequence.isEmpty()) {
            behaviourNoteString += "<br>" + String.format(context.getString(R.string.consequence), behaviorNote.consequence);
        }
        holder.detailsTv.setHtml(behaviourNoteString);
        holder.teacherNameTv.setHtml(behaviorNote.owner.name);
        holder.messageTv.setHtml(String.format(context.getString(R.string.note), behaviorNote.note.replace("<p>","")));
        holder.dateTextView.setText(Util.getAnnouncementDate(behaviorNote.createdAt, context));
    }

    @Override
    public int getItemCount() {
        return behaviorNotes.size();
    }

    public void addData(ArrayList<BehaviorNote> behaviorNotes) {
        this.behaviorNotes = behaviorNotes;
        notifyDataSetChanged();
    }

    class BehaviourNotesViewHolder extends RecyclerView.ViewHolder {
        HtmlTextView teacherNameTv,titleTv, messageTv, detailsTv;
        TextView dateTextView;


        public BehaviourNotesViewHolder(View itemView) {
            super(itemView);
            teacherNameTv = itemView.findViewById(R.id.teacher_tv);
            titleTv = itemView.findViewById(R.id.title_tv);
            messageTv = itemView.findViewById(R.id.message_tv);
            dateTextView = itemView.findViewById(R.id.date_tv);
            detailsTv = itemView.findViewById(R.id.details_tv);
        }
    }

}
