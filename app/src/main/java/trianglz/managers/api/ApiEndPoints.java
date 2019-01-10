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
        return "/api/threads/"+threadId;
    }
    public static String setAsSeen(String userId){
        return "/api/users/"+ userId +"/notifications/mark_as_seen";
    }

    public static String getNotificationUrl(int pageNumber,String user_type){

    return "/api/announcements?order_by_end_at=asc&page="+pageNumber+"&per_page=10&running_announcement=true&user_type="+user_type;

    }

}
