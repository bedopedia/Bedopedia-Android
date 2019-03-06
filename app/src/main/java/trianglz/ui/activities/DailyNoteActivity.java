package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.BottomItemDecoration;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
import trianglz.models.DailyNote;
import trianglz.models.RootClass;
import trianglz.models.Student;
import trianglz.ui.adapters.DailyNoteAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class DailyNoteActivity extends SuperActivity implements View.OnClickListener {
    private ImageButton backButton;
    private Student student;
    private DailyNote dailyNote;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private RecyclerView recyclerView;
    private DailyNoteAdapter dailyNoteAdapter;
    private TextView dailyNoteNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_note);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent(){
        dailyNote = (DailyNote) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_DAILY_NOTE);
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        imageLoader = new PicassoLoader();
    }

    private void bindViews(){
        backButton = findViewById(R.id.btn_back);
        studentImageView = findViewById(R.id.img_student);
        if(student != null){
            setStudentImage();
        }
        recyclerView = findViewById(R.id.recycler_view);
        dailyNoteAdapter = new DailyNoteAdapter(this,dailyNote);
        recyclerView.setAdapter(dailyNoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int)Util.convertDpToPixel(16,this),false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int)Util.convertDpToPixel(16,this),false));
        dailyNoteNameTextView = findViewById(R.id.tv_name);
        dailyNoteNameTextView.setText(dailyNote.getTitle());
    }

    private void setListeners(){
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
    private void setStudentImage() {
        final String imageUrl = student.getAvatar();
        final String name = student.firstName + " " + student.lastName;
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
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
