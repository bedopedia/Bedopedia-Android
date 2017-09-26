package login;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Teacher;
import Tools.Dialogue;
import Tools.SharedPreferenceUtils;
import grades.CourseGroup;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.progress;


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

    }


    public void getStudentCourseGroups(){
        String url = "api/students/" + studentId + "/course_groups";
        Map<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(), curUserKey );
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(getActivity(),getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                } else if (statusCode == 200) {
                    for (int i = 0 ; i < response.body().size() ; i++) {
                        JsonObject courseGroupData = response.body().get(i);
                        JsonObject course = courseGroupData.get("course").getAsJsonObject();

                        courseGroups.add(new CourseGroup(
                                courseGroupData.get("id").getAsInt(),
                                course.get("id").getAsInt(),
                                courseGroupData.get("name").getAsString(),
                                courseGroupData.get("course_name").getAsString()
                        ));
                        items.add(courseGroupData.get("course_name").getAsString());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    SelectCourse.setAdapter(adapter);
                }
                progress.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(getActivity(),getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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
                    Dialogue.AlertDialog(getActivity(),getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                } else if (statusCode == 200) {
                    Log.v("RESPONSEE",response.body().get(0).toString());
                    ArrayList teachers = response.body();
                    for (int i = 0; i <teachers.size(); i++) {
                        try {
                            JSONObject teacherJson = new JSONObject(teachers.get(i).toString());
                            Teachers.add(new Teacher(teacherJson.getInt("id"),
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
                        Log.v("STEPP" + i, teachers.get(i).toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, teachersList);
                    SelectTeacher.setAdapter(adapter);

                }
                progress.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(getActivity(),getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
            }
        });
    }

}
