package trianglz.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.NewMessagePresenter;
import trianglz.core.views.NewMessageView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.MessageThread;
import trianglz.models.Student;
import trianglz.models.Subject;
import trianglz.models.Teacher;
import trianglz.ui.activities.ChatActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.NewMessageAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 11/09/2019.
 */
public class NewMessageFragment extends Fragment implements View.OnClickListener,
        NewMessagePresenter, NewMessageAdapter.NewMessageAdapterInterface {
    private View rootView;
    private StudentMainActivity activity;
    private Student student;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private NewMessageView newMessageView;
    private NewMessageAdapter newMessageAdapter;
    private TextView subjectHeaderTextView, teacherHeaderTextView;
    private ArrayList<Object> subjectObjectArrayList;
    private ArrayList<MessageThread> messageThreadArrayList;
    private Subject selectedSubject;
    private SwipeRefreshLayout pullRefreshLayout;
    private SkeletonScreen skeletonScreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_new_message2, container, false);
        activity = (StudentMainActivity) getActivity();
        //    onBackPress();
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        getCourseGroups();
    }

    private void getCourseGroups() {
        if (Util.isNetworkAvailable(activity)) {
            showShimmer();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getCourseGroups(student.childId);
            newMessageView.getCourseGroups(url);
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }

    private void showShimmer() {
        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(newMessageAdapter)
                .load(R.layout.skeleton_new_message_layout)
                .count(16)
                .angle(45)
                .duration(500)
                .color(R.color.white)
                .show();
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        newMessageView = new NewMessageView(activity, this);
        newMessageAdapter = new NewMessageAdapter(activity, this, NewMessageAdapter.Type.SUBJECT);
        recyclerView.setAdapter(newMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        subjectHeaderTextView = rootView.findViewById(R.id.tv_header_subject);
        teacherHeaderTextView = rootView.findViewById(R.id.tv_header_teacher);
        subjectObjectArrayList = new ArrayList<>();
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        subjectHeaderTextView.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCourseGroups();
            }
        });
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = Student.create(bundle.getString(Constants.STUDENT));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.toolbarView.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.tv_header_subject:
                if (subjectHeaderTextView.getText().toString() != getResources().getString(R.string.select_a_course)) {
                    newMessageAdapter.subjectName = "";
                    teacherHeaderTextView.setVisibility(View.GONE);
                    subjectHeaderTextView.setText(getResources().getString(R.string.select_a_course));
                    subjectHeaderTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                    newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
                }
                break;
        }
    }

    @Override
    public void onGetCourseGroupsSuccess(ArrayList<Subject> subjectArrayList) {
        subjectObjectArrayList.addAll(subjectArrayList);
        newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
        skeletonScreen.hide();
        pullRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onGetCourseGroupsFailure(String message, int errorCode) {
        skeletonScreen.hide();
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onSubjectSelected(int position) {
        if (newMessageAdapter.type.equals(NewMessageAdapter.Type.SUBJECT)) {
            if (((Subject) newMessageAdapter.mDataList.get(position)).courseName.equals(subjectHeaderTextView.getText().toString())) {
                newMessageAdapter.subjectName = "";
                teacherHeaderTextView.setVisibility(View.GONE);
                subjectHeaderTextView.setText(getResources().getString(R.string.select_a_course));
                subjectHeaderTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
            } else {
                selectedSubject = ((Subject) newMessageAdapter.mDataList.get(position));
                subjectHeaderTextView.setText(((Subject) newMessageAdapter.mDataList.get(position)).courseName);
                teacherHeaderTextView.setVisibility(View.VISIBLE);
                subjectHeaderTextView.setTextColor(getResources().getColor(R.color.gunmetal));
                newMessageAdapter.subjectName = ((Subject) newMessageAdapter.mDataList.get(position)).courseName;
                ArrayList<Object> teacherObjectArrayList = new ArrayList<>();
                teacherObjectArrayList.addAll(((Subject) newMessageAdapter.mDataList.get(position)).teacherArrayList);
                newMessageAdapter.addData(teacherObjectArrayList, NewMessageAdapter.Type.TEACHER);

            }
        }
    }

    @Override
    public void onTeacherSelected(int position) {
        Teacher teacher = (Teacher) newMessageAdapter.mDataList.get(position);
        openChatActivity(teacher.id, selectedSubject.id);
    }

    private void openChatActivity(int teacherId, int courseId) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(Constants.KEY_TEACHER_ID, teacherId);
        intent.putExtra(Constants.KEY_COURSE_ID, courseId);
        startActivity(intent);
    }
}
