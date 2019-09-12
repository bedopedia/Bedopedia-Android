package trianglz.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import trianglz.components.HideKeyboardOnTouch;
import trianglz.core.presenters.AttachFileToTeacherPostPresenter;
import trianglz.core.presenters.CreateTeacherPostPresenter;
import trianglz.core.views.AttachFileToTeacherPostView;
import trianglz.core.views.CreateTeacherPostView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.PostDetails;
import trianglz.ui.adapters.TeacherAttachmentAdapter;
import trianglz.utils.Constants;

public class CreateTeacherPostActivity extends SuperActivity implements AttachFileToTeacherPostPresenter, TeacherAttachmentAdapter.TeacherAttachmentInterface, CreateTeacherPostPresenter, View.OnClickListener {
    private Button uploadBtn, postBtn;
    private EditText postEditText;
    private ImageButton closeBtn;
    private RecyclerView recyclerView;
    private PostDetails postDetails;
    private AttachFileToTeacherPostView attachFileToTeacherPostView;
    private LinearLayout attachmentLayout, rootView;
    private int courseGroupId, attachmentIndex = 0;
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
        attachFileToTeacherPostView = new AttachFileToTeacherPostView(this, this);
        createTeacherPostView = new CreateTeacherPostView(this, this);
        rootView = findViewById(R.id.root_view);
        uploadBtn = findViewById(R.id.upload_file_btn);
        postBtn = findViewById(R.id.post_btn);
        closeBtn = findViewById(R.id.close_btn);
        postEditText = findViewById(R.id.post_edit_text);
        attachmentLayout = findViewById(R.id.attachment_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new TeacherAttachmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setListeners() {
        uploadBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        rootView.setOnTouchListener(new HideKeyboardOnTouch(this));
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

        postDetails = post;
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.attachFiletoPost();
        if (!(adapter.filesList.isEmpty())) {
            attachFileToTeacherPostView.attachFileToTeacherPost(url, postDetails.getId(), adapter.filesList.get(attachmentIndex));
        } else {
            if (progress.isShowing()) {
                progress.dismiss();
            }
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public void onPostCreatedFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");
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
            showErrorDialog(this, -3, getResources().getString(R.string.post_is_empty));
            valid = false;
        }
        return valid;
    }

    private void openFileChooser() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constants.PICKFILE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    if (null != data) {
                        if (null != data.getClipData()) { // checking multiple selection or not
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                File file = createFile(uri);
                                if (checkFileSize(file) && !(adapter.filesList.contains(file))) {
                                    adapter.filesList.add(file);
                                    adapter.notifyDataSetChanged();
                                    attachmentLayout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else if (!checkFileSize(file)) {
                                    if (adapter.filesList.isEmpty()) {
                                        attachmentLayout.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    showErrorDialog(this,-3,getResources().getString(R.string.file_is_big));
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            File file = createFile(uri);
                            if (checkFileSize(file) && !(adapter.filesList.contains(file))) {
                                adapter.filesList.add(file);
                                adapter.notifyDataSetChanged();
                                attachmentLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else if (!checkFileSize(file)) {
                                if (adapter.filesList.isEmpty()) {
                                    attachmentLayout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                showErrorDialog(this, -3, getResources().getString(R.string.file_is_big));
                            }
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void onDeleteClicked(int position) {
        //todo put in string file
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.delete_entry))
                .setMessage(getResources().getString(R.string.delete_attachment))
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

    @Override
    public void onAttachmentUploadedSuccess(JSONObject response) {
        attachmentIndex++;
        if (attachmentIndex < adapter.filesList.size()) {
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.attachFiletoPost();
            attachFileToTeacherPostView.attachFileToTeacherPost(url, postDetails.getId(), adapter.filesList.get(attachmentIndex));
        } else {
            if (progress.isShowing()) {
                progress.dismiss();
            }
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

    }

    @Override
    public void onAttachmentUploadedFailure(String message, int errorCode) {
        Log.d("failing", "onAttachmentUploadedFailure: " + message + errorCode);
    }
}
