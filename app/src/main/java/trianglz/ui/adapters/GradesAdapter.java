package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import trianglz.models.CourseGroup;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.Holder> {
    public Context context;
    public List<CourseGroup> mDataList;
    GradesAdapterInterface gradesAdapterInterface;


    public GradesAdapter(Context context, GradesAdapterInterface gradesAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.gradesAdapterInterface = gradesAdapterInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CourseGroup courseGroup = mDataList.get(position);
        if(courseGroup.getLetter().isEmpty() || courseGroup.getLetter().equals("--")){
            holder.gradeTextView.setVisibility(View.GONE);
        }else {
            holder.gradeTextView.setVisibility(View.VISIBLE);
            holder.gradeTextView.setText(courseGroup.getLetter());
        }

        holder.subjectNameTextView.setText(courseGroup.getCourseName());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradesAdapterInterface.onSubjectSelected(position);
            }
        });

        setSubjectName(courseGroup.getIcon(),getSubjectNameForPlaceHolder(courseGroup.getCourseName()),holder);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<CourseGroup> courseGroupList) {
        this.mDataList.clear();
        this.mDataList.addAll(courseGroupList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, gradeTextView;
        public AvatarView subjectImageView;
        public LinearLayout itemLayout;
        public IImageLoader imageLoader;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            gradeTextView = itemView.findViewById(R.id.tv_grade);
            subjectImageView = itemView.findViewById(R.id.img_subject);
            itemLayout = itemView.findViewById(R.id.item_layout);
            imageLoader = new PicassoLoader();
        }
    }

    public interface GradesAdapterInterface{
        void onSubjectSelected(int position);
    }


    private void setSubjectName(String imageUrl, final String name, final Holder holder) {
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