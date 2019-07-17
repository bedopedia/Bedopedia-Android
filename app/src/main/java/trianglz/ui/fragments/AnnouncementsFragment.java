package trianglz.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.core.presenters.AnnouncementInterface;
import trianglz.core.views.AnnouncementView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.Announcement;
import trianglz.ui.activities.AnnouncementDetailActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AnnouncementAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment implements View.OnClickListener
        , AnnouncementAdapter.AnnouncementAdapterInterface
        , AnnouncementInterface {

    // rootView
    private View rootView;

    // parent activity
    StudentMainActivity activity;
    private ImageButton backBtn;
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
        setListeners();
        if(Util.isNetworkAvailable(getActivity())){
            getAnnouncement(false);
            activity.showLoadingDialog();
        }else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
        return rootView;
    }

    private void bindViews(){
        backBtn = rootView.findViewById(R.id.btn_back);
        adapter = new AnnouncementAdapter(getActivity(),this);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(6,getActivity()),false));
        announcementView = new AnnouncementView(getActivity(),this);

    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            activity.progress.dismiss();
        }
        newIncomingNotificationData = announcementArrayList.size() != 0;
        adapter.addData(announcementArrayList, newIncomingNotificationData);
    }

    @Override
    public void onGetAnnouncementFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
    }


    private void  openAnnouncementDetailActivity(Announcement announcement){
        Intent intent = new Intent(getActivity(), AnnouncementDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ANNOUNCEMENTS, announcement);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);

    }


    @Override
    public void onStop() {
        super.onStop();
        if(activity.progress.isShowing()){
            activity.progress.dismiss();
        }
    }
}
