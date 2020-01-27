package trianglz.core.views;

import android.content.Context;

import trianglz.core.presenters.GradeDetailPresenter;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailView {

    private Context context;
    private GradeDetailPresenter presenter;

    public GradeDetailView (Context context, GradeDetailPresenter gradeDetailPresenter) {
        this.context = context;
        this.presenter = gradeDetailPresenter;
    }


}
