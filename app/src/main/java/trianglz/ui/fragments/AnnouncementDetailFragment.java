package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Announcement;
import trianglz.models.GeneralNote;
import trianglz.models.Student;
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
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private ImageView announcementImageView;
    private WebView webView;
    private GeneralNote generalNote;

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
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            announcement = (Announcement) bundle.getSerializable(Constants.KEY_ANNOUNCEMENTS);
            if (announcement == null) {
                generalNote = (GeneralNote) bundle.getSerializable(Constants.KEY_WEEKLY_NOTE);
            }
        }
    }

    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        backBtn = rootView.findViewById(R.id.btn_back);
        headerTextView = rootView.findViewById(R.id.tv_header);
        announcementImageView = rootView.findViewById(R.id.img_annoucement);
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        if (student != null) {
            setStudentImage(student.avatar, student.firstName + " " + student.lastName);
        } else {
            studentImageView.setVisibility(View.GONE);
        }
        if(announcement != null){
            setAnnouncement();
        }else {
            setWeeklyNote();
        }
    }



    private void setWeeklyNote() {
        headerTextView.setText(generalNote.title);
        if(generalNote.image.url != null && !generalNote.image.url.isEmpty() && !generalNote.image.url.equals("null")){
            announcementImageView.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(generalNote.image.url)
                    .centerInside()
                    .fit()
                    .into(announcementImageView);
        }else {
            announcementImageView.setVisibility(View.GONE);
        }
        webView = rootView.findViewById(R.id.web_view);
        webView.loadData(generalNote.description, "text/html", null);
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
                    .centerInside()
                    .fit()
                    .into(announcementImageView);
            announcementImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> images = new ArrayList<>();
                    images.add(announcement.imageUrl);
                    new ImageViewer.Builder<>(activity, images)
                            .setStartPosition(0)
                            .hideStatusBar(false)
                            .allowZooming(true)
                            .allowSwipeToDismiss(true)
                            .setBackgroundColorRes((R.color.black))
                            .show();
                }
            });

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
                if (announcement != null) {
                    activity.headerLayout.setVisibility(View.VISIBLE);
                    activity.toolbarView.setVisibility(View.VISIBLE);
                }
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }
    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }
}
