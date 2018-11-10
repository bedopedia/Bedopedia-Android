package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.core.views.GradeDetailView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Assignment;
import trianglz.models.CourseGradingPeriods;
import trianglz.models.CourseGroup;
import trianglz.models.GradeItem;
import trianglz.models.Quiz;
import trianglz.models.Student;
import trianglz.ui.adapters.GradeDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class GradeDetailActivity extends SuperActivity implements View.OnClickListener,GradeDetailPresenter {
    private CourseGroup courseGroup;
    private RecyclerView recyclerView;
    private GradeDetailAdapter gradeDetailAdapter;
    private Student student;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private ImageButton backBtn;
    private GradeDetailView gradeDetailView;
    private TextView subjectHeaderTextView;
    private ArrayList<Quiz> quizArrayList;
    private ArrayList<Assignment> assignmentArrayList;
    private ArrayList<GradeItem> gradeItemArrayList;
    private HashMap<CourseGradingPeriods,Object> semsterHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.averageGradeEndPoint(courseGroup.getCourseId(),courseGroup.getId());
            gradeDetailView.getAverageGrade(url,student.getId()+"");
        }else {
            Util.showNoInternetConnectionDialog(this);
        }
        
    }

    private void getValueFromIntent() {
        courseGroup = (CourseGroup) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_COURSE_GROUPS);
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        gradeDetailAdapter = new GradeDetailAdapter(this);
        recyclerView.setAdapter(gradeDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        imageLoader = new PicassoLoader();
        setStudentImage();
        backBtn = findViewById(R.id.btn_back);
        gradeDetailView = new GradeDetailView(this,this);
        subjectHeaderTextView = findViewById(R.id.tv_subject_header);
        subjectHeaderTextView.setText(courseGroup.getCourseName());
        quizArrayList = new ArrayList<>();
        assignmentArrayList = new ArrayList<>();
        gradeItemArrayList = new ArrayList<>();
        semsterHashMap = new HashMap<>();
    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }


    private void setStudentImage() {
        final String imageUrl = student.getAvatar();
        final String name = student.firstName + " " + student.lastName;
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

    @Override
    public void onGetAverageGradesSuccess() {
        String courseGradePeriodUrl = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.studentGradeBook(courseGroup.getCourseId(),courseGroup.getId());
        gradeDetailView.getStudentGradeBook(courseGradePeriodUrl,student.getId());
    }

    @Override
    public void onGetAverageGradeFailure(String message,int errorCode) {
        progress.dismiss();
        // TODO: 11/8/2018 check error code 
    }

    @Override
    public void onGetStudentGradeBookSuccess(ArrayList<Assignment> assignmentArrayList, ArrayList<Quiz> quizArrayList, ArrayList<GradeItem> gradeItemArrayList) {
        this.assignmentArrayList = assignmentArrayList;
        this.quizArrayList = quizArrayList;
        this.gradeItemArrayList = gradeItemArrayList;
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getSemesters();
        gradeDetailView.getSemesters(url, courseGroup.getId()+"");
    }


    @Override
    public void onGetStudentGradeBookFailure(String message,int errorCode) {
        progress.dismiss();
    }

    @Override
    public void onGetSemestersSuccess(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        setAdapterData(courseGradingPeriodsArrayList);
        progress.dismiss();

    }

    @Override
    public void onGetSemesterFailure(String message, int errorCode) {
        progress.dismiss();
    }


    private void setAdapterData(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList){
        ArrayList<CourseGradingPeriods> allSemesterArrayList = handleSemesters(courseGradingPeriodsArrayList);
        setQuizzesInHashMap(allSemesterArrayList);
        setGradingItems(allSemesterArrayList);
    }

    private  ArrayList<CourseGradingPeriods> handleSemesters(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList){
        ArrayList<CourseGradingPeriods> expandedSemesters = new ArrayList<>();
        for(int i =0; i<courseGradingPeriodsArrayList.size(); i++){
            CourseGradingPeriods courseGradingPeriods = courseGradingPeriodsArrayList.get(i);
            expandedSemesters.add(courseGradingPeriods);
            if(courseGradingPeriods.subGradingPeriodsAttributes != null){
                expandedSemesters.add(courseGradingPeriods.subGradingPeriodsAttributes);
            }
        }
        return expandedSemesters;
    }

    private void setQuizzesInHashMap( ArrayList<CourseGradingPeriods> expandedSemesters ){
        for(int i = 0; i<quizArrayList.size(); i++){
            for(int j = 0; j< expandedSemesters.size(); j++){
                if(Util.isDateInside(expandedSemesters.get(i).startDate,
                        expandedSemesters.get(i).endDate,quizArrayList.get(i).endDate)){
                    semsterHashMap.put(expandedSemesters.get(j),quizArrayList.get(i));
                }
            }
        }
    }

    private void setGradingItems( ArrayList<CourseGradingPeriods> expandedSemesters ){
        for(int i = 0; i<gradeItemArrayList.size(); i++){
            for(int j = 0; j< expandedSemesters.size(); j++){
                if(Util.isDateInside(expandedSemesters.get(i).startDate,
                        expandedSemesters.get(i).endDate,gradeItemArrayList.get(i).endDate)){
                    semsterHashMap.put(expandedSemesters.get(j),gradeItemArrayList.get(i));
                }
            }
        }
    }

    private void setAssignmentArrayList( ArrayList<CourseGradingPeriods> expandedSemesters ){
        for(int i = 0; i<assignmentArrayList.size(); i++){
            for(int j = 0; j< expandedSemesters.size(); j++){
                if(Util.isDateInside(expandedSemesters.get(i).startDate,
                        expandedSemesters.get(i).endDate,assignmentArrayList.get(i).endDate)){
                    semsterHashMap.put(expandedSemesters.get(j),assignmentArrayList.get(i));
                }
            }
        }
    }
}
