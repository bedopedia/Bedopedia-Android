package trianglz.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import grades.GradesAvtivity;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.CourseGroup;
import trianglz.models.Student;
import trianglz.ui.adapters.GradesAdapter;
import trianglz.utils.Constants;

public class GradesActivity extends AppCompatActivity implements GradesAdapter.GradesAdapterInterface,View.OnClickListener {
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private RecyclerView recyclerView;
    private GradesAdapter gradesAdapter;
    private Student student;
    private ArrayList<CourseGroup> courseGroups;
    private IImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent(){
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        this.courseGroups = (ArrayList<CourseGroup>)getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_COURSE_GROUPS);
    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        studentImageView = findViewById(R.id.img_student);
        recyclerView = findViewById(R.id.recycler_view);
        gradesAdapter = new GradesAdapter(this,this);
        imageLoader = new PicassoLoader();
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(),studentName);
        recyclerView.setAdapter(gradesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        gradesAdapter.addData(courseGroups);
    }
    private void setListeners(){
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onSubjectSelected(int position) {
        openGradeDetailActivity(courseGroups.get(position));
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    private void openGradeDetailActivity(CourseGroup courseGroup){
        Intent intent = new Intent(this,GradeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS,courseGroup);
        bundle.putSerializable(Constants.STUDENT,student);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
