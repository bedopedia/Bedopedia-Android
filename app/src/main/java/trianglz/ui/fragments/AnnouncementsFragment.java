package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
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
        if (Util.isNetworkAvailable(getActivity())) {
            getAnnouncement(false);
            if (!activity.isCalling)
                activity.showLoadingDialog();
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
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(6, getActivity()), false));
        announcementView = new AnnouncementView(getActivity(), this);

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
        if (!activity.isCalling)
            activity.showLoadingDialog();
        if (!pagination) {
            pageNumber = 1;
        }
        String type;
        if (SessionManager.getInstance().getUserType()) {
            type = "parent";
        } else {
            type = "student";
        }
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnnouncementUrl(pageNumber, type, 20);
        announcementView.getAnnouncement(url);

    }

    @Override
    public void onGetAnnouncementSuccess(ArrayList<Announcement> announcementArrayList) {
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
        newIncomingNotificationData = announcementArrayList.size() != 0;
        adapter.addData(announcementArrayList, newIncomingNotificationData);
    }

    @Override
    public void onGetAnnouncementFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
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
//        Intent intent = new Intent(getActivity(), AnnouncementDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.KEY_ANNOUNCEMENTS, announcement);
//        intent.putExtra(Constants.KEY_BUNDLE, bundle);
//        startActivity(intent);

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
        Boolean isStudent = SessionManager.getInstance().getStudentAccount();
        Boolean isParent = SessionManager.getInstance().getUserType() && !isStudent;
        if (isStudent) {
            if (activity.pager.getCurrentItem() == 3) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if (isParent) {
            if (activity.pager.getCurrentItem() == 0) {
                getActivity().finish();
                return;
            }
            getChildFragmentManager().popBackStack();
            if (getChildFragmentManager().getFragments().size() == 1) {
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
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
