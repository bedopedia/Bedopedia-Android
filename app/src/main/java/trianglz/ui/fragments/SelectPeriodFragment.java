package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.SelectPeriodPresenter;
import trianglz.core.views.SelectPeriodView;
import trianglz.models.GradingPeriod;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectPeriodFragment extends Fragment implements SelectPeriodPresenter {
    private SelectPeriodView selectPeriodView;
    private PostsResponse courseGroup;
    private Student student;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getValueFromIntent();
        bindViews();
        return inflater.inflate(R.layout.fragment_select_period, container, false);
    }
    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroup = (PostsResponse) bundle.getSerializable(Constants.KEY_COURSE_GROUPS);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }
    private void bindViews() {
        selectPeriodView = new SelectPeriodView(this);
        selectPeriodView.getGradingPeriods(courseGroup.getCourseId());
    }

    @Override
    public void onGetGradingPeriodsSuccess(ArrayList<GradingPeriod> gradingPeriods) {

    }

    @Override
    public void onGetGradingPeriodsFailure(String message, int errorCode) {

    }
}
