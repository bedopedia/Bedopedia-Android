package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.DailyNote;

/**
 * This file is spawned by Gemy on 1/20/2019.
 */
public class DayFragmentAdapter extends RecyclerView.Adapter<DayFragmentAdapter.Holder> {
    public Context context;
    public List<DailyNote> mDataList;


    public DayFragmentAdapter(Context context) {
        this.context = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        DailyNote dailyNote = mDataList.get(position);
//        holder.subjectNameTextView.setText(dailyNote.getSubjectName());
//        holder.gradeTextView.setVisibility(View.GONE);
//        setSubjectName(null,getSubjectNameForPlaceHolder(dailyNote.getSubjectName()),holder);

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<DailyNote> dailyNoteList) {
        this.mDataList.clear();
        this.mDataList.addAll(dailyNoteList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView subjectNameTextView,gradeTextView;
        private AvatarView subjectImageView;
        private LinearLayout itemLayout;
        public IImageLoader imageLoader;

        private Holder(View itemView) {
            super(itemView);
            gradeTextView = itemView.findViewById(R.id.tv_grade);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            subjectImageView = itemView.findViewById(R.id.img_subject);
            itemLayout = itemView.findViewById(R.id.item_layout);
            imageLoader = new PicassoLoader();
        }
    }



    private void setSubjectName(String imageUrl, final String name, final DayFragmentAdapter.Holder holder) {
        if (imageUrl == null || imageUrl.equals("")) {
            holder.imageLoader = new PicassoLoader();
            holder.imageLoader.loadImage(holder.subjectImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(context)
                    .load(imageUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.subjectImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            holder.imageLoader = new PicassoLoader();
                            holder.imageLoader.loadImage(holder.subjectImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }


    private String getSubjectNameForPlaceHolder(String name){

        if(name.indexOf('&') != -1){
            int indexOfAnd = name.indexOf('&');
            name = name.substring(0, indexOfAnd-1) + name.substring(indexOfAnd+1);
        }

        return name;
    }

}
