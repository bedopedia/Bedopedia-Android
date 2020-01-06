package trianglz.managers.api;

import android.net.Uri;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class ApiEndPoints {
    //Base url
    public static final String BASE_URL = "https://bedopedia-schools.herokuapp.com/";
    //SCHOOL_CODE_URL
    public static final String SCHOOL_CODE_BASE_URL = BASE_URL + "schools/get_by_code";

    public static String averageGradeEndPoint(int courseId, int courseGroupId) {
        return "/api/courses/" + courseId + "/course_groups/" + courseGroupId + "/student_grade";
    }

    public static String studentGradeBook(int courseId, int courseGroupId, int studentId) {
        return "/api/courses/" + courseId + "/course_groups/" + courseGroupId + "/student_grade_book?student_id=" + studentId;
    }

    public static String showAssignment(int courseId, int assignmentId) {
        return "/api/courses/" + courseId + "/assignments/" + assignmentId;
    }

    public static String getAssignmentSubmissions(int courseId, int courseGroupId, int assignmentId) {
        return "/api/courses/" + courseId + "/course_groups/" + courseGroupId + "/assignments/" + assignmentId + "/submissions";
    }

    public static String postAssignmentGrade(int courseId, int courseGroupId, int assignmentId) {
        return "/api/courses/" + courseId + "/course_groups/" + courseGroupId + "/assignments/" + assignmentId + "/student_grade";
    }

    public static String postQuizGrade(int courseId, int courseGroupId, int quizId) {
        return "/api/courses/" + courseId + "/course_groups/" + courseGroupId + "/quizzes/" + quizId + "/student_grade";
    }

    public static String postSubmissionFeedback() {
        return "/api/feedbacks";
    }

    public static String getQuizzesCourses(int studentId) {
        return "/api/students/" + studentId + "/course_groups_with_quizzes_number";
    }

    public static String getQuizzesDetails(int studentId, int courseId, int page) {
        return "/api/students/" + studentId + "/quizzes?page=" + page + "&per_page=20&course_group_ids=[" + courseId + "]";
    }

    public static String getTeacherQuizzes(String courseGroupId) {
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("api")
                .appendPath("quizzes")
                .appendQueryParameter("fields%5Bgrading_period_lock%5D", "true")
                .appendQueryParameter("fields%5Bid%5D", "true")
                .appendQueryParameter("fields%5Blesson_id%5D", "true")
                .appendQueryParameter("fields%5Bname%5D", "true")
                .appendQueryParameter("fields%5Bstart_date%5D", "true")
                .appendQueryParameter("fields%5Bstate%5D", "true")
                .appendQueryParameter("fields%5Bstudent_solve%5D", "true")
                .appendQueryParameter("course_group_ids[]", courseGroupId);
        url = builder.toString();
        return url;
    }

    public static String getQuizzesSubmissions(int quizId, int courseGroupId) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("api")
                .appendPath("quizzes")
                .appendPath(quizId + "")
                .appendPath("submissions")
                .appendQueryParameter("course_group_id", courseGroupId + "");
        return builder.toString();

    }

    public static String postsApi(int id) {
        return "/api/students/" + id + "/course_groups_recent_posts";
    }

    public static String postsDetailsApi(int courseId, int page) {
        return "/api/posts?access_by_entity=Course+Group+Posts&course_group_id=" + courseId + "&page=" + page + "&per_page=10";
    }

    public static String postReply() {
        return "/api/comments";
    }

    public static String getSemesters() {
        return "/api/grading_periods/course_grading_periods";
    }

    public static String getThreads() {
        return "/api/threads";
    }

    public static String getSingleThread(int threadId) {
        return "/api/threads/" + threadId + "/messages";
    }

    public static String getCourseGroups(int studentId) {
        return "/api/students/" + studentId + "/course_groups";
    }

    public static String getTeacherCourses(String teacherActableId) {
        return "/api/teachers/" + teacherActableId + "/courses";
    }

    public static String getSendMessageUrl(int threadId) {
        return "/api/threads/" + threadId;
    }

    public static String getSendImageId(int threadId) {
        return "/api/threads/" + threadId + "/messages";
    }

    public static String setAsSeen(String userId) {
        return "/api/users/" + userId + "/notifications/mark_as_seen";
    }

    public static String getAnnouncementUrl(int pageNumber, String user_type, int numberPerPage) {

        return "/api/announcements?order_by_end_at=asc&page=" + pageNumber + "&per_page=" + numberPerPage + "&running_announcement=true&user_type=" + user_type;

    }


    public static String getSetReadThreadUrl() {
        return "/api/thread_participants/bulk_mark_as_read";
    }

    public static String getWeeklyPlanerUrl(String date) {
        return "/api/weekly_plans?search_by_date=" + date;
    }

    public static String getEvents(int userId, String type, String endDate, String startDate) {
        return "/api/events?by_subscriber%5Bsubscriber_id%5D=" + userId + "&by_subscriber%5Bsubscriber_type%5D=" + type + "&start_date_between%5Bend_date%5D=" + endDate + "&start_date_between%5Bstart_date%5D=" + startDate;
    }

    public static String createEvent() {
        return "/api/events";

    }

    public static String createPostCourseGroup() {
        return "/api/posts";
    }

    public static String attachFiletoPost() {
        return "/api/posts/create_uploaded_file_for_posts";
    }

    public static String getFullDayAttendance(int courseGroupId, int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        return "/api/course_groups/" + courseGroupId + "/attendances?by_period%5Bend_date%5D=" + endDay + "%2F" + endMonth + "%2F" + endYear + "&by_period%5Bstart_date%5D=" + startDay + "%2F" + startMonth + "%2F" + startYear;
    }

    public static String getPerSlotAttendance(int courseGroupId, int day, int month, int year) {
        return "/api/course_groups/" + courseGroupId + "/attendances?by_slots=" + day + "%2F" + month + "%2F" + year;
    }

    public static String createBatchAttendance() {
        return "/api/attendances/batch_create";
    }

    public static String deleteBatchAttendance() {
        return "/api/attendances/batch_destroy";
    }

    public static String getQuizQuestions(int quizId) {
        return "/api/quizzes/" + quizId;
    }

    public static String createQuizSubmission() {
        return "/api/active_quizzes/create_submission";
    }

    public static String getQuizSolveDetails(int quizId) {
        return "/api/quizzes/" + quizId + "/quiz_solve_details";
    }

    public static String postAnswerSubmission() {
        return "/api/answer_submissions";
    }

    public static String getAnswerSubmission(int submissionId) {
        return "/api/answer_submissions?by_quiz_sumbission=" + submissionId;
    }

    public static String deleteAnswerSubmission() {
        return "/api/answer_submissions/remove_answer_submission";
    }

    public static String submitQuiz() {
        return "/api/active_quizzes/submit_quiz";
    }

    public static String changePassword(int userId) {
        return "/api/users/" + userId;
    }
}
