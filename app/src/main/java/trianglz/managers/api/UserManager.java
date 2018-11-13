package trianglz.managers.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            jsonObject.put(Constants.KEY_EMAIL, email);
            jsonObject.put(Constants.KEY_PASSWORD, password);
            jsonObject.put(Constants.KEY_MOBILE, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.post(url + "", jsonObject, hashMap, new HandleResponseListener() {
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

    public static void updateToken(String url, String token, final ResponseListener responseListener) {
        HashMap<String, String> hashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject params = new JSONObject();
        JSONObject tokenJson = new JSONObject();
        try {
            tokenJson.put(Constants.MOBILE_DEVICE_TOKEN, token);
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
                SessionManager.getInstance().setTokenChangedValue(false);
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                SessionManager.getInstance().setTokenChangedValue(true);
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

    public static void getNotifications(String url, int pageNumber, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", pageNumber + "");
        params.put("per_page" , "20");
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
        paramsHashMap.put(Constants.KEY_USER_TYPE , Constants.KEY_PARENTS);
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

    public static void getMessages(String url, String id, final ArrayResponseListener responseListener){
        HashMap<String,String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String,String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_USER_ID,id);
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
}