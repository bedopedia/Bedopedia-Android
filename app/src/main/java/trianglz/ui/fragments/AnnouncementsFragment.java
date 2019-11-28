package trianglz.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.AnnouncementInterface;
import trianglz.core.views.AnnouncementView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.Announcement;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AnnouncementAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment implements View.OnClickListener
        , AnnouncementAdapter.AnnouncementAdapterInterface
        , AnnouncementInterface, StudentMainActivity.OnBackPressedInterface {

    // rootView
    private View rootView;

    // parent activity
    StudentMainActivity activity;
    private AnnouncementAdapter adapter;
    private RecyclerView recyclerView;
    private AnnouncementView announcementView;
    private int pageNumber = 1;
    private Actor actor;
    private boolean newIncomingNotificationData;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout placeholderLinearLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

    public AnnouncementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_announcements, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        setListeners();
        if (Util.isNetworkAvailable(getActivity())) {
            getAnnouncement(false);
//            if (!activity.isCalling)
//                activity.showLoadingDialog();
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
        return rootView;
    }

    private void bindViews() {

        adapter = new AnnouncementAdapter(getActivity(), this);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        announcementView = new AnnouncementView(getActivity(), this);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
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
                getAnnouncement(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onAnnouncementSelected(Announcement announcement) {
        openAnnouncementDetailActivity(announcement);
    }

    @Override
    public void onReachPosition() {
        pageNumber++;
        getAnnouncement(true);
    }

    private void getAnnouncement(boolean pagination) {
        if (!pagination) {
            pageNumber = 1;
            showSkeleton(true);
        }
        String type;
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            type = "parent";
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            type = "teacher";
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            type = "hod";
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString())) {
            type = "admin";
        } else {
            type = "student";
        }
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnnouncementUrl(pageNumber, type, 20);
        announcementView.getAnnouncement(url);

    }

    @Override
    public void onGetAnnouncementSuccess(ArrayList<Announcement> announcementArrayList) {
        showSkeleton(false);
        if (pageNumber == 1 && announcementArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            if (pageNumber == 1 && !adapter.mDataList.isEmpty()) {
                adapter.mDataList.clear();
            }
            newIncomingNotificationData = announcementArrayList.size() != 0;
            adapter.addData(announcementArrayList, newIncomingNotificationData);
        }
    }

    @Override
    public void onGetAnnouncementFailure(String message, int errorCode) {
       showSkeleton(false);
        if (pageNumber == 1) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    private void openAnnouncementDetailActivity(Announcement announcement) {
        AnnouncementDetailFragment announcementDetailFragment = new AnnouncementDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ANNOUNCEMENTS, announcement);
        announcementDetailFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.announcement_root, announcementDetailFragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_row_layout, null);
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
    public void onStop() {
        super.onStop();
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            if (activity.pager.getCurrentItem() == 3) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            if (activity.pager.getCurrentItem() == 0) {
                if (getChildFragmentManager().getFragments().size() == 0) {
                    getActivity().finish();
                    return;
                }
            }
            getChildFragmentManager().popBackStack();
            if (getChildFragmentManager().getFragments().size() == 1) {
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
            }
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString())) {
            if (activity.pager.getCurrentItem() == 0) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (activity.pager.getCurrentItem() == 1) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }

            }
        }
    }
}
