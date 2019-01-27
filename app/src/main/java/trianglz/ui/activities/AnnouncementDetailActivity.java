package trianglz.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import trianglz.models.Announcement;
import trianglz.utils.Constants;

public class AnnouncementDetailActivity extends SuperActivity implements View.OnClickListener {
    private ImageButton backBtn;
    private TextView headerTextView;
    private Announcement announcement;
    private ImageView announcementImageView;
    private TextView secondaryTextView,bodyTextView;
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
    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        headerTextView = findViewById(R.id.tv_header);
        announcementImageView = findViewById(R.id.img_annoucement);
        headerTextView.setText(announcement.title);
        secondaryTextView = findViewById(R.id.tv_secondary_title);
        secondaryTextView.setText(announcement.title);
        bodyTextView = findViewById(R.id.tv_body);
        String body = android.text.Html.fromHtml(announcement.body).toString();
        body = StringEscapeUtils.unescapeJava(body);
        bodyTextView.setText(body);
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
}
