package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.skolera.skolera_android.R;

import trianglz.core.presenters.HomePresenter;
import trianglz.core.views.HomeView;
import trianglz.managers.SessionManager;
import trianglz.ui.adapters.KidsAdapter;

public class HomeActivity extends SuperActivity implements HomePresenter{
    private RecyclerView recyclerView;
    private KidsAdapter kidsAdapter;
    private String id;
    private HomeView homeView;


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
        kidsAdapter = new KidsAdapter(this);
        recyclerView.setAdapter(kidsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        homeView = new HomeView(this,this);

    }

    private void setListeners(){

    }


    private void getStudentsHome() {
        id = SessionManager.getInstance().getId();
        String url = SessionManager.getInstance().getBaseUrl()+ "/api/parents/" + id + "/children";
        showLoadingDialog();
        homeView.getStudents(url,id);
    }

    @Override
    public void onGetStudentsHomeSuccess() {
        progress.dismiss();
    }

    @Override
    public void onGetStudentsHomeFailure() {
        progress.dismiss();
    }
}
