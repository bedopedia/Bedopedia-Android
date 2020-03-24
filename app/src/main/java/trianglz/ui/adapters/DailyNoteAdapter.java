package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import trianglz.models.PlannerSubject;
import trianglz.utils.Util;

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
            if (!plannerSubject.classWork.isEmpty() && !plannerSubject.classWork.equals("null")) {
                holder.contentWebView.loadData((plannerSubject.classWork), "text/html", "UTF-8");
            } else {
                if (Util.getLocale(holder.itemView.getContext()).equals("ar")) {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no_classwork),
                            "text/html", "UTF-8");
                } else {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no)
                            + " " + context.getResources().getString(R.string.class_work), "text/html", "UTF-8");
                }
            }
        } else if (position == 1) {
            holder.dailyNoteImageView.setImageResource((R.drawable.ic_home_work));
            holder.headerTextView.setText(context.getResources().getString(R.string.homework));
            if (!plannerSubject.homework.isEmpty() && !plannerSubject.homework.equals("null")) {
                holder.contentWebView.loadData((plannerSubject.homework), "text/html", "UTF-8");
            } else {
                if (Util.getLocale(holder.itemView.getContext()).equals("ar")) {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no_homework),
                            "text/html", "UTF-8");
                } else {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no)
                            + " " + context.getResources().getString(R.string.homework), "text/html", "UTF-8");
                }
            }
        } else {
            holder.dailyNoteImageView.setImageResource((R.drawable.ic_activity));
            holder.headerTextView.setText(context.getResources().getString(R.string.activity));
            if (!plannerSubject.activities.isEmpty() && !plannerSubject.activities.equals("null")) {
                holder.contentWebView.loadData((plannerSubject.activities), "text/html", "UTF-8");
            } else {
                if (Util.getLocale(holder.itemView.getContext()).equals("ar")) {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no_activities),
                            "text/html", "UTF-8");
                } else {
                    holder.contentWebView.loadData(context.getResources().getString(R.string.there_is_no)
                            + " " + context.getResources().getString(R.string.activity), "text/html", "UTF-8");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



    public static class Holder extends RecyclerView.ViewHolder {

        public TextView headerTextView, dateTextView;
        public WebView contentWebView;
        public ImageView dailyNoteImageView;

        public Holder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.tv_header);
            dateTextView = itemView.findViewById(R.id.tv_date);
            contentWebView = itemView.findViewById(R.id.tv_content);
            contentWebView.setVerticalScrollBarEnabled(true);
            contentWebView.setHorizontalScrollBarEnabled(true);
            dailyNoteImageView = itemView.findViewById(R.id.img_daily_note);
        }
    }

}