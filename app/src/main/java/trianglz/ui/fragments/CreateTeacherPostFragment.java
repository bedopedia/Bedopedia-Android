package trianglz.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import trianglz.core.presenters.FragmentCommunicationInterface;
import trianglz.core.views.AttachFileToTeacherPostView;
import trianglz.core.views.CreateTeacherPostView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.PostDetails;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherAttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 08/09/2019.
 */
public class CreateTeacherPostFragment extends Fragment implements AttachFileToTeacherPostPresenter, TeacherAttachmentAdapter.TeacherAttachmentInterface, CreateTeacherPostPresenter, View.OnClickListener {
    private StudentMainActivity activity;
    private View rootView;
    private Button uploadBtn, postBtn;
    private EditText postEditText;
    private ImageButton closeBtn;
    private RecyclerView recyclerView;
    private PostDetails postDetails;
    private AttachFileToTeacherPostView attachFileToTeacherPostView;
    private LinearLayout attachmentLayout, rootViewLinear;
    private int courseGroupId, attachmentIndex = 0;
    private TeacherAttachmentAdapter adapter;
    private CreateTeacherPostView createTeacherPostView;
    private FragmentCommunicationInterface fragmentCommunicationInterface;

    public static CreateTeacherPostFragment newInstance(FragmentCommunicationInterface fragmentCommunicationInterface) {
        CreateTeacherPostFragment createTeacherPostFragment = new CreateTeacherPostFragment();
        createTeacherPostFragment.fragmentCommunicationInterface = fragmentCommunicationInterface;
        return createTeacherPostFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_create_teacher_post, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
        setListeners();
        getValuesFromIntent();
    }
    private void bindViews() {
        attachFileToTeacherPostView = new AttachFileToTeacherPostView(activity, this);
        createTeacherPostView = new CreateTeacherPostView(activity, this);
        rootViewLinear = rootView.findViewById(R.id.root_view);
        uploadBtn = rootView.findViewById(R.id.upload_file_btn);
        postBtn = rootView.findViewById(R.id.post_btn);
        closeBtn = rootView.findViewById(R.id.close_btn);
        postEditText = rootView.findViewById(R.id.post_edit_text);
        attachmentLayout = rootView.findViewById(R.id.attachment_layout);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new TeacherAttachmentAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
    }

    private void setListeners() {
        uploadBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        rootViewLinear.setOnTouchListener(new HideKeyboardOnTouch(activity));
    }
    private void getValuesFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroupId = bundle.getInt(Constants.KEY_COURSE_GROUP_ID, 0);
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
    private Boolean validate() {
        String post = postEditText.getText().toString();
        Boolean valid = true;
        if (post.isEmpty()) {
            Util.showErrorDialog(activity, "Skolera", getResources().getString(R.string.post_is_empty));
            valid = false;
        }
        return valid;
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
    void createPost() {
        activity.showLoadingDialog();
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createPostCourseGroup();
        createTeacherPostView.createPost(url, postEditText.getText().toString(), Integer.parseInt(SessionManager.getInstance().getUserId()), courseGroupId, "CourseGroup");
    }
    private File createFile(Uri uri) {
        InputStream in = null;
        File myFile = new File(uri.toString());
        String displayName = null;
        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = activity.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = myFile.getName();
        }
        File file = new File(activity.getCacheDir(), displayName);
        try {
            in = activity.getContentResolver().openInputStream(uri);
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
    private void openFileChooser() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constants.PICKFILE_RESULT_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                getParentFragment().getChildFragmentManager().popBackStack();
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
    public void onAttachmentUploadedSuccess(JSONObject response) {
        attachmentIndex++;
        if (attachmentIndex < adapter.filesList.size()) {
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.attachFiletoPost();
            attachFileToTeacherPostView.attachFileToTeacherPost(url, postDetails.getId(), adapter.filesList.get(attachmentIndex));
        } else {
            if (activity.progress.isShowing()) {
                activity.progress.dismiss();
            }
            fragmentCommunicationInterface.reloadEvents();
            getParentFragment().getChildFragmentManager().popBackStack();
        }
    }

    @Override
    public void onAttachmentUploadedFailure(String message, int errorCode) {

    }

    @Override
    public void onPostCreatedSuccess(PostDetails post) {
        postDetails = post;
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.attachFiletoPost();
        if (!(adapter.filesList.isEmpty())) {
            attachFileToTeacherPostView.attachFileToTeacherPost(url, postDetails.getId(), adapter.filesList.get(attachmentIndex));
        } else {
            if (activity.progress.isShowing()) {
                activity.progress.dismiss();
            }
            getParentFragment().getChildFragmentManager().popBackStack();
            fragmentCommunicationInterface.reloadEvents();
        }
    }

    @Override
    public void onPostCreatedFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
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
                                    Util.showErrorDialog(activity, getResources().getString(R.string.cannot_select_file), getResources().getString(R.string.file_is_big));

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
                                Util.showErrorDialog(activity, "Skolera", getResources().getString(R.string.file_is_big));
                            }
                        }
                    }

                }
                break;
        }
    }
    @Override
    public void onDeleteClicked(int position) {
        new AlertDialog.Builder(activity)
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
}
