package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.GradingPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Feedback;
import trianglz.models.GradeModel;
import trianglz.models.StudentSubmission;

public class GradingView {
    Context context;
    GradingPresenter gradingPresenter;

    public GradingView(Context context, GradingPresenter gradingPresenter) {
        this.context = context;
        this.gradingPresenter = gradingPresenter;
    }

    public void getAssignmentSubmissions(int courseId, int courseGroupId, int assignmentId) {
        UserManager.getAssignmentSubmissions(courseId, courseGroupId, assignmentId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                ArrayList<StudentSubmission> submissions = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    submissions.add(StudentSubmission.create(responseArray.opt(i).toString()));
                }
                gradingPresenter.onGetAssignmentSubmissionsSuccess(submissions);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradingPresenter.onGetAssignmentSubmissionsFailure(message, errorCode);
            }
        });
    }

    public void postAssignmentGrade(GradeModel gradeModel) {
        UserManager.postAssignmentGrade(gradeModel, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                gradingPresenter.onPostAssignmentGradeSuccess(StudentSubmission.create(response.toString()));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradingPresenter.onPostAssignmentGradeFailure(message, errorCode);
            }
        });
    }
    public void postQuizGrade(GradeModel gradeModel) {
        UserManager.postQuizGrade(gradeModel, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                gradingPresenter.onPostQuizGradeSuccess(StudentSubmission.create(response.toString()));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradingPresenter.onPostQuizGradeFailure(message, errorCode);
            }
        });
    }

    public void getQuizzesSubmissions(int quizId) {
        UserManager.getQuizzesSubmissions(quizId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                ArrayList<StudentSubmission> studentSubmissions = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    studentSubmissions.add(StudentSubmission.create(responseArray.opt(i).toString()));
                }
                gradingPresenter.onGetQuizzesSubmissionsSuccess(studentSubmissions);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradingPresenter.onGetQuizzesSubmissionsFailure(message, errorCode);
            }
        });
    }

    public void postSubmissionFeedback(Feedback feedback) {
        UserManager.postSubmissionFeedback(feedback, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                gradingPresenter.onPostFeedbackSuccess(Feedback.create(response.toString()));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                gradingPresenter.onPostFeedbackFailure(message, errorCode);
            }
        });
    }
}
