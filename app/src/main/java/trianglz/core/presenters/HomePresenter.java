package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Student;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public interface HomePresenter {
    void onGetStudentsHomeSuccess(ArrayList<Student> studentArrayList);
    void onGetStudentsHomeFailure();
}
