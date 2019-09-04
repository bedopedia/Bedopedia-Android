package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.CourseGroup;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradesAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradesFragment extends Fragment implements GradesAdapter.GradesAdapterInterface, View.OnClickListener {

    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private RecyclerView recyclerView;
    private GradesAdapter gradesAdapter;
    private Student student;
    private ArrayList<CourseGroup> courseGroups;
    private IImageLoader imageLoader;
    private StudentMainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_grades, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        onBackPress();
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            this.courseGroups = (ArrayList<CourseGroup>) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
        }
//        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
//        this.courseGroups = (ArrayList<CourseGroup>) getIntent().getBundleExtra(Constants.KEY_BUNDLE)
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        studentImageView = rootView.findViewById(R.id.img_student);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        gradesAdapter = new GradesAdapter(activity, this);
        imageLoader = new PicassoLoader();
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
        recyclerView.setAdapter(gradesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        gradesAdapter.addData(courseGroups);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
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
                //    activity.toolbarView.setVisibility(View.VISIBLE);
                //    activity.headerLayout.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onSubjectSelected(int position) {
        openGradeDetailFragment(courseGroups.get(position));

    }

    private void openGradeDetailFragment(CourseGroup courseGroup) {

        GradeDetailFragment gradeDetailFragment = new GradeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, courseGroup);
        bundle.putSerializable(Constants.STUDENT, student);
        gradeDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, gradeDetailFragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

}
