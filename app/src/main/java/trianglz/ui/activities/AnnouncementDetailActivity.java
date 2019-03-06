package trianglz.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import trianglz.models.Announcement;
import trianglz.models.WeeklyNote;
import trianglz.utils.Constants;

public class AnnouncementDetailActivity extends SuperActivity implements View.OnClickListener {
    private ImageButton backBtn;
    private TextView headerTextView;
    private Announcement announcement;
    private ImageView announcementImageView;
    private WebView webView;
    private WeeklyNote weeklyNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoucement_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        announcement = (Announcement) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ANNOUNCEMENTS);
        if(announcement == null){
        weeklyNote = (WeeklyNote)getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_WEEKLY_NOTE);
        }
    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        headerTextView = findViewById(R.id.tv_header);
        announcementImageView = findViewById(R.id.img_annoucement);
        if(announcement != null){
            setAnnouncement();
        }else {
            setWeeklyNote();
        }

    }

    private void setWeeklyNote() {
        headerTextView.setText(weeklyNote.getTitle());
        if(weeklyNote.getImageUrl() != null && !weeklyNote.getImageUrl().isEmpty() && !weeklyNote.getImageUrl().equals("null")){
            announcementImageView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(weeklyNote.getImageUrl())
                    .centerCrop()
                    .fit()
                    .into(announcementImageView);
        }else {
            announcementImageView.setVisibility(View.GONE);
        }
        webView = findViewById(R.id.web_view);
        webView.loadData(weeklyNote.getDescription(), "text/html", null);
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

    private void setAnnouncement(){
        headerTextView.setText(announcement.title);
        if(announcement.imageUrl != null && !announcement.imageUrl.isEmpty() && !announcement.imageUrl.equals("null")){
            announcementImageView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(announcement.imageUrl)
                    .centerCrop()
                    .fit()
                    .into(announcementImageView);
        }else {
            announcementImageView.setVisibility(View.GONE);
        }
        webView = findViewById(R.id.web_view);
        webView.loadData(announcement.body, "text/html", null);
    }
}
