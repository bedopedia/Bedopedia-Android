package trianglz.managers.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import trianglz.managers.SessionManager;
import trianglz.managers.network.HandleArrayResponseListener;
import trianglz.managers.network.HandleResponseListener;
import trianglz.managers.network.NetworkManager;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class UserManager {

    public static void getSchoolUrl(String url, String code, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_CODE, code);
        NetworkManager.getWithParameter(url + "", paramsHashMap, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }


    public static void login(String url, String email, String password, final ResponseListener responseListener) {
        HashMap<String, String> hashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        try {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                jsonObject.put(Constants.KEY_USERNAME,email);
            }else {
                jsonObject.put(Constants.KEY_EMAIL, email);
            }
            jsonObject.put(Constants.KEY_PASSWORD, password);
            jsonObject.put(Constants.KEY_MOBILE, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.postLogin(url + "", jsonObject, hashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }
    public static void getRecentPosts(int id, final ArrayResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postsApi(id);
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getTeacherCourses(String teacherId, final ArrayResponseListener arrayResponseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getTeacherCourses(teacherId);
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message,errorCode);
            }
        });
    }

    public static void postReply(String message, String userId, int postId, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postReply();
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        JSONObject comment = new JSONObject();
        try {
            comment.put(Constants.CONTENT, message);
            comment.put(Constants.KEY_OWNER_ID, userId);
            comment.put(Constants.POST_ID, postId);
            jsonObject.put(Constants.KEY_COMMENT, comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, jsonObject, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }
    public static void getPostDetails (int courseId, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postsDetailsApi(courseId);
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.get(url, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }
    public static void updateToken(String url, String token,String locale, final ResponseListener responseListener) {
        HashMap<String, String> hashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject params = new JSONObject();
        JSONObject tokenJson = new JSONObject();

        try {
            tokenJson.put(Constants.MOBILE_DEVICE_TOKEN, token);
            tokenJson.put(Constants.KEY_LOCALE, locale);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put(Constants.USER, tokenJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.put(url, params, hashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getStudentsHome(String url, String id, final ArrayResponseListener arrayResponseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.PARENT_ID, id);
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getNotifications(String url, int pageNumber,String numberPerPage ,final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", pageNumber + "");
        params.put("per_page" , numberPerPage);
        NetworkManager.getWithParameter(url, params, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }


    public static void getStudentCourse(String url, final ArrayResponseListener arrayResponseListener) {
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message,errorCode);
            }
        });
    }


    public static void getStudentGrades(String url, final ResponseListener responseListener) {
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
       NetworkManager.getWithParameter(url, headerHashMap, paramsHashMap, new HandleResponseListener() {
           @Override
           public void onSuccess(JSONObject response) {
               responseListener.onSuccess(response);
           }

           @Override
           public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
           }
       });
    }


    public static void getStudentTimeTable(String url, final ArrayResponseListener arrayResponseListener) {
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message,errorCode);
            }
        });
    }

    public static void getStudentBehaviourNotes(String url,String studentId, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_STUDENT_ID , studentId);
        paramsHashMap.put(Constants.KEY_USER_TYPE ,"Parents");
        NetworkManager.getWithParameter(url, paramsHashMap, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }


    public static void getWeeklyPlanner(String url, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        NetworkManager.get(url, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }

    public static void getAverageGrades(String url,String id, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_STUDENT_ID,id);
        NetworkManager.getWithParameter(url,paramsHashMap,headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }

    public static void getStudentGradeBook(String url, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getWithParameter(url,paramsHashMap,headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }

    public static void getSemesters(String url, String id, final ArrayResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_COURSE_ID,id);
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message,errorCode);
            }
        });
    }

    public static void getMessages(String url, String id, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_USER_ID,id);
        NetworkManager.getWithParameter(url, paramsHashMap, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message,errorCode);
            }
        });
    }

    public static void getCourseGroups(String url, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_SOURCE, Constants.KEY_HOME);
        NetworkManager.getJsonArray(url + "", paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void sendMessage(String url, String body, String messageThreadId, String userId,
                                   String id, String title, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        JSONObject messageThreadJsonObject = new JSONObject();
        JSONArray messageAttributesJsonArray = new JSONArray();
        JSONObject messageAttributesJsonObject = new JSONObject();
        try {
            messageAttributesJsonObject.put(Constants.KEY_BODY,body);
            messageAttributesJsonObject.put(Constants.KEY_MESSAGE_THREAD_ID, messageThreadId);
            messageAttributesJsonObject.put(Constants.KEY_USER_ID, userId);
            messageAttributesJsonArray.put(messageAttributesJsonObject);
            messageThreadJsonObject.put(Constants.KEY_MESSAGE_ATTRIBUTES,messageAttributesJsonArray);
            messageThreadJsonObject.put(Constants.KEY_ID,id);
            messageThreadJsonObject.put(Constants.KEY_TITLE,title);
            parametersJsonObject.put(Constants.KEY_MESSAGE_THREAD,messageThreadJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.put(url,parametersJsonObject,headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }

    public static void showAssignment (String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        NetworkManager.get(url, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getQuizzesCourses(String url, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        NetworkManager.getJsonArray(url, params, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message,errorCode);
            }
        });
    }

    public static void getQuizzesDetails(int studentId, int courseId, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizzesDetails(studentId, courseId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        NetworkManager.get(url, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
            responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getTeacherQuizzes(String courseGroupId, final ArrayResponseListener arrayResponseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getGetTeacherQuizzes(courseGroupId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        NetworkManager.getJsonArray(url, params, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void sendImage(String url, String fileName, URI uri, final ResponseListener responseListener ) {
        HashMap<String, String> headersValues = SessionManager.getInstance().getHeaderHashMap();
        File image = new File(uri);
        NetworkManager.postImageFile(url, fileName, image, headersValues, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void sendFirstMessage(String url,String teacherId,String userId,String body,String courseId , final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        JSONObject messageThreadJsonObject = new JSONObject();
        JSONArray messageAttributesJsonArray = new JSONArray();
        JSONObject messageAttributesJsonObject = new JSONObject();
        String[] userIds = new String[]{teacherId,userId};
        ArrayList<String> list = new ArrayList<String>();
        list.add(teacherId);
        list.add(userId);

        try {
            messageAttributesJsonObject.put(Constants.KEY_BODY,body);
            messageAttributesJsonObject.put(Constants.KEY_USER_ID, userId);
            messageAttributesJsonArray.put(messageAttributesJsonObject);
            messageThreadJsonObject.put(Constants.KEY_MESSAGE_ATTRIBUTES,messageAttributesJsonArray);
            messageThreadJsonObject.put(Constants.KEY_COURSE_ID,courseId);
            messageThreadJsonObject.put(Constants.KEY_NAME,".");
            messageThreadJsonObject.put(Constants.KEY_TAG,".");
            parametersJsonObject.put(Constants.KEY_MESSAGE_THREAD,messageThreadJsonObject);
            parametersJsonObject.put(Constants.KEY_USER_IDS,new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url,parametersJsonObject,headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });

    }


    public static void setAsSeen(String url , final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        NetworkManager.postSeenNotification(url, null, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });

    }


    public static void getAnnouncements(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        NetworkManager.getWithParameter(url, params, headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }


    public static void setAsSeenThread(String url,int threadId, final ResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(threadId);
        try {
            parametersJsonObject.put(Constants.KEY_THREADS_IDS,new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url,parametersJsonObject,headerHashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }


    public static void getCourseAssignment(String url ,final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message,errorCode);
            }
        });

    }


    public static void getAssignmentDetail(String url ,final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message,errorCode);
            }
        });

    }

    public static void getAssignmentSubmissions(int courseId, int courseGroupId, int assignmentId, final ArrayResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }
}