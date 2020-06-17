package trianglz.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.NotificationsPresenter;
import trianglz.core.views.NotificationsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Meta;
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
    private int nextPage = -1;
    private int totalPages = -1;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout placeholderLinearLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;
    private String url;
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
        if (Util.isNetworkAvailable(getActivity())) {
            getNotifications();
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SessionManager.getInstance().getNotficiationCounter() > 0) {
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.setAsSeen(SessionManager.getInstance().getUserId());
            notificationsView.setAsSeen(url);
            SessionManager.getInstance().setNotificationCounterToZero();
        }
    }


    private void bindViews() {
        url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                SessionManager.getInstance().getUserId() + "/notifications";
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notificationsView = new NotificationsView(getActivity(), this);
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setListeners() {
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
        if (Util.isNetworkAvailable(getActivity())) {
            if(nextPage == -1){
                showSkeleton(true);
                notificationsView.getNotifications(url,1,20);
            }else {
                notificationsView.getNotifications(url, nextPage, 20);
            }
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
    }

    @Override
    public void onGetNotificationSuccess(ArrayList<Notification> notifications, Meta meta) {
        showSkeleton(false);
        if (meta.getTotalCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {

            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
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
        activity.showErrorDialog(activity, errorCode, "");
        if (nextPage == -1) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        }
    }
    

    @Override
    public void onReachPosition() {
        getNotifications();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            if (activity.pager.getCurrentItem() == 2) {
                getActivity().finish();
            }
        }
    }
    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_row_layout, (ViewGroup) rootView, false);
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
    public void onClick(View v) {
    }
}
