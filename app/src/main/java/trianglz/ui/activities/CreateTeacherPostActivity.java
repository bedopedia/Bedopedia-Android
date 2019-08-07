package trianglz.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

import java.io.File;
import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.CreateTeacherPostPresenter;
import trianglz.core.views.CreateTeacherPostView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.PostDetails;
import trianglz.ui.adapters.TeacherAttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CreateTeacherPostActivity extends SuperActivity implements TeacherAttachmentAdapter.TeacherAttachmentInterface, CreateTeacherPostPresenter, View.OnClickListener {
    private Button uploadBtn, postBtn;
    private EditText postEditText;
    private ImageButton closeBtn;
    private ArrayList<Uri> filesUri;
    private ArrayList<File> files;
    public static final int PICKFILE_RESULT_CODE = 1;
    private RecyclerView recyclerView;
    private PostDetails postDetails;
    private LinearLayout attachmentLayout;
    private int courseGroupId;
    private TeacherAttachmentAdapter adapter;
    private CreateTeacherPostView createTeacherPostView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_post);
        bindViews();
        setListeners();
        getValuesFromIntent();
    }

    private void bindViews() {
        filesUri = new ArrayList<>();
        files = new ArrayList<>();
        createTeacherPostView = new CreateTeacherPostView(this, this);
        uploadBtn = findViewById(R.id.upload_file_btn);
        postBtn = findViewById(R.id.post_btn);
        closeBtn = findViewById(R.id.close_btn);
        postEditText = findViewById(R.id.post_edit_text);
        attachmentLayout = findViewById(R.id.attachment_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new TeacherAttachmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16, this), false));
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
            case R.id.post_btn:
                if (validate()) {
                    createPost();
                }
                break;
            case R.id.upload_file_btn:
                openFileChooser();
                break;
        }
    }


    @Override
    public void onPostCreatedSuccess(PostDetails post) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        postDetails = post;
    }

    @Override
    public void onPostCreatedFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            logoutUser(this);
        } else {
            showErrorDialog(this);
        }
    }

    void createPost() {
        showLoadingDialog();
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createPostCourseGroup();
        createTeacherPostView.createPost(url, postEditText.getText().toString(), Integer.parseInt(SessionManager.getInstance().getUserId()), courseGroupId, "CourseGroup");
    }

    private void getValuesFromIntent() {
        courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, 0);
    }

    private Boolean validate() {
        String post = postEditText.getText().toString();
        Boolean valid = true;
        if (post.isEmpty()) {
            if (Util.getLocale(this).equals("ar")) {
                Util.showErrorDialog(this, "Skolera", "المنشور فارغ");
            } else {
                Util.showErrorDialog(this, "Skolera", "Post is empty");
            }
            valid = false;
        }
        return valid;
    }

    private void openFileChooser() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    if (null != data) {
                        if (null != data.getClipData()) { // checking multiple selection or not
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                File file = new File(uri.toString());
//                                if (checkFileSize(uri)) {
//                                    files.add(file);
//                                    filesUri.add(uri);
//                                } else {
//                                    if (Util.getLocale(this).equals("ar")) {
//                                        Util.showErrorDialog(this, "Skolera", "حجم الملف كبير");
//                                    } else {
//                                        Util.showErrorDialog(this, "Skolera", "File is too big");
//                                    }
//                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            File file = new File(uri.toString());
                            files.add(file);
                            filesUri.add(uri);
                        }
                    }
                    adapter.addData(filesUri);
                    attachmentLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDeleteClicked(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this attachment?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        filesUri.remove(position);
                        files.remove(position);
                        adapter.addData(filesUri);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private Boolean checkFileSize(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        File file = new File(filePath);
        Log.d("farah", "checkFileSize: " + file.length());
        Boolean valid = true;
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        if (megabytes > 10) {
            return false;
        }
        return valid;
    }
}
