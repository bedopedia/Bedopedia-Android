package trianglz.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import trianglz.components.ErrorDialog;
import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.core.views.GradeDetailView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Assignment;
import trianglz.models.CourseGradingPeriods;
import trianglz.models.GradeHeader;
import trianglz.models.GradeItem;
import trianglz.models.PostsResponse;
import trianglz.models.Quiz;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradeDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradeDetailFragment extends Fragment implements View.OnClickListener, GradeDetailPresenter, ErrorDialog.DialogConfirmationInterface {

    private View rootView;
    private StudentMainActivity activity;
    private PostsResponse courseGroup;
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
    private FrameLayout listFrameLayout, placeholderFrameLayout;
    private boolean isToShowPlaceHolder = true;
    private ErrorDialog mErrorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_grade_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (Util.isNetworkAvailable(activity)) {
            activity.showLoadingDialog();

            String courseGradePeriodUrl = SessionManager.getInstance().getBaseUrl() +
                    ApiEndPoints.studentGradeBook(courseGroup.getCourseId(), courseGroup.getId());
            gradeDetailView.getStudentGradeBook(courseGradePeriodUrl, student.getId());
//            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.averageGradeEndPoint(courseGroup.getCourseId(), courseGroup.getId());
//            gradeDetailView.getAverageGrade(url, student.getId() + "");
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroup = (PostsResponse) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        gradeDetailAdapter = new GradeDetailAdapter(activity);
        recyclerView.setAdapter(gradeDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        imageLoader = new PicassoLoader();
        setStudentImage();
        backBtn = rootView.findViewById(R.id.btn_back);
        gradeDetailView = new GradeDetailView(activity, this);
        subjectHeaderTextView = rootView.findViewById(R.id.tv_subject_header);
        subjectHeaderTextView.setText(courseGroup.getCourseName());
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);
        quizArrayList = new ArrayList<>();
        assignmentArrayList = new ArrayList<>();
        gradeItemArrayList = new ArrayList<>();
        semesterHashMap = new HashMap<>();
        allSemestersList = new ArrayList<>();
        currentSemester = new ArrayList<>();
        quizzesHashMap = new HashMap<>();
        assignmentsHashMap = new HashMap<>();
        gradeItemHashMap = new HashMap<>();

        segmentedGroup = rootView.findViewById(R.id.segmented);
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }

        // radio buttons
        allBtn = rootView.findViewById(R.id.btn_all);
        currentBtn = rootView.findViewById(R.id.btn_current);
        segmentedGroup.check(allBtn.getId());
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        currentBtn.setOnClickListener(this);
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
            Picasso.with(activity)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.btn_all:
                gradeDetailAdapter.addData(allSemestersList);
                break;
            case R.id.btn_current:
                gradeDetailAdapter.addData(currentSemester);
                break;
        }
    }

    @Override
    public void onGetAverageGradesSuccess(HashMap<String, Double> quizzesHashMap, HashMap<String, Double> assignmentsHashMap, HashMap<String, Double> gradeItemHashMap) {
        this.quizzesHashMap = quizzesHashMap;
        this.assignmentsHashMap = assignmentsHashMap;
        this.gradeItemHashMap = gradeItemHashMap;

    }

    @Override
    public void onGetAverageGradeFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }

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
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 403) {
            mErrorDialog = new ErrorDialog(activity, getResources().getString(R.string.un_authorized), ErrorDialog.DialogType.ERROR, this);
            mErrorDialog.show();
        } else
            activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onGetSemestersSuccess(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        setAdapterData(courseGradingPeriodsArrayList);
        activity.progress.dismiss();
        if (allSemestersList.isEmpty()) {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else if (gradeItemArrayList.isEmpty() && quizArrayList.isEmpty() && assignmentArrayList.isEmpty()) {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else if (isToShowPlaceHolder) {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else {
            listFrameLayout.setVisibility(View.VISIBLE);
            placeholderFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetSemesterFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode, "");

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
        setData(allSemesterArrayList);

    }

    private ArrayList<CourseGradingPeriods> handleSemesters(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList) {
        ArrayList<CourseGradingPeriods> expandedSemesters = new ArrayList<>();
        for (int i = 0; i < courseGradingPeriodsArrayList.size(); i++) {
            CourseGradingPeriods courseGradingPeriods = courseGradingPeriodsArrayList.get(i);
            expandedSemesters.add(courseGradingPeriods);
            if (courseGradingPeriods.subGradingPeriodsAttributes != null) {
                for (int j = 0; j < courseGradingPeriods.subGradingPeriodsAttributes.size(); j++) {
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
            if (expandedSemesters.get(semester).isParent) {
                continue;
            }
            isToAdd = true;
            for (int quiz = 0; quiz < quizArrayList.size(); quiz++) {
                if (Util.isDateInside(expandedSemesters.get(semester).startDate,
                        expandedSemesters.get(semester).endDate, quizArrayList.get(quiz).endDate)) {
                    isToShowPlaceHolder = false;
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
                    if (objectArrayList.get(headerPosition) instanceof GradeHeader) {
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
            if (expandedSemesters.get(i).isParent) {
                continue;
            }
            isToAdd = true;
            for (int j = 0; j < gradeItemArrayList.size(); j++) {
                if (expandedSemesters.get(i).id == gradeItemArrayList.get(j).gradingPeriodId) {
                    isToShowPlaceHolder = false;
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
            if (expandedSemesters.get(i).isParent) {
                continue;
            }
            isToAdd = true;
            for (int j = 0; j < assignmentArrayList.size(); j++) {
                if (Util.isDateInside(expandedSemesters.get(i).startDate,
                        expandedSemesters.get(i).endDate, assignmentArrayList.get(j).endDate)) {
                    isToShowPlaceHolder = false;
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

    private void setData(ArrayList<CourseGradingPeriods> allSemesterArrayList) {
        boolean isCurrent = false;
        allSemestersList = new ArrayList<>();
        currentSemester = new ArrayList<>();
        List keys = allSemesterArrayList;
        for (int i = 0; i < keys.size(); i++) {
            allSemestersList.add(keys.get(i));
            CourseGradingPeriods courseGradingPeriods = (CourseGradingPeriods) keys.get(i);
            if (Util.isDateInside(courseGradingPeriods.startDate, courseGradingPeriods.endDate, Util.getCurrentDate())
                    && !courseGradingPeriods.isParent) {
                currentSemester.add(keys.get(i));
                isCurrent = true;
            } else if (courseGradingPeriods.isChild) {
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
                    Long time2 = (Util.convertStringToDate(entry2.endDate).getTime());
                    Long time3 = (Util.convertStringToDate(entry1.startDate).getTime());
                    long time4 = (Util.convertStringToDate(entry2.startDate).getTime());
                    int endResult = time1.compareTo(time2);
                    int startResult = time3.compareTo(time4);
                    if (startResult == 0 || endResult == 0) {
                        if (startResult == 0) {
                            if (endResult > 0) {
                                return -1;
                            } else {
                                return 1;
                            }
                        } else {
                            return startResult;
                        }

                    } else {
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

    @Override
    public void onConfirm() {
        Log.d("", "onConfirm: ");
        getParentFragment().getChildFragmentManager().popBackStack();

    }

    @Override
    public void onCancel() {
        Log.d("", "onConfirm: ");

    }
}
