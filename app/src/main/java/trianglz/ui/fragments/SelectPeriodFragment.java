package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.SelectPeriodPresenter;
import trianglz.core.views.SelectPeriodView;
import trianglz.models.GradingPeriod;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.adapters.GradingPeriodsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectPeriodFragment extends Fragment implements SelectPeriodPresenter, GradingPeriodsAdapter.GradingPeriodsInterface, View.OnClickListener {
    private SelectPeriodView selectPeriodView;
    private PostsResponse courseGroup;
    private Student student;
    private GradingPeriodsAdapter gradingPeriodsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Object> gradingPeriodsArray;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private GradingPeriod selectedGradingPeriod;
    private TextView selectSemesterTextView, selectQuarterTextView;
    private ImageButton backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_period, container, false);
        getValueFromIntent();
        bindViews(rootView);
        setListeners();
        selectPeriodView.getGradingPeriods(courseGroup.getCourseId());
        return rootView;
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        selectSemesterTextView.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroup = (PostsResponse) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }
    private void bindViews(View rootView) {
        gradingPeriodsArray = new ArrayList<>();
        recyclerView = rootView.findViewById (R.id.recycler_view);
        selectSemesterTextView = rootView.findViewById(R.id.tv_header_semester);
        selectQuarterTextView = rootView.findViewById(R.id.tv_header_quarter);
        backBtn = rootView.findViewById(R.id.btn_back);
        studentImageView = rootView.findViewById(R.id.img_student);
        gradingPeriodsAdapter = new GradingPeriodsAdapter(getActivity(), this, GradingPeriodsAdapter.Period.SEMESTER);
        recyclerView.setAdapter(gradingPeriodsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, getActivity()), false));
        selectPeriodView = new SelectPeriodView(this);
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
    }
    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(getActivity())
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
    public void onGetGradingPeriodsSuccess(ArrayList<GradingPeriod> gradingPeriods) {
        gradingPeriodsArray.addAll(gradingPeriods);
        gradingPeriodsAdapter.addData(gradingPeriodsArray, GradingPeriodsAdapter.Period.SEMESTER);
    }

    @Override
    public void onGetGradingPeriodsFailure(String message, int errorCode) {

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (getParentFragment() != null) getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.tv_header_semester:
                if (selectSemesterTextView.getText().toString() != getResources().getString(R.string.select_a_semester)) {
                    gradingPeriodsAdapter.subjectName = "";
                    selectQuarterTextView.setVisibility(View.GONE);
                    selectSemesterTextView.setText(getResources().getString(R.string.select_a_semester));
                    selectSemesterTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                    gradingPeriodsAdapter.addData(gradingPeriodsArray, GradingPeriodsAdapter.Period.SEMESTER);
                }
                break;
        }
    }
    @Override
    public void onSemesterSelected(int position, boolean noQuarters) {
        if (noQuarters) {
            Toast.makeText(getActivity(), "semester but no quarters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (gradingPeriodsAdapter.type.equals(GradingPeriodsAdapter.Period.SEMESTER)) {
            if (((GradingPeriod) gradingPeriodsAdapter.mDataList.get(position)).name.equals(selectSemesterTextView.getText().toString())) {
                gradingPeriodsAdapter.subjectName = "";
                selectQuarterTextView.setVisibility(View.GONE);
                selectSemesterTextView.setText(getResources().getString(R.string.select_a_semester));
                selectSemesterTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                gradingPeriodsAdapter.addData(gradingPeriodsArray, GradingPeriodsAdapter.Period.SEMESTER);
            } else {
                selectedGradingPeriod = ((GradingPeriod) gradingPeriodsAdapter.mDataList.get(position));
                selectSemesterTextView.setText(((GradingPeriod) gradingPeriodsAdapter.mDataList.get(position)).name);
                selectQuarterTextView.setVisibility(View.VISIBLE);
                selectSemesterTextView.setTextColor(getResources().getColor(R.color.gunmetal));
                gradingPeriodsAdapter.subjectName = ((GradingPeriod) gradingPeriodsAdapter.mDataList.get(position)).name;
                ArrayList<Object> teacherObjectArrayList =
                        new ArrayList<>(((GradingPeriod) gradingPeriodsAdapter.mDataList.get(position)).subGradingPeriods);
                gradingPeriodsAdapter.addData(teacherObjectArrayList, GradingPeriodsAdapter.Period.QUARTER);

            }
    }
    }

    @Override
    public void onQuarterSelected(int position) {
        Toast.makeText(getActivity(), "quarter clicked", Toast.LENGTH_SHORT).show();
    }
}
