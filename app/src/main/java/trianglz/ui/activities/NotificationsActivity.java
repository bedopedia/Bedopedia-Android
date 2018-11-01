package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.NotificationsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.models.Notification;
import trianglz.ui.activities.adapters.NotificationsAdapter;

public class NotificationsActivity extends SuperActivity implements NotificationsPresenter , View.OnClickListener{

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private Button closeBtn;
    private NotificationsView notificationsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        bindViews();
        setListeners();
        getNotifications();
    }
    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationsView = new NotificationsView(this, this);
        closeBtn = findViewById(R.id.btn_close);
    }

    private void setListeners() {
        closeBtn.setOnClickListener(this);
    }
    private void getNotifications() {
        String url = SessionManager.getInstance().getBaseUrl()+ "/api/users/"+"3164" +"/notifications";
        showLoadingDialog();
        notificationsView.getNotifications(url);
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notifications) {
     progress.dismiss();
     adapter.addData(notifications);
    }

    @Override
    public void onGetNotificationFailure() {
        progress.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                finish();
                break;
        }
    }
}
