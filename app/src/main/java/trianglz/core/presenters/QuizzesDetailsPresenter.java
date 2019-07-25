package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Quizzes;

// created by gemy
public interface QuizzesDetailsPresenter {
    void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes);
    void onGetQuizzesDetailsFailure();
}
