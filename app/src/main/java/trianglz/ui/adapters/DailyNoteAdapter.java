package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.models.PlannerSubject;

public class DailyNoteAdapter extends RecyclerView.Adapter<DailyNoteAdapter.Holder> {
    public Context context;
    private PlannerSubject plannerSubject;

    public DailyNoteAdapter(Context context, PlannerSubject plannerSubject) {
        this.context = context;
        this.plannerSubject = plannerSubject;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_note, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (position == 0) {
            holder.dailyNoteImageView.setImageResource((R.drawable.ic_class_work));
            holder.headerTextView.setText(context.getResources().getString(R.string.class_work));
            if (!plannerSubject.getClassWork().isEmpty() && !plannerSubject.getClassWork().equals("null")) {
                holder.contentTextView.setText(Html.fromHtml(plannerSubject.getClassWork()));
            } else {
                holder.contentTextView.setText(context.getResources().getString(R.string.there_is_no)
                        + " " + context.getResources().getString(R.string.class_work));
            }
        } else if (position == 1) {
            holder.dailyNoteImageView.setImageResource((R.drawable.ic_home_work));
            holder.headerTextView.setText(context.getResources().getString(R.string.homework));
            if (!plannerSubject.getHomework().isEmpty() && !plannerSubject.getHomework().equals("null")) {
                holder.contentTextView.setText(Html.fromHtml(plannerSubject.getHomework()));
            } else {
                holder.contentTextView.setText(context.getResources().getString(R.string.there_is_no)
                        + " " + context.getResources().getString(R.string.homework));
            }
        } else {
            holder.dailyNoteImageView.setImageResource((R.drawable.ic_activity));
            holder.headerTextView.setText(context.getResources().getString(R.string.activity));
            if (!plannerSubject.getActivities().isEmpty() && !plannerSubject.getActivities().equals("null")) {
                holder.contentTextView.setText(Html.fromHtml(plannerSubject.getActivities()));
            } else {
                holder.contentTextView.setText(context.getResources().getString(R.string.there_is_no)
                        + " " + context.getResources().getString(R.string.activity));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



    public static class Holder extends RecyclerView.ViewHolder {

        public TextView headerTextView, dateTextView, contentTextView;
        public ImageView dailyNoteImageView;

        public Holder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.tv_header);
            dateTextView = itemView.findViewById(R.id.tv_date);
            contentTextView = itemView.findViewById(R.id.tv_content);
            dailyNoteImageView = itemView.findViewById(R.id.img_daily_note);
        }
    }

}