package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.NotificatoinsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.ui.activities.adapters.NotificationsAdapter;

public class NotificationsActivity extends SuperActivity implements NotificatoinsPresenter {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private String id;
    private NotificationsView notificationsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        bindViews();
        getNotifications();
    }
    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.addData(getFakeData());
        notificationsView = new NotificationsView(this, this);

    }
    private ArrayList<String> getFakeData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");

        }
        return list;
    }
    private void getNotifications() {
        String url = SessionManager.getInstance().getBaseUrl()+ "/api/users/"+"3164" +"/notifications";
        showLoadingDialog();
        notificationsView.getNotifications(url);
    }

    @Override
    public void onGetNotificationSuccess(JSONObject response) {
        progress.dismiss();
    }

    @Override
    public void onGetNotificationFailure() {
        progress.dismiss();
        Toast.makeText(this, "network failed", Toast.LENGTH_SHORT).show();
    }
}
