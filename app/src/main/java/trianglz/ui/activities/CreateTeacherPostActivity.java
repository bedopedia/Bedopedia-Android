package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

import trianglz.components.TopItemDecoration;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Util;

public class CreateTeacherPostActivity extends SuperActivity implements View.OnClickListener, AttachmentAdapter.AttachmentAdapterInterface {
    private Button uploadBtn, postBtn;
    private ImageButton closeBtn;
    RecyclerView recyclerView;
    private LinearLayout attachmentLayout;
    private AttachmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_post);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        uploadBtn = findViewById(R.id.upload_file_btn);
        postBtn = findViewById(R.id.post_btn);
        closeBtn = findViewById(R.id.close_btn);
        attachmentLayout = findViewById(R.id.attachment_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16,this),false));
    }

    private void setListeners() {
        uploadBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onAttachmentClicked(String fileUrl) {

    }
}
