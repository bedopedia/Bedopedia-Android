package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import trianglz.ui.adapters.AnnouncementAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AnnouncementActivity extends SuperActivity implements View.OnClickListener
        ,AnnouncementAdapter.AnnouncementAdapterInterface
        ,AnnouncementInterface {

    private ImageButton backBtn;
    private AnnouncementAdapter adapter;
    private RecyclerView recyclerView;
    private AnnouncementView announcementView;
    private int pageNumber = 1;
    private Actor actor;
    private boolean newIncomingNotificationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        getValueFromIntent();
        bindViews();
        setListeners();
        if(Util.isNetworkAvailable(this)){
            getAnnouncement(false);
            showLoadingDialog();
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    private void getValueFromIntent() {
        actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
    }

    private void bindViews(){
        backBtn = findViewById(R.id.btn_back);
        adapter = new AnnouncementAdapter(this,this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int)Util.convertDpToPixel(6,this),false));
        announcementView = new AnnouncementView(this,this);

    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
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

            showLoadingDialog();
            if (!pagination) {
                pageNumber = 1;
            }
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getAnnouncementUrl(pageNumber,actor.actableType, 20);
            announcementView.getAnnouncement(url);

    }

    @Override
    public void onGetAnnouncementSuccess(ArrayList<Announcement> announcementArrayList) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        newIncomingNotificationData = announcementArrayList.size() != 0;
        adapter.addData(announcementArrayList, newIncomingNotificationData);
    }

    @Override
    public void onGetAnnouncementFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    private void  openAnnouncementDetailActivity(Announcement announcement){
        Intent intent = new Intent(this, AnnouncementDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ANNOUNCEMENTS, announcement);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);

    }
}
