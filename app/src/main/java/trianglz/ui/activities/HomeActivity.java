package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.HomePresenter;
import trianglz.core.views.HomeView;
import trianglz.managers.SessionManager;
import trianglz.models.Student;
import trianglz.ui.adapters.HomeAdapter;

public class HomeActivity extends SuperActivity implements HomePresenter,View.OnClickListener,HomeAdapter.HomeAdapterInterface{
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private String id;
    private HomeView homeView;
    private ImageButton notificationBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
        setListeners();
        getStudentsHome();

    }


    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        homeAdapter = new HomeAdapter(this,this);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        homeView = new HomeView(this,this);
        notificationBtn = findViewById(R.id.btn_notification);

    }

    private void setListeners(){
        notificationBtn.setOnClickListener(this);
    }


    private void getStudentsHome() {
        id = SessionManager.getInstance().getId();
        String url = SessionManager.getInstance().getBaseUrl()+ "/api/parents/" + id + "/children";
        showLoadingDialog();
        homeView.getStudents(url,id);
    }

    @Override
    public void onGetStudentsHomeSuccess(ArrayList<Student> studentArrayList) {
        progress.dismiss();
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
                break;
        }
    }

    @Override
    public void onOpenStudentClicked(Student student) {

    }
}
