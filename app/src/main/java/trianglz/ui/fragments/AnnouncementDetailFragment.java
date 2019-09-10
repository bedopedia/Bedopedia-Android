package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import trianglz.models.Announcement;
import trianglz.models.WeeklyNote;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class AnnouncementDetailFragment extends Fragment  implements View.OnClickListener {
    private View rootView;
    private StudentMainActivity activity;
    private ImageButton backBtn;
    private TextView headerTextView;
    private Announcement announcement;
    private ImageView announcementImageView;
    private WebView webView;
    private WeeklyNote weeklyNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_annoucement_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
   //     onBackPress();
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            announcement = (Announcement) bundle.getSerializable(Constants.KEY_ANNOUNCEMENTS);
            if (announcement == null) {
                weeklyNote = (WeeklyNote) bundle.getSerializable(Constants.KEY_WEEKLY_NOTE);
            }
        }
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        headerTextView = rootView.findViewById(R.id.tv_header);
        announcementImageView = rootView.findViewById(R.id.img_annoucement);
        if(announcement != null){
            setAnnouncement();
        }else {
            setWeeklyNote();
        }
    }


    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getParentFragment().getChildFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void setWeeklyNote() {
        headerTextView.setText(weeklyNote.getTitle());
        if(weeklyNote.getImageUrl() != null && !weeklyNote.getImageUrl().isEmpty() && !weeklyNote.getImageUrl().equals("null")){
            announcementImageView.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(weeklyNote.getImageUrl())
                    .centerCrop()
                    .fit()
                    .into(announcementImageView);
        }else {
            announcementImageView.setVisibility(View.GONE);
        }
        webView = rootView.findViewById(R.id.web_view);
        webView.loadData(weeklyNote.getDescription(), "text/html", null);
    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
    }

    private void setAnnouncement(){
        headerTextView.setText(announcement.title);
        if(announcement.imageUrl != null && !announcement.imageUrl.isEmpty() && !announcement.imageUrl.equals("null")){
            announcementImageView.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(announcement.imageUrl)
                    .centerCrop()
                    .fit()
                    .into(announcementImageView);
        }else {
            announcementImageView.setVisibility(View.GONE);
        }
        webView = rootView.findViewById(R.id.web_view);
        webView.loadData(announcement.body, "text/html", null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }
}
