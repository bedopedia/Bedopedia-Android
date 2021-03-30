package trianglz.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.NotificationsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Meta;
import trianglz.models.Notification;
import trianglz.ui.adapters.NotificationsAdapter;
import trianglz.utils.Util;

public class NotificationsActivity extends SuperActivity implements NotificationsPresenter, View.OnClickListener, AdapterPaginationInterface, NotificationsAdapter.NotificationInterface {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ImageButton backBtn;
    private NotificationsView notificationsView;
    private int nextPage = -1;
    private String url = "";
    private LinearLayout placeholderLinearLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private SwipeRefreshLayout pullRefreshLayout;


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
            getNotifications();
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    private void bindViews() {
        url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                SessionManager.getInstance().getUserId() + "/notifications";
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(this, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationsView = new NotificationsView(this, this);
        backBtn = findViewById(R.id.btn_back);
        placeholderLinearLayout = findViewById(R.id.placeholder_layout);
        skeletonLayout = findViewById(R.id.skeletonLayout);
        shimmer = findViewById(R.id.shimmer_view_container);
        pullRefreshLayout = findViewById(R.id.pullToRefresh);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                nextPage = -1;
                getNotifications();
                placeholderLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }



    private void getNotifications() {
        if (Util.isNetworkAvailable(this)) {
            if(nextPage == -1){
                showSkeleton(true);
                showRecyclerView(true);
                notificationsView.getNotifications(url, 1,20);
            }else {
                if(nextPage != 0) {
                    notificationsView.getNotifications(url, nextPage, 20);
                }
            }
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void showRecyclerView(boolean isToShowRecyclerView){
        if(isToShowRecyclerView){
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notifications, Meta meta) {
        showSkeleton(false);
        if (meta.getTotalCount() == 0) {
           showRecyclerView(false);
        } else {
            showRecyclerView(true);
            nextPage =  meta.getNextPage();
            if(meta.getCurrentPage() == 1){
                adapter.notificationArrayList.clear();
                adapter.totalCount = meta.getTotalCount();
            }
            adapter.addData(notifications);
        }

    }

    @Override
    public void onGetNotificationFailure(String message, int errorCode) {
        showSkeleton(false);
        showErrorDialog(this, errorCode,"");
        if (nextPage == -1) {
            showRecyclerView(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onReachPosition() {
        getNotifications();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progress.isShowing()){
            progress.dismiss();
        }
    }


    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(this);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup)getLayoutInflater().inflate(R.layout.skeleton_row_layout, null);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNotificationClicked(String zoomUrl , String name, Integer eventId) {

    }
}
