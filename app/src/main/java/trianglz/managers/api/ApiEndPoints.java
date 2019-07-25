package trianglz.managers.api;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class ApiEndPoints {
    //Base url
    public static final String BASE_URL = "https://bedopedia-schools.herokuapp.com/";
    //SCHOOL_CODE_URL
    public static final String SCHOOL_CODE_BASE_URL = BASE_URL + "schools/get_by_code";

    public static String averageGradeEndPoint(int courseId, int courseGroupId){
        return "/api/courses/"+ courseId +"/course_groups/"+ courseGroupId+"/student_grade";
    }

    public static String studentGradeBook (int courseId, int courseGroupId){
       return  "/api/courses/"+ courseId +"/course_groups/"+ courseGroupId +"/student_grade_book";
    }

    public static String showAssignment(int courseId, int assignmentId) {
        return "/api/courses/" + courseId + "/assignments/" + assignmentId;
    }

    public static String getquizzesCourses(int studentId) {
        return "/api/students/" + studentId + "/course_groups_with_quizzes_number";
    }

    public static String getQuizzesDetails(int studentId, int courseId) {
        return "/api/students/" + studentId + "/quizzes?course_group_ids=["+courseId+"]";
    }

    public static String postsApi(int id) {
        return  "/api/students/"+ id +"/course_groups_recent_posts";
    }
    public static String postsDetailsApi(int courseId) {
        return "/api/posts?access_by_entity=Course+Group+Posts&course_group_id=" + courseId;
    }

    public static String postReply() {
        return "/api/comments";
    }
    public static String getSemesters (){
        return  "/api/grading_periods/course_grading_periods";
    }

    public static String getThreads(){
        return "/api/threads";
    }

    public static String getCourseGroups(int studentId){
        return "/api/students/" + studentId + "/course_groups";
    }

    public static String getSendMessageUrl(int threadId){
        return "/api/threads/" + threadId;
    }
    public static String getSendImageId (int threadId){
        return "/api/threads/" + threadId + "/messages";
    }

    public static String setAsSeen(String userId){
        return "/api/users/"+ userId +"/notifications/mark_as_seen";
    }

    public static String getAnnouncementUrl(int pageNumber, String user_type, int numberPerPage){

    return "/api/announcements?order_by_end_at=asc&page="+pageNumber+"&per_page="+numberPerPage+"&running_announcement=true&user_type="+user_type;

    }


    public static String getSetReadThreadUrl(){
        return "/api/thread_participants/bulk_mark_as_read";
    }

    public static String getWeeklyPlanerUrl(String date){
        return "/api/weekly_plans?search_by_date=" + date;
    }

}
