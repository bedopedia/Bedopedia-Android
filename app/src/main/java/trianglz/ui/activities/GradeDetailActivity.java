package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import trianglz.models.GradeHeader;
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
    private HashMap<CourseGradingPeriods,ArrayList<Object>> semesterHashMap;
    private TextView allTextView,currentTextView;
    private ArrayList<Object> allSemestersList;
    private ArrayList<Object> currentSemester;
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
        semesterHashMap = new HashMap<>();
        allTextView = findViewById(R.id.tv_all);
        currentTextView = findViewById(R.id.tv_current);
        allSemestersList = new ArrayList<>();
        currentSemester = new ArrayList<>();
    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
        allTextView.setOnClickListener(this);
        currentTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.tv_all:
                setTextBackgrounds(0);
                gradeDetailAdapter.addData(allSemestersList);
                break;
            case R.id.tv_current:
                setTextBackgrounds(1);
                gradeDetailAdapter.addData(currentSemester);
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
        for(int i = 0; i<allSemesterArrayList.size(); i++){
            semesterHashMap.put(allSemesterArrayList.get(i),new ArrayList<>());
        }
        setQuizzesInHashMap(allSemesterArrayList);
        setAssignmentArrayList(allSemesterArrayList);
        setGradingItems(allSemesterArrayList);
        setData();

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
        return (expandedSemesters);
    }

    private void setQuizzesInHashMap( ArrayList<CourseGradingPeriods> expandedSemesters ){
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for(int quiz = 0; quiz<quizArrayList.size(); quiz++){
            for(int semester = 0; semester< expandedSemesters.size(); semester++){
                if(Util.isDateInside(expandedSemesters.get(semester).startDate,
                        expandedSemesters.get(semester).endDate,quizArrayList.get(quiz).endDate)){
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(semester));
                    if(isToAdd){
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.quizzes);
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size()-1;
                    }
                    Quiz quiz1 = quizArrayList.get(quiz);
                    objectArrayList.add(quiz1);
                    totalMarks = totalMarks + quiz1.total;
                    studentMark = studentMark + quiz1.grade;
                    GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                    gradeHeader.sumOfStudentMarks = studentMark;
                    gradeHeader.totalSummtion = totalMarks;
                    objectArrayList.set(headerPosition,gradeHeader);
                    semesterHashMap.put(expandedSemesters.get(semester),objectArrayList);
                }
            }
        }
    }

    private void setGradingItems( ArrayList<CourseGradingPeriods> expandedSemesters ){
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for(int i = 0; i<gradeItemArrayList.size(); i++){
            for(int j = 0; j< expandedSemesters.size(); j++){
                if(Util.isDateInside(expandedSemesters.get(j).startDate,
                        expandedSemesters.get(j).endDate,gradeItemArrayList.get(i).endDate)){
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(j));
                    if(isToAdd){
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.grade_items);
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size()-1;
                    }
                    GradeItem gradeItem = gradeItemArrayList.get(i);
                    objectArrayList.add(gradeItem);
                    totalMarks = totalMarks + gradeItem.total;
                    studentMark = studentMark + gradeItem.grade;
                    GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                    gradeHeader.sumOfStudentMarks = studentMark;
                    gradeHeader.totalSummtion = totalMarks;
                    objectArrayList.set(headerPosition,gradeHeader);
                    semesterHashMap.put(expandedSemesters.get(j),objectArrayList);
                }
            }
        }
    }

        private void setAssignmentArrayList( ArrayList<CourseGradingPeriods> expandedSemesters ){
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for(int i = 0; i<assignmentArrayList.size(); i++){
            for(int j = 0; j< expandedSemesters.size(); j++){
                if(Util.isDateInside(expandedSemesters.get(j).startDate,
                        expandedSemesters.get(j).endDate,assignmentArrayList.get(i).endDate)){
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(j));
                    if(isToAdd){
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.assignments);
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size()-1;
                    }
                    Assignment assignment = assignmentArrayList.get(i);
                    objectArrayList.add(assignment);
                    totalMarks = totalMarks + assignment.total;
                    studentMark = studentMark + assignment.grade;
                    GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                    gradeHeader.sumOfStudentMarks = studentMark;
                    gradeHeader.totalSummtion = totalMarks;
                    objectArrayList.set(headerPosition,gradeHeader);
                    semesterHashMap.put(expandedSemesters.get(j),objectArrayList);
                }
            }

        }
    }

    private void setData(){
        boolean isCurrent = false;
        allSemestersList = new ArrayList<>();
       currentSemester = new ArrayList<>();
        List keys = sortKeys();
        for(int i = 0 ;i <keys.size() ; i++){
            allSemestersList.add(keys.get(i));
            CourseGradingPeriods courseGradingPeriods = (CourseGradingPeriods) keys.get(i);
            if(Util.isDateInside(courseGradingPeriods.startDate,courseGradingPeriods.endDate,Util.getCurrentDate())){
                currentSemester.add(keys.get(i));
                isCurrent = true;
            }
            ArrayList<Object> objectArrayList = semesterHashMap.get(keys.get(i));
            for(int j = 0; j<objectArrayList.size(); j++){
                allSemestersList.add(objectArrayList.get(j));
                if(isCurrent){
                    currentSemester.add(objectArrayList.get(j));
                }
            }
            isCurrent = false;
        }
        gradeDetailAdapter.addData(allSemestersList);
    }

    private List sortKeys(){
        List keys = new ArrayList(semesterHashMap.keySet());
        if(keys.size()>0){
            CourseGradingPeriods keyArray[] = new CourseGradingPeriods[keys.size()];
            for(int i = 0; i<keys.size(); i++){
                keyArray[i] =(CourseGradingPeriods) keys.get(i);
            }
            Arrays.sort(keyArray, new Comparator<CourseGradingPeriods>() {
                @Override
                public int compare(CourseGradingPeriods entry1, CourseGradingPeriods entry2) {
                    Long time1 =(Util.convertStringToDate(entry1.endDate).getTime());
                    long time2 = (Util.convertStringToDate(entry2.endDate).getTime());
                    return time1.compareTo(time2);
                }
            });
            for(int i = 0; i<keys.size(); i++){
                keys.set(i,keyArray[i]);
            }
        }
        return keys;
    }

    private void setTextBackgrounds(int pageNumber) {
        if (pageNumber == 0) {
            allTextView.setBackground(getResources().getDrawable(R.drawable.text_solid_background));
            allTextView.setTextColor(getResources().getColor(R.color.white));
            currentTextView.setBackgroundColor(getResources().getColor(R.color.white_three));
            currentTextView.setTextColor(getResources().getColor(R.color.jade_green));
        } else {
            allTextView.setBackgroundColor(getResources().getColor(R.color.white_three));
            currentTextView.setBackground(getResources().getDrawable(R.drawable.text_solid_background));
            currentTextView.setTextColor(getResources().getColor(R.color.white));
            allTextView.setTextColor(getResources().getColor(R.color.jade_green));
        }
    }
}
