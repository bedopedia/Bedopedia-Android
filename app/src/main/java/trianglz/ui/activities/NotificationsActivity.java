package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.NotificationsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Notification;
import trianglz.ui.adapters.NotificationsAdapter;
import trianglz.utils.Util;

public class NotificationsActivity extends SuperActivity implements NotificationsPresenter, View.OnClickListener, AdapterPaginationInterface {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private Button closeBtn;
    private NotificationsView notificationsView;
    private int pageNumber;
    private boolean newIncomingNotificationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        bindViews();
        setListeners();
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.setAsSeen(SessionManager.getInstance().getUserId());
        notificationsView.setAsSeen(url);
        SessionManager.getInstance().setNotificationCounterToZero();
        if(Util.isNetworkAvailable(this)){
            getNotifications(false);
            showLoadingDialog();
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    private void bindViews() {
        pageNumber = 1;
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationsView = new NotificationsView(this, this);
        closeBtn = findViewById(R.id.btn_close);
    }

    private void setListeners() {
        closeBtn.setOnClickListener(this);
    }

    private void getNotifications(boolean pagination) {
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                    SessionManager.getInstance().getUserId() + "/notifications";
            if (!pagination) {
                pageNumber = 1;
            }
            notificationsView.getNotifications(url, pageNumber,20);
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notifications) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        newIncomingNotificationData = notifications.size() != 0;
        adapter.addData(notifications, newIncomingNotificationData);
    }

    @Override
    public void onGetNotificationFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }

    @Override
    public void onReachPosition() {
        pageNumber++;
        getNotifications(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progress.isShowing()){
            progress.dismiss();
        }
    }
}
