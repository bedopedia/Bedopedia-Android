package trianglz.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.skolera.skolera_android.R;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.HomePresenter;
import trianglz.core.views.HomeView;
import trianglz.managers.SessionManager;
import trianglz.models.Student;
import trianglz.ui.adapters.HomeAdapter;
import trianglz.utils.Constants;

public class HomeActivity extends SuperActivity implements HomePresenter, View.OnClickListener,
        HomeAdapter.HomeAdapterInterface {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private String id;
    private HomeView homeView;
    private ImageButton notificationBtn;
    private ArrayList<JSONArray> kidsAttendances;
    private ImageButton settingsBtn;
    private ImageView redCircleImageView;

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
        homeAdapter = new HomeAdapter(this, this);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        homeView = new HomeView(this, this);
        notificationBtn = findViewById(R.id.btn_notification);
        redCircleImageView = findViewById(R.id.img_red_circle);
        kidsAttendances = new ArrayList<>();
        settingsBtn = findViewById(R.id.btn_setting);
    }

    private void setListeners() {
        notificationBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SessionManager.getInstance().getNotficiationCounter() > 0) {
            redCircleImageView.setVisibility(View.VISIBLE);
        } else {
            redCircleImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void getStudentsHome() {
        id = SessionManager.getInstance().getId();
        String url = SessionManager.getInstance().getBaseUrl() + "/api/parents/" + id + "/children";
        showLoadingDialog();
        homeView.getStudents(url, id);
    }

    @Override
    public void onGetStudentsHomeSuccess(ArrayList<Object> dataObjectArrayList) {
        progress.dismiss();
        ArrayList<JSONArray> attendanceJsonArray = (ArrayList<JSONArray>) dataObjectArrayList.get(0);
        this.kidsAttendances = attendanceJsonArray;
        ArrayList<Student> studentArrayList = (ArrayList<Student>) dataObjectArrayList.get(1);
        homeAdapter.addData(studentArrayList);

    }

    @Override
    public void onGetStudentsHomeFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode, "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification:
                openNotificationsActivity();
                break;
            case R.id.btn_setting:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
    }

    private void openNotificationsActivity() {
        Intent myIntent = new Intent(HomeActivity.this, NotificationsActivity.class);
        HomeActivity.this.startActivity(myIntent);
    }

    @Override
    public void onOpenStudentClicked(Student student, int position) {
        openStudentDetailActivity(student, position);
    }

    @Override
    public void onAssignmentClicked(Student student) {
        openAssignmentDetailActivity(student);
    }

    private void openAssignmentDetailActivity(Student student) {

    }

    private void openStudentDetailActivity(Student student, int position) {
        Intent intent = new Intent(this, StudentMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        bundle.putSerializable(Constants.KEY_ATTENDANCE, kidsAttendances.get(position).toString());
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

}
