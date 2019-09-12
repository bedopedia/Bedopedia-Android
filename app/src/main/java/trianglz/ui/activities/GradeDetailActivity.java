package trianglz.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
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

public class GradeDetailActivity extends SuperActivity implements View.OnClickListener, GradeDetailPresenter {
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
    private HashMap<CourseGradingPeriods, ArrayList<Object>> semesterHashMap;
    // add segmented group
    private SegmentedGroup segmentedGroup;
    private RadioButton allBtn, currentBtn;
    private ArrayList<Object> allSemestersList;
    private ArrayList<Object> currentSemester;
    private HashMap<String, Double> quizzesHashMap;
    private HashMap<String, Double> assignmentsHashMap;
    private HashMap<String, Double> gradeItemHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.averageGradeEndPoint(courseGroup.getCourseId(), courseGroup.getId());
            gradeDetailView.getAverageGrade(url, student.getId() + "");
        } else {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        imageLoader = new PicassoLoader();
        setStudentImage();
        backBtn = findViewById(R.id.btn_back);
        gradeDetailView = new GradeDetailView(this, this);
        subjectHeaderTextView = findViewById(R.id.tv_subject_header);
        subjectHeaderTextView.setText(courseGroup.getCourseName());
        quizArrayList = new ArrayList<>();
        assignmentArrayList = new ArrayList<>();
        gradeItemArrayList = new ArrayList<>();
        semesterHashMap = new HashMap<>();
        allSemestersList = new ArrayList<>();
        currentSemester = new ArrayList<>();
        quizzesHashMap = new HashMap<>();
        assignmentsHashMap = new HashMap<>();
        gradeItemHashMap = new HashMap<>();

        segmentedGroup = findViewById (R.id.segmented);
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        // radio buttons
        allBtn = findViewById(R.id.btn_all);
        currentBtn = findViewById(R.id.btn_current);
        segmentedGroup.check(allBtn.getId());
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        currentBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_all:
                gradeDetailAdapter.addData(allSemestersList);
                break;
            case R.id.btn_current:
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
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
                    .load(imageUrl)
                    .noPlaceholder()
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
    public void onGetAverageGradesSuccess(HashMap<String, Double> quizzesHashMap,
                                          HashMap<String, Double> assignmentsHashMap,
                                          HashMap<String, Double> gradeItemHashMap) {

        this.quizzesHashMap = quizzesHashMap;
        this.assignmentsHashMap = assignmentsHashMap;
        this.gradeItemHashMap = gradeItemHashMap;

        String courseGradePeriodUrl = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.studentGradeBook(courseGroup.getCourseId(), courseGroup.getId());
        gradeDetailView.getStudentGradeBook(courseGradePeriodUrl, student.getId());
    }

    @Override
    public void onGetAverageGradeFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");

    }

    @Override
    public void onGetStudentGradeBookSuccess(ArrayList<Assignment> assignmentArrayList, ArrayList<Quiz> quizArrayList, ArrayList<GradeItem> gradeItemArrayList) {
        this.assignmentArrayList = assignmentArrayList;
        this.quizArrayList = quizArrayList;
        this.gradeItemArrayList = gradeItemArrayList;
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getSemesters();
        gradeDetailView.getSemesters(url, courseGroup.getCourseId() + "");
    }


    @Override
    public void onGetStudentGradeBookFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");

    }

    @Override
    public void onGetSemestersSuccess(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        setAdapterData(courseGradingPeriodsArrayList);
        progress.dismiss();

    }

    @Override
    public void onGetSemesterFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");
    }


    private void setAdapterData(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        ArrayList<CourseGradingPeriods> allSemesterArrayList = handleSemesters(courseGradingPeriodsArrayList);
        for (int i = 0; i < allSemesterArrayList.size(); i++) {
            semesterHashMap.put(allSemesterArrayList.get(i), new ArrayList<>());
        }
        setQuizzesInHashMap(allSemesterArrayList);
        setAssignmentArrayList(allSemesterArrayList);
        setGradingItems(allSemesterArrayList);
        setAverageValues();
        setData();

    }

    private ArrayList<CourseGradingPeriods> handleSemesters(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        ArrayList<CourseGradingPeriods> expandedSemesters = new ArrayList<>();
        for (int i = 0; i < courseGradingPeriodsArrayList.size(); i++) {
            CourseGradingPeriods courseGradingPeriods = courseGradingPeriodsArrayList.get(i);
            expandedSemesters.add(courseGradingPeriods);
            if (courseGradingPeriods.subGradingPeriodsAttributes != null) {
                for(int j = 0; j<courseGradingPeriods.subGradingPeriodsAttributes.size(); j++){
                    expandedSemesters.add(courseGradingPeriods.subGradingPeriodsAttributes.get(j));
                }

            }
        }
        return (expandedSemesters);
    }

    private void setQuizzesInHashMap(ArrayList<CourseGradingPeriods> expandedSemesters) {
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for (int semester = 0; semester < expandedSemesters.size(); semester++) {
            if(expandedSemesters.get(semester).isParent){
                continue;
            }
            isToAdd = true;
            for (int quiz = 0; quiz < quizArrayList.size(); quiz++) {
                if (Util.isDateInside(expandedSemesters.get(semester).startDate,
                        expandedSemesters.get(semester).endDate, quizArrayList.get(quiz).endDate)) {
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(semester));
                    if (isToAdd) {
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.quizzes);
                        gradeHeader.publish = expandedSemesters.get(semester).publish;
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size() - 1;
                    }
                    Quiz quiz1 = quizArrayList.get(quiz);
                    objectArrayList.add(quiz1);
                    totalMarks = totalMarks + quiz1.total;
                    studentMark = studentMark + quiz1.grade;
                    if(objectArrayList.get(headerPosition) instanceof GradeHeader){
                        GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                        gradeHeader.sumOfStudentMarks = studentMark;
                        gradeHeader.totalSummtion = totalMarks;
                        objectArrayList.set(headerPosition, gradeHeader);
                        semesterHashMap.put(expandedSemesters.get(semester), objectArrayList);
                    }
                }
            }
        }
    }

    private void setGradingItems(ArrayList<CourseGradingPeriods> expandedSemesters) {
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for (int i = 0; i < expandedSemesters.size(); i++) {
            if(expandedSemesters.get(i).isParent){
                continue;
            }
            isToAdd = true;
            for (int j = 0; j < gradeItemArrayList.size(); j++) {
                if (expandedSemesters.get(i).id == gradeItemArrayList.get(j).gradingPeriodId ) {
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(i));
                    if (isToAdd) {
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.grade_items);
                        gradeHeader.publish = expandedSemesters.get(i).publish;
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size() - 1;
                    }
                    GradeItem gradeItem = gradeItemArrayList.get(j);
                    objectArrayList.add(gradeItem);
                    totalMarks = totalMarks + gradeItem.total;
                    studentMark = studentMark + gradeItem.grade;
                    if (objectArrayList.get(headerPosition) instanceof GradeHeader) {
                        GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                        gradeHeader.sumOfStudentMarks = studentMark;
                        gradeHeader.totalSummtion = totalMarks;
                        objectArrayList.set(headerPosition, gradeHeader);
                        semesterHashMap.put(expandedSemesters.get(i), objectArrayList);
                    }
                }
            }
        }
    }

    private void setAssignmentArrayList(ArrayList<CourseGradingPeriods> expandedSemesters) {
        boolean isToAdd = true;
        int headerPosition = -1;
        double studentMark = 0;
        double totalMarks = 0;
        for (int i = 0; i < expandedSemesters.size(); i++) {
            if(expandedSemesters.get(i).isParent){
                continue;
            }
            isToAdd = true;
            for (int j = 0; j < assignmentArrayList.size(); j++) {
                if (Util.isDateInside(expandedSemesters.get(i).startDate,
                        expandedSemesters.get(i).endDate, assignmentArrayList.get(j).endDate)) {
                    ArrayList<Object> objectArrayList = semesterHashMap.get(expandedSemesters.get(i));
                    if (isToAdd) {
                        isToAdd = false;
                        GradeHeader gradeHeader = new GradeHeader();
                        gradeHeader.header = getResources().getString(R.string.assignments);
                        gradeHeader.publish = expandedSemesters.get(i).publish;
                        objectArrayList.add(gradeHeader);
                        headerPosition = objectArrayList.size() - 1;
                    }
                    Assignment assignment = assignmentArrayList.get(j);
                    objectArrayList.add(assignment);
                    totalMarks = totalMarks + assignment.total;
                    studentMark = studentMark + assignment.grade;
                    if (objectArrayList.get(headerPosition) instanceof GradeHeader) {
                        GradeHeader gradeHeader = (GradeHeader) objectArrayList.get(headerPosition);
                        gradeHeader.sumOfStudentMarks = studentMark;
                        gradeHeader.totalSummtion = totalMarks;
                        objectArrayList.set(headerPosition, gradeHeader);
                        semesterHashMap.put(expandedSemesters.get(i), objectArrayList);
                    }
                }
            }

        }
    }

    private void setData() {
        boolean isCurrent = false;
        allSemestersList = new ArrayList<>();
        currentSemester = new ArrayList<>();
        List keys = sortKeys();
        for (int i = 0; i < keys.size(); i++) {
            allSemestersList.add(keys.get(i));
            CourseGradingPeriods courseGradingPeriods = (CourseGradingPeriods) keys.get(i);
            if (Util.isDateInside(courseGradingPeriods.startDate, courseGradingPeriods.endDate, Util.getCurrentDate())
                    && !courseGradingPeriods.isParent) {
                currentSemester.add(keys.get(i));
                isCurrent = true;
            }else if(courseGradingPeriods.isChild){
                if (Util.isDateInside(courseGradingPeriods.parentStartDate, courseGradingPeriods.parentEndDate, Util.getCurrentDate())) {
                    currentSemester.add(keys.get(i));
                    isCurrent = true;
                }
            }
            ArrayList<Object> objectArrayList = semesterHashMap.get(keys.get(i));
            for (int j = 0; j < objectArrayList.size(); j++) {
                allSemestersList.add(objectArrayList.get(j));
                if (isCurrent) {
                    currentSemester.add(objectArrayList.get(j));
                }
            }
            isCurrent = false;
        }
        gradeDetailAdapter.addData(allSemestersList);
    }

    private List sortKeys() {
        List keys = new ArrayList(semesterHashMap.keySet());
        if (keys.size() > 0) {
            CourseGradingPeriods keyArray[] = new CourseGradingPeriods[keys.size()];
            for (int i = 0; i < keys.size(); i++) {
                keyArray[i] = (CourseGradingPeriods) keys.get(i);
            }
            Arrays.sort(keyArray, new Comparator<CourseGradingPeriods>() {
                @Override
                public int compare(CourseGradingPeriods entry1, CourseGradingPeriods entry2) {
                    Long time1 = (Util.convertStringToDate(entry1.endDate).getTime());
                    long time2 = (Util.convertStringToDate(entry2.endDate).getTime());
                    Long time3 = (Util.convertStringToDate(entry1.startDate).getTime());
                    long time4 = (Util.convertStringToDate(entry2.startDate).getTime());
                    int endResult =  time1.compareTo(time2);
                    int startResult = time3.compareTo(time4);
                    if(startResult == 0 || endResult == 0){
                        if(startResult == 0){
                            if(endResult>0){
                                return -1;
                            }else {
                                return 1;
                            }
                        }else{
                            return startResult;
                        }

                    }else {
                        return endResult;
                    }

                }
            });
            for (int i = 0; i < keys.size(); i++) {
                keys.set(i, keyArray[i]);
            }
        }
        return keys;
    }





    private void setAverageValues() {
        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < assignmentArrayList.size(); i++) {
            if (assignmentsHashMap.get(assignmentArrayList.get(i).id + "") != null) {
                assignmentArrayList.get(i).averageGrade = df.format(assignmentsHashMap.get(assignmentArrayList.get(i).id + ""));
            }
        }
        for (int i = 0; i < quizArrayList.size(); i++) {
            if (quizzesHashMap.get(quizArrayList.get(i).id + "") != null) {
                quizArrayList.get(i).averageGrade = df.format(quizzesHashMap.get(quizArrayList.get(i).id + ""));

            }
        }

        for (int i = 0; i < gradeItemArrayList.size(); i++) {
            if (gradeItemHashMap.get(gradeItemArrayList.get(i).id + "") != null) {
                gradeItemArrayList.get(i).averageGrade = df.format(gradeItemHashMap.get(gradeItemArrayList.get(i).id + ""));
            }
        }
    }
}
