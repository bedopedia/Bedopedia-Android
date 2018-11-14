package trianglz.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.skolera.skolera_android.R;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.components.LocalHelper;
import trianglz.components.SettingsDialog;
import trianglz.core.presenters.HomePresenter;
import trianglz.core.views.HomeView;
import trianglz.managers.SessionManager;
import trianglz.models.Student;
import trianglz.ui.adapters.HomeAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class HomeActivity extends SuperActivity implements HomePresenter,View.OnClickListener,
        HomeAdapter.HomeAdapterInterface,SettingsDialog.SettingsDialogInterface{
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private String id;
    private HomeView homeView;
    private ImageButton notificationBtn;
    private  ArrayList<JSONArray> kidsAttendances;
    private SettingsDialog settingsDialog;
    private ImageButton settingsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
        setListeners();
        getStudentsHome();
        homeView.refreshFireBaseToken();
    }


    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        homeAdapter = new HomeAdapter(this,this);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        homeView = new HomeView(this,this);
        notificationBtn = findViewById(R.id.btn_notification);
        kidsAttendances = new ArrayList<>();
        settingsDialog = new SettingsDialog(this,R.style.SettingsDialog,this);
        settingsBtn = findViewById(R.id.btn_setting);
    }

    private void setListeners(){
        notificationBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }


    private void getStudentsHome() {
        id = SessionManager.getInstance().getId();
        String url = SessionManager.getInstance().getBaseUrl()+ "/api/parents/" + id + "/children";
        showLoadingDialog();
        homeView.getStudents(url,id);
    }

    @Override
    public void onGetStudentsHomeSuccess(ArrayList<Object> dataObjectArrayList) {
        progress.dismiss();
        ArrayList<JSONArray> attendanceJsonArray =(ArrayList<JSONArray>) dataObjectArrayList.get(0);
        this.kidsAttendances = attendanceJsonArray;
        ArrayList<Student> studentArrayList = (ArrayList<Student>) dataObjectArrayList.get(1);
        homeAdapter.addData(studentArrayList);

    }

    @Override
    public void onGetStudentsHomeFailure() {
        progress.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_notification:
                openNotificationsActivity();
                break;
            case R.id.btn_setting:
                settingsDialog.show();
                break;
        }
    }

    private void openNotificationsActivity() {
        Intent myIntent = new Intent(HomeActivity.this, NotificationsActivity.class);
        HomeActivity.this.startActivity(myIntent);
    }

    @Override
    public void onOpenStudentClicked(Student student,int position) {
        openStudentDetailActivity(student,position);
    }

    private void openStudentDetailActivity(Student student,int position){
        Intent intent = new Intent(this,StudentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
        bundle.putSerializable(Constants.KEY_ATTENDANCE,kidsAttendances.get(position).toString());
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }


    @Override
    public void onChangeLanguageClicked() {
        changeLanguage();
    }

    @Override
    public void onSignOutClicked() {

    }




    private void changeLanguage() {
        if (LocalHelper.getLanguage(this).equals("ar")) {
            updateViews("en");
        } else {
            updateViews("ar");
        }
    }


    private void updateViews(String languageCode) {

        LocalHelper.setLocale(this, languageCode);
        LocalHelper.getLanguage(this);
        restartApp();
    }

    public void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (this instanceof Activity) {
            (this).finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 0);

    }
}
