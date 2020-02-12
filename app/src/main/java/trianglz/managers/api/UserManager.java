package trianglz.managers.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import trianglz.managers.SessionManager;
import trianglz.managers.network.HandleArrayResponseListener;
import trianglz.managers.network.HandleMultiPartResponseListener;
import trianglz.managers.network.HandleResponseListener;
import trianglz.managers.network.NetworkManager;
import trianglz.models.AnswerSubmission;
import trianglz.models.Answers;
import trianglz.models.Feedback;
import trianglz.models.GradeModel;
import trianglz.utils.Constants;
import trianglz.utils.Util;

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
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                jsonObject.put(Constants.KEY_USERNAME, email);
            } else {
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
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void getGradesCourses(int studentId, final ArrayResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.gradesCourses(studentId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        String headerMap = Util.convertHeaderMapToBulk(headerHashMap);
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void getGradesDetails(int studentId, int courseId, int courseGroupId,int gradingPeriodId, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.gradesDetails(courseId, courseGroupId, studentId, gradingPeriodId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        headerHashMap.put("Mobile-Version", "application/vnd.skolera.v1");
        HashMap<String, String> paramsHashMap = new HashMap<>();
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
    public static void getTeacherCourses(String teacherId, final ArrayResponseListener arrayResponseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getTeacherCourses(teacherId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void postReply(String message, String userId, int postId, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postReply();
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
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

    public static void getPostDetails(int courseId, int page, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postsDetailsApi(courseId, page);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void updateToken(String url, String token, String locale, final ResponseListener responseListener) {
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

    public static void getNotifications(String url, int pageNumber, String numberPerPage, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", pageNumber + "");
        params.put("per_page", numberPerPage);
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
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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


    public static void getStudentGrades(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void getStudentBehaviourNotes(String url, String studentId, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_STUDENT_ID, studentId);
        paramsHashMap.put(Constants.KEY_USER_TYPE, "Parents");
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


    public static void getWeeklyPlanner(String url, final ResponseListener responseListener) {
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

    public static void getAverageGrades(String url, String id, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_STUDENT_ID, id);
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

    public static void getStudentGradeBook(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
//        headerHashMap.put("Accept","application/vnd.skolera.v1");
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void getSemesters(String url, String id, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_COURSE_ID, id);
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

    public static void getMessages(String url, String id, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_USER_ID, id);
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
    public static void getSingleThread(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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
                                   String id, String title, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        JSONObject messageThreadJsonObject = new JSONObject();
        JSONArray messageAttributesJsonArray = new JSONArray();
        JSONObject messageAttributesJsonObject = new JSONObject();
        try {
            messageAttributesJsonObject.put(Constants.KEY_BODY, body);
            messageAttributesJsonObject.put(Constants.KEY_MESSAGE_THREAD_ID, messageThreadId);
            messageAttributesJsonObject.put(Constants.KEY_USER_ID, userId);
            messageAttributesJsonArray.put(messageAttributesJsonObject);
            messageThreadJsonObject.put(Constants.KEY_MESSAGE_ATTRIBUTES, messageAttributesJsonArray);
            messageThreadJsonObject.put(Constants.KEY_ID, id);
            messageThreadJsonObject.put(Constants.KEY_TITLE, title);
            parametersJsonObject.put(Constants.KEY_MESSAGE_THREAD, messageThreadJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.put(url, parametersJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void showAssignment(String url, final ResponseListener responseListener) {
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
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getQuizzesDetails(int studentId, int courseId, int page, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizzesDetails(studentId, courseId, page);
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
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getTeacherQuizzes(courseGroupId);
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

    public static void getQuizzesSubmissions(int quizId, int courseGroupId, final ArrayResponseListener arrayResponseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizzesSubmissions(quizId, courseGroupId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        String headers = Util.convertHeaderMapToBulk(headerHashMap);
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

    public static void sendImage(String url, String fileName, URI uri, final ResponseListener responseListener) {
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

    public static void sendFirstMessage(String url, String teacherId, String userId, String body, String courseId, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        JSONObject messageThreadJsonObject = new JSONObject();
        JSONArray messageAttributesJsonArray = new JSONArray();
        JSONObject messageAttributesJsonObject = new JSONObject();
        String[] userIds = new String[]{teacherId, userId};
        ArrayList<String> list = new ArrayList<String>();
        list.add(teacherId);
        list.add(userId);

        try {
            messageAttributesJsonObject.put(Constants.KEY_BODY, body);
            messageAttributesJsonObject.put(Constants.KEY_USER_ID, userId);
            messageAttributesJsonArray.put(messageAttributesJsonObject);
            messageThreadJsonObject.put(Constants.KEY_MESSAGE_ATTRIBUTES, messageAttributesJsonArray);
            messageThreadJsonObject.put(Constants.KEY_COURSE_ID, courseId);
            messageThreadJsonObject.put(Constants.KEY_NAME, ".");
            messageThreadJsonObject.put(Constants.KEY_TAG, ".");
            parametersJsonObject.put(Constants.KEY_MESSAGE_THREAD, messageThreadJsonObject);
            parametersJsonObject.put(Constants.KEY_USER_IDS, new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, parametersJsonObject, headerHashMap, new HandleResponseListener() {
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


    public static void setAsSeen(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
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


    public static void setAsSeenThread(String url, int threadId, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject parametersJsonObject = new JSONObject();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(threadId);
        try {
            parametersJsonObject.put(Constants.KEY_THREADS_IDS, new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, parametersJsonObject, headerHashMap, new HandleResponseListener() {
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


    public static void getCourseAssignment(String url, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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


    public static void getAssignmentDetail(String url, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void getEvents(String url, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void createEvent(String url, Date startDate, Date endDate, String type, String allDay, String title, String listenerType, int listenerId, String description, String cancel, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject eventJsonObject = new JSONObject();
        JSONObject eventAttributesJsonObject = new JSONObject();
        JSONObject listenersJsonObject = new JSONObject();
        JSONArray listenerJsonArray = new JSONArray();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String d1 = sdf.format(startDate);
        String d2 = sdf.format(endDate);
        try {
            eventAttributesJsonObject.put(Constants.KEY_START_DATE, d1);
            eventAttributesJsonObject.put(Constants.KEY_END_DATE, d2);
            eventAttributesJsonObject.put(Constants.KEY_TYPE, type);
            eventAttributesJsonObject.put(Constants.KEY_ALL_DAY, allDay);
            eventAttributesJsonObject.put(Constants.KEY_TITLE, title);
            //listeners
            listenersJsonObject.put(Constants.KEY_SUBSCRIBER_TYPE, listenerType);
            listenersJsonObject.put(Constants.KEY_SUBSCRIBER_ID, listenerId);
            listenerJsonArray.put(listenersJsonObject);

            eventAttributesJsonObject.put(Constants.KEY_SUBSCRIPTIONS_ATTRIBUTES, listenerJsonArray);
            eventAttributesJsonObject.put(Constants.KEY_DESCRIPTION, description);
            eventAttributesJsonObject.put(Constants.KEY_CANCEL, cancel);

            eventJsonObject.put(Constants.KEY_EVENT, eventAttributesJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.post(url, eventJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void getAssignmentSubmissions(int courseId, int courseGroupId, int assignmentId, final ArrayResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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

    public static void postAssignmentGrade(GradeModel gradeModel, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.postAssignmentGrade(gradeModel.getCourseId(),
                        gradeModel.getCourseGroupId(), gradeModel.getAssignmentId());
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        headerHashMap.put("Accept", "application/vnd.skolera.v1");
        String header = Util.convertHeaderMapToBulk(headerHashMap);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(gradeModel.toString());
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

    public static void postQuizGrade(GradeModel gradeModel, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() +
                ApiEndPoints.postQuizGrade(gradeModel.getCourseId(),
                        gradeModel.getCourseGroupId(), gradeModel.getQuizId());
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        headerHashMap.put("Accept", "application/vnd.skolera.v1");
        String header = Util.convertHeaderMapToBulk(headerHashMap);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(gradeModel.toString());
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

    public static void postSubmissionFeedback(Feedback feedback, final ResponseListener responseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.postSubmissionFeedback();
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(feedback.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject parentJsonObject = new JSONObject();
        try {
            parentJsonObject.putOpt(Constants.KEY_FEED_BACK, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String headers = Util.convertHeaderMapToBulk(headerHashMap);
        NetworkManager.post(url, parentJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void createTeacherPost(String url, String post, int ownerId, int courseGroupId, String postableType, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        JSONObject postJsonObject = new JSONObject();
        try {
            postJsonObject.put(Constants.CONTENT, post);
            postJsonObject.put(Constants.KEY_OWNER_ID, ownerId);
            postJsonObject.put(Constants.KEY_POSTABLE_ID, courseGroupId);
            postJsonObject.put(Constants.KEY_POSTABLE_TYPE, postableType);
            postJsonObject.put(Constants.KEY_VIDEO_PREVIEW, "");
            postJsonObject.put(Constants.KEY_VIDEO_URL, "");
            jsonObject.put(Constants.KEY_POST, postJsonObject);
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

            }
        });
    }


    public static void attachFileToTeacherPost(String url, int postId, File file, MultiPartResponseListener multiPartResponseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> multiPartHashmap = new HashMap<>();
        multiPartHashmap.put(Constants.KEY_NAME, file.getName());
        multiPartHashmap.put(Constants.KEY_POST_IDS, Integer.toString(postId));
        NetworkManager.upload(url, multiPartHashmap, file, headerHashMap, new HandleMultiPartResponseListener() {
            @Override
            public void onProgress(long uploaded, long total) {

            }

            @Override
            public void onSuccess(JSONObject response) {
                multiPartResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String s, int errorCode) {
                multiPartResponseListener.onFailure(s, errorCode);
            }
        });
    }

    public static void getTeacherAttendance(String url, final ResponseListener responseListener) {
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

    public static void createBatchAttendance(String url, String date, String comment, String status, ArrayList<Integer> studentIds, int timetableSlotId, final ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject rootJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray attendancesJsonArray = new JSONArray();
        try {
            for (int i = 0; i < studentIds.size(); i++) {
                JSONObject attendanceJson = new JSONObject();
                attendanceJson.put(Constants.KEY_DATE, date);
                attendanceJson.put(Constants.KEY_COMMENT, comment);
                attendanceJson.put(Constants.KEY_STATUS, status);
                attendanceJson.put(Constants.KEY_STUDENT_ID, studentIds.get(i));
                attendanceJson.put(Constants.TIMETABLE_SLOTS_ID, timetableSlotId);
                attendancesJsonArray.put(attendanceJson);
            }
            jsonObject.put(Constants.KEY_ATTENDANCES, attendancesJsonArray);
            rootJsonObject.put(Constants.KEY_ATTENDANCE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleArrayResponseListener() {
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

    public static void createBatchAttendance(String url, String date, String comment, String status, ArrayList<Integer> studentIds, final ArrayResponseListener arrayResponseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject rootJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray attendancesJsonArray = new JSONArray();
        try {
            for (int i = 0; i < studentIds.size(); i++) {
                JSONObject attendanceJson = new JSONObject();
                attendanceJson.put(Constants.KEY_DATE, date);
                attendanceJson.put(Constants.KEY_COMMENT, comment);
                attendanceJson.put(Constants.KEY_STATUS, status);
                attendanceJson.put(Constants.KEY_STUDENT_ID, studentIds.get(i));
                attendancesJsonArray.put(attendanceJson);
            }
            jsonObject.put(Constants.KEY_ATTENDANCES, attendancesJsonArray);
            rootJsonObject.put(Constants.KEY_ATTENDANCE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleArrayResponseListener() {
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


    public static void deleteBatchAttendance(String url, ArrayList<Integer> attendanceIds, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject rootJsonObject = new JSONObject();
        JSONArray attendanceIdsJsonArray = new JSONArray();
        try {
            for (int i = 0; i < attendanceIds.size(); i++) {
                attendanceIdsJsonArray.put(attendanceIds.get(i));
            }
            rootJsonObject.put(Constants.KEY_IDS, attendanceIdsJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void getQuizQuestions(String url, ResponseListener responseListener) {
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

    public static void createQuizSubmission(String url, int quizId, int studentId, int courseGroupId, int score, ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        JSONObject rootJsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_QUIZ_ID, quizId);
            jsonObject.put(Constants.KEY_STUDENT_ID, studentId);
            jsonObject.put(Constants.KEY_COURSE_GROUP_ID, courseGroupId);
            jsonObject.put(Constants.SCORE, score);
            rootJsonObject.put(Constants.SUBMISSION, jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void getQuizSolveDetails(String url, ResponseListener responseListener) {
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

    public static void postAnswerSubmission(String url, int quizSubmissionId, AnswerSubmission answerSubmission, ArrayResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject rootJsonObject = new JSONObject();
        JSONArray answerJsonArray = new JSONArray();
        try {
            for (Answers answers : answerSubmission.getAnswerSubmission()) {
                JSONObject answerJsonObject = new JSONObject();
                answerJsonObject.put(Constants.KEY_ANSWER_ID, answers.getId());
                answerJsonObject.put(Constants.KEY_IS_CORRECT, answers.isCorrect());
                answerJsonObject.put(Constants.KEY_MATCH, answers.getMatch());
                answerJsonObject.put(Constants.KEY_QUESTION_ID, answers.getQuestionId());
                answerJsonObject.put(Constants.KEY_QUIZ_SUBMISSION_ID, quizSubmissionId);
                answerJsonArray.put(answerJsonObject);
            }
            rootJsonObject.put(Constants.KEY_ANSWER_SUBMISSION, answerJsonArray);
            rootJsonObject.put(Constants.KEY_QUESTION_ID, answerSubmission.getQuestionId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleArrayResponseListener() {
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

    public static void getAnswerSubmission(String url, ResponseListener responseListener) {
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

    public static void deleteAnswerSubmission(String url, int questionId, int quizSubmissionId, ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_QUIZ_SUBMISSION_ID, quizSubmissionId);
            jsonObject.put(Constants.KEY_QUESTION_ID, questionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.delete(url, jsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void submitQuiz(String url, int submissionId, ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        JSONObject rootJsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_ID, submissionId);
            rootJsonObject.put(Constants.SUBMISSION, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url, rootJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void changePassword(String url, String currentPassword, int userId, String newPassword, HashMap<String, String> headerHashMap, ResponseListener responseListener) {
        JSONObject rootJsonObject = new JSONObject();
        JSONObject userJsonObject = new JSONObject();
        try {
            userJsonObject.put(Constants.KEY_CURRENT_PASSWORD, currentPassword);
            userJsonObject.put(Constants.KEY_ID, userId);
            userJsonObject.put(Constants.KEY_PASSWORD, newPassword);
            userJsonObject.put(Constants.KEY_PASSWORD_CONFIRMATION, newPassword);
            userJsonObject.put(Constants.KEY_RESET_PASSWORD, true);
            rootJsonObject.put(Constants.USER, userJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.put(url, rootJsonObject, headerHashMap, new HandleResponseListener() {
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

    public static void getGradingPeriods(int courseID, final ArrayResponseListener arrayResponseListener) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.gradingPeriods(courseID);
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
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
}