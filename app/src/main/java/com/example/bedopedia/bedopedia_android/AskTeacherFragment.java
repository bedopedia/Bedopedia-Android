package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.AskTeacherAdapter;
import Models.Message;
import Models.MessageThread;
import Models.User;
import Tools.Dialogue;
import Tools.SharedPreferenceUtils;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import myKids.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static timetable.TimetableActivity.context;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link AskTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskTeacherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Context context ;
    final String curUserKey = "cur_user";
    ProgressDialog progress;
    Map<String,Pair<String,ArrayList<MessageThread>>> items =  new HashMap() ;
    ArrayList<ArrayList<MessageThread>> itemsParam = new ArrayList<>();

    ArrayList<String> header = new ArrayList<>();
    final String userIdKey = "user_id";
    final String courseIdKey = "course_id";



    // TODO: Rename and change types of parameters

    public AskTeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
   * @return A new instance of fragment AskTeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AskTeacherFragment newInstance(Context context) {
        AskTeacherFragment fragment = new AskTeacherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_ask_teacher, container, false);
        return rootView ;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.context = getActivity() ;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
    }

    private class MessageThreads extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading();
            progress.show();
        }

        protected void onProgressUpdate(String... progress) {
            loading();
        }

        @Override
        protected List<Student> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(context, curUserKey );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String id = SharedPreferenceUtils.getStringValue(userIdKey, "",sharedPreferences);
            String url = "/api/threads";
            Map <String, String> params = new HashMap<>();
            params.put(userIdKey , id);

            Call<ArrayList<JsonObject>> call = apiService.getServiseArr(url, params);

            call.enqueue(new Callback<ArrayList<JsonObject>>() {

                @Override
                public void onResponse(Call<ArrayList<JsonObject>>  call, Response<ArrayList<JsonObject>> response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                    } else if (statusCode == 200) {
                        ArrayList<JsonObject> threads = response.body();
                        for (int i = 0 ; i < threads.size(); i++) {
                            JsonObject messageThread = threads.get(i);
                            JsonArray messages = messageThread.get("messages").getAsJsonArray();
                            MessageThread thread;
                            ArrayList<Message> threadMessages = new ArrayList<Message>();
                            for (JsonElement messageElement : messages) { // gest needed data from assig, quizz, grade item
                                JsonObject messageObj = messageElement.getAsJsonObject();
                                JsonObject user = messageObj.get("user").getAsJsonObject();
                                User sender = new User(user.get("id").getAsInt(),
                                        user.get("firstname").getAsString(),
                                        user.get("lastname").getAsString(),
                                        user.get("gender").getAsString(),
                                        "" ,
                                        user.get("avatar_url").getAsString(),
                                        user.get("user_type").getAsString()
                                );

                                Message message = new Message(messageObj.get("body").getAsString(),
                                        messageObj.get("created_at").getAsString(),
                                        messageObj.get("updated_at").getAsString(),
                                        sender,
                                        messageThread.get("id").getAsInt()
                                );
                                threadMessages.add(message);
                            }
                            if ( items.containsKey(messageThread.get(courseIdKey).toString())) {
                                ArrayList<MessageThread> array = items.get(messageThread.get(courseIdKey).toString()).second;

                                thread = new MessageThread(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString(),
                                        threadMessages,
                                        messageThread.get("id").getAsInt(),
                                        messageThread.get("others_names").getAsString()
                                );

                                array.add(thread);
                            }
                            else if (!(messageThread.get(courseIdKey) == JsonNull.INSTANCE)) {
                                ArrayList<MessageThread> array = new ArrayList<MessageThread>();
                                thread = new MessageThread(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString(),
                                        threadMessages,
                                        messageThread.get("id").getAsInt(),
                                        messageThread.get("others_names").getAsString()
                                );
                                array.add(thread);
                                items.put(messageThread.get("course_id").toString(), new Pair<String, ArrayList<MessageThread>>(messageThread.get("course_name").toString(),array));
                            }
                        }
                    }
                    for (String key : items.keySet()) {
                        itemsParam.add(items.get(key).second);
                        header.add(items.get(key).first);
                    }

                    AskTeacherAdapter askTeacherAdapter = new AskTeacherAdapter(context, R.layout.activity_ask_teacher,itemsParam, header);
                    ListView askTeacherListView = (ListView) getActivity().findViewById(R.id.ask_teacher_list);
                    askTeacherListView.setAdapter(askTeacherAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                }

            });
            return null;
        }
    }

}
