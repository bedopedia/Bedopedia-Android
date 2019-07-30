package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.models.CourseGroups;
import trianglz.models.TeacherCourse;

public class TeacherCoursesAdapter extends RecyclerView.Adapter<TeacherCoursesAdapter.Holder> {
    public Context context;
    public List<TeacherCourse> mDataList;
    public List<CourseGroups> courseGroupList;
    TeacherCoursesInterface teacherCoursesInterface;
    private TeacherCourse teacherCourse;


    public TeacherCoursesAdapter(Context context, TeacherCoursesInterface teacherCoursesInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.courseGroupList = new ArrayList<>();
        this.teacherCoursesInterface = teacherCoursesInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_course, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (mDataList != null && !mDataList.isEmpty()) {
            final TeacherCourse teacherCourse = mDataList.get(position);
            holder.subjectNameTextView.setText(teacherCourse.getName());
            IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(holder.subjectImageView, new AvatarPlaceholderModified(teacherCourse.getName()), "Path");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teacherCoursesInterface.onCourseSelected(teacherCourse);
                }
            });
        }
        if (courseGroupList != null && !courseGroupList.isEmpty()) {
            final CourseGroups courseGroups = courseGroupList.get(position);
            holder.subjectNameTextView.setText(courseGroups.getName());
            IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(holder.subjectImageView, new AvatarPlaceholderModified(courseGroups.getName()), "Path");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teacherCoursesInterface.onCourseGroupSelected(courseGroups);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (!mDataList.isEmpty())return mDataList.size();
        else return courseGroupList.size();
    }

    public void addData(List<TeacherCourse> teacherCourses) {
        this.mDataList.clear();
        if (teacherCourses != null) this.mDataList.addAll(teacherCourses);
        notifyDataSetChanged();
    }
    public void addCourseGroups(List<CourseGroups> courseGroups) {
        this.courseGroupList.clear();
        if (courseGroups != null) this.courseGroupList.addAll(courseGroups);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public AvatarView subjectImageView;
        public TextView subjectNameTextView;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_course_name);
            subjectImageView = itemView.findViewById(R.id.img_course);
        }
    }

    public interface TeacherCoursesInterface{
        void onCourseSelected(TeacherCourse teacherCourse);
        void onCourseGroupSelected(CourseGroups courseGroups);
    }

}
