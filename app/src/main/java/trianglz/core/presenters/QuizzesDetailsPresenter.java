package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Meta;
import trianglz.models.Quizzes;

// created by gemy
public interface QuizzesDetailsPresenter {
    void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes, Meta meta);
    void onGetQuizzesDetailsFailure(String message, int errorCode);
}
