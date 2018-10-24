package trianglz.managers.network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import trianglz.managers.App;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class NetworkManager {


    public static void get(String url, HashMap<String, String> headerValue, final HandleResponseListener handleResponseListener) {
        GsonBuilder builder;
        Gson gson;

        builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL,Modifier.TRANSIENT,Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.serializeNulls();
        gson =builder.create();
        AndroidNetworking.setParserFactory(new GsonParserFactory(gson));
        AndroidNetworking.get(url)
                .addPathParameter("Code", "cis")
                .addQueryParameter("Code", "cis")
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

    public static void getJsonArray(String url, String headerValue, final HandleArrayResponseListener handleArrayResponseListener) {
        AndroidNetworking.get(url)
                .addHeaders("Authorization", headerValue)
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


    public static void post(String url, JSONObject object, String headerValue, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.post(url)
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
            AndroidNetworking.post(url)
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

    public static void put(String url, JSONObject object, String headerValue, final HandleResponseListener handleResponseListener) {
        if (object == null) {
            AndroidNetworking.put(url)
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
            AndroidNetworking.put(url)
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

    public static void upload(String url, File file, String headerValue, final HandleResponseListener handleResponseListener) {

        AndroidNetworking.upload(url)
                .addHeaders("Authorization", headerValue)
                .addMultipartFile("certificate_data", file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.v("UPLOADED_PROGRESS", bytesUploaded + "-" + totalBytes);

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
            JSONObject errorBody = new JSONObject(anError.getErrorBody());
            message = errorBody.optString(Constants.KEY_MESSAGE);
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
}
