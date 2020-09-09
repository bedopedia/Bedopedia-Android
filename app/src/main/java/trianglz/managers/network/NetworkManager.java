package trianglz.managers.network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import okhttp3.Headers;
import okhttp3.Response;
import trianglz.managers.SessionManager;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class NetworkManager {


    public static void getWithParameter(String url, HashMap params, HashMap<String, String> headerValue, final HandleResponseListener handleResponseListener) {
        AndroidNetworking.get(url)
                .addQueryParameter(params)
                .addHeaders(headerValue)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                    }
                });
    }

    public static void get(String url, HashMap<String, String> headerValue, final HandleResponseListener handleResponseListener) {
        AndroidNetworking.get(url)
                .addHeaders(headerValue)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        Headers headers = okHttpResponse.headers();
                        if(headers != null && headers.size() > 0) {
                            String accessToken = headers.get("access-token");
                            String tokenType = headers.get("token-type");
                            String clientCode = headers.get("client");
                            String uid = headers.get("uid");
                            String timeZone = headers.get("Timezone");
                            SessionManager.getInstance().setHeadersValue(accessToken, tokenType, clientCode, uid, timeZone);
                        }
                        handleResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    public static void getJsonArray(String url, HashMap<String, String> params, HashMap<String, String> headersValue, final HandleArrayResponseListener handleArrayResponseListener) {
        AndroidNetworking.get(url)
                .addQueryParameter(params)
                .addHeaders(headersValue)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        handleArrayResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleArrayResponseListener.onFailure(getErrorMessage(anError), anError.getErrorCode());
                    }
                });

    }


    public static void post(String url, JSONObject object, final HashMap<String, String> headerValues, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            Headers headers = okHttpResponse.headers();
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


        } else {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            handleResponseListener.onFailure(getErrorMessage(anError), anError.getErrorCode());
                        }
                    });

        }

    }

    public static void post(String url, JSONObject object, final HashMap<String, String> headerValues, final HandleArrayResponseListener handleArrayResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponseAndJSONArray(new OkHttpResponseAndJSONArrayRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONArray response) {
                            Headers headers = okHttpResponse.headers();
                            handleArrayResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


        } else {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build().getAsOkHttpResponseAndJSONArray(new OkHttpResponseAndJSONArrayRequestListener() {
                @Override
                public void onResponse(Response okHttpResponse, JSONArray response) {
                    Headers headers = okHttpResponse.headers();
                    handleArrayResponseListener.onSuccess(response);
                }

                @Override
                public void onError(ANError anError) {
                    handleArrayResponseListener.onFailure(getErrorMessage(anError), anError.getErrorCode());

                }
            });


        }

    }

    public static void put(String url, JSONObject object, HashMap<String, String> headersValues, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.put(url)
                    .addHeaders(headersValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        } else {
            AndroidNetworking.put(url)
                    .addHeaders(headersValues)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        }

    }

    public static void postImageFile(String url, String fileName, File image, HashMap<String, String> headersValues, final HandleResponseListener handleResponseListener) {

        AndroidNetworking.upload(url)
                .addHeaders(headersValues)
                .addMultipartParameter(Constants.KEY_BODY, Constants.KEY_FILE_ATTACHED)
                .addMultipartParameter(Constants.KEY_FILENAME, fileName)
                .addMultipartFile(Constants.KEY_ATTACHMENT, image)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError error) {
                    }
                });


    }

    public static void delete(String url, JSONObject object, String headerValue, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.delete(url)
                    .addHeaders("Authorization", headerValue)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        } else {
            AndroidNetworking.delete(url)
                    .addHeaders("Authorization", headerValue)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        }

    }
    public static void delete(String url, JSONObject object, HashMap<String, String> headersValues , final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.delete(url)
                    .addHeaders(headersValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        } else {
            AndroidNetworking.delete(url)
                    .addHeaders(headersValues)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        }

    }
    public static void upload(String url, JSONObject jsonObject, File file, HashMap<String, String> headersValues, final HandleResponseListener handleResponseListener) {
        AndroidNetworking.upload(url)
                .addHeaders(headersValues)
                .addMultipartParameter(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }

                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        handleResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());

                    }
                });
    }

    private static String getErrorMessage(ANError anError) {

        if (anError.getErrorCode() == 500) {
            return ("network error");
        }
        String message = "";
        try {
            if (anError.getErrorBody() == null) {
                return "";
            }
            JSONObject errorBody = new JSONObject(anError.getErrorBody());
            if(errorBody.has(Constants.KEY_REASONS)){
                JSONArray jsonArray = errorBody.optJSONArray(Constants.KEY_REASONS);
                message = jsonArray.optString(0);
            }else{
                message = errorBody.optString(Constants.KEY_MESSAGE);
            }
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static void postNotification(String url, JSONObject object, HashMap<String, String> headerHashMap, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
                    .addHeaders(headerHashMap)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            ANError anError = error;
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        } else {
            AndroidNetworking.post(url)
                    .addHeaders(headerHashMap)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError error) {
                            handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                        }
                    });

        }
    }


    public static void postLogin(String url, JSONObject object, final HashMap<String, String> headerValues, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            Headers headers = okHttpResponse.headers();
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


        } else {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .addJSONObjectBody(object)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            Headers headers = okHttpResponse.headers();
                            String accessToken = headers.get("access-token");
                            String tokenType = headers.get("token-type");
                            String clientCode = headers.get("client");
                            String uid = headers.get("uid");
                            String timeZone = headers.get("Timezone");
                            SessionManager.getInstance().setHeadersValue(accessToken, tokenType, clientCode, uid,timeZone);
                            handleResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            handleResponseListener.onFailure(getErrorMessage(anError), anError.getErrorCode());
                        }
                    });

        }

    }

    public static void postSeenNotification(String url, JSONObject object, final HashMap<String, String> headerValues, final HandleArrayResponseListener handleArrayResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
                    .addHeaders(headerValues)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            handleArrayResponseListener.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            handleArrayResponseListener.onFailure(getErrorMessage(anError), anError.getErrorCode());
                        }
                    });

        }
    }

    public static void upload(String url, HashMap<String, String> multiPartParamterHashMap, File file, HashMap<String, String> headerValue, final HandleMultiPartResponseListener handleResponseListener) {
        AndroidNetworking.upload(url)
                .addHeaders(headerValue)
                .addMultipartFile("file[file]", file)
                .addMultipartParameter(multiPartParamterHashMap)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.v("UPLOADED_PROGRESS", bytesUploaded + "-" + totalBytes);
                        handleResponseListener.onProgress(bytesUploaded, totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        handleResponseListener.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        handleResponseListener.onFailure(getErrorMessage(error), error.getErrorCode());
                    }
                });
    }
}