package com.skolera.skolera_android;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.MessageAttributes;
import Models.NewMessageThread;
import Models.Teacher;
import Tools.SharedPreferenceUtils;
import grades.CourseGroup;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trianglz.utils.Util;


public class NewMessageFragment extends Fragment {

    final String curUserKey = "cur_user";
    final String studentIdKey = "student_id";
    String studentId;
    ArrayList<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
    ProgressDialog progress;
    ArrayList<String> items = new ArrayList();
    ArrayList<String> teachersList = new ArrayList();
    ArrayList<Teacher> Teachers= new ArrayList<>();
    Spinner SelectCourse;
    Spinner SelectTeacher;
    EditText SubjectText;

    public NewMessageFragment() {

    }

    public static NewMessageFragment newInstance() {
        NewMessageFragment fragment = new NewMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= this.getActivity().getIntent().getExtras();
        studentId = extras.getString(studentIdKey);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SelectCourse = (Spinner) getActivity().findViewById(R.id.select_course);
        SelectTeacher = (Spinner) getActivity().findViewById(R.id.select_teacher);
        SubjectText = (EditText) getActivity().findViewById(R.id.subject_text);

        SelectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                for (int i = 0; i <courseGroups.size() ; i++) {
                    if (courseGroups.get(i).getCourseName().equals(items.get(position))) {
                        teachersList.clear();
                        Teachers.clear();
                        getCourseGroupTeachers(String.valueOf(courseGroups.get(i).getCourseId()), String.valueOf(courseGroups.get(i).getId()));
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });



        getStudentCourseGroups();

        ImageButton sendingButton = (ImageButton) getActivity().findViewById(R.id.sending_new_messgae_id);
        sendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int courseGroupPosition = SelectCourse.getSelectedItemPosition();
                int teacherPosition = SelectTeacher.getSelectedItemPosition();
                EditText body = (EditText) getActivity().findViewById(R.id.new_message_id_body);

                Teacher teacher = Teachers.get(teacherPosition);
                CourseGroup courseGroup = courseGroups.get(courseGroupPosition);
                try {
                    if (validateParams(SubjectText.getText().toString(), String.valueOf(courseGroup.getCourseId()), body.getText().toString(), String.valueOf(teacher.getId()),SubjectText,body ))
                        sendNewMessage(SubjectText.getText().toString(), String.valueOf(courseGroup.getCourseId()), body.getText().toString(), String.valueOf(teacher.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public boolean validateParams(String subjectName, String courseID, String body, String userID, EditText subjectEdit, EditText bodyEdit){
        if (subjectName.equals("")){
            subjectEdit.requestFocus();
            return false;
        } else if (body.equals("")) {
            bodyEdit.requestFocus();
            return false;
        } else if (courseID.equals("") || userID.equals("")) {
            return false;
        }
        return true;
    }

    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
    }

    public void sendNewMessage(String subjectName, String courseID, String body, String userID) throws JSONException {
        loading();
        progress.show();
        String url = "/api/threads";
        Map<String, Object> params = new HashMap<>();
        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(),"cur_user" );
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Map<String, String> message_thread = new HashMap<>();
        String id = SharedPreferenceUtils.getStringValue("user_id", "",sharedPreferences);
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(id);
        userIds.add(userID);
        params.put("user_ids", userIds);

        NewMessageThread newMessageThread = new NewMessageThread(subjectName,courseID,"");
        MessageAttributes messageAttributes = new MessageAttributes(Integer.valueOf(id),body,"");
        newMessageThread.sendMessage(messageAttributes);

        params.put("message_thread", newMessageThread);
        Call<NewMessageThread> call = apiService.POSTThreadMessages(params);

        call.enqueue(new Callback<NewMessageThread>() {

            @Override
            public void onResponse(Call<NewMessageThread> call, Response<NewMessageThread> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    Util.showErrorDialog(getActivity(),getString(R.string.Dialogue401Body));
                } else {
                    Intent intent = new Intent(getActivity(), com.skolera.skolera_android.AskTeacherActivity.class);
                    intent.putExtra(studentIdKey, studentId);
                    startActivity(intent);
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<NewMessageThread> call, Throwable throwable) {
                progress.dismiss();
                Util.showErrorDialog(getActivity(),getString(R.string.ConnectionErrorBody));
            }
        });
    }


    public void getStudentCourseGroups(){
        loading();
        progress.show();
        String url = "api/students/" + studentId + "/course_groups";
        Map<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(), curUserKey );
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);
        params.put("source","home");
        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    Util.showErrorDialog(getActivity(),getString(R.string.Dialogue401Body));
                } else if (statusCode == 200) {
                    for (int i = 0 ; i < response.body().size() ; i++) {
                        JsonObject courseGroupData = response.body().get(i);
                        JsonObject course = courseGroupData.get("course").getAsJsonObject();

                        courseGroups.add(new CourseGroup(
                                courseGroupData.get("id").getAsInt(),
                                course.get("id").getAsInt(),
                                courseGroupData.get("name").getAsString(),
                                course.get("name").getAsString()
                        ));
                        items.add(course.get("name").getAsString());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    SelectCourse.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Util.showErrorDialog(getActivity(),getString(R.string.ConnectionErrorBody));
            }
        });
    }


    public void getCourseGroupTeachers(String courseId, String courseGroupId){
        String url = "/api/courses/"+courseId +"/course_groups/"+courseGroupId+"/teachers";
        Map<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(), curUserKey );
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    Util.showErrorDialog(getActivity(),getString(R.string.Dialogue401Body));
                } else if (statusCode == 200) {
                    ArrayList teachers = response.body();
                    for (int i = 0; i <teachers.size(); i++) {
                        try {
                            JSONObject teacherJson = new JSONObject(teachers.get(i).toString());
                            Teachers.add(new Teacher(teacherJson.getInt("user_id"),
                                    teacherJson.getString("firstname"),
                                    teacherJson.getString("lastname"),
                                    teacherJson.getString("gender"),
                                    teacherJson.getString("email"),
                                    teacherJson.getString("avatar_url"),
                                    teacherJson.getString("user_type")
                                    ));
                            teachersList.add(teacherJson.getString("name"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, teachersList);
                    SelectTeacher.setAdapter(adapter);

                }
                progress.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Util.showErrorDialog(getActivity(),getString(R.string.ConnectionErrorBody));
            }
        });
    }

}
