package trianglz.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                                File file = createFile(uri);
                                if (checkFileSize(file)) {
                                    adapter.filesList.add(file);

                                } else {
                                    if (Util.getLocale(this).equals("ar")) {
                                        Util.showErrorDialog(this, "Skolera", "حجم الملف كبير");
                                    } else {
                                        Util.showErrorDialog(this, "Skolera", "File is too big");
                                    }
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            File file = createFile(uri);
                            if (checkFileSize(file)) {
                                adapter.filesList.add(file);
                                //todo filesUri.add(uri);
                            } else {
                                if (Util.getLocale(this).equals("ar")) {
                                    Util.showErrorDialog(this, "Skolera", "حجم الملف كبير");
                                } else {
                                    Util.showErrorDialog(this, "Skolera", "File is too big");
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    attachmentLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDeleteClicked(int position) {
        //todo put in string file
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this attachment?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.filesList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapter.filesList.size());
                        if (adapter.filesList.isEmpty()) {
                            attachmentLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private Boolean checkFileSize(File file) {
        Boolean valid = true;
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        if (megabytes > 10) {
            return false;
        }
        return valid;
    }

    private File createFile(Uri uri) {
        InputStream in = null;
        File myFile = new File(uri.toString());
        String displayName = null;
        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = myFile.getName();
        }
        File file = new File(getCacheDir(), displayName);
        try {
            in = getContentResolver().openInputStream(uri);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = in.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
