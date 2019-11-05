package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.NotificationsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Notification;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.NotificationsAdapter;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements NotificationsPresenter, View.OnClickListener, AdapterPaginationInterface, StudentMainActivity.OnBackPressedInterface {

    // fragment root view 
    private View rootView;

    // parent activity
    private StudentMainActivity activity;
    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private NotificationsView notificationsView;
    private int pageNumber;
    private boolean newIncomingNotificationData;
    private SwipeRefreshLayout pullRefreshLayout;
    private FrameLayout listFrameLayout, placeholderFrameLayout;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        setListeners();
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.setAsSeen(SessionManager.getInstance().getUserId());
        notificationsView.setAsSeen(url);
        if (Util.isNetworkAvailable(getActivity())) {
            getNotifications(false);
            activity.showLoadingDialog();
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
        return rootView;
    }

    private void bindViews() {
        pageNumber = 1;
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notificationsView = new NotificationsView(getActivity(), this);
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
    }

    private void setListeners() {
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifications(false);
                activity.showLoadingDialog();
            }
        });
    }

    private void getNotifications(boolean pagination) {
        if (Util.isNetworkAvailable(getActivity())) {
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                    SessionManager.getInstance().getUserId() + "/notifications";
            if (!pagination) {
                pageNumber = 1;
            }
            notificationsView.getNotifications(url, pageNumber, 20);
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notifications) {
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
        if (pageNumber == 1 && notifications.isEmpty()) {
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else {
            listFrameLayout.setVisibility(View.VISIBLE);
            placeholderFrameLayout.setVisibility(View.GONE);
        }
        if (pageNumber == 1 && !adapter.notificationArrayList.isEmpty()) {
            adapter.notificationArrayList.clear();
        }
        newIncomingNotificationData = notifications.size() != 0;
        adapter.addData(notifications, newIncomingNotificationData);
        pullRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onGetNotificationFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode, "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                activity.finish();
                break;
        }
    }

    @Override
    public void onReachPosition() {
        pageNumber++;
        getNotifications(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            if (activity.pager.getCurrentItem() == 2) {
                getActivity().finish();
            }
        }
    }
}
