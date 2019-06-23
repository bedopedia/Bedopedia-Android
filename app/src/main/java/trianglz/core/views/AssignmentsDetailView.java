package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import trianglz.core.presenters.AssignmentsDetailPresenter;


/**
 * Created by ${Aly} on 4/22/2019.
 */
public class AssignmentsDetailView {
    private Context context;
    private Gson gson;
    private AssignmentsDetailPresenter presenter;

    public AssignmentsDetailView(Context context, AssignmentsDetailPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        gson = new Gson();
    }

}
