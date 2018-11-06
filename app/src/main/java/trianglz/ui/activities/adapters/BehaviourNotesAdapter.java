package trianglz.ui.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.BehaviorNote;

/**
 * This file is spawned by Gemy on 11/6/2018.
 */
public class BehaviourNotesAdapter extends RecyclerView.Adapter<BehaviourNotesAdapter.BehaviourNotesViewHolder> {
    private Context context;
    private ArrayList<BehaviorNote> behaviorNotes;

    public BehaviourNotesAdapter(Context context) {
        this.context = context;
        this.behaviorNotes = new ArrayList<>();

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
        holder.teacherNameTv.setText(behaviorNote.teacherName);
        if (behaviorNote.title != null) {
            holder.titleTv.setText(behaviorNote.title);
        } else {
            holder.titleTv.setText("No title");
        }

        holder.messageTv.setText(behaviorNote.message);
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
        LinearLayout itemLayout;
        TextView teacherNameTv, titleTv, messageTv;

        public BehaviourNotesViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.behaviour_item_layout);
            teacherNameTv = itemView.findViewById(R.id.teacher_tv);
            titleTv = itemView.findViewById(R.id.title_tv);
            messageTv = itemView.findViewById(R.id.message_tv);
        }
    }

}
